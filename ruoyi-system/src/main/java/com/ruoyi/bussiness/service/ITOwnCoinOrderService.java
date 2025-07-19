package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TOwnCoinOrder;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 申购订单Service接口
 * 
 * @author ruoyi
 * @date 2023-09-20
 */
public interface ITOwnCoinOrderService extends IService<TOwnCoinOrder>
{
    /**
     * 查询申购订单
     * 
     * @param id 申购订单主键
     * @return 申购订单
     */
    public TOwnCoinOrder selectTOwnCoinOrderById(Long id);

    /**
     * 查询申购订单列表
     * 
     * @param tOwnCoinOrder 申购订单
     * @return 申购订单集合
     */
    public List<TOwnCoinOrder> selectTOwnCoinOrderList(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 新增申购订单
     * 
     * @param tOwnCoinOrder 申购订单
     * @return 结果
     */
    public int insertTOwnCoinOrder(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 修改申购订单
     * 
     * @param tOwnCoinOrder 申购订单
     * @return 结果
     */
    public int updateTOwnCoinOrder(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 批量删除申购订单
     * 
     * @param ids 需要删除的申购订单主键集合
     * @return 结果
     */
    public int deleteTOwnCoinOrderByIds(Long[] ids);

    /**
     * 删除申购订单信息
     * 
     * @param id 申购订单主键
     * @return 结果
     */
    public int deleteTOwnCoinOrderById(Long id);

    String createOrder(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 订阅申购新发币
     *
     * @param tOwnCoinOrder
     * @return
     */
    AjaxResult placingCoins(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 审批申购订单
     *
     * @param tOwnCoinOrder
     * @return
     */
    AjaxResult editPlacing(TOwnCoinOrder tOwnCoinOrder);
}
