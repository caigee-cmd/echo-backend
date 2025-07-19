package com.ruoyi.bussiness.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发币对象 t_own_coin
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_own_coin")
public class TOwnCoinVO extends BaseEntity {

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

    /**
     * 申购数量
     */
    private Long numLimit;

    private Long beginTimes;
    private Long endTimes;
}
