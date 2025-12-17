package com.ruoyi.datacontrol.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.datacontrol.domain.ModbusData;
import com.ruoyi.datacontrol.service.IModbusDataService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.util.List;
/**
 * 实时数据控制器
 */
@Api(tags = "Modbus实时数据管理")
@RestController
@RequestMapping("/api/realtime-data")
public class RealTimeDataController {

    @Resource
    private IModbusDataService modbusDataService;

    /**
     * 获取实时数据
     */
    @ApiOperation("获取实时数据")
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