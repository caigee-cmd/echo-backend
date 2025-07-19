package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

import com.ruoyi.bussiness.domain.TMineUser;
import com.ruoyi.bussiness.mapper.TMineUserMapper;
import com.ruoyi.bussiness.service.ITMineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
@Service
public class TMineUserServiceImpl extends ServiceImpl<TMineUserMapper, TMineUser> implements ITMineUserService
{
    @Autowired
    private TMineUserMapper tMineUserMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param userId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TMineUser selectTMineUserByUserId(Long userId)
    {
        return tMineUserMapper.selectTMineUserByUserId(userId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineUser 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TMineUser> selectTMineUserList(TMineUser tMineUser)
    {
        return tMineUserMapper.selectTMineUserList(tMineUser);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineUser 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTMineUser(TMineUser tMineUser)
    {
        return tMineUserMapper.insertTMineUser(tMineUser);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineUser 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTMineUser(TMineUser tMineUser)
    {
        return tMineUserMapper.updateTMineUser(tMineUser);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param userIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineUserByUserIds(Long[] userIds)
    {
        return tMineUserMapper.deleteTMineUserByUserIds(userIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param userId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineUserByUserId(Long userId)
    {
        return tMineUserMapper.deleteTMineUserByUserId(userId);
    }
}
