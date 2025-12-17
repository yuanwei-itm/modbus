package com.ruoyi.datacontrol.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.datacontrol.domain.ModbusData;
import com.ruoyi.datacontrol.service.IModbusDataService;
import com.ruoyi.datacontrol.task.SerialModbusTask; // 引入Task
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    // 注入刚才写的任务类
    @Resource
    private SerialModbusTask serialModbusTask;

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
        return R.ok(realTimeData);
    }

    /**
     * 强制触发采集 (读硬件，速度慢)
     * 前端“立即刷新”按钮调用这个
     */
    @ApiOperation("强制触发硬件采集")
    @PostMapping("/trigger")
    public R<Void> triggerCollection() {
        // 调用加锁的方法，如果此时定时任务正在跑，这里会阻塞等待，直到拿到锁
        serialModbusTask.manualCollect();
        return R.ok();
    }
}