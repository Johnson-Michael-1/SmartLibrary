package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.Borrow;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 11:23 2022/07/12
 */
public interface IBorrowService {

    /**
     * 查询借阅信息列表集合
     * @param borrow
     * @return
     */
    List<Borrow> selectList(Borrow borrow);

    /**
     * 新增借阅信息
     * @param borrow
     * @return
     */
    int insert(Borrow borrow);

    /**
     * 更新借阅信息
     * @param borrow
     * @return
     */
    int updateBorrow(Borrow borrow);

    /**
     * 根据id获取借阅信息
     * @param id
     * @return
     */
    Borrow getBorrow(Long id);

    /**
     * 根据id批量删除
     * @param borrowIds
     * @return
     */
    int deleteBorrowByIds(Long[] borrowIds);

    /**
     * 续借
     * @param borrow
     * @return
     */
    int bkRenew(Borrow borrow);

    /**
     * 查询读者在借详情
     * @param borrow
     * @return
     */
    List<Borrow> readerBorrowList(Borrow borrow);

    /**
     * 查询借阅书籍状态
     * @param readerInfo
     * @return
     */
    List<Borrow> selectBorrowState(ReaderInfo readerInfo);
}
