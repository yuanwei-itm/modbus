package com.ruoyi.datacontrol.service;

import java.util.List;
import com.ruoyi.datacontrol.domain.DeviceArchive;

public interface IDeviceArchiveService
{
    /**
     * 查询设备档案列表
     */
    public List<DeviceArchive> selectDeviceArchiveList(DeviceArchive deviceArchive);
}