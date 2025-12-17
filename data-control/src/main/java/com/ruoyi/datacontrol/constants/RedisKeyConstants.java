package com.ruoyi.datacontrol.constants;

/**
 * Redis键名常量
 */
public class RedisKeyConstants {
    /** 实时数据缓存键（全部设备实时数据） */
    public static final String MODBUS_REALTIME_ALL_LATEST = "modbus:realtime:all_latest";

    public static final String MODBUS_REALTIME_DEVICE_LATEST = "modbus:realtime:device:%s:latest";
}