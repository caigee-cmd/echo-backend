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
 * 【请填写功能名称】对象 t_mine_financial
 *
 * @author ruoyi
 * @date 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_mine_financial")
public class TMineFinancial extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 图标
     */
    private String icon;
    /**
     * 启用禁用(展示在前端)1开0关
     */
    private Long status;
    /**
     * 天数(如 7,10,30)
     */
    private String days;
    /**
     * 违约利率
     */
    private BigDecimal defaultOdds;
    /**
     * 最小日利率百分比
     */
    private BigDecimal minOdds;
    /**
     * 最大日利率百分比
     */
    private BigDecimal maxOdds;
    /**
     * 每人限购次数，0表示不限
     */
    private Long timeLimit;
    /**
     * 最小金额
     */
    private BigDecimal limitMin;
    /**
     * 最大金额
     */
    private BigDecimal limitMax;
    /**
     * 是否热销1是0否
     */
    private Long isHot;
    /**
     * 排序
     */
    private Long sort;
    /**
     *  购买次数
     */
    private Long buyPurchase;
    /**
     * 日平均利率
     */
    private BigDecimal avgRate;
    /**
     * 币种 
     */
    private String coin;
    /**
     * 分类（0 普通  1 vip  2 增值）
     */
    private String classify;
    /**
     * 平台基础投资金额
     */
    private BigDecimal basicInvestAmount;
    /**
     * 平台总投资额
     */
    private BigDecimal totalInvestAmount;
    /**
     * VIP等级 
     */
    private Long level;
    /**
     * 项目进度
     */
    private BigDecimal process;
    /**
     * 剩余金额
     */
    private BigDecimal remainAmount;
    /**
     * 已购金额
     */
    private BigDecimal purchasedAmount;
    /**
     * 常见问题
     */
    private String problem;
    /**
     * 产品介绍
     */
    private String prodectIntroduction;
}
