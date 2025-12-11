package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ModbusData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;


@Mapper
public interface ModbusDataMapper {


    /**
     * 按时间范围查询历史数据（PageHelper分页）
     */
    List<ModbusData> selectHistoryDataByTimeRange(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("slaveId") Integer slaveId // 新增：按设备ID筛选
    );

    List<ModbusData> selectLatestDataByAllSlaveIds();

    int insert(ModbusData modbusData);

    int countByDeviceIdAndReadTime(@Param("deviceId") String deviceId, @Param("readTime") Date readTime);
    int batchInsertModbusData(List<ModbusData> batchDataList);
}
