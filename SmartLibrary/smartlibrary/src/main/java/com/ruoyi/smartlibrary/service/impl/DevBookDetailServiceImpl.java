package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.DevBookDetailMapper;
import com.ruoyi.smartlibrary.pojo.DevBookDetail;
import com.ruoyi.smartlibrary.service.IDevBookDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 11:41 2022/07/12
 */
@Service
public class DevBookDetailServiceImpl implements IDevBookDetailService {

    @Autowired
    private DevBookDetailMapper devBookDetailMapper;

    /**
     * 查询设备书籍详情集合
     * @return
     */
    @Override
    public List<DevBookDetail> selectList() {
        return devBookDetailMapper.selectList();
    }

    /**
     * 新增设备书籍信息记录
     * @param devBookDetail
     * @return
     */
    @Override
    public int insert(DevBookDetail devBookDetail) {
        return devBookDetailMapper.insert(devBookDetail);
    }
}
