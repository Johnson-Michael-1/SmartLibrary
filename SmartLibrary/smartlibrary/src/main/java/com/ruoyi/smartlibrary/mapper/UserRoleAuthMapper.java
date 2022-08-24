package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.UserRoleAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 11:08 2022/07/11
 */
@Mapper
public interface UserRoleAuthMapper {

    /**
     * 根据id删除角色管理
     * @param roleId
     * @return
     */
    int deleteById(Long roleId);

    /**
     * 新增角色管理
     * @param userRoleAuth
     * @return
     */
    int insert(UserRoleAuth userRoleAuth);

    /**
     * 根据id查询角色管理
     * @param roleId
     * @return
     */
    UserRoleAuth selectById(Long roleId);

    /**
     * 根据id进行更新角色管理
     * @param roleId
     * @return
     */
    int updateById(Long roleId);

}
