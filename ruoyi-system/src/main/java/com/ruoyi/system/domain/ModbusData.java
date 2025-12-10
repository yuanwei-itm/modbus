package com.ruoyi.system.domain;

import lombok.Data;
import java.util.Date;

/**
 * 温湿度数据实体（纯MyBatis，无MyBatis-Plus注解）
 */
@Data
public class ModbusData {
    /** 主键ID（自增） */
    private Long id;
    /** 设备名称 */
    private String deviceName;
    /** Modbus从站ID（设备ID） */
    private String slaveId;
    /** 温度（℃） */
    private String temperature;
    /** 湿度（%RH） */
    private String humidity;
    /** 采集时间 */
    private Date readTime;
}