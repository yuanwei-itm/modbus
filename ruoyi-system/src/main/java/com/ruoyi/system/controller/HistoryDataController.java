package com.ruoyi.system.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.service.IModbusDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 历史数据控制器
 * 处理Modbus历史数据的分页查询请求
 */
@RestController
@RequestMapping("/api/history-data")
public class HistoryDataController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(HistoryDataController.class);

    @Resource
    private IModbusDataService modbusDataService;

    // 使用ThreadLocal包装
    private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    );

    /**
     * 分页查询历史数据
     * 示例：/api/history-data/query?pageNum=1&pageSize=10&startTime=2025-12-01 00:00:00&endTime=2025-12-08 23:59:59&slaveId=1&deviceId=dev001
     *
     * @param pageNum   当前页码（默认1）
     * @param pageSize  每页条数（默认10）
     * @param startTime 开始时间（格式：yyyy-MM-dd HH:mm:ss，可选）
     * @param endTime   结束时间（格式：yyyy-MM-dd HH:mm:ss，可选）
     * @param slaveId   从站ID（可选）
     * @param deviceId  设备ID（可选，精准筛选单个设备）
     */
    @GetMapping("/query")
    public AjaxResult queryHistoryData(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer slaveId,
            @RequestParam(required = false) String deviceId
    ) {
        // 时间参数解析
        Date start = null;
        Date end = null;
        try {
            if (startTime != null && !startTime.trim().isEmpty()) {
                start = SDF.get().parse(startTime.trim());
            }
            if (endTime != null && !endTime.trim().isEmpty()) {
                end = SDF.get().parse(endTime.trim());
            }
        } catch (ParseException e) {
            log.error("时间参数解析失败：startTime={}, endTime={}", startTime, endTime, e);
            return AjaxResult.error("时间格式错误，请使用：yyyy-MM-dd HH:mm:ss");
        }

        // 打印请求参数日志，便于调试
        log.info("接收历史数据查询请求：pageNum={}, pageSize={}, startTime={}, endTime={}, slaveId={}, deviceId={}",
                pageNum, pageSize, startTime, endTime, slaveId, deviceId);

        try {
            // 调用Service层
            PageInfo<ModbusData> pageInfo = modbusDataService.queryHistoryData(
                    pageNum, pageSize, start, end, slaveId, deviceId
            );

            // 封装分页结果
            Map<String, Object> data = new HashMap<>();
            data.put("pagenum", pageNum);
            data.put("pagesize", pageSize);
            data.put("list", pageInfo.getList());       // 当前页数据
            data.put("total", pageInfo.getTotal());     // 总条数
            data.put("pages", pageInfo.getPages());     // 总页数（可选补充）
            data.put("hasNextPage", pageInfo.isHasNextPage()); // 是否有下一页（可选补充）

            log.info("历史数据查询成功：总条数={}，当前页数据={}条", pageInfo.getTotal(), pageInfo.getList().size());
            return AjaxResult.success(data);
        } catch (IllegalArgumentException e) {
            // 捕获Service层的参数校验异常（如时间区间超过30天）
            log.error("历史数据查询参数异常", e);
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            log.error("历史数据查询失败", e);
            return AjaxResult.error("历史数据查询失败，请联系管理员");
        }
    }
}