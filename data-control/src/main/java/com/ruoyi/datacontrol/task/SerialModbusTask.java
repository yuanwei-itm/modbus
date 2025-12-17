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

/**
 * Modbus 串口采集任务（线程安全版）
 */
@Component
public class SerialModbusTask {

    private static final Logger log = LoggerFactory.getLogger(SerialModbusTask.class);

    @Resource
    private DeviceArchiveMapper deviceArchiveMapper;

    @Resource
    private IModbusDataService modbusDataService;

    @Resource
    private SerialModbusUtils serialModbusUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 定时任务入口
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void scheduledCollect() {
        executeCollection("定时任务");
    }

    /**
     * 手动触发入口
     */
    public void manualCollect() {
        executeCollection("手动强制刷新");
    }

    /**
     * 核心采集逻辑 (加锁 synchronized)
     * 关键：synchronized 保证同一时间只能有一个线程操作串口
     * 无论是定时器还是人点的，都要排队，防止串口冲突报错
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void executeCollection(String triggerType) {
        log.info("===== 开始采集温湿度数据 (触发源: {}) =====", triggerType);
        long startTime = System.currentTimeMillis();

        // 查所有启用的设备
        List<DeviceArchive> allDevices = deviceArchiveMapper.selectEnabledDevices();
        if (allDevices.isEmpty()) {
            log.warn("没找到启用的设备，跳过采集");
            return;
        }

        List<ModbusData> batchDataList = new ArrayList<>();
        List<ModbusData> allLatestDataList = new ArrayList<>();

        Map<Integer, List<DeviceArchive>> slaveGroup = allDevices.stream()
                .collect(Collectors.groupingBy(DeviceArchive::getSlaveId));

        for (Map.Entry<Integer, List<DeviceArchive>> entry : slaveGroup.entrySet()) {
            Integer slaveId = entry.getKey();
            List<DeviceArchive> devicesInSlave = entry.getValue();

            try {
                // 动态地址计算
                int startAddress = devicesInSlave.stream().mapToInt(DeviceArchive::getCommAddress).min().orElse(0);
                int maxEndAddress = devicesInSlave.stream().mapToInt(d -> d.getCommAddress() + d.getRegCount()).max().orElse(0);
                int readLength = maxEndAddress - startAddress;

                if (readLength <= 0) continue;

                // --- 真正的硬件读取操作 (最耗时、最危险的步骤) ---
                int[] allRegValues = serialModbusUtils.readModbusSlaveData(slaveId, startAddress, readLength);

                if (allRegValues == null || allRegValues.length == 0) {
                    log.warn("从站Slave{} 读取无响应，跳过", slaveId);
                    continue;
                }

                Date batchTime = new Date();

                // 解析数据
                for (DeviceArchive dev : devicesInSlave) {
                    try {
                        String deviceId = dev.getDeviceId();
                        int relativeIndex = dev.getCommAddress() - startAddress;

                        if (relativeIndex < 0 || (relativeIndex + dev.getRegCount()) > allRegValues.length) continue;

                        int[] devRegValues = Arrays.copyOfRange(allRegValues, relativeIndex, relativeIndex + dev.getRegCount());

                        double tempValue = devRegValues[0] / 10.0;
                        double humiValue = devRegValues[1] / 10.0;

                        ModbusData data = new ModbusData();
                        data.setDeviceId(deviceId);
                        data.setSlaveId(String.valueOf(slaveId));
                        data.setDeviceName(dev.getDeviceName());
                        data.setTemperature(String.valueOf(tempValue));
                        data.setHumidity(String.valueOf(humiValue));
                        data.setReadTime(batchTime);

                        // 准备入库和缓存
                        batchDataList.add(data);
                        allLatestDataList.add(data);

                        // 更新单设备缓存
                        String deviceCacheKey = String.format(RedisKeyConstants.MODBUS_REALTIME_DEVICE_LATEST, deviceId);
                        redisTemplate.opsForValue().set(deviceCacheKey, JSON.toJSONString(data), 1, TimeUnit.HOURS);

                    } catch (Exception e) {
                        log.error("解析设备[{}]失败", dev.getDeviceName(), e);
                    }
                }

            } catch (Exception e) {
                log.error("采集物理从站Slave{}失败", slaveId, e);
            }
        }

        // 批量入库
        if (!batchDataList.isEmpty()) {
            modbusDataService.batchInsertModbusData(batchDataList);
        }

        // 重构全量缓存
        if (!allLatestDataList.isEmpty()) {
            redisTemplate.delete(RedisKeyConstants.MODBUS_REALTIME_ALL_LATEST);
            redisTemplate.opsForValue().set(RedisKeyConstants.MODBUS_REALTIME_ALL_LATEST, JSON.toJSONString(allLatestDataList), 1, TimeUnit.HOURS);
        }

        long cost = System.currentTimeMillis() - startTime;
        log.info("===== 采集结束 (触发源: {})，耗时: {}ms =====", triggerType, cost);
    }
}