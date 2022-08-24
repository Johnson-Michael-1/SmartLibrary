package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 14:57 2022/07/15
 */
public class AlarmInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 报警记录ID */
    private Long alarmId;

    /** 组织ID */
    private Long deptId;

    /** 组织名称 */
    private String deptName;

    /** 组织ID */
    private Integer orgId;

    /** 报警设备序列号 */
    private String devSn;

    /** 读者姓名 */
    private String rdName;

    /** 读者身份证号 */
    private String rdIdNumber;

    /** 组织名称 */
    private String orgName;


    /** 报警设备名称 */
    private String devName;

    /** 报警时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;

    /** 报警类型  0代表未知 1代表开门超时报警 2代表震动报警 */
    private String alarmType;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getDevSn() {
        return devSn;
    }

    public void setDevSn(String devSn) {
        this.devSn = devSn;
    }

    public String getRdName() {
        return rdName;
    }

    public void setRdName(String rdName) {
        this.rdName = rdName;
    }

    public String getRdIdNumber() {
        return rdIdNumber;
    }

    public void setRdIdNumber(String rdIdNumber) {
        this.rdIdNumber = rdIdNumber;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "alarmId=" + alarmId +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", orgId=" + orgId +
                ", devSn='" + devSn + '\'' +
                ", rdName='" + rdName + '\'' +
                ", rdIdNumber='" + rdIdNumber + '\'' +
                ", orgName='" + orgName + '\'' +
                ", devName='" + devName + '\'' +
                ", alarmTime=" + alarmTime +
                ", alarmType='" + alarmType + '\'' +
                '}';
    }
}
