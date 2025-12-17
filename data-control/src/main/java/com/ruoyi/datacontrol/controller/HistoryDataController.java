package com.ruoyi.datacontrol.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.datacontrol.domain.ModbusData;
import com.ruoyi.datacontrol.domain.ModbusQuery;
import com.ruoyi.datacontrol.service.IModbusDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import java.util.List;

/**
 * 历史数据控制器
 */
@Api(tags = "Modbus历史数据管理")
@RestController
@RequestMapping("/api/history-data")
public class HistoryDataController extends BaseController {

    @Resource
    private IModbusDataService modbusDataService;

    /**
     * 获取历史数据列表
     * @param query 查询参数
     * @return 分页数据
     */
    @ApiOperation("分页查询历史数据列表")
    @GetMapping("/query")
    public TableDataInfo<ModbusData> list(ModbusQuery query) {
        // 开启分页
        startPage();

        // 数据转换：将前端传来的 Query 对象转回 Service 需要的 ModbusData 对象
        ModbusData params = new ModbusData();

        // 处理时间范围
        if (StringUtils.isNotEmpty(query.getStartTime())) {
            params.getParams().put("beginTime", query.getStartTime());
        }

        if (StringUtils.isNotEmpty(query.getEndTime())) {
            params.getParams().put("endTime", query.getEndTime());
        }

        // 查询数据
        List<ModbusData> list = modbusDataService.selectModbusDataList(params);

        // 返回自定义格式
        return getDataTable(list);
    }
}