package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Objects;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.experimental.Accessors;

/**
 * 玩家资产对象 t_app_asset
 *
 * @author ruoyi
 * @date 2023-06-30
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_app_asset")
public class TAppAsset extends BaseEntity {

private static final long serialVersionUID=1L;
    /**
     * $column.columnComment
     */
    private Long userId;
    /**
     * 地址
     */
    private String adress;
    /**
     * 币种
     */
    private String symbol;
    /**
     * 资产总额
     */
    private BigDecimal amout;
    /**
     * 占用资产
     */
    private BigDecimal occupiedAmount;
    /**
     * 可用资产
     */
    private BigDecimal availableAmount;
    /**
     * 钱包类型
     */
    private Integer type;

    @TableField(exist=false)
    private BigDecimal exchageAmount; //币种折合成usdt
    @TableField(exist=false)
    private String adminParentIds; //币种折合成usdt
    @TableField(exist=false)
    private String loge; //图标

    public Comparable<BigDecimal> getBtcDefaultIfNull(BigDecimal defaultValue) {
        return Objects.isNull(this.availableAmount)?defaultValue:this.availableAmount;
    }
}
