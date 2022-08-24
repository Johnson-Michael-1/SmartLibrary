package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.Borrow;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import com.ruoyi.smartlibrary.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 11:28 2022/07/12
 */
@RestController
@RequestMapping("/borrow")
public class BorrowController extends BaseController {

    @Autowired
    private IBorrowService borrowService;

    /**
     * 查询借阅信息列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:borrow:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(Borrow borrow){
        startPage();
        List<Borrow> borrowList = borrowService.selectList(borrow);
        logger.info("信息"+borrowList);
        return getDataTable(borrowList);
    }

    /**
     * 新增借阅记录
     * @param borrow
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:add')")
    @Log(title = "借阅管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Borrow borrow){
        borrow.setCreateBy(getUsername());
        return toAjax(borrowService.insert(borrow));
    }

    /**
     * 修改借阅信息
     * @param borrow
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:edit')")
    @Log(title = "借阅管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateBorrow(@Validated @RequestBody Borrow borrow){
        borrow.setCreateBy(getUsername());
        return toAjax(borrowService.updateBorrow(borrow));
    }

    /**
     * 根据id获取借阅信息
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:getBorrow')")
    @GetMapping("/get/{id}")
    public AjaxResult getBorrow(@PathVariable Long id){
        return AjaxResult.success(borrowService.getBorrow(id));
    }


    /**
     * 删除借阅记录
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:remove')")
    @Log(title = "借阅管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds)
    {
        return toAjax(borrowService.deleteBorrowByIds(borrowIds));
    }


    /**
     * 续借
     * @param borrow
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:renew')")
    @Log(title = "借阅管理", businessType = BusinessType.UPDATE)
    @PutMapping("/renew")
    public AjaxResult bkRenew(@Validated @RequestBody Borrow borrow){
        borrow.setCreateBy(getUsername());
        return toAjax(borrowService.bkRenew(borrow));
    }

}
