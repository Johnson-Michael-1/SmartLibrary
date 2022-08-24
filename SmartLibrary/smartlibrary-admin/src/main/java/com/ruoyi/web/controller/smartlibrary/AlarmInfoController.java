package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.AlarmInfo;
import com.ruoyi.smartlibrary.pojo.BookInfo;
import com.ruoyi.smartlibrary.service.IAlarmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 15:18 2022/07/15
 */
@RestController
@RequestMapping("/alarmInfo")
public class AlarmInfoController extends BaseController {

    @Autowired
    private IAlarmInfoService alarmInfoService;

    /**
     * 查询报警记录
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:alarmInfo:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(AlarmInfo alarmInfo){
        startPage();
        List<AlarmInfo> alarmInfoList = alarmInfoService.selectList(alarmInfo);
        logger.info("信息："+ alarmInfoList);
        return getDataTable(alarmInfoList);
    }

    /**
     * 新增报警信息
     * @param alarmInfo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:alarmInfo:add')")
    @Log(title = "记录管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AlarmInfo alarmInfo){
        alarmInfo.setCreateBy(getUsername());
        return toAjax(alarmInfoService.insert(alarmInfo));
    }

    /**
     * 修改报警信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarmInfo:edit')")
    @Log(title = "记录管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateAlarmInfo(@Validated @RequestBody AlarmInfo alarmInfo) {
        alarmInfo.setCreateBy(getUsername());
        return toAjax(alarmInfoService.updateAlarmInfo(alarmInfo));
    }

    /**
     * 获取报警信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarmInfo:getAlarmInfo')")
    @GetMapping("/get/{id}")
    public AjaxResult getAlarmInfo(@PathVariable Long id){
        return AjaxResult.success(alarmInfoService.getAlarmInfo(id));
    }


    /**
     * 删除报警
     */
    @PreAuthorize("@ss.hasPermi('system:alarmInfo:remove')")
    @Log(title = "记录管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{alarmInfoIds}")
    public AjaxResult remove(@PathVariable Long[] alarmInfoIds)
    {
        return toAjax(alarmInfoService.deleteAlarmInfoByIds(alarmInfoIds));
    }




}
