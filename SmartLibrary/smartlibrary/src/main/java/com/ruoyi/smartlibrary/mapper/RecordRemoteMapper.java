package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.RecordRemote;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: duzhibin
 * @description:
 * @date: create in 15:05 2022/07/11
 */
@Mapper
public interface RecordRemoteMapper {

    /**
     * 根据id删除远程开门记录
     * @param openIds
     * @return
     */
    int deleteByIds(Long[] openIds);

    /**
     * 新增组织信息
     * @param kmRecordRemote
     * @return
     */
    int insert(RecordRemote kmRecordRemote);

    /**
     * 根据id进行查询
     * @param openId
     * @return
     */
    RecordRemote selectById(Long openId);

    /**
     * 根据id进行更新
     * @param recordRemote
     * @return
     */
    int updateRecordRemote(RecordRemote recordRemote);

    /**
     * 查询远程开门记录
     * @param recordRemote
     * @return
     */
    List<RecordRemote> selectList(RecordRemote recordRemote);
}
