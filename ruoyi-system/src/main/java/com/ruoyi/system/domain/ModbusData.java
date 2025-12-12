package com.ruoyi.system.domain;

import com.alibaba.fastjson.annotation.JSONField; // 用于Redis缓存(FastJSON)
import com.fasterxml.jackson.annotation.JsonFormat; // 用于API返回(Jackson)
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder; // 用于API排序(Jackson)
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import java.util.Date;

@Data

@JsonPropertyOrder({ "deviceId", "deviceName", "slaveId", "temperature", "humidity", "readTime" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModbusData extends BaseEntity {

    @JSONField(ordinal = 1)
    private Long id;

    @JSONField(ordinal = 2)
    private String deviceId;

    @JSONField(ordinal = 3)
    private String deviceName;

    @JSONField(ordinal = 4)
    private String slaveId;

    @JSONField(ordinal = 5)
    private String temperature;

    @JSONField(ordinal = 6)
    private String humidity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 99, format = "yyyy-MM-dd HH:mm:ss") // 给 Redis 用
    private Date readTime;

}