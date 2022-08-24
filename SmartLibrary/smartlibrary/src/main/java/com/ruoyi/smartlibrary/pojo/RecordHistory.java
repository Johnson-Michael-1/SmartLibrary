package com.ruoyi.smartlibrary.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:10 2022/07/15
 */
@Data
public class RecordHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 开门记录id */
    private Long openId;

    /** 组织id */
    private Long orgId;

    /** 设备序列号 */
    private String devSn;

    /** 设备名称 */
    private String devName;

    /** 抓拍图片路径 */
    private String takePhotoPath;

    /** 开门者姓名 */
    private String personName;

    /** 开门者账号 */
    private String cardNo;

    /** 开门方式 1代表人脸 2代表刷卡 3代表微信) */
    private String openMode;

    /** 开门类型 ( 0借阅  1归还) */
    private String openType;

    /** 组织名称 */
    private String orgName;

    /** 设备地点 */
    private String devAddress;

    /** 开门时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openTime;

    /** 开门结果 */
    private String openResult;

}
