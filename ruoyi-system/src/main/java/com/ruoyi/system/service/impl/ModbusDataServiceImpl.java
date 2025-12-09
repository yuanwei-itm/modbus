package com.ruoyi.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.mapper.ModbusDataMapper;
import com.ruoyi.system.service.IModbusDataService;
import com.ruoyi.system.domain.vo.PageResultVO; // 新增：导入分页VO
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
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
        // 原有逻辑不变...
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
     * 关键修改：返回类型从 Map 改为 PageResultVO，结果封装适配VO
     */
    @Override
    public PageResultVO queryHistoryData(  // 返回类型修改为 PageResultVO
                                           Integer pageNum,
                                           Integer pageSize,
                                           Date startTime,
                                           Date endTime
    ) {
        // 1. 参数校验（原有逻辑不变）
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        // 2. 时间范围限制（原有逻辑不变）
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            if (startTime.after(endTime)) {
                throw new IllegalArgumentException("开始时间不能晚于结束时间");
            }
            long diffDays = (endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays > 30) {
                throw new IllegalArgumentException("查询区间不能超过30天");
            }
        }

        // 3. PageHelper分页查询（原有逻辑不变）
        PageHelper.startPage(pageNum, pageSize);
        List<ModbusData> dataList = modbusDataMapper.selectHistoryDataByTimeRange(startTime, endTime);
        PageInfo<ModbusData> pageInfo = new PageInfo<>(dataList);

        // 4. 封装结果为 PageResultVO（核心修改：替换Map为VO）
        return new PageResultVO(
                pageNum,                // pagenum（当前页码）
                pageSize,               // pagesize（每页条数）
                pageInfo.getTotal(),    // total（总条数）
                pageInfo.getList()      // list（数据列表）
        );
    }
}