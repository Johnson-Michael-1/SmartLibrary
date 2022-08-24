package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.Firmware;
import com.ruoyi.smartlibrary.pojo.LoginRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 10:50 2022/08/17
 */
@Mapper
public interface FirmwareMapper {

    /**
     * 查询固件信息列表
     * @param firmware
     * @return
     */
    List<Firmware> selectList(Firmware firmware);

    /**
     * 根据id进行删除
     * @param ids
     * @return
     */
    int deleteByIds(Long[] ids);

    /**
     * 新增固件信息
     * @param firmware
     * @return
     */
    int insert(Firmware firmware);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Firmware selectById(Long id);

    /**
     * 根据id更新
     * @param firmware
     * @return
     */
    int updateById(Firmware firmware);

    /**
     * 获取最新版本
     * @return
     */
    Firmware getLatestVersion();

}
