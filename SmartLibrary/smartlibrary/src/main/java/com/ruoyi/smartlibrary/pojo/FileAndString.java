package com.ruoyi.smartlibrary.pojo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.File;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 15:02 2022/08/19
 */
@Data
public class FileAndString extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** apk文件 */
    private File file;

    /** 日志 */
    private String updateLog;

}
