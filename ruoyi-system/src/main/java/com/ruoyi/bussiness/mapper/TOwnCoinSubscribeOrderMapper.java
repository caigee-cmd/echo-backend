package com.ruoyi.bussiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TOwnCoinSubscribeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台新币订阅订单Mapper接口
 *
 * @author ruoyi
 * @date 2023/10/9
 */
public interface TOwnCoinSubscribeOrderMapper extends BaseMapper<TOwnCoinSubscribeOrder> {

    /**
     * 新增订阅订单
     *
     * @param tOwnCoinSubscribeOrder 订阅订单
     * @return 结果
     */
    public int insertTOwnCoinSubscribeOrder(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder);

    /**
     * 修改订阅订单
     *
     * @param tOwnCoinSubscribeOrder 订阅订单
     * @return 结果
     */
    public int updateTOwnCoinSubscribeOrder(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder);

    /**
     * 查询用户订阅新发币列表
     *
     * @param tOwnCoinSubscribeOrder
     * @return
     */
    List<TOwnCoinSubscribeOrder> selectTOwnCoinSubscribeOrderList(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder);

    /**
     * 条件查询用户订阅记录
     *
     * @param tOwnCoinSubscribeOrder
     * @return
     */
    int selectTOwnCoinSubscribeOrderRecord(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder);

    /**
     * 查询订阅审批订单
     *
     * @param ownId
     * @param userId
     * @return
     */
    TOwnCoinSubscribeOrder getOrderById(@Param("ownId") Long ownId, @Param("userId") Long userId);

    /**
     * 修改订阅记录
     *
     * @param userId 用户ID
     * @param ownId 新币ID
     * @param orderId 申购订单ID
     * @return
     */
    int updateTOwnCoinSubscribeOrderById(
            @Param("userId") Long userId, @Param("ownId") Long ownId, @Param("orderId") String orderId);

    /**
     * 订阅详情查询
     *
     * @param id
     * @return
     */
    TOwnCoinSubscribeOrder selectTOwnCoinSubscribeOrderById(@Param("id") Long id);
}

