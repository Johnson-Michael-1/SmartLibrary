package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员表 t_manager
 *
 * @author: duzhibin
 * @description:
 * @date: create in 19:46 2022/07/07
 */
@Data
public class UerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 管理员序号 */
    private Long maId;

    /** 管理员身份证号 */
    private String maIdNumber;

    /** 管理员姓名 */
    private String maName;

    /** 所属组织 */
    private String orgName;

    /** 管理员账户名 */
    private String maAccount;

    /** 管理员登录密码 */
    private String maPassword;

    /** 管理员手机号 */
    private String maPhone;

    /** 人员状态  0正常 1禁用 */
    private String maState;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}