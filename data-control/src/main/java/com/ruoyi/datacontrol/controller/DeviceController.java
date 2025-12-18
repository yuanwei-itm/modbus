package com.ruoyi.datacontrol.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.datacontrol.domain.DeviceArchive;
import com.ruoyi.datacontrol.service.IDeviceArchiveService;

@RestController
@RequestMapping("/datacontrol/device")
public class DeviceController extends BaseController
{
    @Autowired
    private IDeviceArchiveService deviceArchiveService;

    /**
     * 查询设备档案列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DeviceArchive deviceArchive)
    {
        startPage();
        List<DeviceArchive> list = deviceArchiveService.selectDeviceArchiveList(deviceArchive);
        return getDataTable(list);
    }
}