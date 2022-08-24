package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 09:06 2022/07/11
 */
@Data
public class RecordRemote extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 开门记录id */
    private String openId;

    /** 组织id */
    private String orgId;

    /** 组织名称（设备地点） */
    private String orgName;

    /** 开门时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openTime;

    /** 操作人员 */
    private String operator;

    /** 设备名称 */
    private String devName;

    /** 设备序列号 */
    private String devSn;

    /** 开门结果  ( 0成功 1失败 ) */
    private String openResult;

}
