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
 * 控线详情对象 t_bot_kline_model_info
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Data
@TableName("t_bot_kline_model_info")
public class TBotKlineModelInfo{

private static final long serialVersionUID=1L;

    /**
     * id
     */
   @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * t_bot_kline_model 的主键
     */
    private Long modelId;
    /**
     * 时间戳
     */
    private Long dateTime;
    /**
     * 开盘价
     */
    private BigDecimal open;
    /**
     * 封盘价
     */
    private BigDecimal close;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 最低价
     */
    private BigDecimal low;

    private String x;
    private String y;
}
