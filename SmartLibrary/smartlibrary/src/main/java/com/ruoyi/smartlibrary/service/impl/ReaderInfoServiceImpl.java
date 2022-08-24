package com.ruoyi.smartlibrary.service.impl;

import com.ruoyi.smartlibrary.mapper.ReaderInfoMapper;
import com.ruoyi.smartlibrary.pojo.ReaderInfo;
import com.ruoyi.smartlibrary.service.IReaderInfoService;
import com.ruoyi.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 10:12 2022/07/18
 */
@Service
public class ReaderInfoServiceImpl implements IReaderInfoService {

    @Autowired
    private ReaderInfoMapper readerInfoMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询读者信息列表集合
     * @param readerInfo
     * @return
     */
    @Override
    public List<ReaderInfo> selectList(ReaderInfo readerInfo) {
        return readerInfoMapper.selectList(readerInfo);
    }

    /**
     * 新增读者信息
     * @param readerInfo
     * @return
     */
    @Override
    public int insert(ReaderInfo readerInfo) {
        System.out.println(readerInfo.toString());
        @NotBlank(message = "组织名称不能为空") @Size(min = 0, max = 30, message = "组织名称长度不能超过30个字符")
        String deptName = sysDeptMapper.selectDeptById(readerInfo.getDeptId()).getDeptName();
        readerInfo.setDeptName(deptName);
        return readerInfoMapper.insert(readerInfo);
    }

    /**
     * 更新读者信息
     * @param readerInfo
     * @return
     */
    @Override
    public int updateReaderInfo(ReaderInfo readerInfo) {
        @NotBlank(message = "组织名称不能为空") @Size(min = 0, max = 30, message = "组织名称长度不能超过30个字符")
        String deptName = sysDeptMapper.selectDeptById(readerInfo.getDeptId()).getDeptName();
        readerInfo.setDeptName(deptName);
        return readerInfoMapper.updateReaderInfo(readerInfo);
    }

    /**
     * 根据id获取读者信息
     * @param id
     * @return
     */
    @Override
    public ReaderInfo getReaderInfo(Long id) {
        return readerInfoMapper.selectById(id);
    }

    /**
     * 根据id批量删除
     * @param readerInfoIds
     * @return
     */
    @Override
    public int deleteReaderInfoByIds(Long[] readerInfoIds) {
        return readerInfoMapper.deleteByIds(readerInfoIds);
    }

    /**
     * 查询读者信息是否存在
     * @param readerInfo
     * @return
     */
    @Override
    public ReaderInfo selectReaderInfo(ReaderInfo readerInfo) {
        return readerInfoMapper.selectReaderInfo(readerInfo);
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @Override
    public int resetPassword(Long id) {
        ReaderInfo readerInfo = readerInfoMapper.selectById(id);
        readerInfo.setRdPassword("123456");
        return readerInfoMapper.updateReaderInfo(readerInfo);
    }
}
