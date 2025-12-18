package com.ruoyi.datacontrol.mapper;

import com.ruoyi.datacontrol.domain.DeviceArchive;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 设备档案Mapper
 */
@Mapper
public interface DeviceArchiveMapper {

    /**
     * 查询设备档案列表
     * * @param deviceArchive 设备档案信息
     * @return 设备档案集合
     */
    List<DeviceArchive> selectDeviceArchiveList(DeviceArchive deviceArchive);

    /**
     * 查询所有启用的设备（status=0）
     * @return 设备列表（你的表中3条启用数据）
     */
    List<DeviceArchive> selectEnabledDevices();
}