package com.ruoyi.system.domain;

import com.alibaba.fastjson.annotation.JSONField; // 用于Redis缓存(FastJSON)
import com.fasterxml.jackson.annotation.JsonFormat; // 用于API返回(Jackson)
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder; // 用于API排序(Jackson)
import lombok.Data;
import java.util.Date;

@Data

@JsonPropertyOrder({ "deviceId", "deviceName", "slaveId", "temperature", "humidity", "readTime" })
public class ModbusData {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JSONField(ordinal = 1)
    private Long id;

    @JSONField(ordinal = 2)
    private String deviceName;

    @JSONField(ordinal = 3)
    private String slaveId;

    @JSONField(ordinal = 4)
    private String deviceId;

    @JSONField(ordinal = 5)
    private String temperature;

    @JSONField(ordinal = 6)
    private String humidity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 99, format = "yyyy-MM-dd HH:mm:ss") // 给 Redis 用
    private Date readTime;

    @JsonIgnore
    @JSONField(serialize = false)
    private Date startTime;

    @JsonIgnore
    @JSONField(serialize = false)
    private Date endTime;
}