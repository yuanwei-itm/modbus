package com.ruoyi.system.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.service.IModbusDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

// 实时数据专属Controller
@RestController
@RequestMapping("/api/realtime-data") // 新的接口路径（替换原来的/history-data）
public class RealTimeDataController {

    @Resource
    private IModbusDataService modbusDataService;

    // 实时数据查询接口（从原来的HistoryDataController移过来）
    @GetMapping("/realtime")
    public AjaxResult getRealTimeData() {
        List<ModbusData> realTimeData = modbusDataService.getLatestDataByAllSlaveIds();
        return AjaxResult.success("获取实时数据成功", realTimeData);
    }

    // 后续如果有其他实时数据接口（比如最近N条），也可以放在这个类里
}