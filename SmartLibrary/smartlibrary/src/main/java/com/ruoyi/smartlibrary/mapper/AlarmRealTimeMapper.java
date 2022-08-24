package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.AlarmRealTime;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmRealTimeMapper {

    /**
     * 根据id进行删除
     * @param alarmId
     * @return
     */
    int deleteById(Long alarmId);

    /**
     * 新增报警记录
     * @param devAlarm
     * @return
     */
    int insert(AlarmRealTime  devAlarm);

    /**
     * 根据id进行查询
     * @param alarmId
     * @return
     */
    AlarmRealTime  selectById(Long alarmId);

    /**
     * 根据id更新
     * @param alarmId
     * @return
     */
    int updateById(Long alarmId);

    /**
     * 查询实时报警记录集合
     * @return
     */
    List<AlarmRealTime> selectList();
}