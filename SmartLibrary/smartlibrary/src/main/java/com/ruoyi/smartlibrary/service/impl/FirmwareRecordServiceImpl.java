package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.FirmwareRecordMapper;
import com.ruoyi.smartlibrary.pojo.FirmwareRecord;
import com.ruoyi.smartlibrary.service.IFirmwareRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 11:56 2022/08/17
 */
@Service
public class FirmwareRecordServiceImpl implements IFirmwareRecordService {

    @Autowired
    private FirmwareRecordMapper firmwareRecordMapper;

    /**
     * 查询固件更新记录
     * @param firmwareRecord
     * @return
     */
    @Override
    public List<FirmwareRecord> selectList(FirmwareRecord firmwareRecord) {
        return firmwareRecordMapper.selectList(firmwareRecord);
    }

    /**
     * 新增固件更新记录
     * @param firmwareRecord
     * @return
     */
    @Override
    public int insert(FirmwareRecord firmwareRecord) {
        return firmwareRecordMapper.insert(firmwareRecord);
    }

    /**
     * 根据id获取固件更新记录详细
     * @param id
     * @return
     */
    @Override
    public FirmwareRecord getFirmwareRecord(Long id) {
        return firmwareRecordMapper.selectById(id);
    }
}
