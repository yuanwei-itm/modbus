package com.ruoyi.datacontrol.domain;

import lombok.Data;

/**
 * 设备档案实体
 */
@Data
public class DeviceArchive {
    // 对应表中device_id（设备唯一标识）
    private String deviceId;

    // 对应表中device_name（设备名称）
    private String deviceName;

    // 对应表中comm_address（起始寄存器地址）
    private Integer commAddress;

    // 对应表中slave_id（Modbus从站ID，唯一）
    private Integer slaveId;

    // 对应表中status（状态：0=启用，1=禁用）
    private String status;

    // 对应表中reg_count（寄存器数量，固定2）
    private Integer regCount;


}