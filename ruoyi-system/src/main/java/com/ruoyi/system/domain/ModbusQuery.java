package com.ruoyi.system.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 历史数据查询参数对象
 * * 只包含前端允许传入的筛选条件
 */
@Data
public class ModbusQuery implements Serializable {

    /** 开始时间 (格式：2025-12-01 10:00:00) */
    private String startTime;

    /** 结束时间 (格式：2025-12-01 12:00:00) */
    private String endTime;
    private Integer pageNum;
    private Integer pageSize;
}