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
 * 自发币种配置对象 t_spontaneous_coin
 *
 * @author ruoyi
 * @date 2023-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_spontaneous_coin")
public class TSpontaneousCoin extends BaseEntity {

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

}
