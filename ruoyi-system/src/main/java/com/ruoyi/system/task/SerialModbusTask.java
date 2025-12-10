package com.ruoyi.system.task;

import com.alibaba.fastjson.JSON;
import com.ruoyi.system.constants.RedisKeyConstants;
import com.ruoyi.system.domain.DeviceArchive;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.mapper.DeviceArchiveMapper;
import com.ruoyi.system.service.IModbusDataService;
import com.ruoyi.common.utils.SerialModbusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Modbus温湿度数据采集的定时任务（String缓存版，无Hash冲突）
 */
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

    // 每5秒采集一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void collectModbusData() {
        log.info("===== 开始采集温湿度数据 =====");

        // 查所有启用的设备
        List<DeviceArchive> devices = deviceArchiveMapper.selectEnabledDevices();
        if (devices.isEmpty()) {
            log.warn("没找到启用的设备，跳过采集");
            return;
        }

        // 遍历设备采集
        for (DeviceArchive device : devices) {
            try {
                int slaveId = device.getSlaveId();       // 设备的从站ID
                int regStart = device.getCommAddress();  // 起始寄存器地址
                int regCount = device.getRegCount();     // 读取数量（2个）
                String deviceName = device.getDeviceName();

                log.info("开始采集设备[{}]（Slave{}，寄存器{}~{}）",
                        deviceName, slaveId, regStart, regStart + regCount - 1);

                // 核心：调用工具类读取数据（需传入当前设备的参数，下面会修改工具类支持）
                int[] regValues = serialModbusUtils.readModbusSlaveData(slaveId, regStart, regCount);

                // 解析数据（寄存器值转温湿度）
                float temperature = regValues[0] / 10.0f;
                float humidity = regValues[1] / 10.0f;
                log.info("设备[{}]采集完成：温度{}℃，湿度{}%RH",
                        deviceName, temperature, humidity);

                // 封装入库
                ModbusData data = new ModbusData();
                data.setSlaveId(String.valueOf(slaveId)); // int转String
                data.setTemperature(String.valueOf(temperature)); // Double转String
                data.setHumidity(String.valueOf(humidity)); // Double转String
                data.setReadTime(new Date());
                modbusDataService.insertModbusData(data);

                // 保留单设备String缓存
                try {
                    // 生成当前设备的独立缓存键（String类型）
                    String slaveCacheKey = String.format(RedisKeyConstants.MODBUS_REALTIME_SLAVE_LATEST, slaveId);
                    // 更新该设备的独立String缓存（有效期1小时）
                    redisTemplate.opsForValue().set(
                            slaveCacheKey,
                            JSON.toJSONString(data),
                            1, TimeUnit.HOURS
                    );
                    log.info("设备[{}]单设备缓存已实时更新（Slave{}）", deviceName, slaveId);
                } catch (Exception e) {
                    // 缓存更新失败仅打日志，不影响采集/入库主流程
                    log.error("更新设备[{}]（Slave{}）单设备缓存失败", deviceName, slaveId, e);
                }

            } catch (Exception e) {
                log.error("设备[{}]（Slave{}）采集失败",
                        device.getDeviceName(), device.getSlaveId(), e);
            }
        }

        // 删除全量String缓存，确保查询时加载最新数据
        try {
            redisTemplate.delete(RedisKeyConstants.MODBUS_REALTIME_ALL_LATEST);
            log.info("所有设备采集完成，已删除全量实时数据缓存（下次查询自动加载最新数据）");
        } catch (Exception e) {
            log.error("删除全量实时数据缓存失败", e);
        }

        log.info("===== 所有设备采集结束 =====");
    }
}