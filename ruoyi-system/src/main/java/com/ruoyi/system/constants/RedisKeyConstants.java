package com.ruoyi.system.constants;

/**
 * Redis键名常量
 */
public class RedisKeyConstants {
    /** 实时数据缓存键（全部设备实时数据） */
    public static final String MODBUS_REALTIME_ALL_LATEST = "modbus:realtime:all_latest";

    /** 单设备最新数据的String缓存键 */
    public static final String MODBUS_REALTIME_SLAVE_LATEST = "modbus:realtime:slave_{}";
}