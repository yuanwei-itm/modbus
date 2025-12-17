package com.ruoyi.datacontrol.task;

import com.alibaba.fastjson.JSON;
import com.ruoyi.datacontrol.constants.RedisKeyConstants;
import com.ruoyi.datacontrol.domain.DeviceArchive;
import com.ruoyi.datacontrol.domain.ModbusData;
import com.ruoyi.datacontrol.mapper.DeviceArchiveMapper;
import com.ruoyi.datacontrol.service.IModbusDataService;
import com.ruoyi.common.utils.SerialModbusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SerialModbusTask {

    private static final Logger log = LoggerFactory.getLogger(SerialModbusTask.class);

    // 注入设备档案Mapper
    @Resource
    private DeviceArchiveMapper deviceArchiveMapper;

    // 注入数据入库服务
    @Resource
    private IModbusDataService modbusDataService;

    // 注入你的串口工具类
    @Resource
    private SerialModbusUtils serialModbusUtils;

    //注入 RedisTemplate
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    // 每10秒采集一次
    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void collectModbusData() {
        log.info("===== 开始采集温湿度数据 =====");

        // 查所有启用的设备
        List<DeviceArchive> allDevices = deviceArchiveMapper.selectEnabledDevices();
        if (allDevices.isEmpty()) {
            log.warn("没找到启用的设备，跳过采集");
            return;
        }

        // 存储所有设备的有效采集数据（用于批量入库）
        List<ModbusData> batchDataList = new ArrayList<>();
        // 存储所有设备的最新数据（用于重构全量缓存）
        List<ModbusData> allLatestDataList = new ArrayList<>();

        // 按物理从站ID分组（核心：一个从站只采集一次）
        Map<Integer, List<DeviceArchive>> slaveGroup = allDevices.stream()
                .collect(Collectors.groupingBy(DeviceArchive::getSlaveId));

        // 遍历每个物理从站批量采集
        for (Map.Entry<Integer, List<DeviceArchive>> entry : slaveGroup.entrySet()) {
            Integer slaveId = entry.getKey(); // 物理从站ID（Integer）
            List<DeviceArchive> devicesInSlave = entry.getValue();
            log.info("开始采集物理从站Slave{}，包含{}个逻辑设备", slaveId, devicesInSlave.size());

            try {
                // 动态地址优化核心逻辑
                // 计算该从站下所有设备的最小comm_address
                int startAddress = devicesInSlave.stream()
                        .mapToInt(DeviceArchive::getCommAddress)
                        .min()
                        .orElse(0);

                // 计算该从站下所有设备的最大结束位置（comm_address + regCount）
                int maxEndAddress = devicesInSlave.stream()
                        .mapToInt(d -> d.getCommAddress() + d.getRegCount())
                        .max()
                        .orElse(0);

                // 计算实际需要读取的寄存器长度（结束位置 - 起始地址）
                int readLength = maxEndAddress - startAddress;

                log.info("从站Slave{} 优化采集策略：起始地址={}, 最大结束地址={}, 读取长度={}",
                        slaveId, startAddress, maxEndAddress, readLength);

                // 读取长度不合理（负数/0）时跳过该从站
                if (readLength <= 0) {
                    log.warn("从站Slave{} 读取长度不合理（readLength={}），跳过采集", slaveId, readLength);
                    continue;
                }

                // 批量读取该从站的寄存器数据
                int[] allRegValues = serialModbusUtils.readModbusSlaveData(slaveId, startAddress, readLength);
                log.info("从站Slave{}读取到的寄存器数组：{}", slaveId, Arrays.toString(allRegValues));

                // 防空指针崩溃
                if (allRegValues == null || allRegValues.length == 0) {
                    log.warn("从站Slave{} 读取无响应，跳过", slaveId);
                    continue;
                }

                // 统一批次时间（所有逻辑设备共用一个采集时间，保证时间一致性）
                Date batchTime = new Date();

                // 按每个逻辑设备的配置切分数据并封装
                for (DeviceArchive dev : devicesInSlave) {
                    try {
                        String deviceId = dev.getDeviceId();       // 逻辑设备唯一ID（String）
                        String deviceName = dev.getDeviceName();   // 设备名称（String）
                        int commAddress = dev.getCommAddress();    // 替换：offset→comm_address（设备物理寄存器地址）
                        int regCount = dev.getRegCount();          // 设备需要读取的寄存器数量

                        // 用comm_address计算相对偏移
                        // 相对偏移量 = 设备物理地址 - 批量读取的起始地址（本地数组的切分下标）
                        int relativeIndex = commAddress - startAddress;

                        // 校验：相对偏移量越界（负数 或 超出数组长度）
                        if (relativeIndex < 0 || (relativeIndex + regCount) > allRegValues.length) {
                            log.error("设备[{}]（Slave{}）配置越界：设备物理地址={}, 读取数量={}, 相对下标={}, 数组长度={}",
                                    deviceName, slaveId, commAddress, regCount, relativeIndex, allRegValues.length);
                            continue;
                        }

                        // 截取当前设备的寄存器值（使用相对偏移量）
                        int[] devRegValues = Arrays.copyOfRange(allRegValues, relativeIndex, relativeIndex + regCount);
                        log.info("设备[{}]（Slave{}）截取到的寄存器值：{}",
                                deviceName, slaveId, Arrays.toString(devRegValues));

                        // 解析温湿度并转为String（适配实体类）
                        double tempValue = devRegValues[0] / 10.0;  // 先算数值
                        double humiValue = devRegValues[1] / 10.0;
                        String temperature = String.valueOf(tempValue); // 转String
                        String humidity = String.valueOf(humiValue);
                        log.info("设备[{}]解析完成：温度{}℃，湿度{}%RH", deviceName, temperature, humidity);

                        // 封装入库
                        ModbusData data = new ModbusData();
                        data.setDeviceId(deviceId);          // 逻辑设备唯一ID（String）
                        data.setSlaveId(String.valueOf(slaveId)); // 物理从站ID转String
                        data.setDeviceName(deviceName);      // 设备名称（String）
                        data.setTemperature(temperature);    // 温度（String）
                        data.setHumidity(humidity);          // 湿度（String）
                        data.setReadTime(batchTime);         // 采集时间（Date）

                        // 防重校验
                        Date checkTime = new Date(batchTime.getTime() / 1000 * 1000);
                        if (!modbusDataService.checkDataExists(deviceId, checkTime)) {
                            batchDataList.add(data); // 加入批量入库列表
                            log.info("设备[{}]数据加入批量入库列表", deviceName);
                        } else {
                            log.warn("设备[{}]（Slave{}）在{}已存在记录，跳过入库",
                                    deviceName, slaveId, checkTime);
                        }

                        // 更新单设备Redis缓存
                        try {
                            String dataJson = JSON.toJSONString(data);
                            String deviceCacheKey = String.format(RedisKeyConstants.MODBUS_REALTIME_DEVICE_LATEST, deviceId);
                            redisTemplate.opsForValue().set(
                                    deviceCacheKey,
                                    dataJson,
                                    1, TimeUnit.HOURS
                            );
                            log.info("设备[{}]缓存更新成功（deviceId={}）", deviceName, deviceId);
                            allLatestDataList.add(data); // 加入全量缓存列表
                        } catch (Exception e) {
                            log.error("更新设备[{}]缓存失败", deviceName, e);
                        }

                    } catch (Exception e) {
                        log.error("解析设备[{}]（Slave{}）数据失败", dev.getDeviceName(), slaveId, e);
                    }
                }

            } catch (Exception e) {
                log.error("采集物理从站Slave{}失败", slaveId, e);
            }
        }

        // 批量入库
        if (!batchDataList.isEmpty()) {
            boolean batchInsertSuccess = modbusDataService.batchInsertModbusData(batchDataList);
            log.info("本次采集共{}条有效数据，批量入库结果：{}", batchDataList.size(), batchInsertSuccess ? "成功" : "失败");
        } else {
            log.warn("本次采集无有效数据，跳过批量入库");
        }

        // 先删旧缓存，再存新缓存
        try {
            redisTemplate.delete(RedisKeyConstants.MODBUS_REALTIME_ALL_LATEST);
            if (!allLatestDataList.isEmpty()) {
                String allDataJson = JSON.toJSONString(allLatestDataList);
                redisTemplate.opsForValue().set(
                        RedisKeyConstants.MODBUS_REALTIME_ALL_LATEST,
                        allDataJson,
                        1, TimeUnit.HOURS
                );
                log.info("全量实时数据缓存重构成功，共{}个设备", allLatestDataList.size());
            } else {
                log.warn("本次采集无有效数据，全量缓存已删除");
            }
        } catch (Exception e) {
            log.error("重构全量实时数据缓存失败", e);
        }

        log.info("===== 所有设备采集结束 =====");
    }
}