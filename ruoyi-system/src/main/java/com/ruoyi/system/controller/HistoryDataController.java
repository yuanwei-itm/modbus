package com.ruoyi.system.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.service.IModbusDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer slaveId // 新增：设备ID参数
    ) throws ParseException {
        // 解析时间参数（逻辑不变）
        Date start = (startTime == null || startTime.trim().isEmpty()) ? null : SDF.parse(startTime);
        Date end = (endTime == null || endTime.trim().isEmpty()) ? null : SDF.parse(endTime);

        //调用ruoyi自带的pageinfo进行结果的分页
        PageInfo<ModbusData> result = modbusDataService.queryHistoryData(pageNum, pageSize, start, end,slaveId);
        return AjaxResult.success(result); // 直接返回VO，自动封装为目标格式
    }

}