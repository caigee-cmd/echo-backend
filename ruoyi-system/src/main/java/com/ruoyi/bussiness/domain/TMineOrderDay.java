package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 t_mine_order_day
 *
 * @author ruoyi
 * @date 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_mine_order_day")
public class TMineOrderDay extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableId(value = "id",type = IdType.AUTO)
    private  Long id;

            /**
     * 投资金额（分）
     */
    private BigDecimal amount;
    /**
     * 当日利率
     */
    private BigDecimal odds;
    /**
     * 收益
     */
    private BigDecimal earn;
    /**
     * 项目id
     */
    private Long planId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 地址
     */
    private String address;
    /**
     * 0 质押挖矿 1 非质押挖矿
     */
    private Long type;
    /**
     * 1 待结算  2  结算
     */
    private Integer status;

}
