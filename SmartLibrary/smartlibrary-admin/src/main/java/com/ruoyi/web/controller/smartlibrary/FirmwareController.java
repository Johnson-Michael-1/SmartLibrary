package com.ruoyi.web.controller.smartlibrary;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.smartlibrary.pojo.FileAndString;
import com.ruoyi.smartlibrary.pojo.Firmware;
import com.ruoyi.smartlibrary.service.IFirmwareService;
import com.ruoyi.smartlibrary.socket.WsServerEndpoint;
import com.ruoyi.web.ftp.FTP;
import io.swagger.v3.oas.annotations.Parameters;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 14:38 2022/08/17
 */
@RestController
@RequestMapping("firmware")
public class FirmwareController extends BaseController {

    @Autowired
    private IFirmwareService firmwareService;

    /**
     * 请求终端远程升级
     * @param jsonObject
     * @return
     */
    @RequestMapping("/uploadFirmware")
    @ResponseBody
    public AjaxResult uploadFirmware(@RequestBody JSONObject jsonObject) {
        return firmwareService.uploadFirmware(jsonObject);
    }


    /**
     * 查询固件信息列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:firmware:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(Firmware firmware){
        startPage();
        List<Firmware> firmwareList = firmwareService.selectList(firmware);
        logger.info("信息"+ firmwareList);
        return getDataTable(firmwareList);
    }

    /**
     * 获取最新版本
     * @return
     */
    @PreAuthorize("@ss.hasPermi('smartLibrary:firmware:getVersion')")
    @GetMapping("/getLatestVersion")
    public String getLatestVersion(){
        JSONObject jsonObject = new JSONObject();
        Firmware firmware = firmwareService.getLatestVersion();
        if (null != firmware){
            jsonObject.put("url",firmware.getDownloadUrl());
            jsonObject.put("version",firmware.getVersion());
            return jsonObject.toJSONString();
        }else {
            jsonObject.put("url","null");
            jsonObject.put("version","null");
            return jsonObject.toJSONString();
        }
    }

    /**
     * 上传文件
     * @param file 上传的文件
     * @return
     * @throws IOException
     */
    @PreAuthorize("@ss.hasPermi('system:firmware:add')")
    @Log(title = "固件管理", businessType = BusinessType.UPDATE)
    @PostMapping("/file")
    public AjaxResult uploadFile(@RequestBody MultipartFile file) throws IOException
    {
        System.out.println("文件名是：" + file.getOriginalFilename());
        if (!file.isEmpty())
        {
            System.out.println("去上传------------");
//            String uploadUrl = FileUploadUtils.upload(RuoYiConfig.getPath(), file);
            // 创建FTP对象
            FTP ftp = new FTP("ATTATEST_");
            try {

                // 连接FTP
                ftp.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            File f = null;
//            boolean upload = ftp.upload((File) file);
            try {
                f = M2F(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File finalF = f;
            new Thread(() -> {
                boolean upload = ftp.upload(finalF);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action","uploadFile");
                if(upload){
                    jsonObject.put("msg","上传文件成功");
                    jsonObject.put("code",1);
                }else{
                    jsonObject.put("msg","上传文件失败");
                    jsonObject.put("code",-1);
                }
                WsServerEndpoint.sendToWebView(JSON.toJSONString(jsonObject),"uploadFile");
            }).start();
            return AjaxResult.success();
//            boolean upload = ftp.upload(f);
//            System.out.println("上传文件：" + upload);
//
//            if (upload)
//            {
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("uploadUrl", null);
//                return ajax;
//            }
        }
        return AjaxResult.error("上传文件异常，请联系管理员");
    }


    public File M2F(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        File f = File.createTempFile(originalFilename.substring(0,originalFilename.length()-4), ".apk");

        file.transferTo(f);
        return f;
    }





    /**
     * 新增固件信息
     * @param firmware
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:firmware:add')")
    @Log(title = "固件管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Firmware firmware){
        firmware.setCreateBy(getUsername());
        String downloadUrl = firmware.getDownloadUrl();
        firmware.setDownloadUrl("http://47.99.130.179:18080" + downloadUrl);
        String name = StringUtils.substringAfterLast(downloadUrl, "/");
        firmware.setApkName(name);
        String version = StringUtils.substringBetween(downloadUrl, "_", "_");
        firmware.setVersion(version);
        firmware.setUploadTime(new Date());
        System.out.println(firmware.toString());
        return toAjax(firmwareService.insert(firmware));
    }

    /**
     * 修改固件信息
     */
    @PreAuthorize("@ss.hasPermi('system:firmware:edit')")
    @Log(title = "固件管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateFirmware(@Validated @RequestBody Firmware firmware) {
        firmware.setCreateBy(getUsername());
        return toAjax(firmwareService.updateFirmware(firmware));
    }

    /**
     * 根据id获取固件信息
     */
    @PreAuthorize("@ss.hasPermi('system:firmware:getfirmware')")
    @GetMapping("/get/{id}")
    public AjaxResult getFirmware(@PathVariable Long id){
        return AjaxResult.success(firmwareService.getFirmware(id));
    }


    /**
     * 删除固件信息
     */
    @PreAuthorize("@ss.hasPermi('system:firmware:remove')")
    @Log(title = "固件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(firmwareService.deleteFirmwareByIds(ids));
    }

}
