package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.smartlibrary.pojo.DevBookDetail;
import com.ruoyi.smartlibrary.service.IDevBookDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 14:50 2022/07/12
 */
@RestController
@RequestMapping("/devBookDetail")
public class DevBookDetailController {

    @Autowired
    private IDevBookDetailService devBookDetailService;

    /**
     * 查询设备没书籍信息列表
     * @return
     */
    public List<DevBookDetail> selectList(){
        return devBookDetailService.selectList();
    }

    /**
     * 新增设备书籍信息
     * @return
     */
    @PostMapping
    public int add(DevBookDetail devBookDetail){
        return devBookDetailService.insert(devBookDetail);
    }
}
