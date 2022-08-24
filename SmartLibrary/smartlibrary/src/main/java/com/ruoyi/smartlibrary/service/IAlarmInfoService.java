package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.AlarmInfo;
import com.ruoyi.smartlibrary.pojo.BookInfo;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 15:14 2022/07/15
 */
public interface IAlarmInfoService {

    /**
     * 查询报警记录
     * @return
     */
    List<AlarmInfo> selectList(AlarmInfo alarmInfo);

    /**
     * 新增报警信息
     * @param alarmInfo
     * @return
     */
    int insert(AlarmInfo alarmInfo);

    /**
     * 更新报警信息
     * @param alarmInfo
     * @return
     */
    int updateAlarmInfo(AlarmInfo alarmInfo);

    /**
     * 根据id获取报警信息
     * @param id
     * @return
     */
    AlarmInfo getAlarmInfo(Long id);

    /**
     * 根据id批量删除
     * @param alarmInfoIds
     * @return
     */
    int deleteAlarmInfoByIds(Long[] alarmInfoIds);
}
