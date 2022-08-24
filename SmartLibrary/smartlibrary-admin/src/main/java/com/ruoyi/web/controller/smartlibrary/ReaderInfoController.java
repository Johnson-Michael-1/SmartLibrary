package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import com.ruoyi.smartlibrary.service.IReaderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 10:35 2022/07/18
 */
@RestController
@RequestMapping("/readerInfo")
public class ReaderInfoController extends BaseController {

    @Autowired
    private IReaderInfoService readerInfoService;

    /**
     * 查询读者信息列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:readerInfo:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(ReaderInfo readerInfo){
        startPage();
        List<ReaderInfo> readerInfoList = readerInfoService.selectList(readerInfo);
        logger.info("信息"+ readerInfoList);
        return getDataTable(readerInfoList);
    }

    /**
     * 新增读者信息
     * @param readerInfo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:readerInfo:add')")
    @Log(title = "读者管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody ReaderInfo readerInfo){
        readerInfo.setCreateBy(getUsername());
        return toAjax(readerInfoService.insert(readerInfo));
    }

    /**
     * 修改读者信息
     */
    @PreAuthorize("@ss.hasPermi('system:readerInfo:edit')")
    @Log(title = "读者管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateBookInfo(@Validated @RequestBody ReaderInfo readerInfo) {
        readerInfo.setCreateBy(getUsername());
        return toAjax(readerInfoService.updateReaderInfo(readerInfo));
    }

    /**
     * 根据id获取读者信息
     */
    @PreAuthorize("@ss.hasPermi('system:readerInfo:getreaderInfo')")
    @GetMapping("/get/{id}")
    public AjaxResult getBookInfo(@PathVariable Long id){
        return AjaxResult.success(readerInfoService.getReaderInfo(id));
    }


    /**
     * 删除读者
     */
    @PreAuthorize("@ss.hasPermi('system:readerInfo:remove')")
    @Log(title = "读者管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{readerInfoIds}")
    public AjaxResult remove(@PathVariable Long[] readerInfoIds)
    {
        return toAjax(readerInfoService.deleteReaderInfoByIds(readerInfoIds));
    }

    /**
     * 重置读者登录密码
     */
    @PreAuthorize("@ss.hasPermi('system:readerInfo:edit')")
    @Log(title = "读者管理", businessType = BusinessType.UPDATE)
    @PutMapping("/reset")
    public AjaxResult resetPassword(@Validated @RequestBody Long id) {
        return toAjax(readerInfoService.resetPassword(id));
    }

}
