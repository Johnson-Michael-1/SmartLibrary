package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.smartlibrary.mapper.BookInfoMapper;
import com.ruoyi.smartlibrary.pojo.BookInfo;
import com.ruoyi.smartlibrary.service.IBookInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 10:57 2022/07/12
 */
@Service
public class BookInfoServiceImpl implements IBookInfoService {

    private static final Logger log = LoggerFactory.getLogger(BookInfoServiceImpl.class);

    @Autowired
    private BookInfoMapper bookInfoMapper;

    /**
     * 查询书籍信息列表
     * @param bookInfo
     * @return
     */
    @Override
    public List<BookInfo> selectList(BookInfo bookInfo) {
        return bookInfoMapper.selectList(bookInfo);
    }

    /**
     * 新增书籍信息
     * @param bookInfo
     * @return
     */
    @Override
    public int insert(BookInfo bookInfo) {
        return bookInfoMapper.insert(bookInfo);
    }

    /**
     * 更新图书信息
     * @param bookInfo
     * @return
     */
    @Override
    public int updateBookInfo(BookInfo bookInfo) {
        return bookInfoMapper.updateById(bookInfo);
    }

    /**
     * 根据id获取图书信息
     * @param id
     * @return
     */
    @Override
    public BookInfo getBookInfo(Long id) {
        return bookInfoMapper.selectById(id);
    }

    /**
     * 根据id批量删除
     * @param bookInfoIds
     * @return
     */
    @Override
    public int deleteBookInfoByIds(Long[] bookInfoIds) {
        return bookInfoMapper.deleteByIds(bookInfoIds);
    }

    /**
     * 根据rfid查询书籍信息
     * @param bkRfid
     * @return
     */
    @Override
    public BookInfo selectByBkRfid(String bkRfid) {
        return bookInfoMapper.selectByRfid(bkRfid);
    }

    /**
     * 导入书籍数据
     *
     * @param bookList 书籍数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据  (没用到，因为rfid唯一，不存在更新)
     * @param operName 操作书籍
     * @return 结果
     */
    @Override
    public String importBook(List<BookInfo> bookList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(bookList) || bookList.size() == 0)
        {
            throw new ServiceException("导入书籍数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
//        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (BookInfo bookInfo : bookList)
        {
            try
            {
                // 验证是否存在这本书
//                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                BookInfo info = bookInfoMapper.selectByRfid(bookInfo.getBkRfid());
                if (StringUtils.isNull(info))
                {
//                    BeanValidators.validateWithException(validator, user);
//                    user.setPassword(SecurityUtils.encryptPassword(password));
                    bookInfo.setCreateBy(operName);
                    bookInfoMapper.insert(bookInfo);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、书籍 " + bookInfo.getBkName() + " 导入成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、书籍 " + bookInfo.getBkRfid() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、书籍 " + bookInfo.getBkRfid() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }



}
