package com.ruoyi.smartlibrary.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.smartlibrary.pojo.Firmware;
import com.ruoyi.smartlibrary.pojo.LoginRecord;

import java.util.List;

/**
 *
 * 固件信息业务逻辑层
 *
 * @author: Mei Pq
 * @description:
 * @date: create in 10:54 2022/08/17
 */
public interface IFirmwareService {

    /**
     * 查询固件信息列表
     * @param firmware
     * @return
     */
    List<Firmware> selectList(Firmware firmware);

    /**
     * 新增固件信息
     * @param firmware
     * @return
     */
    int insert(Firmware firmware);

    /**
     * 更新固件信息
     * @param firmware
     * @return
     */
    int updateFirmware(Firmware firmware);

    /**
     * 根据id获取固件信息
     * @param id
     * @return
     */
    Firmware getFirmware(Long id);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    int deleteFirmwareByIds(Long[] ids);

    /**
     * 获取最新版本
     * @return
     */
    Firmware getLatestVersion();

    /**
     * 请求终端升级
     * @param jsonObject
     * @return
     */
    AjaxResult uploadFirmware(JSONObject jsonObject);
}
