package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IModbusDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 温湿度历史数据接口（纯MyBatis+PageHelper）
 */
@RestController
@RequestMapping("/api/history-data")
public class HistoryDataController extends BaseController {

    @Resource
    private IModbusDataService modbusDataService;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 分页查询历史数据
     * 示例：/api/history-data/query?pageNum=1&pageSize=10&startTime=2025-12-01 00:00:00&endTime=2025-12-08 23:59:59
     */
    @GetMapping("/query")
    public AjaxResult queryHistoryData(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime
    ) throws ParseException {
        // 解析时间参数
        Date start = (startTime == null || startTime.trim().isEmpty()) ? null : SDF.parse(startTime);
        Date end = (endTime == null || endTime.trim().isEmpty()) ? null : SDF.parse(endTime);

        // 调用Service查询
        Map<String, Object> result = modbusDataService.queryHistoryData(pageNum, pageSize, start, end);
        return AjaxResult.success(result);
    }

    /**
     * 实时数据接口（获取最新一条温湿度）
     * 示例：/api/history-data/realtime
     */
    @GetMapping("/realtime")
    public AjaxResult getRealtimeData() {
        // 查询最新一条数据（需在Mapper/Service新增方法）
        // 此处简化：实际需补充Mapper的selectLatestData方法
        return AjaxResult.success("实时数据接口待补充（基于selectLatestData实现）");
    }
}