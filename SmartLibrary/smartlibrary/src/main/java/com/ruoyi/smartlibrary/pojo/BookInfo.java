package com.ruoyi.smartlibrary.pojo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.annotation.Excels;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 书籍信息表  t_bookInfo
 *
 * @author: duzhibin
 * @description:
 * @date: create in 19:24 2022/07/07
 */
@Data
public class BookInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 书籍序号 */
    @Excel(name = "书籍序号", cellType = ColumnType.NUMERIC, prompt = "书籍编号")
    private Long bkId;

    /** 国际标准书号 */
    @Excel(name = "国际标准书号", cellType = ColumnType.NUMERIC, prompt = "书籍ISBN")
    private String bkIsbn;

    /** 书籍rfid编号 */
    @Excel(name = "书籍RFID", cellType = ColumnType.NUMERIC, prompt = "书籍RFID")
    private String bkRfid;

    /** 书名 */
    @Excel(name = "书名")
    private String bkName;

    /** 书籍封面信息 */
    private String bkImg;

    /** 作者 */
    @Excel(name = "作者")
    private String bkAuthor;

    /** 价格(元) */
    @Excel(name = "价格")
    private Double bkPrice;

    /** 书籍类型 */
    @Excel(name = "书籍类型")
    private String bkType;

    /** 出版社 */
    @Excel(name = "出版社")
    private String bkPress;

    /** 摘要（备注） */
    @Excel(name = "摘要（备注）")
    private String bkAbstract;

    /** 图书状态 0正常 1借出 2其他 */
    @Excel(name = "图书状态", readConverterExp = "0=正常,1=借出,2=其他")
    private String bkState;

    /** 出版时间 */
    @Excel(name = "出版时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date publishTime;

    /** 页数 */
    @Excel(name = "页数")
    private Long bkPagination;

    /** 组织id */
    @Excel(name = "组织id")
    private Long deptId;

    /** 组织名称 */
    @Excel(name = "组织名称")
    private String deptName;

}