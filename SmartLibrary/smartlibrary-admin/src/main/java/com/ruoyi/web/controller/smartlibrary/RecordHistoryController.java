package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import com.ruoyi.smartlibrary.pojo.RecordHistory;
import com.ruoyi.smartlibrary.service.IRecordHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 10:03 2022/07/16
 */
@RestController
@RequestMapping("/recordHistory")
public class RecordHistoryController extends BaseController{

    @Autowired
    private IRecordHistoryService recordHistoryService;

    /**
     * 查询历史开门记录信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:recordHistory:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(RecordHistory recordHistory){
        startPage();
        List<RecordHistory> recordHistoryList = recordHistoryService.selectList(recordHistory);
        logger.info("信息"+ recordHistoryList);
        return getDataTable(recordHistoryList);
    }

    /**
     * 新增开门信息
     * @param recordHistory
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:recordHistory:add')")
    @Log(title = "记录管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody RecordHistory recordHistory){
        recordHistory.setCreateBy(getUsername());
        return toAjax(recordHistoryService.insert(recordHistory));
    }

    /**
     * 修改开门记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:recordHistory:edit')")
    @Log(title = "记录管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateRecordHistory(@Validated @RequestBody RecordHistory recordHistory) {
        recordHistory.setCreateBy(getUsername());
        return toAjax(recordHistoryService.updateRecordHistory(recordHistory));
    }

    /**
     * 根据id获取开门记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:recordHistory:getrecordHistory')")
    @GetMapping("/get/{id}")
    public AjaxResult getRecordHistory(@PathVariable Long id){
        return AjaxResult.success(recordHistoryService.getRecordHistory(id));
    }


    /**
     * 删除开门记录
     */
    @PreAuthorize("@ss.hasPermi('system:recordHistory:remove')")
    @Log(title = "记录管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{openIds}")
    public AjaxResult remove(@PathVariable Long[] openIds)
    {
        return toAjax(recordHistoryService.deleteRecordHistoryByIds(openIds));
    }



}
