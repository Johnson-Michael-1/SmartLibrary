package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.DevInfo;
import com.ruoyi.smartlibrary.service.IDevInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 14:55 2022/07/19
 */
@RestController
@RequestMapping("/devInfo")
public class DevInfoController extends BaseController {


    public static IDevInfoService devInfoService;

    @Autowired
    public void setDevInfoService(IDevInfoService devInfoService){
        DevInfoController.devInfoService = devInfoService;
    }

    /**
     * 查询设备信息列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:devInfo:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(DevInfo devInfo){
        startPage();
        List<DevInfo> devInfoList = devInfoService.selectList(devInfo);
        logger.info("信息"+ devInfoList);
        return getDataTable(devInfoList);
    }


    /**
     * 查询在线设备列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:devInfo:list')")
    @GetMapping("/listDev")
    public TableDataInfo listDev(DevInfo devInfo){
        startPage();
        List<DevInfo> devInfoList = devInfoService.selectListDev(devInfo);
        logger.info("信息"+ devInfoList);
        return getDataTable(devInfoList);
    }

    /**
     * 新增设备信息
     * @param devInfo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:devInfo:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody DevInfo devInfo){
        devInfo.setLinkTime(new Date());
        devInfo.setCreateBy(getUsername());
        return toAjax(devInfoService.insert(devInfo));
    }

    /**
     * 修改设备信息
     */
    @PreAuthorize("@ss.hasPermi('system:devInfo:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateDevInfo(@Validated @RequestBody DevInfo devInfo) {
        devInfo.setCreateBy(getUsername());
        return toAjax(devInfoService.updateDevInfo(devInfo));
    }

    /**
     * 根据id获取设备信息
     */
    @PreAuthorize("@ss.hasPermi('system:devInfo:get')")
    @GetMapping("/get/{id}")
    public AjaxResult getDevInfo(@PathVariable Long id){
        return AjaxResult.success(devInfoService.getDevInfo(id));
    }


    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('system:devInfo:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{devInfoIds}")
    public AjaxResult remove(@PathVariable Long[] devInfoIds)
    {
        return toAjax(devInfoService.deleteDevInfoByIds(devInfoIds));
    }

}
