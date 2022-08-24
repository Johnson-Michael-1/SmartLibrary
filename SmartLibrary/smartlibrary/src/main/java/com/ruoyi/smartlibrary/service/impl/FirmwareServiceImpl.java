package com.ruoyi.smartlibrary.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.smartlibrary.mapper.FirmwareMapper;
import com.ruoyi.smartlibrary.pojo.Firmware;
import com.ruoyi.smartlibrary.service.IFirmwareService;
import com.ruoyi.smartlibrary.socket.WsServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 10:59 2022/08/17
 */
@Service
public class FirmwareServiceImpl implements IFirmwareService {

    @Autowired
    private FirmwareMapper firmwareMapper;

    /**
     * 查询固件信息列表
     * @param firmware
     * @return
     */
    @Override
    public List<Firmware> selectList(Firmware firmware) {
        return firmwareMapper.selectList(firmware);
    }

    /**
     * 新增固件信息
     * @param firmware
     * @return
     */
    @Override
    public int insert(Firmware firmware) {
        firmware.setUploadTime(new Date());
        return firmwareMapper.insert(firmware);
    }

    /**
     * 更新固件信息
     * @param firmware
     * @return
     */
    @Override
    public int updateFirmware(Firmware firmware) {
        return firmwareMapper.updateById(firmware);
    }

    /**
     * 根据id获取固件信息
     * @param id
     * @return
     */
    @Override
    public Firmware getFirmware(Long id) {
        return firmwareMapper.selectById(id);
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @Override
    public int deleteFirmwareByIds(Long[] ids) {
        return firmwareMapper.deleteByIds(ids);
    }

    /**
     * 获取最新版本
     * @return
     */
    @Override
    public Firmware getLatestVersion() {
        return firmwareMapper.getLatestVersion();
    }

    /**
     * 发送终端升级
     * @param jsonObject
     * @return
     */
    @Override
    public AjaxResult uploadFirmware(JSONObject jsonObject) {
        WsServerEndpoint wsServerEndpoint = new WsServerEndpoint();
        Boolean flag = wsServerEndpoint.upload(jsonObject);
        if (flag){
            return AjaxResult.success("发送成功");
        }else {
            return AjaxResult.error("发送失败");
        }
    }



}
