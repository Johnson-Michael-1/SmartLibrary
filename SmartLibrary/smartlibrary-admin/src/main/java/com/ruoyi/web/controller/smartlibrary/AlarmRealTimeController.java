package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.smartlibrary.pojo.AlarmRealTime;
import com.ruoyi.smartlibrary.service.IAlarmRealTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author: duzhibin
 * @description:
 * @date: create in 10:19 2022/07/12
 */
@RestController
@RequestMapping("/alarmRealTime")
public class AlarmRealTimeController {

    @Autowired
    private IAlarmRealTimeService alarmRealTimeService;

    /**
     * 查询实时报警记录集合
     * @return
     */
    @GetMapping("/list")
    public List<AlarmRealTime> selectList(){
        return alarmRealTimeService.selectList();
    }

    @PostMapping
    public int add(AlarmRealTime alarmRealTime){
        return alarmRealTimeService.insert(alarmRealTime);
    }
}
