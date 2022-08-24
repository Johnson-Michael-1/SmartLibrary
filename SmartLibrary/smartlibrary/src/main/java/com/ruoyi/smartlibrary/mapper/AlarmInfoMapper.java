package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.AlarmInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 15:06 2022/07/15
 */
@Mapper
public interface AlarmInfoMapper {

    /**
     * 查询报警记录集合
     * @param alarmInfo
     * @return
     */
    List<AlarmInfo> selectList(AlarmInfo alarmInfo);

    /**
     * 新增
     * @param alarmInfo
     * @return
     */
    int insert(AlarmInfo alarmInfo);

    /**
     * 更新
     * @param alarmInfo
     * @return
     */
    int updateById(AlarmInfo alarmInfo);

    /**
     * 查询
     * @param id
     * @return
     */
    AlarmInfo selectById(Long id);

    /**
     * 批量删除
     * @param alarmInfoIds
     * @return
     */
    int deleteByIds(Long[] alarmInfoIds);
}
