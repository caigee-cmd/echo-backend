package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.DefiActivityMapper;
import com.ruoyi.bussiness.domain.DefiActivity;
import com.ruoyi.bussiness.service.IDefiActivityService;

/**
 * 空投活动Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
@Service
public class DefiActivityServiceImpl extends ServiceImpl<DefiActivityMapper,DefiActivity> implements IDefiActivityService
{
    @Autowired
    private DefiActivityMapper defiActivityMapper;

    /**
     * 查询空投活动
     * 
     * @param id 空投活动主键
     * @return 空投活动
     */
    @Override
    public DefiActivity selectDefiActivityById(Long id)
    {
        return defiActivityMapper.selectDefiActivityById(id);
    }

    /**
     * 查询空投活动列表
     * 
     * @param defiActivity 空投活动
     * @return 空投活动
     */
    @Override
    public List<DefiActivity> selectDefiActivityList(DefiActivity defiActivity)
    {
        return defiActivityMapper.selectDefiActivityList(defiActivity);
    }

    /**
     * 新增空投活动
     * 
     * @param defiActivity 空投活动
     * @return 结果
     */
    @Override
    public int insertDefiActivity(DefiActivity defiActivity)
    {
        defiActivity.setCreateTime(DateUtils.getNowDate());
        return defiActivityMapper.insertDefiActivity(defiActivity);
    }

    /**
     * 修改空投活动
     * 
     * @param defiActivity 空投活动
     * @return 结果
     */
    @Override
    public int updateDefiActivity(DefiActivity defiActivity)
    {
        defiActivity.setUpdateTime(DateUtils.getNowDate());
        return defiActivityMapper.updateDefiActivity(defiActivity);
    }

    /**
     * 批量删除空投活动
     * 
     * @param ids 需要删除的空投活动主键
     * @return 结果
     */
    @Override
    public int deleteDefiActivityByIds(Long[] ids)
    {
        return defiActivityMapper.deleteDefiActivityByIds(ids);
    }

    /**
     * 删除空投活动信息
     * 
     * @param id 空投活动主键
     * @return 结果
     */
    @Override
    public int deleteDefiActivityById(Long id)
    {
        return defiActivityMapper.deleteDefiActivityById(id);
    }
}
