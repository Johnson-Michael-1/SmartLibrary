package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.LoginRecordMapper;
import com.ruoyi.smartlibrary.pojo.LoginRecord;
import com.ruoyi.smartlibrary.service.ILoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:24 2022/08/01
 */
@Service
public class LoginRecordServiceImpl implements ILoginRecordService {

    @Autowired
    private LoginRecordMapper loginRecordMapper;

    /**
     * 查询读者登录信息列表
     * @param loginRecord
     * @return
     */
    @Override
    public List<LoginRecord> selectList(LoginRecord loginRecord) {
        return loginRecordMapper.selectList(loginRecord);
    }

    /**
     * 新增登录信息
     * @param loginRecord
     * @return
     */
    @Override
    public int insert(LoginRecord loginRecord) {
        return loginRecordMapper.insert(loginRecord);
    }

    /**
     * 更新登录信息
     * @param loginRecord
     * @return
     */
    @Override
    public int updateLoginRecord(LoginRecord loginRecord) {
        return loginRecordMapper.updateByIdNumber(loginRecord);
    }

    /**
     * 根据身份证号获取登录信息
     * @param loginId
     * @return
     */
    @Override
    public LoginRecord getLoginRecord(String loginId) {
        return loginRecordMapper.selectById(loginId);
    }

    /**
     * 根据id批量删除
     * @param loginIds
     * @return
     */
    @Override
    public int deleteLoginRecordByIds(Long[] loginIds) {
        return loginRecordMapper.deleteByIds(loginIds);
    }
}
