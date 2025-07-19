package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 申购订单对象 t_own_coin_order
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_own_coin_order")
public class TOwnCoinOrder extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 用户
     */
    private Long userId;
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
    private BigDecimal amount;
    /**
     * 申购数量
     */
    private Long number;
    /**
     * 申购价
     */
    private BigDecimal price;
    /**
     * 状态
     */
    private String status;
    /**
     * 上级用户IDS
     */
    private String adminUserIds;
    /**
     * 上级后台用户IDS
     */
    private String adminParentIds;

}
