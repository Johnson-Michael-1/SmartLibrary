package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 10:50 2022/07/11
 */
@Data
public class UserRoleAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色id */
    private Long roleId;

    /** 角色id */
    private String roleName;

    /** 角色权限字符串 */
    private String roleKey;

    /** 角色状态（0正常 1停用 2已删除 */
    private String status;

    /** 显示顺序 */
    private Integer roleSort;

    /** 数据范围（1：全部数据权限 2：自定数据权限） */
    private String dataScope;

    /** 组织树选择项是否关联显示 */
    private Integer orgCheckStrictly;

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

    /** 备注 */
    private String remark;

}
