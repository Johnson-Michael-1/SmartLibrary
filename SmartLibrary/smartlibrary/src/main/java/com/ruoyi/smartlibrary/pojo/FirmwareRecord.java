package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 11:13 2022/08/17
 */
@Data
public class FirmwareRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 固件升级记录id */
    private Long id;

    /** 设备序列号 */
    private String devSn;

    /** 升级前版本号 */
    private String oldVersion;

    /** 升级后版本号 */
    private String newVersion;

    /** 升级时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date upgradeTime;

    /** 是否强制安装(0:否 、1：是) */
    private String updateInstall;

    /** 更新结果(0:成功、1：失败) */
    private String updateResult;

    /** 设备名称 */
    private String devName;

    /** 组织id */
    private Long deptId;

    /** 组织名称 */
    private String deptName;

}
