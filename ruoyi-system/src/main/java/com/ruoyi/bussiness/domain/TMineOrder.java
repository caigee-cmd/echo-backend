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
 * 【请填写功能名称】对象 t_mine_order
 *
 * @author ruoyi
 * @date 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_mine_order")
public class TMineOrder extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 地址
     */
    private String adress;
    /**
     * 投资金额（分）
     */
    private BigDecimal amount;
    /**
     * 投资期限（天）
     */
    private Long days;
    /**
     * 0 收益  1 结算  2 赎回
     */
    private Long status;
    /**
     * 项目id
     */
    private Long planId;
    /**
     * 项目名称
     */
    private String planTitle;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 到期时间
     */
    private Date endTime;
    /**
     * 结算时间
     */
    private Date settleTime;
    /**
     * 累计收益
     */
    private BigDecimal accumulaEarn;
    /**
     * 最小利率
     */
    private BigDecimal minOdds;
    /**
     * 最大利率
     */
    private BigDecimal maxOdds;
    /**
     * 违约利率
     */
    private BigDecimal defaultOdds;
    /**
     * 后台用户id
     */
    private String adminUserIds;
    /**
     * 币种
     */
    private String coin;
    /**
     * 币种
     */
    private BigDecimal avgRate;
    /**
     * 0 质押挖矿 1 非质押挖矿
     */
    private Long type;
    /**
     * $column.columnComment
     */
    private String collectionOrder;
    /**
     * $column.columnComment
     */
    private Long userId;
    /**
     * $column.columnComment
     */
    private BigDecimal orderAmount;

    //业务字段
    /**
     *  结算类型  1 指定天数   2  每日  3 产品到期结算
     */
    @TableField(exist = false)
    private Integer settlementType;
    /**
     *  结算日期 1-31
     */
    @TableField(exist = false)
    private Integer settlementDay;
}
