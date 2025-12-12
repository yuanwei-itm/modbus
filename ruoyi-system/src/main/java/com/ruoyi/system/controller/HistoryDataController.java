package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.domain.ModbusData;
import com.ruoyi.system.service.IModbusDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 历史数据控制器
 * 处理Modbus历史数据的分页查询请求
 */
@RestController
@RequestMapping("/api/history-data")
public class HistoryDataController extends BaseController {

    @Resource
    private IModbusDataService modbusDataService;

    @GetMapping("/query")
    public Map<String, Object> list(ModbusData modbusData) {
        //开启分页
        startPage();

        //查询数据
        List<ModbusData> list = modbusDataService.selectModbusDataList(modbusData);

        //返回自定义格式
        return getMyPageData(list);
    }
}