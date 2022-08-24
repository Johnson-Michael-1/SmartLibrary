package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.RecordRemoteMapper;
import com.ruoyi.smartlibrary.pojo.RecordRemote;
import com.ruoyi.smartlibrary.service.IRecordRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 15:02 2022/07/13
 */
@Service
public class RecordRemoteServiceImpl implements IRecordRemoteService {

    @Autowired
    private RecordRemoteMapper recordRemoteMapper;

    /**
     * 查询开门记录
     * @return
     */
    @Override
    public List<RecordRemote> selectList(RecordRemote recordRemote) {
        return recordRemoteMapper.selectList(recordRemote);
    }

    /**
     * 新增远程开门信息
     * @param recordRemote
     * @return
     */
    @Override
    public int insert(RecordRemote recordRemote) {
        return recordRemoteMapper.insert(recordRemote);
    }

    /**
     * 更新远程开门信息
     * @param recordRemote
     * @return
     */
    @Override
    public int updateRecordRemote(RecordRemote recordRemote) {
        return recordRemoteMapper.updateRecordRemote(recordRemote);
    }

    /**
     * 根据id获取远程开门信息
     * @param id
     * @return
     */
    @Override
    public RecordRemote getRecordRemote(Long id) {
        return recordRemoteMapper.selectById(id);
    }

    /**
     * 根据id批量删除
     * @param openIds
     * @return
     */
    @Override
    public int deleteRecordRemoteByIds(Long[] openIds) {
        return recordRemoteMapper.deleteByIds(openIds);
    }
}
