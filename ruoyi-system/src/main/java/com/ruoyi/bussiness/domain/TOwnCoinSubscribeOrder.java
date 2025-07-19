package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订阅订单对象 t_own_coin_subscribe_order
 *
 * @Author ruoyi
 * @Date 2023/10/9
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_own_coin_subscribe_order")
public class TOwnCoinSubscribeOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户
     */
    private Long userId;
    /**
     * 订阅ID
     */
    private String subscribeId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 申购币种ID
     */
    private Long ownId;
    /**
     * 申购币种
     */
    private String ownCoin;
    /**
     * 申购额（usdt）
     */
    private BigDecimal amountLimit;
    /**
     * 申购数量
     */
    private Long numLimit;
    /**
     * 申购单价
     */
    private BigDecimal price;
    /**
     * 状态，1订阅中、2订阅成功、3成功消息推送完成、4被拒绝
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
