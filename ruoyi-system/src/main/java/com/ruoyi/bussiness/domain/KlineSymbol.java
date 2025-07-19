package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.experimental.Accessors;

/**
 * 数据源对象 t_kline_symbol
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_kline_symbol")
@Accessors(chain = true)
public class KlineSymbol extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 交易所
     */
    private String market;
    /**
     * 币种简称
     */
    private String symbol;
    /**
     * 币种名称
     */
    private String slug;
    /**
     * 是否开启
     */
    private Long status;
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
     * 价格百分比
     */
    private BigDecimal proportion;




}
