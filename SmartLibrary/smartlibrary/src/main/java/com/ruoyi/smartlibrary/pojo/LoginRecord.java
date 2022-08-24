package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 *
 * 读者登录记录
 *
 * @author: Mei Pq
 * @description:
 * @date: create in 16:11 2022/08/01
 */
@Data
public class LoginRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 登录序号 */
    private Long loginId;

    /** 读者姓名 */
    private String rdName;

    /** 读者身份证号 */
    private String rdIdNumber;

    /** 读者手机号 */
    private String rdPhone;

    /** 登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /** 登录方式 */
    private String loginType;

    /** 读者性别 */
    private String rdSex;

    /** 读者状态 */
    private String rdState;

    /** 设备序列号 */
    private String devSn;

    /** 设备名称 */
    private String devName;

}
