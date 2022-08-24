package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.BookInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookInfoMapper {

    /**
     * 根据id删除
     * @param bkId
     * @return
     */
    int deleteByIds(Long[] bkId);

    /**
     * 新增书籍信息
     * @param bookInfo
     * @return
     */
    int insert(BookInfo bookInfo);

    /**
     * 根据id查书籍
     * @param bookId
     * @return
     */
    BookInfo selectById(Long bookId);


    /**
     * 根据id更新书籍信息
     * @param bookInfo
     * @return
     */
    int updateById(BookInfo bookInfo);

    /**
     * 查询书籍信息列表
     * @param bookInfo
     * @return
     */
    List<BookInfo> selectList(BookInfo bookInfo);

    /**
     * 查询书籍信息
     * @param bkRfid
     * @return
     */
    BookInfo selectByRfid(String bkRfid);
}