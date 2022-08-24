package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.DevBookDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 16:32 2022/07/11
 */
@Mapper
public interface DevBookDetailMapper {

    /**
     * 根据id删除
     * @param devId
     * @return
     */
    int deleteById(Long devId);

    /**
     * 新增设备信息
     * @param devBookDetail
     * @return
     */
    int insert(DevBookDetail devBookDetail);

    /**
     * 根据id查询
     * @param devId
     * @return
     */
    DevBookDetail selectById(Long devId);

    /**
     * 跟据id进行更新
     * @param devId
     * @return
     */
    int updateById(Long devId);

    /**
     * 查询设备内书籍列表信息
     * @return
     */
    List<DevBookDetail> selectList();
}
