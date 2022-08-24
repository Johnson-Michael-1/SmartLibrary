package com.ruoyi.web.controller.smartlibrary;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.smartlibrary.pojo.AlarmHistory;
import com.ruoyi.smartlibrary.service.IAlarmHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 09:13 2022/07/12
 */
@RestController
@RequestMapping("/alarmHistory")
public class AlarmHistoryController extends BaseController {

    @Autowired
    private IAlarmHistoryService alarmHistoryService;

    /**
     * 查询历史报警信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:alarmHistory:list')")
    @GetMapping("/list")
    public TableDataInfo alarmHistory(){
        startPage();
        List<AlarmHistory> alarmHistoryList = alarmHistoryService.selectList();
        logger.info("信息"+alarmHistoryList);
        return getDataTable(alarmHistoryList);
    }

    /**
     * 新增历史报警信息
     * @param alarmHistory
     * @return
     */
    @PostMapping
    public  int  add(AlarmHistory alarmHistory){
        return alarmHistoryService.insertAlarm(alarmHistory);
    }

}
