package com.ruoyi.datacontrol.service;

import com.ruoyi.datacontrol.domain.ModbusData;
import java.util.Date;
import java.util.List;

public interface IModbusDataService {

    List<ModbusData> getLatestDataByAllSlaveIds();

    boolean checkDataExists(String deviceId, Date readTime);

    List<ModbusData> selectModbusDataList(ModbusData modbusData);

    boolean batchInsertModbusData(List<ModbusData> batchDataList);
}