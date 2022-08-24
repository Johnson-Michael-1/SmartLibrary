package com.ruoyi.smartlibrary.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.smartlibrary.pojo.BookInfo;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 10:55 2022/07/12
 */
public interface IBookInfoService {

    /**
     * 查询书籍信息列表集合
     * @param bookInfo
     * @return
     */
    List<BookInfo> selectList(BookInfo bookInfo);

    /**
     * 新增书籍信息
     * @param bookInfo
     * @return
     */
    int insert(BookInfo bookInfo);

    /**
     * 更新图书信息
     * @param bookInfo
     * @return
     */
    int updateBookInfo(BookInfo bookInfo);

    /**
     * 根据id获取图书信息
     * @param id
     * @return
     */
    BookInfo getBookInfo(Long id);

    /**
     * 根据id批量删除
     * @param bookInfoIds
     * @return
     */
    int deleteBookInfoByIds(Long[] bookInfoIds);

    /**
     * 查询书籍信息
     * @param bkRfid
     * @return
     */
    BookInfo selectByBkRfid(String bkRfid);

    /**
     * 导入书籍数据
     *
     * @param bookList 书籍数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作书籍
     * @return 结果
     */
    public String importBook(List<BookInfo> bookList, Boolean isUpdateSupport, String operName);
}
