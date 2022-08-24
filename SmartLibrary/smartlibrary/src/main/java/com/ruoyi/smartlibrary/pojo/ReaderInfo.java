package com.ruoyi.smartlibrary.pojo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 读者表 t_reader
 *
 * @author: duzhibin
 * @description:
 * @date: create in 20:09 2022/07/07
 */
@Data
public class ReaderInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 读者序号 */
    private Long rdId;

    /** 读者编号 */
    private String rdNumber;

    /** 读者身份证号 */
    private String rdIdNumber;

    /** 微信号 */
    private String rdWxNumber;

    /** 支付宝号 */
    private String rdZfbNumber;

    /** 读者姓名 */
    private String rdName;

    /** 读者手机号 */
    private String rdPhone;

    /** 读者照片 */
    private String rdPic;

    /** 读者登录密码 */
    private String rdPassword;

    /** 读者性别  0女 1男 */
    private String rdSex;

    /** 读者状态  0正常 1注销 */
    private String rdState;

    /** 备注 */
    private String rdRemark;

    /** 昵称 */
    private String nickName;

    /** 组织id */
    private Long deptId;

    /** 组织名称 */
    private String deptName;



}