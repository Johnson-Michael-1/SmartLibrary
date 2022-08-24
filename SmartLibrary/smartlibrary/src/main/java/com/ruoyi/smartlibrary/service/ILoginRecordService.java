package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.LoginRecord;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:18 2022/08/01
 */
public interface ILoginRecordService {

    /**
     * 查询读者登录信息列表
     * @param loginRecord
     * @return
     */
    List<LoginRecord> selectList(LoginRecord loginRecord);

    /**
     * 新增登录信息
     * @param loginRecord
     * @return
     */
    int insert(LoginRecord loginRecord);

    /**
     * 更新登录信息
     * @param loginRecord
     * @return
     */
    int updateLoginRecord(LoginRecord loginRecord);

    /**
     * 根据身份证号获取登录信息
     * @param loginId
     * @return
     */
    LoginRecord getLoginRecord(String loginId);

    /**
     * 根据id批量删除
     * @param loginIds
     * @return
     */
    int deleteLoginRecordByIds(Long[] loginIds);

}
