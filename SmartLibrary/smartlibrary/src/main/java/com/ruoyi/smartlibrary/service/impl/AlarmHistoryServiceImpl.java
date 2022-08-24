package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.AlarmHistoryMapper;
import com.ruoyi.smartlibrary.pojo.AlarmHistory;
import com.ruoyi.smartlibrary.service.IAlarmHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 09:17 2022/07/12
 */
@Service
public class AlarmHistoryServiceImpl implements IAlarmHistoryService {

    @Autowired
    private AlarmHistoryMapper alarmHistoryMapper;

    /**
     * 查询报警记录
     * @return
     */
    @Override
    public List<AlarmHistory> selectList() {
        return alarmHistoryMapper.selectList();
    }

    /**
     * 新增报警记录
     * @return
     */
    @Override
    public int insertAlarm(AlarmHistory alarmHistory) {
        return alarmHistoryMapper.insert(alarmHistory);
    }


}
