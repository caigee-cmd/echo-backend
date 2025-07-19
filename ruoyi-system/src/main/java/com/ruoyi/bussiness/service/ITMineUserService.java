package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.ruoyi.bussiness.domain.TMineUser;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
public interface ITMineUserService extends IService<TMineUser>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param userId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TMineUser selectTMineUserByUserId(Long userId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineUser 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TMineUser> selectTMineUserList(TMineUser tMineUser);

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineUser 【请填写功能名称】
     * @return 结果
     */
    public int insertTMineUser(TMineUser tMineUser);

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineUser 【请填写功能名称】
     * @return 结果
     */
    public int updateTMineUser(TMineUser tMineUser);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param userIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTMineUserByUserIds(Long[] userIds);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param userId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTMineUserByUserId(Long userId);
}
