package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.TMineOrder;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
public interface ITMineOrderService extends IService<TMineOrder>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TMineOrder selectTMineOrderById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineOrder 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TMineOrder> selectTMineOrderList(TMineOrder tMineOrder);

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineOrder 【请填写功能名称】
     * @return 结果
     */
    public int insertTMineOrder(TMineOrder tMineOrder);

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineOrder 【请填写功能名称】
     * @return 结果
     */
    public int updateTMineOrder(TMineOrder tMineOrder);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTMineOrderByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTMineOrderById(Long id);
}
