package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.Firmware;
import com.ruoyi.smartlibrary.pojo.FirmwareRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 11:49 2022/08/17
 */
@Mapper
public interface FirmwareRecordMapper {

    /**
     * 查询固件更新记录
     * @param firmwareRecord
     * @return
     */
    List<FirmwareRecord> selectList(FirmwareRecord firmwareRecord);

    /**
     * 新增固件更新记录
     * @param firmwareRecord
     * @return
     */
    int insert(FirmwareRecord firmwareRecord);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    FirmwareRecord selectById(Long id);


}
