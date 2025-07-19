package com.ruoyi.bussiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.THelpCenterInfo;
import com.ruoyi.bussiness.mapper.THelpCenterInfoMapper;
import com.ruoyi.bussiness.service.ITHelpCenterInfoService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 帮助中心问题详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
@Service
public class THelpCenterInfoServiceImpl extends ServiceImpl<THelpCenterInfoMapper,THelpCenterInfo> implements ITHelpCenterInfoService
{
    @Autowired
    private THelpCenterInfoMapper tHelpCenterInfoMapper;

    /**
     * 查询帮助中心问题详情
     * 
     * @param id 帮助中心问题详情主键
     * @return 帮助中心问题详情
     */
    @Override
    public THelpCenterInfo selectTHelpCenterInfoById(Long id)
    {
        return tHelpCenterInfoMapper.selectTHelpCenterInfoById(id);
    }

    /**
     * 查询帮助中心问题详情列表
     * 
     * @param tHelpCenterInfo 帮助中心问题详情
     * @return 帮助中心问题详情
     */
    @Override
    public List<THelpCenterInfo> selectTHelpCenterInfoList(THelpCenterInfo tHelpCenterInfo)
    {
        return tHelpCenterInfoMapper.selectTHelpCenterInfoList(tHelpCenterInfo);
    }

    /**
     * 新增帮助中心问题详情
     * 
     * @param tHelpCenterInfo 帮助中心问题详情
     * @return 结果
     */
    @Override
    public int insertTHelpCenterInfo(THelpCenterInfo tHelpCenterInfo)
    {
        tHelpCenterInfo.setCreateTime(DateUtils.getNowDate());
        tHelpCenterInfo.setUpdateTime(DateUtils.getNowDate());
        tHelpCenterInfo.setCreateBy(SecurityUtils.getUsername());
        tHelpCenterInfo.setUpdateBy(SecurityUtils.getUsername());
        tHelpCenterInfo.setDelFlag("0");
        return tHelpCenterInfoMapper.insertTHelpCenterInfo(tHelpCenterInfo);
    }

    /**
     * 修改帮助中心问题详情
     * 
     * @param tHelpCenterInfo 帮助中心问题详情
     * @return 结果
     */
    @Override
    public int updateTHelpCenterInfo(THelpCenterInfo tHelpCenterInfo)
    {
        tHelpCenterInfo.setUpdateTime(DateUtils.getNowDate());
        tHelpCenterInfo.setUpdateBy(SecurityUtils.getUsername());
        return tHelpCenterInfoMapper.updateTHelpCenterInfo(tHelpCenterInfo);
    }

    /**
     * 批量删除帮助中心问题详情
     * 
     * @param ids 需要删除的帮助中心问题详情主键
     * @return 结果
     */
    @Override
    public int deleteTHelpCenterInfoByIds(Long[] ids)
    {
        return tHelpCenterInfoMapper.deleteTHelpCenterInfoByIds(ids);
    }

    /**
     * 删除帮助中心问题详情信息
     * 
     * @param id 帮助中心问题详情主键
     * @return 结果
     */
    @Override
    public int deleteTHelpCenterInfoById(Long id)
    {
        return tHelpCenterInfoMapper.deleteTHelpCenterInfoById(id);
    }
}
