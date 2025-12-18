package com.ruoyi.datacontrol.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.datacontrol.mapper.DeviceArchiveMapper;
import com.ruoyi.datacontrol.domain.DeviceArchive;
import com.ruoyi.datacontrol.service.IDeviceArchiveService;

@Service
public class DeviceArchiveServiceImpl implements IDeviceArchiveService
{
    @Autowired
    private DeviceArchiveMapper deviceArchiveMapper;

    @Override
    public List<DeviceArchive> selectDeviceArchiveList(DeviceArchive deviceArchive)
    {
        return deviceArchiveMapper.selectDeviceArchiveList(deviceArchive);
    }
}