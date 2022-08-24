package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.AlarmRealTime;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 10:20 2022/07/12
 */
public interface IAlarmRealTimeService {


    /**
     * 查询实时报警记录
     * @return
     */
    List<AlarmRealTime> selectList();

    /**
     * 新增实时报警记录
     * @param alarmRealTime
     * @return
     */
    int insert(AlarmRealTime alarmRealTime);

}
