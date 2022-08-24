package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.RecordHistoryMapper;
import com.ruoyi.smartlibrary.pojo.RecordHistory;
import com.ruoyi.smartlibrary.service.IRecordHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:57 2022/07/15
 */
@Service
public class RecordHistoryServiceImpl implements IRecordHistoryService {

    @Autowired
    private RecordHistoryMapper recordHistoryMapper;

    /**
     * 查询历史开门记录
     * @param recordHistory
     * @return
     */
    @Override
    public List<RecordHistory> selectList(RecordHistory recordHistory) {
        return recordHistoryMapper.selectList(recordHistory);
    }

    /**
     * 新增开门信息
     * @param recordHistory
     * @return
     */
    @Override
    public int insert(RecordHistory recordHistory) {
        return recordHistoryMapper.insert(recordHistory);
    }

    /**
     * 更新开门信息
     * @param recordHistory
     * @return
     */
    @Override
    public int updateRecordHistory(RecordHistory recordHistory) {
        return recordHistoryMapper.updateRecordHistory(recordHistory);
    }

    /**
     * 根据id获取开门信息
     * @param id
     * @return
     */
    @Override
    public RecordHistory getRecordHistory(Long id) {
        return recordHistoryMapper.getRecordHistoryById(id);
    }

    /**
     * 根据id批量删除
     * @param openIds
     * @return
     */
    @Override
    public int deleteRecordHistoryByIds(Long[] openIds) {
        return recordHistoryMapper.deleteRecordHistoryByIds(openIds);
    }
}
