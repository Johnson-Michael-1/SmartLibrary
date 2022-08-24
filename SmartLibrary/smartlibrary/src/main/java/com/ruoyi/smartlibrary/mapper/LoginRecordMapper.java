package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.LoginRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:26 2022/08/01
 */
@Mapper
public interface LoginRecordMapper {

    /**
     * 查询读者登录信息列表
     * @param loginRecord
     * @return
     */
    List<LoginRecord> selectList(LoginRecord loginRecord);

    /**
     * 根据id进行删除
     * @param loginIds
     * @return
     */
    int deleteByIds(Long[] loginIds);

    /**
     * 新增登录信息
     * @param loginRecord
     * @return
     */
    int insert(LoginRecord loginRecord);

    /**
     * 根据读者身份证号查询
     * @param loginId
     * @return
     */
    LoginRecord selectById(String loginId);

    /**
     * 根据身份证号更新
     * @param loginRecord
     * @return
     */
    int updateByIdNumber(LoginRecord loginRecord);

}
