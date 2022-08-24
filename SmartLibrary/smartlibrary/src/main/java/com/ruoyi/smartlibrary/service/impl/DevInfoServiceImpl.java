package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.smartlibrary.mapper.DevInfoMapper;
import com.ruoyi.smartlibrary.pojo.DevInfo;
import com.ruoyi.smartlibrary.service.IDevInfoService;
import com.ruoyi.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 14:27 2022/07/19
 */
@Service
public class DevInfoServiceImpl implements IDevInfoService {

    @Autowired
    private DevInfoMapper devInfoMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询设备信息列表集合
     * @param devInfo
     * @return
     */
    @Override
    public List<DevInfo> selectList(DevInfo devInfo) {
        return devInfoMapper.selectList(devInfo);
    }

    /**
     * 新增设备信息
     * @param devInfo
     * @return
     */
    @Override
    public int insert(DevInfo devInfo) {
        SysDept sysDept = deptMapper.selectDeptById(devInfo.getDeptId());
        devInfo.setDeptName(sysDept.getDeptName());
        return devInfoMapper.insert(devInfo);
    }

    /**
     * 更新设备信息
     * @param devInfo
     * @return
     */
    @Override
    public int updateDevInfo(DevInfo devInfo) {
        SysDept sysDept = deptMapper.selectDeptById(devInfo.getDeptId());
        devInfo.setDeptName(sysDept.getDeptName());
        return devInfoMapper.updateById(devInfo);
    }

    /**
     * 根据id获取设备信息
     * @param id
     * @return
     */
    @Override
    public DevInfo getDevInfo(Long id) {
        return devInfoMapper.selectById(id);
    }

    /**
     * 根据id批量删除
     * @param devInfoIds
     * @return
     */
    @Override
    public int deleteDevInfoByIds(Long[] devInfoIds) {
        return devInfoMapper.deleteByIds(devInfoIds);
    }

    /**
     * 根据设备序列号更新设备状态
     * @param devSn
     */
    @Override
    public Integer updateDevState(String devSn) {
        return devInfoMapper.updateByDevSn(devSn);
    }

    /**
     * 根据设备序列号查询设备
     * @param devSn
     * @return
     */
    @Override
    public DevInfo selectDevSn(String devSn) {
        return devInfoMapper.selectByDevSn(devSn);
    }

    /**
     * 查询在线
     * @param devInfo
     * @return
     */
    @Override
    public List<DevInfo> selectListDev(DevInfo devInfo) {
        return devInfoMapper.selectListDev(devInfo);
    }

}
