package com.ruoyi.web.controller.smartlibrary;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.smartlibrary.pojo.BookInfo;
import com.ruoyi.smartlibrary.service.IBookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 11:02 2022/07/12
 */
@RestController
@RequestMapping("bookInfo")
public class BookInfoController extends BaseController {

    @Autowired
    private IBookInfoService bookInfoService;

    /**
     * 查询书籍信息列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:bookInfo:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(BookInfo bookInfo){
        startPage();
        List<BookInfo> bookInfoList = bookInfoService.selectList(bookInfo);
        logger.info("信息"+bookInfoList);
        return getDataTable(bookInfoList);
    }

    /**
     * 导入书籍数据
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @Log(title = "书籍管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:bookInfo:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<BookInfo> util = new ExcelUtil<BookInfo>(BookInfo.class);
        List<BookInfo> bookList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = bookInfoService.importBook(bookList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 下载模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BookInfo> util = new ExcelUtil<BookInfo>(BookInfo.class);
        util.importTemplateExcel(response, "书籍数据");
    }

    /**
     * 新增书籍信息
     * @param bookInfo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:bookInfo:add')")
    @Log(title = "书籍管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody BookInfo bookInfo){
        bookInfo.setCreateBy(getUsername());
        return toAjax(bookInfoService.insert(bookInfo));
    }

    /**
     * 修改书籍信息
     */
    @PreAuthorize("@ss.hasPermi('system:bookInfo:edit')")
    @Log(title = "书籍管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateBookInfo(@Validated @RequestBody BookInfo bookInfo) {
        bookInfo.setCreateBy(getUsername());
        return toAjax(bookInfoService.updateBookInfo(bookInfo));
    }

    /**
     * 根据id获取图书信息
     */
    @PreAuthorize("@ss.hasPermi('system:bookInfo:getBookInfo')")
    @GetMapping("/get/{id}")
    public AjaxResult getBookInfo(@PathVariable Long id){
        System.out.println("===========" + bookInfoService.getBookInfo(id));
        System.out.println(AjaxResult.success(bookInfoService.getBookInfo(id)));
        return AjaxResult.success(bookInfoService.getBookInfo(id));
    }


    /**
     * 删除图书
     */
    @PreAuthorize("@ss.hasPermi('system:bookInfo:remove')")
    @Log(title = "书籍管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{bookInfoIds}")
    public AjaxResult remove(@PathVariable Long[] bookInfoIds)
    {
        return toAjax(bookInfoService.deleteBookInfoByIds(bookInfoIds));
    }

}
