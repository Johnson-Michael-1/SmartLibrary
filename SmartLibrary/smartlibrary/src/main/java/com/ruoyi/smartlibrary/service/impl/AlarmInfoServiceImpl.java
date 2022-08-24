package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.AlarmInfoMapper;
import com.ruoyi.smartlibrary.pojo.AlarmInfo;
import com.ruoyi.smartlibrary.service.IAlarmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 15:15 2022/07/15
 */
@Service
public class AlarmInfoServiceImpl implements IAlarmInfoService {

    @Autowired
    private AlarmInfoMapper alarmInfoMapper;

    /**
     * 查询报警记录集合
     * @return
     */
    @Override
    public List<AlarmInfo> selectList(AlarmInfo alarmInfo) {
        return alarmInfoMapper.selectList(alarmInfo);
    }

    /**
     * 新增报警信息
     * @param alarmInfo
     * @return
     */
    @Override
    public int insert(AlarmInfo alarmInfo) {
        return alarmInfoMapper.insert(alarmInfo);
    }

    /**
     * 更新报警信息
     * @param alarmInfo
     * @return
     */
    @Override
    public int updateAlarmInfo(AlarmInfo alarmInfo) {
        return alarmInfoMapper.updateById(alarmInfo);
    }

    /**
     * 根据id获取报警信息
     * @param id
     * @return
     */
    @Override
    public AlarmInfo getAlarmInfo(Long id) {
        return alarmInfoMapper.selectById(id);
    }

    /**
     * 根据id批量删除
     * @param alarmInfoIds
     * @return
     */
    @Override
    public int deleteAlarmInfoByIds(Long[] alarmInfoIds) {
        return alarmInfoMapper.deleteByIds(alarmInfoIds);
    }




}
