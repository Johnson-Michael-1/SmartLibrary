package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.AlarmHistory;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 09:15 2022/07/12
 */
public interface IAlarmHistoryService {

    /**
     * 查询报警历史记录
     * @return
     */
    List<AlarmHistory> selectList();

    /**
     * 新增报警记录
     * @param alarmHistory
     * @return
     */
    int insertAlarm(AlarmHistory alarmHistory);

}
