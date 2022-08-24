package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.LoginRecord;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import com.ruoyi.smartlibrary.service.ILoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 17:20 2022/08/01
 */
@RestController
@RequestMapping("/loginRecord")
public class LoginRecordController extends BaseController {

    @Autowired
    private ILoginRecordService loginRecordService;

    /**
     * 查询读者登录列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:loginRecord:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(LoginRecord loginRecord){
        startPage();
        List<LoginRecord> loginRecordList = loginRecordService.selectList(loginRecord);
        logger.info("信息"+ loginRecordList);
        return getDataTable(loginRecordList);
    }

    /**
     * 新增读者登录记录
     * @param loginRecord
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:loginRecord:add')")
    @Log(title = "读者管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody LoginRecord loginRecord){
        loginRecord.setCreateBy(getUsername());
        return toAjax(loginRecordService.insert(loginRecord));
    }

    /**
     * 修改读者登录信息
     */
    @PreAuthorize("@ss.hasPermi('system:loginRecord:edit')")
    @Log(title = "读者管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateLoginRecord(@Validated @RequestBody LoginRecord loginRecord) {
        loginRecord.setCreateBy(getUsername());
        return toAjax(loginRecordService.updateLoginRecord(loginRecord));
    }

    /**
     * 根据身份证号获取读者登录信息
     */
    @PreAuthorize("@ss.hasPermi('system:loginRecord:getLoginRecord')")
    @GetMapping("/get/{idNumber}")
    public AjaxResult getLoginRecord(@PathVariable String idNumber){
        return AjaxResult.success(loginRecordService.getLoginRecord(idNumber));
    }


    /**
     * 根据id删除登录记录
     */
    @PreAuthorize("@ss.hasPermi('system:loginRecord:remove')")
    @Log(title = "读者管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{loginIds}")
    public AjaxResult remove(@PathVariable Long[] loginIds)
    {
        return toAjax(loginRecordService.deleteLoginRecordByIds(loginIds));
    }

}
