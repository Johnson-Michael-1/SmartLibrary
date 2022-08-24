package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.BookInfo;
import com.ruoyi.smartlibrary.pojo.DevInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DevInfoMapper {

    /**
     * 根据id删除
     * @param devIds
     * @return
     */
    int deleteByIds(Long[] devIds);

    /**
     * 新增设备信息
     * @param devInfo
     * @return
     */
    int insert(DevInfo devInfo);

    /**
     * 根据id查询
     * @param devId
     * @return
     */
    DevInfo selectById(Long devId);

    /**
     * 跟据id进行更新
     * @param devInfo
     * @return
     */
    int updateById(DevInfo devInfo);

    /**
     * 查询设备列表集
     * @param devInfo
     * @return
     */
    List<DevInfo> selectList(DevInfo devInfo);

    /**
     * 更新设备状态
     * @param devSn
     * @return
     */
    Integer updateByDevSn(String devSn);

    /**
     * 查询设备
     * @param devSn
     * @return
     */
    DevInfo selectByDevSn(String devSn);

    /**
     * 查询在线
     * @param devInfo
     * @return
     */
    List<DevInfo> selectListDev(DevInfo devInfo);
}