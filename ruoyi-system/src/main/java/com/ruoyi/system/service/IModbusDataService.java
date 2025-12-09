package com.ruoyi.system.service;

import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.domain.vo.PageResultVO; // 新增：导入VO类
import java.util.Date;
import java.util.List;

public interface IModbusDataService {
    /**
     * 批量插入温湿度数据
     */
    boolean batchInsertModbusData(List<ModbusData> dataList);

    /**
     * 分页查询历史数据（PageHelper）
     * 注意：返回类型从 Map<String, Object> 改为 PageResultVO
     */
    PageResultVO queryHistoryData(
            Integer pageNum,
            Integer pageSize,
            Date startTime,
            Date endTime
    );

    ModbusData getLatestData();
}