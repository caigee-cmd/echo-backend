package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.bussiness.domain.TMineOrderDay;
import com.ruoyi.bussiness.mapper.TMineOrderDayMapper;
import com.ruoyi.bussiness.service.ITMineOrderDayService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
@Service
public class TMineOrderDayServiceImpl extends ServiceImpl<TMineOrderDayMapper, TMineOrderDay> implements ITMineOrderDayService
{
    @Autowired
    private TMineOrderDayMapper tMineOrderDayMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param amount 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TMineOrderDay selectTMineOrderDayByAmount(BigDecimal amount)
    {
        return tMineOrderDayMapper.selectTMineOrderDayByAmount(amount);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineOrderDay 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TMineOrderDay> selectTMineOrderDayList(TMineOrderDay tMineOrderDay)
    {
        return tMineOrderDayMapper.selectTMineOrderDayList(tMineOrderDay);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineOrderDay 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTMineOrderDay(TMineOrderDay tMineOrderDay)
    {
        tMineOrderDay.setCreateTime(DateUtils.getNowDate());
        return tMineOrderDayMapper.insertTMineOrderDay(tMineOrderDay);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineOrderDay 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTMineOrderDay(TMineOrderDay tMineOrderDay)
    {
        tMineOrderDay.setUpdateTime(DateUtils.getNowDate());
        return tMineOrderDayMapper.updateTMineOrderDay(tMineOrderDay);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param amounts 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineOrderDayByAmounts(BigDecimal[] amounts)
    {
        return tMineOrderDayMapper.deleteTMineOrderDayByAmounts(amounts);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param amount 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineOrderDayByAmount(BigDecimal amount)
    {
        return tMineOrderDayMapper.deleteTMineOrderDayByAmount(amount);
    }
}
