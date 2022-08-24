package com.ruoyi.smartlibrary.service;

import com.ruoyi.smartlibrary.pojo.DevBookDetail;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 11:38 2022/07/12
 */
public interface IDevBookDetailService {

    /**
     * 查询设备书籍详情集合
     * @return
     */
    List<DevBookDetail> selectList();

    /**
     * 新增设备书籍信息记录
     * @param devBookDetail
     * @return
     */
    int insert(DevBookDetail devBookDetail);
}
