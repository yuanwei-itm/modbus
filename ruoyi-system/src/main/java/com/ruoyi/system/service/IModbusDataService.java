package com.ruoyi.system.service;

import com.ruoyi.system.domain.ModbusData;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 温湿度数据Service（纯MyBatis，无MyBatis-Plus）
 */
public interface IModbusDataService {
    /**
     * 批量插入温湿度数据
     */
    boolean batchInsertModbusData(List<ModbusData> dataList);

    /**
     * 分页查询历史数据（PageHelper）
     */
    Map<String, Object> queryHistoryData(
            Integer pageNum,
            Integer pageSize,
            Date startTime,
            Date endTime
    );
}