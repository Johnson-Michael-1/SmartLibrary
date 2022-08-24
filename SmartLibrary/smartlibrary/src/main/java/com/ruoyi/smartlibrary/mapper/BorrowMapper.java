package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.Borrow;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BorrowMapper {
    /**
     * 根据id删除
     * @param borrowIds
     * @return
     */
    int deleteByIds(Long[] borrowIds);

    /**
     * 新增借阅记录
     * @param borrow
     * @return
     */
    int insert(Borrow borrow);

    /**
     * 根据id查询
     * @param borrowId
     * @return
     */
    Borrow selectById(Long borrowId);

    /**
     * 根据rfid进行更新
     * @param borrow
     * @return
     */
    int updateByBkIsbn(Borrow borrow);

    /**
     * 根据id进行更新
     * @param borrow
     * @return
     */
    int updateById(Borrow borrow);

    /**
     * 查询借阅信息列表
     * @param borrow
     * @return
     */
    List<Borrow> selectList(Borrow borrow);

    /**
     * 查询读者在借详情
     * @param borrow
     * @return
     */
    List<Borrow> readerBorrowList(Borrow borrow);

    /**
     * 查询续借状态
     * @param bkRfid
     * @return
     */
    Borrow selectByIsbn(String bkRfid);

    /**
     * 更新借阅状态
     * @param borrow
     * @return
     */
    int updateByRfid(Borrow borrow);

    /**
     * 查询是否超期
     * @param readerInfo
     * @return
     */
    List<Borrow> selectBorrowState(ReaderInfo readerInfo);
}