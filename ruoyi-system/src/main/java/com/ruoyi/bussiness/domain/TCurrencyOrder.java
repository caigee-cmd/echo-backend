package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 币币交易订单对象 t_currency_order
 *
 * @author ruoyi
 * @date 2023-07-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_currency_order")
public class TCurrencyOrder extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * (0 买入 1卖出)
     */
    private Integer type;
    /**
     * 委托类型（0 限价 1 市价 2 止盈止损  3 计划委托）
     */
    private Integer delegateType;
    /**
     * 状态  0 （等待成交  1 完全成交  3已撤销）
     */
    private Integer status;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 交易币种
     */
    private String symbol;
    /**
     * 结算币种
     */
    private String coin;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 委托总量
     */
    private BigDecimal delegateTotal;
    /**
     * 委托价格
     */
    private BigDecimal delegatePrice;
    /**
     * 已成交量
     */
    private BigDecimal dealNum;
    /**
     * 成交价
     */
    private BigDecimal dealPrice;
    /**
     * 委托价值
     */
    private BigDecimal delegateValue;
    /**
     * 成交价值
     */
    private BigDecimal dealValue;
    /**
     * 委托时间
     */
    private Date delegateTime;
    /**
     * 成交时间
     */
    private Date dealTime;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * $column.columnComment
     */
    private String searchValue;

    /**
     * 后台代理id
     */
    private String adminParentIds;



}
