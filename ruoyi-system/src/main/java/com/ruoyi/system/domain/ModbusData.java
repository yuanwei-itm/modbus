package com.ruoyi.system.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data

@JsonPropertyOrder({ "id","deviceId", "deviceName", "slaveId", "temperature", "humidity", "readTime" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModbusData extends BaseEntity {

    /**
     * 数据库主键id
     * @mock 1
     */
    @ApiModelProperty("设备编号")
    @JSONField(ordinal = 1) // Redis序列化字段排序
    private Long id;

    /**
     * 设备编号
     * @mock dev001
     */
    @ApiModelProperty("设备编号")
    @JSONField(ordinal = 2)
    private String deviceId;

    /**
     * 设备名称
     * @mock 温湿度传感器A
     */
    @ApiModelProperty("设备名称")
    @JSONField(ordinal = 3)
    private String deviceName;

    /**
     * Modbus Slave ID
     * @mock 3
     */
    @ApiModelProperty("Modbus Slave ID")
    @JSONField(ordinal = 4)
    private String slaveId;

    /**
     * 温度
     * @mock 23.5
     */
    @ApiModelProperty("温度")
    @JSONField(ordinal = 5)
    private String temperature;

    /**
     * 湿度
     * @mock 45.2
     */
    @ApiModelProperty("湿度")
    @JSONField(ordinal = 6)
    private String humidity;

    /**
     * 读取时间
     * @mock 2025-12-01 11:30:00
     */
    @ApiModelProperty("读取时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // API返回时间格式化
    @JSONField(ordinal = 99, format = "yyyy-MM-dd HH:mm:ss") // Redis序列化时间格式化
    private Date readTime;

}