package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
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
import java.util.concurrent.TimeUnit;

/**
 * Modbus数据业务层实现类
 * 处理Modbus数据的增删改查、缓存等核心业务逻辑
 */
@Service
public class ModbusDataServiceImpl implements IModbusDataService {
    private static final Logger log = LoggerFactory.getLogger(ModbusDataServiceImpl.class);

    @Resource
    private ModbusDataMapper modbusDataMapper;

    // 注入RedisTemplate
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean checkDataExists(String deviceId, Date readTime) {
        return modbusDataMapper.countByDeviceIdAndReadTime(deviceId, readTime) > 0;
    }


    /**
     * 批量插入Modbus数据（核心补全方法）
     * 保证原子性：要么全插入成功，要么全回滚
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsertModbusData(List<ModbusData> batchDataList) {
        // 空值校验：避免空列表插入
        if (batchDataList == null || batchDataList.isEmpty()) {
            log.warn("批量插入数据为空，跳过入库操作");
            return false;
        }

        // 执行批量入库
        int insertCount = modbusDataMapper.batchInsertModbusData(batchDataList);
        boolean isSuccess = insertCount == batchDataList.size();

        // 日志输出结果
        if (isSuccess) {
            log.info("批量插入Modbus数据成功：共{}条", insertCount);
        } else {
            log.error("批量插入Modbus数据异常：预期插入{}条，实际插入{}条",
                    batchDataList.size(), insertCount);
        }

        return isSuccess;
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

        // Redis无数据/异常时，查询数据库获取所有设备最新数据
        List<ModbusData> dbDataList = modbusDataMapper.selectLatestDataByAllSlaveIds();
        if (!dbDataList.isEmpty()) {
            log.info("从数据库获取所有设备实时数据成功，共{}条", dbDataList.size());
            // 将数据库查询结果缓存到Redis
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

    @Override
    public List<ModbusData> selectModbusDataList(ModbusData modbusData) {

        return modbusDataMapper.selectModbusDataList(modbusData);
    }
}