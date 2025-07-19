package com.ruoyi.bussiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.THelpCenter;
import com.ruoyi.bussiness.mapper.THelpCenterMapper;
import com.ruoyi.bussiness.service.ITHelpCenterService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 帮助中心Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
@Service
public class THelpCenterServiceImpl extends ServiceImpl<THelpCenterMapper,THelpCenter> implements ITHelpCenterService
{
    @Autowired
    private THelpCenterMapper tHelpCenterMapper;

    /**
     * 查询帮助中心
     * 
     * @param id 帮助中心主键
     * @return 帮助中心
     */
    @Override
    public THelpCenter selectTHelpCenterById(Long id)
    {
        return tHelpCenterMapper.selectTHelpCenterById(id);
    }

    /**
     * 查询帮助中心列表
     * 
     * @param tHelpCenter 帮助中心
     * @return 帮助中心
     */
    @Override
    public List<THelpCenter> selectTHelpCenterList(THelpCenter tHelpCenter)
    {
        return tHelpCenterMapper.selectTHelpCenterList(tHelpCenter);
    }

    /**
     * 新增帮助中心
     * 
     * @param tHelpCenter 帮助中心
     * @return 结果
     */
    @Override
    public int insertTHelpCenter(THelpCenter tHelpCenter)
    {
        tHelpCenter.setCreateTime(DateUtils.getNowDate());
        tHelpCenter.setDelFlag("0");
        tHelpCenter.setUpdateTime(DateUtils.getNowDate());
        tHelpCenter.setUpdateBy(SecurityUtils.getUsername());
        tHelpCenter.setCreateBy(SecurityUtils.getUsername());
        return tHelpCenterMapper.insertTHelpCenter(tHelpCenter);
    }

    /**
     * 修改帮助中心
     * 
     * @param tHelpCenter 帮助中心
     * @return 结果
     */
    @Override
    public int updateTHelpCenter(THelpCenter tHelpCenter)
    {
        tHelpCenter.setUpdateTime(DateUtils.getNowDate());
        tHelpCenter.setUpdateBy(SecurityUtils.getUsername());
        return tHelpCenterMapper.updateTHelpCenter(tHelpCenter);
    }

    /**
     * 批量删除帮助中心
     * 
     * @param ids 需要删除的帮助中心主键
     * @return 结果
     */
    @Override
    public int deleteTHelpCenterByIds(Long[] ids)
    {
        return tHelpCenterMapper.deleteTHelpCenterByIds(ids);
    }

    /**
     * 删除帮助中心信息
     * 
     * @param id 帮助中心主键
     * @return 结果
     */
    @Override
    public int deleteTHelpCenterById(Long id)
    {
        return tHelpCenterMapper.deleteTHelpCenterById(id);
    }

    @Override
    public List<THelpCenter> getCenterList(THelpCenter tHelpCenter) {
        return tHelpCenterMapper.getCenterList(tHelpCenter);
    }
}
