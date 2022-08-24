package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.RecordRemote;
import com.ruoyi.smartlibrary.service.IRecordRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Mei pq
 * @description:
 * @date: create in 14:52 2022/07/13
 */
@RestController
@RequestMapping("/recordRemote")
public class RecordRemoteController extends BaseController {

    @Autowired
    private IRecordRemoteService recordRemoteService;

    /**
     * 查询远程开门记录信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:recordRemote:list')")
    @GetMapping("/list")
    public TableDataInfo recordRemoteList(RecordRemote recordRemote){
        startPage();
        List<RecordRemote> recordRemoteList = recordRemoteService.selectList(recordRemote);
        logger.info("信息"+recordRemoteList);
        return getDataTable(recordRemoteList);
    }

    /**
     * 新增远程开门信息
     * @param recordRemote
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:recordRemote:add')")
    @Log(title = "记录管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody RecordRemote recordRemote){
        recordRemote.setCreateBy(getUsername());
        return toAjax(recordRemoteService.insert(recordRemote));
    }

    /**
     * 修改远程开门记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:recordRemote:edit')")
    @Log(title = "记录管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateRecordRemote(@Validated @RequestBody RecordRemote recordRemote) {
        recordRemote.setCreateBy(getUsername());
        return toAjax(recordRemoteService.updateRecordRemote(recordRemote));
    }

    /**
     * 根据id获取远程开门记录信息
     */
    @PreAuthorize("@ss.hasPermi('system:recordRemote:getrecordRemote')")
    @GetMapping("/get/{id}")
    public AjaxResult getRecordRemote(@PathVariable Long id){
        return AjaxResult.success(recordRemoteService.getRecordRemote(id));
    }


    /**
     * 删除远程开门记录
     */
    @PreAuthorize("@ss.hasPermi('system:recordRemote:remove')")
    @Log(title = "记录管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{openIds}")
    public AjaxResult remove(@PathVariable Long[] openIds)
    {
        return toAjax(recordRemoteService.deleteRecordRemoteByIds(openIds));
    }


}
