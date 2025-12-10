package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.system.constants.RedisKeyConstants;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.mapper.ModbusDataMapper;
import com.ruoyi.system.service.IModbusDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Modbus数据业务层实现类
 * 处理Modbus数据的增删改查、缓存等核心业务逻辑
 *
 */
@Service
public class ModbusDataServiceImpl implements IModbusDataService {
    private static final Logger log = LoggerFactory.getLogger(ModbusDataServiceImpl.class);

    @Resource
    private ModbusDataMapper modbusDataMapper;

    // 注入RedisTemplate（Spring自动配置，用于缓存设备实时数据）
    @Resource
    private RedisTemplate<String, String> redisTemplate;



    // 改造后的单条插入方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertModbusData(ModbusData modbusData) {
        // 空数据校验：避免插入空对象
        if (modbusData == null) {
            log.warn("单条插入数据为空，跳过入库操作");
            return 0;
        }

        // 执行单条数据入库
        int insertCount = modbusDataMapper.insert(modbusData);
        log.info("单条插入Modbus数据（slaveId={}）：{}",
                modbusData.getSlaveId(), insertCount > 0 ? "成功" : "失败");

        // 直接返回插入结果：1=成功，0=失败
        return insertCount;
    }

    /**
     * 查询所有设备各自的最新一条数据（优先从Redis获取，缓存失效则查库兜底）
     *
     * @return 所有设备的最新数据列表（按slaveId升序排列）
     */
    @Override
    public List<ModbusData> getLatestDataByAllSlaveIds() {
        try {
            // 1. 优先从Redis读取所有设备最新数据缓存
            String jsonData = redisTemplate.opsForValue().get(RedisKeyConstants.MODBUS_REALTIME_ALL_LATEST);
            if (jsonData != null && !jsonData.isEmpty()) {
                List<ModbusData> cacheData = JSON.parseArray(jsonData, ModbusData.class);
                log.info("从Redis获取所有设备实时数据成功，共{}条", cacheData.size());
                return cacheData;
            }
        } catch (Exception e) {
            log.error("Redis查询所有设备实时数据失败，将查询数据库兜底", e);
        }

        // 2. Redis无数据/异常时，查询数据库获取所有设备最新数据
        List<ModbusData> dbDataList = modbusDataMapper.selectLatestDataByAllSlaveIds();
        if (!dbDataList.isEmpty()) {
            log.info("从数据库获取所有设备实时数据成功，共{}条", dbDataList.size());
            // 3. 将数据库查询结果缓存到Redis（有效期1小时）
            try {
                redisTemplate.opsForValue().set(
                        RedisKeyConstants.MODBUS_REALTIME_ALL_LATEST,
                        JSON.toJSONString(dbDataList),
                        1, TimeUnit.HOURS
                );
            } catch (Exception e) {
                log.error("数据库兜底数据同步Redis失败", e);
            }
        } else {
            log.warn("数据库中无任何设备的实时数据");
        }
        return dbDataList;
    }

    /**
     * 分页查询Modbus历史数据（支持时间范围、设备ID筛选）
     *
     * @param pageNum    当前页码（默认1）
     * @param pageSize   每页条数（默认10）
     * @param startTime  采集开始时间（可选）
     * @param endTime    采集结束时间（可选）
     * @param slaveId    设备ID（可选）
     * @return 分页结果对象（包含总条数、当前页数据、分页元信息）
     */
    @Override
    public PageInfo<ModbusData> queryHistoryData(
            Integer pageNum,
            Integer pageSize,
            Date startTime,
            Date endTime,
            Integer slaveId
    ) {
        // 1. 分页参数校验（默认值填充）
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        // 2. 时间范围合法性校验（最大查询区间30天）
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            if (startTime.after(endTime)) {
                throw new IllegalArgumentException("查询条件异常：开始时间不能晚于结束时间");
            }
            long diffDays = (endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays > 30) {
                throw new IllegalArgumentException("查询条件异常：时间区间不能超过30天");
            }
        }

        // 3. PageHelper启动分页（紧跟查询语句，自动拼接LIMIT）
        PageHelper.startPage(pageNum, pageSize);
        // 4. 执行条件查询
        List<ModbusData> dataList = modbusDataMapper.selectHistoryDataByTimeRange(startTime, endTime, slaveId);
        // 5. 封装分页结果（包含总条数、总页数等元信息）
        PageInfo<ModbusData> pageInfo = new PageInfo<>(dataList);

        log.info("分页查询Modbus历史数据：页码{}，页大小{}，总条数{}",
                pageNum, pageSize, pageInfo.getTotal());
        return pageInfo;
    }
}