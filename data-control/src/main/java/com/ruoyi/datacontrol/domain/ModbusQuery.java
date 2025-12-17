package com.ruoyi.datacontrol.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 历史数据查询参数对象
 */
@Data
@EqualsAndHashCode(callSuper = true) // 建议加上这个
public class ModbusQuery extends BaseEntity {


    /** 设备编号
     * @mock dev_001 */
    private String deviceId;

    /** 数据页数
     * @mock 1 */
    private Integer pageNum;

    /** 每页数据条数
     * @mock 5 */
    private Integer pageSize;
}