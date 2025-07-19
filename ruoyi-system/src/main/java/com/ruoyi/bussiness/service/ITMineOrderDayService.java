package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.TMineOrderDay;

import java.math.BigDecimal;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
public interface ITMineOrderDayService extends IService<TMineOrderDay>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param amount 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TMineOrderDay selectTMineOrderDayByAmount(BigDecimal amount);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineOrderDay 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TMineOrderDay> selectTMineOrderDayList(TMineOrderDay tMineOrderDay);

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineOrderDay 【请填写功能名称】
     * @return 结果
     */
    public int insertTMineOrderDay(TMineOrderDay tMineOrderDay);

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineOrderDay 【请填写功能名称】
     * @return 结果
     */
    public int updateTMineOrderDay(TMineOrderDay tMineOrderDay);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param amounts 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTMineOrderDayByAmounts(BigDecimal[] amounts);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param amount 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTMineOrderDayByAmount(BigDecimal amount);
}
