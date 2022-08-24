package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 10:28 2022/07/11
 */
@Data
public class AlarmHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 报警记录ID */
    private Long alarmId;

    /** 报警设备序列号 */
    private String devSn;

    /** 报警设备名称 */
    private String devName;

    /** 报警时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;

    /** 报警类型  0代表未知 1代表开门超时报警 2代表震动报警 */
    private String alarmType;

    /** 报警原因 */
    private String alarmReason;

    /** 报警地点(所属组织) */
    private String alarmAddress;















}
