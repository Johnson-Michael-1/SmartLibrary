package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.RecordRemote;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 14:56 2022/07/13
 */
public interface IRecordRemoteService {

    /**
     * 查询远程开门记录
     * @param recordRemote
     * @return
     */
    List<RecordRemote> selectList(RecordRemote recordRemote);

    /**
     * 新增远程开门信息
     * @param recordRemote
     * @return
     */
    int insert(RecordRemote recordRemote);

    /**
     * 更新远程开门信息
     * @param recordRemote
     * @return
     */
    int updateRecordRemote(RecordRemote recordRemote);

    /**
     * 根据id获取远程开门信息
     * @param id
     * @return
     */
    RecordRemote getRecordRemote(Long id);

    /**
     * 根据id批量删除
     * @param openIds
     * @return
     */
    int deleteRecordRemoteByIds(Long[] openIds);

}
