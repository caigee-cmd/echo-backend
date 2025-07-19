package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TCollectionOrderMapper;
import com.ruoyi.bussiness.domain.TCollectionOrder;
import com.ruoyi.bussiness.service.ITCollectionOrderService;

import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
@Service
public class TCollectionOrderServiceImpl extends ServiceImpl<TCollectionOrderMapper,TCollectionOrder> implements ITCollectionOrderService
{
    @Resource
    private TCollectionOrderMapper tCollectionOrderMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TCollectionOrder selectTCollectionOrderById(Long id)
    {
        return tCollectionOrderMapper.selectTCollectionOrderById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tCollectionOrder 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TCollectionOrder> selectTCollectionOrderList(TCollectionOrder tCollectionOrder)
    {
        return tCollectionOrderMapper.selectTCollectionOrderList(tCollectionOrder);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param tCollectionOrder 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTCollectionOrder(TCollectionOrder tCollectionOrder)
    {
        tCollectionOrder.setCreateTime(DateUtils.getNowDate());
        return tCollectionOrderMapper.insertTCollectionOrder(tCollectionOrder);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param tCollectionOrder 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTCollectionOrder(TCollectionOrder tCollectionOrder)
    {
        tCollectionOrder.setUpdateTime(DateUtils.getNowDate());
        return tCollectionOrderMapper.updateTCollectionOrder(tCollectionOrder);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTCollectionOrderByIds(Long[] ids)
    {
        return tCollectionOrderMapper.deleteTCollectionOrderByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTCollectionOrderById(Long id)
    {
        return tCollectionOrderMapper.deleteTCollectionOrderById(id);
    }

    @Override
    public BigDecimal selectCollectionAmountByUserId(Long userId) {
        return tCollectionOrderMapper.selectCollectionAmountByUserId(userId);
    }

    @Override
    public BigDecimal selectCollectionAmountByAgencyId(Long agencyId) {
        return tCollectionOrderMapper.selectCollectionAmountByAgencyId(agencyId);
    }

    @Override
    public BigDecimal selectCollectionAmountDetail(Long appUserId, String adminParentIds) {
        return tCollectionOrderMapper.selectCollectionAmountDetail(appUserId,adminParentIds);
    }

    @Override
    public BigDecimal getDayCollectionAmount(String beginTime, String endTime) {
        return tCollectionOrderMapper.getDayCollectionAmount(beginTime,endTime);
    }
}
