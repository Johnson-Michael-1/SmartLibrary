package com.ruoyi.smartlibrary.pojo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * @author: duzhibin
 * @description:
 * @date: create in 16:00 2022/07/11
 */
@Data
public class DevBookDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 记录序号 */
    private Long devId;

    /** 设备序列号 */
    private String devSn;

    /** 国际标准书号 */
    private String bkIsbn;

    /** rfid标签 */
    private String bkRfid;

    /** 书名 */
    private String bkName;

    /** 书籍封面图片 */
    private String bkImg;

    /** 作者 */
    private String bkAuthor;

    /** 设备名称 */
    private String devName;

    /** 所属组织 */
    private String orgName;

    /** 设备地址 */
    private String devAddress;

    /** 设备备注 */
    private String devRemark;

}
