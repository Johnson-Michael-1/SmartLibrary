package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.AlarmRealTimeMapper;
import com.ruoyi.smartlibrary.pojo.AlarmRealTime;
import com.ruoyi.smartlibrary.service.IAlarmRealTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 10:21 2022/07/12
 */
@Service
public class AlarmRealTimeServiceImpl implements IAlarmRealTimeService {

    @Autowired
    public AlarmRealTimeMapper alarmRealTimeMapper;

    /**
     * 查询实时报警记录
     * @return
     */
    @Override
    public List<AlarmRealTime> selectList() {
        return alarmRealTimeMapper.selectList();
    }

    /**
     * 新增实时报警记录
     * @param alarmRealTime
     * @return
     */
    @Override
    public int insert(AlarmRealTime alarmRealTime) {
        return alarmRealTimeMapper.insert(alarmRealTime);
    }


}
