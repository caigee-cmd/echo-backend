package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.THelpCenterInfo;

import java.util.List;

/**
 * 帮助中心问题详情Service接口
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
public interface ITHelpCenterInfoService extends IService<THelpCenterInfo>
{
    /**
     * 查询帮助中心问题详情
     * 
     * @param id 帮助中心问题详情主键
     * @return 帮助中心问题详情
     */
    public THelpCenterInfo selectTHelpCenterInfoById(Long id);

    /**
     * 查询帮助中心问题详情列表
     * 
     * @param tHelpCenterInfo 帮助中心问题详情
     * @return 帮助中心问题详情集合
     */
    public List<THelpCenterInfo> selectTHelpCenterInfoList(THelpCenterInfo tHelpCenterInfo);

    /**
     * 新增帮助中心问题详情
     * 
     * @param tHelpCenterInfo 帮助中心问题详情
     * @return 结果
     */
    public int insertTHelpCenterInfo(THelpCenterInfo tHelpCenterInfo);

    /**
     * 修改帮助中心问题详情
     * 
     * @param tHelpCenterInfo 帮助中心问题详情
     * @return 结果
     */
    public int updateTHelpCenterInfo(THelpCenterInfo tHelpCenterInfo);

    /**
     * 批量删除帮助中心问题详情
     * 
     * @param ids 需要删除的帮助中心问题详情主键集合
     * @return 结果
     */
    public int deleteTHelpCenterInfoByIds(Long[] ids);

    /**
     * 删除帮助中心问题详情信息
     * 
     * @param id 帮助中心问题详情主键
     * @return 结果
     */
    public int deleteTHelpCenterInfoById(Long id);
}
