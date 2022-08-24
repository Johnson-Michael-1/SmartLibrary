package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 *
 * 固件信息
 *
 * @author: Mei Pq
 * @description:
 * @date: create in 09:58 2022/08/17
 */
@Data
public class Firmware extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** apk文件名称 */
    private String apkName;

    /** 版本号 */
    private String version;

    /** 升级下载网址 */
    private String downloadUrl;

    /** 升级日志 */
    private String updateLog;

    /** 下载次数 */
    private Integer downloadCount;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;




}

