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
 * 发币对象 t_own_coin
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_own_coin")
public class TOwnCoin extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 币种
     */
    private String coin;
    /**
     * 图标
     */
    private String logo;
    /**
     * 参考币种
     */
    private String referCoin;
    /**
     * 参考币种交易所
     */
    private String referMarket;
    /**
     * 展示名称
     */
    private String showSymbol;
    /**
     * 初始价格（单位USDT）
     */
    private BigDecimal price;
    /**
     * 价格百分比
     */
    private BigDecimal proportion;
    /**
     * 总发行量
     */
    private BigDecimal totalAmount;
    /**
     * 私募发行量
     */
    private BigDecimal raisingAmount;
    /**
     * 已筹集额度
     */
    private BigDecimal raisedAmount;
    /**
     * 预购上限
     */
    private Integer purchaseLimit;
    /**
     * 参与人数
     */
    private Long participantsNum;
    /**
     * 筹集期限
     */
    private Long raisingTime;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 1.筹备中  2.进行中 3 筹集成功 4.筹集失败
     */
    private Integer status;
    /**
     * 介绍
     */
    private String introduce;


}
