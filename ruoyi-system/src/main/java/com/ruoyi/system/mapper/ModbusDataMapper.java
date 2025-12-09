package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ModbusData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;


@Mapper
public interface ModbusDataMapper {
    /**
     * 批量插入温湿度数据
     */
    int batchInsertModbusData(List<ModbusData> dataList);

    /**
     * 按时间范围查询历史数据（PageHelper分页）
     */
    List<ModbusData> selectHistoryDataByTimeRange(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );
}