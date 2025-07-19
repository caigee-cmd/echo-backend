package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 控线配置对象 t_bot_kline_model
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_bot_kline_model")
public class TBotKlineModel extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 最大跌幅
     */
    private Long decline;
    /**
     * 控制粒度
     */
    private Long granularity;
    /**
     * 最大涨幅
     */
    private Long increase;
    /**
     * 控盘策略  0 跟随性  2是画线
     */
    private Long model;
    /**
     * 浮动比例
     */
    private Long pricePencent;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 值
     */
    private String searchValue;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    private String lineChartData;
    /**
     * 控制价格
     */
    private BigDecimal conPrice;
}
