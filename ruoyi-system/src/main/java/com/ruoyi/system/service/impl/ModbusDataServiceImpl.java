package com.ruoyi.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.mapper.ModbusDataMapper;
import com.ruoyi.system.service.IModbusDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 温湿度数据Service实现（纯MyBatis+PageHelper）
 */
@Service
public class ModbusDataServiceImpl implements IModbusDataService {
    private static final Logger log = LoggerFactory.getLogger(ModbusDataServiceImpl.class);

    @Resource
    private ModbusDataMapper modbusDataMapper;

    /**
     * 批量插入（事务控制：失败回滚）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsertModbusData(List<ModbusData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            log.warn("插入数据为空，无需操作");
            return false;
        }
        int insertCount = modbusDataMapper.batchInsertModbusData(dataList);
        boolean success = insertCount == dataList.size();
        if (success) {
            log.info("批量插入{}条温湿度数据成功", insertCount);
        } else {
            log.error("批量插入失败：预期{}条，实际{}条", dataList.size(), insertCount);
        }
        return success;
    }

    /**
     * 分页查询历史数据（PageHelper）
     */
    @Override
    public Map<String, Object> queryHistoryData(
            Integer pageNum,
            Integer pageSize,
            Date startTime,
            Date endTime
    ) {
        // 参数校验
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        // 时间范围限制（最多30天）
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            if (startTime.after(endTime)) {
                throw new IllegalArgumentException("开始时间不能晚于结束时间");
            }
            long diffDays = (endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays > 30) {
                throw new IllegalArgumentException("查询区间不能超过30天");
            }
        }

        // PageHelper分页（纯MyBatis核心）
        PageHelper.startPage(pageNum, pageSize);
        List<ModbusData> dataList = modbusDataMapper.selectHistoryDataByTimeRange(startTime, endTime);
        PageInfo<ModbusData> pageInfo = new PageInfo<>(dataList);

        // 封装分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageInfo.getTotal());    // 总条数
        result.put("rows", pageInfo.getList());      // 当前页数据
        result.put("pages", pageInfo.getPages());    // 总页数
        result.put("current", pageNum);              // 当前页
        result.put("size", pageSize);                // 每页条数
        return result;
    }
}