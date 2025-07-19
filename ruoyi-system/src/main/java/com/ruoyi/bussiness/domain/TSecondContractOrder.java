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
 * 秒合约订单对象 t_second_contract_order
 *
 * @author ruoyi
 * @date 2023-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_second_contract_order")
public class TSecondContractOrder extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户地址
     */
    private String userAddress;
    /**
     * 预测方向:1 涨   0 跌
     */
    private String betContent;
    /**
     * 开奖结果
     */
    private String openResult;
    /**
     * 订单状态 0参与中 1已开奖 2已撤销
     */
    private Integer status;
    /**
     * 投注金额
     */
    private BigDecimal betAmount;
    /**
     * 获奖金额
     */
    private BigDecimal rewardAmount;
    /**
     * 赔偿金额
     */
    private BigDecimal compensationAmount;
    /**
     * 开盘价格
     */
    private BigDecimal openPrice;
    /**
     * 关盘价格
     */
    private BigDecimal closePrice;
    /**
     * 开盘时间
     */
    private Long openTime;
    /**
     * 关盘时间
     */
    private Long closeTime;
    /**
     * 交易币符号
     */
    private String coinSymbol;
    /**
     * 结算币符号
     */
    private String baseSymbol;
    /**
     * 订单标记 0正常  1包赢  2包输
     */
    private Integer sign;
    /**
     * 是否人工干预 0是 1否
     */
    private Integer manualIntervention;
    /**
     * 赔率
     */
    private BigDecimal rate;
    /**
     * 赔率标识
     */
     private Boolean rateFlag;
    /**
     * 代理IDS
     */

    private String adminParentIds;


    //业务字段
    @TableField(exist = false)
    private Integer time;

    @TableField(exist = false)
    private Long periodId;
}
