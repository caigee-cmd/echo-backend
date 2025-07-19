package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TMingProductUserMapper;
import com.ruoyi.bussiness.domain.TMingProductUser;
import com.ruoyi.bussiness.service.ITMingProductUserService;

/**
 * 用户购买质押限制Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-11
 */
@Service
public class TMingProductUserServiceImpl extends ServiceImpl<TMingProductUserMapper,TMingProductUser> implements ITMingProductUserService
{
    @Autowired
    private TMingProductUserMapper tMingProductUserMapper;

    /**
     * 查询用户购买质押限制
     * 
     * @param id 用户购买质押限制主键
     * @return 用户购买质押限制
     */
    @Override
    public TMingProductUser selectTMingProductUserById(Long id)
    {
        return tMingProductUserMapper.selectTMingProductUserById(id);
    }

    /**
     * 查询用户购买质押限制列表
     * 
     * @param tMingProductUser 用户购买质押限制
     * @return 用户购买质押限制
     */
    @Override
    public List<TMingProductUser> selectTMingProductUserList(TMingProductUser tMingProductUser)
    {
        return tMingProductUserMapper.selectTMingProductUserList(tMingProductUser);
    }

    /**
     * 新增用户购买质押限制
     * 
     * @param tMingProductUser 用户购买质押限制
     * @return 结果
     */
    @Override
    public int insertTMingProductUser(TMingProductUser tMingProductUser)
    {
        tMingProductUser.setCreateTime(DateUtils.getNowDate());
        tMingProductUser.setCreateBy(SecurityUtils.getUsername());
        tMingProductUser.setUpdateTime(DateUtils.getNowDate());
        tMingProductUser.setUpdateBy(SecurityUtils.getUsername());
        return tMingProductUserMapper.insertTMingProductUser(tMingProductUser);
    }

    /**
     * 修改用户购买质押限制
     * 
     * @param tMingProductUser 用户购买质押限制
     * @return 结果
     */
    @Override
    public int updateTMingProductUser(TMingProductUser tMingProductUser)
    {
        tMingProductUser.setUpdateTime(DateUtils.getNowDate());
        tMingProductUser.setUpdateBy(SecurityUtils.getUsername());
        return tMingProductUserMapper.updateTMingProductUser(tMingProductUser);
    }

    /**
     * 批量删除用户购买质押限制
     * 
     * @param ids 需要删除的用户购买质押限制主键
     * @return 结果
     */
    @Override
    public int deleteTMingProductUserByIds(Long[] ids)
    {
        return tMingProductUserMapper.deleteTMingProductUserByIds(ids);
    }

    /**
     * 删除用户购买质押限制信息
     * 
     * @param id 用户购买质押限制主键
     * @return 结果
     */
    @Override
    public int deleteTMingProductUserById(Long id)
    {
        return tMingProductUserMapper.deleteTMingProductUserById(id);
    }
}
