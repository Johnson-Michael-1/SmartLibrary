package com.ruoyi.smartlibrary.mapper;

import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ReaderInfoMapper {

    /**
     * 查询读者信息列表
     * @param readerInfo
     * @return
     */
    List<ReaderInfo> selectList(ReaderInfo readerInfo);

    /**
     * 根据id进行删除
     * @param readerIds
     * @return
     */
    int deleteByIds(Long[] readerIds);

    /**
     * 新增读者信息
     * @param readerInfo
     * @return
     */
    int insert(ReaderInfo readerInfo);

    /**
     * 根据id查询
     * @param readerId
     * @return
     */
    ReaderInfo selectById(Long readerId);

    /**
     * 根据身份证号更新
     * @param readerInfo
     * @return
     */
    int updateReaderInfo(ReaderInfo readerInfo);

    /**
     * 查询读者信息是否存在
     * @param readerInfo
     * @return
     */
    ReaderInfo selectReaderInfo(ReaderInfo readerInfo);
}