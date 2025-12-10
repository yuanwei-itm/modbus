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

@Service
public class ModbusDataServiceImpl implements IModbusDataService {
    private static final Logger log = LoggerFactory.getLogger(ModbusDataServiceImpl.class);

    @Resource
    private ModbusDataMapper modbusDataMapper;

    // 注入RedisTemplate（Spring自动配置）
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 批量插入数据（同时缓存最新一条到Redis）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsertModbusData(List<ModbusData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            log.warn("插入数据为空，无需操作");
            return false;
        }
        // 1. 入库
        int insertCount = modbusDataMapper.batchInsertModbusData(dataList);
        boolean success = insertCount == dataList.size();
        log.info("批量插入{}条数据，成功{}条", dataList.size(), insertCount);

        // 2. 若入库成功，将最新一条数据缓存到Redis（假设list中最后一条是最新的）
        if (success) {
            ModbusData latestData = dataList.get(dataList.size() - 1);
            try {
                // 序列化数据为JSON字符串存入Redis，设置过期时间（可选，如1小时，避免缓存膨胀）
                redisTemplate.opsForValue().set(
                        RedisKeyConstants.MODBUS_REALTIME_LATEST,
                        JSON.toJSONString(latestData),
                        1, TimeUnit.HOURS
                );
                log.info("实时数据已缓存到Redis，slaveId={}, time={}",
                        latestData.getSlaveId(), latestData.getReadTime());
            } catch (Exception e) {
                // 缓存失败不影响主流程（入库成功即可），仅日志记录
                log.error("Redis缓存实时数据失败", e);
            }
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertModbusData(ModbusData modbusData) {
        if (modbusData == null) {
            log.warn("单条插入数据为空，跳过操作");
            return 0;
        }

        // 1. 调用Mapper插入单条数据
        int insertCount = modbusDataMapper.insert(modbusData);
        log.info("单条插入数据（slaveId={}）：{}", modbusData.getSlaveId(), insertCount > 0 ? "成功" : "失败");

        // 2. 插入成功后，更新Redis缓存（与批量插入逻辑一致，缓存最新数据）
        if (insertCount > 0) {
            try {
                redisTemplate.opsForValue().set(
                        RedisKeyConstants.MODBUS_REALTIME_LATEST,
                        JSON.toJSONString(modbusData),
                        1, TimeUnit.HOURS
                );
                log.info("单条数据已缓存到Redis，slaveId={}, 时间={}",
                        modbusData.getSlaveId(), modbusData.getReadTime());
            } catch (Exception e) {
                log.error("单条数据Redis缓存失败", e);
                // 缓存失败不影响入库主流程，仅记录日志
            }
        }

        return insertCount;
    }
    /**
     * 查询最新一条数据（优先从Redis获取，失败则查库）
     */
    @Override
    public ModbusData getLatestData() {
        try {
            // 1. 先从Redis查询
            String jsonData = redisTemplate.opsForValue().get(RedisKeyConstants.MODBUS_REALTIME_LATEST);
            if (jsonData != null && !jsonData.isEmpty()) {
                log.info("从Redis获取实时数据成功");
                return JSON.parseObject(jsonData, ModbusData.class);
            }
        } catch (Exception e) {
            // Redis异常（如连接失败），不抛出异常，继续查库
            log.error("Redis查询实时数据失败，将查询数据库", e);
        }

        // 2. Redis无数据或异常，查数据库兜底
        ModbusData dbData = modbusDataMapper.selectLatestData();
        if (dbData != null) {
            log.info("从数据库获取实时数据成功");
            // 兜底后同步到Redis（可选，避免下次仍查库）
            try {
                redisTemplate.opsForValue().set(
                        RedisKeyConstants.MODBUS_REALTIME_LATEST,
                        JSON.toJSONString(dbData),
                        1, TimeUnit.HOURS
                );
            } catch (Exception e) {
                log.error("兜底数据同步Redis失败", e);
            }
        } else {
            log.warn("数据库中无实时数据");
        }
        return dbData;
    }

    /**
     * 分页查询历史数据（PageHelper）
     * 关键修改：返回类型从 Map 改为 PageResultVO，结果封装适配VO
     */
    @Override
    public PageInfo<ModbusData> queryHistoryData(
            Integer pageNum,
            Integer pageSize,
            Date startTime,
            Date endTime,
            Integer slaveId
    ) {
        // 1. 参数校验
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        // 2. 时间范围限制
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            if (startTime.after(endTime)) {
                throw new IllegalArgumentException("开始时间不能晚于结束时间");
            }
            long diffDays = (endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays > 30) {
                throw new IllegalArgumentException("查询区间不能超过30天");
            }
        }

        // 3. PageHelper分页（核心：startPage后紧跟查询语句，PageHelper自动拦截）
        PageHelper.startPage(pageNum, pageSize);
        // 4. 调用Mapper查询（XML无需写LIMIT，PageHelper自动拼接）
        List<ModbusData> dataList = modbusDataMapper.selectHistoryDataByTimeRange(startTime, endTime, slaveId);
        // 5. 封装PageInfo（包含总条数、总页数、当前页等全量分页信息）
        PageInfo<ModbusData> pageInfo = new PageInfo<>(dataList);

        log.info("分页查询历史数据：页码{}，页大小{}，总条数{}", pageNum, pageSize, pageInfo.getTotal());
        return pageInfo;
    }
}
