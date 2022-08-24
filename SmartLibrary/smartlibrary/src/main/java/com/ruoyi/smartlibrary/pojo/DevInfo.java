package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 设备信息表 t_device
 *
 * @author: duzhibin
 * @description:
 * @date: create in 19:40 2022/07/07
 */
@Data
public class DevInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 设备序号 */
    private Long devId;

    /** 设备序列号 */
    private String devSn;

    /** 设备名称 */
    private String devName;

    /** 组织ID */
    private Long deptId;

    /** 组织名称 */
    private String deptName;

    /** 设备类型 */
    private String devType;

    /** 关联时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date linkTime;

    /** 设备地址 */
    private String devAddress;

    /** 所属组织 */
    private String orgName;

    /** 设备状态  0正常  1异常 2删除 */
    private String devState;

    /** 设备备注 */
    private String devRemark;

}