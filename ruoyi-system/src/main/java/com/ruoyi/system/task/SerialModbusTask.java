package com.ruoyi.system.task;

import com.ruoyi.system.domain.DeviceArchive;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.mapper.DeviceArchiveMapper;
import com.ruoyi.system.service.IModbusDataService;
import com.ruoyi.common.utils.SerialModbusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Modbus温湿度数据采集的定时任务
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

                // 封装入库（float转Double适配setter）
                ModbusData data = new ModbusData();
                data.setSlaveId(slaveId);
                data.setTemperature((double) temperature);  // 转Double
                data.setHumidity((double) humidity);        // 转Double
                data.setReadTime(new Date());
                modbusDataService.insertModbusData(data);

            } catch (Exception e) {
                log.error("设备[{}]（Slave{}）采集失败",
                        device.getDeviceName(), device.getSlaveId(), e);
            }
        }

        log.info("===== 所有设备采集结束 =====");
    }
}