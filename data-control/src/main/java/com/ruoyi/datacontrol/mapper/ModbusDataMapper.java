package com.ruoyi.datacontrol.mapper;

import com.ruoyi.datacontrol.domain.ModbusData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;


@Mapper
public interface ModbusDataMapper {

    List<ModbusData> selectLatestDataByAllSlaveIds();

    int countByDeviceIdAndReadTime(@Param("deviceId") String deviceId, @Param("readTime") Date readTime);
    int batchInsertModbusData(List<ModbusData> batchDataList);

    List<ModbusData> selectModbusDataList(ModbusData modbusData);
}
