package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 书籍借阅信息表  t_borrow
 *
 * @author: duzhibin
 * @description:
 * @date: create in 19:28 2022/07/07
 */

public class Borrow extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 借阅序号 */
    private Long borId;

    /** 国际标准书号 */
    private String bkIsbn;

    /** 设备序列号 */
    private String devSn;

    /** 设备名称 */
    private String devName;

    /** 归还人 */
    private String backName;

    /** 归还人身份证号 */
    private String backIdNumber;

    /** 书名 */
    private String bkName;

    /** 书籍rfid */
    private String bkRfid;

    /** 借阅者姓名 */
    private String rdName;

    /** 借阅者身份证号 */
    private String rdIdNumber;

    /** 借阅者手机号 */
    private String rdPhone;

    /** 借阅时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date borrowTime;

    /** 续借时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date renewTime;

    /** 应还时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shouldBackTime;

    /** 归还时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /** 借阅状态 0未归还 1归还 */
    private String bkState;

    /** 是否续借 0未续借 1续借 */
    private String renewState;

    /** 是否超期 0未超期 1超期 */
    private String bkOvertime;

    /** 超期要付的金额 */
    private Double bkPrice;





    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Date getRenewTime() {
        return renewTime;
    }

    public String getDevSn() {
        return devSn;
    }

    public void setDevSn(String devSn) {
        this.devSn = devSn;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getBackName() {
        return backName;
    }

    public void setBackName(String backName) {
        this.backName = backName;
    }

    public String getBackIdNumber() {
        return backIdNumber;
    }

    public void setBackIdNumber(String backIdNumber) {
        this.backIdNumber = backIdNumber;
    }

    public void setRenewTime(Date renewTime) {
        this.renewTime = renewTime;
    }

    public String getRenewState() {
        return renewState;
    }

    public void setRenewState(String renewState) {
        this.renewState = renewState;
    }

    public String getBkName() {
        return bkName;
    }

    public void setBkName(String bkName) {
        this.bkName = bkName;
    }

    public String getBkRfid() {
        return bkRfid;
    }

    public void setBkRfid(String bkRfid) {
        this.bkRfid = bkRfid;
    }

    public Long getBorId() {
        return borId;
    }

    public void setBorId(Long borId) {
        this.borId = borId;
    }

    public String getBkIsbn() {
        return bkIsbn;
    }

    public void setBkIsbn(String bkIsbn) {
        this.bkIsbn = bkIsbn;
    }

    public String getRdIdNumber() {
        return rdIdNumber;
    }

    public void setRdIdNumber(String rdIdNumber) {
        this.rdIdNumber = rdIdNumber;
    }

    public String getRdName() {
        return rdName;
    }

    public void setRdName(String rdName) {
        this.rdName = rdName;
    }

    public String getRdPhone() {
        return rdPhone;
    }

    public void setRdPhone(String rdPhone) {
        this.rdPhone = rdPhone;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getShouldBackTime() {
        return shouldBackTime;
    }

    public void setShouldBackTime(Date shouldBackTime) {
        this.shouldBackTime = shouldBackTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getBkState() {
        return bkState;
    }

    public void setBkState(String bkState) {
        this.bkState = bkState;
    }

    public String getBkOvertime() {
        return bkOvertime;
    }

    public void setBkOvertime(String bkOvertime) {
        this.bkOvertime = bkOvertime;
    }

    public Double getBkPrice() {
        return bkPrice;
    }

    public void setBkPrice(Double bkPrice) {
        this.bkPrice = bkPrice;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "borId=" + borId +
                ", bkIsbn='" + bkIsbn + '\'' +
                ", devSn='" + devSn + '\'' +
                ", devName='" + devName + '\'' +
                ", backName='" + backName + '\'' +
                ", backIdNumber='" + backIdNumber + '\'' +
                ", bkName='" + bkName + '\'' +
                ", bkRfid='" + bkRfid + '\'' +
                ", rdName='" + rdName + '\'' +
                ", rdIdNumber='" + rdIdNumber + '\'' +
                ", rdPhone='" + rdPhone + '\'' +
                ", borrowTime=" + borrowTime +
                ", renewTime=" + renewTime +
                ", shouldBackTime=" + shouldBackTime +
                ", returnTime=" + returnTime +
                ", bkState='" + bkState + '\'' +
                ", renewState='" + renewState + '\'' +
                ", bkOvertime='" + bkOvertime + '\'' +
                ", bkPrice=" + bkPrice +
                '}';
    }
}