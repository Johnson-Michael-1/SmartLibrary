package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.Firmware;
import com.ruoyi.smartlibrary.pojo.FirmwareRecord;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 11:51 2022/08/17
 */
public interface IFirmwareRecordService {

    /**
     * 查询固件更新记录
     * @param firmwareRecord
     * @return
     */
    List<FirmwareRecord> selectList(FirmwareRecord firmwareRecord);

    /**
     * 新增固件更新记录
     * @param firmwareRecord
     * @return
     */
    int insert(FirmwareRecord firmwareRecord);

    /**
     * 根据id获取固件更新记录详细
     * @param id
     * @return
     */
    FirmwareRecord getFirmwareRecord(Long id);


}
