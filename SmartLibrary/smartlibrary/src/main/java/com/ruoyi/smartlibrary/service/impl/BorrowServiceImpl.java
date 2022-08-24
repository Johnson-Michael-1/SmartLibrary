package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.BorrowMapper;
import com.ruoyi.smartlibrary.pojo.Borrow;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import com.ruoyi.smartlibrary.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 11:25 2022/07/12
 */
@Service
public class BorrowServiceImpl implements IBorrowService {

    @Autowired
    private BorrowMapper borrowMapper;

    /**
     * 查询借阅信息列表集合
     * @param borrow
     * @return
     */
    @Override
    public List<Borrow> selectList(Borrow borrow) {
        return borrowMapper.selectList(borrow);
    }

    /**
     * 新增借阅信息
     * @param borrow
     * @return
     */
    @Override
    public int insert(Borrow borrow) {
        return borrowMapper.insert(borrow);
    }

    /**
     * 更新借阅信息
     * @param borrow
     * @return
     */
    @Override
    public int updateBorrow(Borrow borrow) {
        return borrowMapper.updateByRfid(borrow);
    }

    /**
     * 根据id获取借阅信息
     * @param id
     * @return
     */
    @Override
    public Borrow getBorrow(Long id) {
        return borrowMapper.selectById(id);
    }

    /**
     * 根据id批量删除
     * @param borrowIds
     * @return
     */
    @Override
    public int deleteBorrowByIds(Long[] borrowIds) {
        return borrowMapper.deleteByIds(borrowIds);
    }

    /**
     * 续借30天
     * @param borrow
     * @return
     */
    @Override
    public int bkRenew(Borrow borrow) {
        //先去根据rfid查续借状态，为0，不能续借
        Borrow res = borrowMapper.selectByIsbn(borrow.getBkIsbn());
        if("1".equals(res.getRenewState())){
            //不可再续借
            return 0;
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(res.getShouldBackTime());
            //应还时间往后+30天
            calendar.add(Calendar.DATE, 30);
            borrow.setShouldBackTime(calendar.getTime());
            borrow.setRenewState("1");
            return borrowMapper.updateByBkIsbn(borrow);
        }
    }

    /**
     * 查询读者在借状态
     * @param borrow
     * @return
     */
    @Override
    public List<Borrow> readerBorrowList(Borrow borrow) {
        return borrowMapper.readerBorrowList(borrow);
    }

    /**
     * 查询是否超期
     * @param readerInfo
     * @return
     */
    @Override
    public List<Borrow> selectBorrowState(ReaderInfo readerInfo) {
        return borrowMapper.selectBorrowState(readerInfo);
    }
}
