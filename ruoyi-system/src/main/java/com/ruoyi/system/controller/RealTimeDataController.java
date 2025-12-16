package com.ruoyi.system.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.service.IModbusDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
/**
 * 实时数据控制器
 */
@RestController
@RequestMapping("/api/realtime-data")
public class RealTimeDataController {

    @Resource
    private IModbusDataService modbusDataService;

    /**
     * 获取实时数据
     */
    @GetMapping("/realtime")
    // 返回类型改成 R<List<ModbusData>>
    public R<List<ModbusData>> getRealTimeData() {
        List<ModbusData> realTimeData = modbusDataService.getLatestDataByAllSlaveIds();

        if (realTimeData != null) {
            for (ModbusData data : realTimeData) {
                data.setId(null);
            }
        }
        // 使用 R.ok() 来包装数据
        return R.ok(realTimeData);
    }
}