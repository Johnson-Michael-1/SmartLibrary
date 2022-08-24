package com.ruoyi.smartlibrary.service;


import com.ruoyi.smartlibrary.pojo.ReaderInfo;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 10:08 2022/07/18
 */
public interface IReaderInfoService {
    /**
     * 查询读者信息列表集合
     * @param readerInfo
     * @return
     */
    List<ReaderInfo> selectList(ReaderInfo readerInfo);

    /**
     * 新增读者信息
     * @param readerInfo
     * @return
     */
    int insert(ReaderInfo readerInfo);

    /**
     * 更新读者信息
     * @param readerInfo
     * @return
     */
    int updateReaderInfo(ReaderInfo readerInfo);

    /**
     * 根据id获取读者信息
     * @param id
     * @return
     */
    ReaderInfo getReaderInfo(Long id);

    /**
     * 根据id批量删除
     * @param readerInfoIds
     * @return
     */
    int deleteReaderInfoByIds(Long[] readerInfoIds);

    /**
     * 查询读者信息是否存在
     * @param readerInfo
     * @return
     */
    ReaderInfo selectReaderInfo(ReaderInfo readerInfo);

    /**
     * 重置密码
     * @param id
     * @return
     */
    int resetPassword(Long id);
}
