package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.AlarmHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 10:30 2022/07/11
 */
@Mapper
public interface AlarmHistoryMapper {

    /**
     * 根据id进行删除
     * @param alarmId
     * @return
     */
    int deleteById(Long alarmId);

    /**
     * 新增报警记录
     * @param alarmHistory
     * @return
     */
    int insert(AlarmHistory alarmHistory);

    /**
     * 根据id进行查询
     * @param alarmId
     * @return
     */
    AlarmHistory  selectById(Long alarmId);

    /**
     * 根据id更新
     * @param alarmId
     * @return
     */
    int updateById(Long alarmId);

    /**
     * 查询历史报警记录集合
     * @return
     */
    List<AlarmHistory> selectList();

}
