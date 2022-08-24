package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.RecordHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:59 2022/07/15
 */
@Mapper
public interface RecordHistoryMapper {

    /**
     * 查询历史开门记录
     * @param recordHistory
     * @return
     */
    List<RecordHistory> selectList(RecordHistory recordHistory);

    /**
     * 新增开门信息
     * @param recordHistory
     * @return
     */
    int insert(RecordHistory recordHistory);

    /**
     * 更新开门信息
     * @param recordHistory
     * @return
     */
    int updateRecordHistory(RecordHistory recordHistory);

    /**
     * 根据id获取开门信息
     * @param id
     * @return
     */
    RecordHistory getRecordHistoryById(Long id);

    /**
     * 根据id批量删除
     * @param openIds
     * @return
     */
    int deleteRecordHistoryByIds(Long[] openIds);
}
