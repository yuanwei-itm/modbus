package com.ruoyi.system.service;

import com.github.pagehelper.PageInfo;
import com.ruoyi.system.domain.ModbusData;
import java.util.Date;
import java.util.List;

public interface IModbusDataService {
    boolean batchInsertModbusData(List<ModbusData> dataList);

    int insertModbusData(ModbusData modbusData);

    ModbusData getLatestData();

    // 修正返回类型为PageInfo
    PageInfo<ModbusData> queryHistoryData(Integer pageNum, Integer pageSize, Date startTime, Date endTime, Integer slaveId);
}