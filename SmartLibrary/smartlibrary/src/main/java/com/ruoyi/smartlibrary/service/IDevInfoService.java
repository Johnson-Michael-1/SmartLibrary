package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.BookInfo;
import com.ruoyi.smartlibrary.pojo.DevInfo;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 14:23 2022/07/19
 */
public interface IDevInfoService {

    /**
     * 查询设备信息列表集合
     * @param devInfo
     * @return
     */
    List<DevInfo> selectList(DevInfo devInfo);

    /**
     * 新增设备信息
     * @param devInfo
     * @return
     */
    int insert(DevInfo devInfo);

    /**
     * 更新设备信息
     * @param devInfo
     * @return
     */
    int updateDevInfo(DevInfo devInfo);

    /**
     * 根据id获取设备信息
     * @param id
     * @return
     */
    DevInfo getDevInfo(Long id);

    /**
     * 根据id批量删除
     * @param devInfoIds
     * @return
     */
    int deleteDevInfoByIds(Long[] devInfoIds);

    /**
     * 更新设备在线状态
     * @param devSn
     * @return
     */
    Integer updateDevState(String devSn);

    /**
     * 跟据devSn查询设备
     * @param devSn
     * @return
     */
    DevInfo selectDevSn(String devSn);

    /**
     * 查询在线列表
     * @param devInfo
     * @return
     */
    List<DevInfo> selectListDev(DevInfo devInfo);
}
