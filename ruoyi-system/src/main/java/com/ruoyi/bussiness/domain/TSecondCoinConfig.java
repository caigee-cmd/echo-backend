package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 秒合约币种配置对象 t_second_coin_config
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_second_coin_config")
public class TSecondCoinConfig extends BaseEntity {

    private static final long serialVersionUID=1L;
    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 合约交易对
     */
    private String symbol;
    /**
     * 合约交易所
     */
    private String market;
    /**
     * 是否启用 2关闭 1启用
     */
    private Long status;
    /**
     * 是否展示 2不展示 1展示
     */
    private Long showFlag;


    /**
     * 币种
     */
    private String coin;
    /**
     * 结算币种
     */
    private String baseCoin;

    /**
     * 排序
     */
    private Long sort;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 图标
     */
    private String logo;
    /**
     * 展示交易对
     */
    private String showSymbol;
    /**
     * 1 外汇  2 虚拟币 3 黄精白银
     */
    private Integer type;


    /**
     * 周期复制币种ID
     */
    @TableField(exist = false)
    private Long periodId;


}
