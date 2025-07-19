package com.ruoyi.bussiness.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.bussiness.domain.TBotKlineModelInfo;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 控线配置对象 t_bot_kline_model
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Data
public class TBotKlineModelVO extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */

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
     * 控盘策略
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

    private BigDecimal conPrice;
    /**
     * 结束时间
     */
    private Date endTime;
    private String lineChartData;
    private List<TBotKlineModelInfo> botInfoList;
}
