package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.smartlibrary.pojo.Firmware;
import com.ruoyi.smartlibrary.pojo.FirmwareRecord;
import com.ruoyi.smartlibrary.service.IFirmwareRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:33 2022/08/17
 */
@RestController
@RequestMapping("firmwareRecord")
public class FirmwareRecordController extends BaseController {

    @Autowired
    private IFirmwareRecordService firmwareRecordService;

    /**
     * 查询固件升级记录信息列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:firmwareRecord:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(FirmwareRecord firmwareRecord){
        startPage();
        List<FirmwareRecord> firmwareRecordList = firmwareRecordService.selectList(firmwareRecord);
        logger.info("信息"+ firmwareRecordList);
        return getDataTable(firmwareRecordList);
    }

    /**
     * 新增固件升级记录
     * @param firmwareRecord
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:firmwareRecord:add')")
    @Log(title = "固件管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody FirmwareRecord firmwareRecord){
        firmwareRecord.setCreateBy(getUsername());
        return toAjax(firmwareRecordService.insert(firmwareRecord));
    }



}
