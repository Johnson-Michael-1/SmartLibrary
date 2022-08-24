package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.UerInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UerInfoMapper {

    /**
     * 根据id删除
     * @param managerId
     * @return
     */
    int deleteById(Long managerId);

    /**
     * 新增管理员
     * @param uerinfo
     * @return
     */
    int insert(UerInfo uerinfo);

    /**
     * 根据id查询管理员
     * @param managerId
     * @return
     */
    UerInfo selectById(Long managerId);

    /**
     * 根据id进行更新
     * @param managerId
     * @return
     */
    int updateById(Long managerId);
}