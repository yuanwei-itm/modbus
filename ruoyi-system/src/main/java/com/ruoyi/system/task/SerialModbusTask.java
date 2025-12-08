package com.ruoyi.system.task;

import com.ruoyi.common.utils.SerialModbusUtils;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.service.IModbusDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Modbus温湿度采集定时任务（纯MyBatis，无MyBatis-Plus）
 */
@Component
public class SerialModbusTask {
    private static final Logger log = LoggerFactory.getLogger(SerialModbusTask.class);

    @Resource
    private SerialModbusUtils serialModbusUtils;
    @Resource
    private IModbusDataService modbusDataService;

    /**
     * 每5秒采集一次（cron：秒 分 时 日 月 周 年）
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void collectModbusData() {
        try {
            log.info("===== 开始采集温湿度数据 =====");
            // 1. 读取寄存器值
            int[] registerValues = serialModbusUtils.readModbusSlaveData();
            if (registerValues == null || registerValues.length < 2) {
                log.warn("寄存器值异常，长度：{}", registerValues == null ? 0 : registerValues.length);
                return;
            }

            // 2. 解析为温湿度（设备协议：寄存器值×10=实际值）
            double temperature = registerValues[0] / 10.0; // 寄存器0=温度
            double humidity = registerValues[1] / 10.0;    // 寄存器1=湿度
            log.info("采集到：温度={}℃，湿度={}%RH", temperature, humidity);

            // 3. 封装数据
            List<ModbusData> dataList = new ArrayList<>();
            ModbusData data = new ModbusData();
            data.setSlaveId(serialModbusUtils.getSlaveId());
            data.setTemperature(temperature);
            data.setHumidity(humidity);
            data.setReadTime(new Date());
            dataList.add(data);

            // 4. 批量入库
            modbusDataService.batchInsertModbusData(dataList);
            log.info("===== 温湿度数据入库成功 =====");

        } catch (Exception e) {
            log.error("温湿度采集任务失败", e);
        }
    }
}