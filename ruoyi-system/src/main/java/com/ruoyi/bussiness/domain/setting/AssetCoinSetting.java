package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值通道配置
 */
@Data
public class AssetCoinSetting {
    /**
     * 币种名称
     */
    private String coinName;
    /**
     * 币种类型
     */
    private String coin;
    /**
     * U-ERC充值地址
     */
    private String coinAddress;
    /**
     * 充值次数
     */
    private Integer rechargeNum;
    /**
     * 充值最大额度
     */
    private BigDecimal rechargeMax;
    /**
     * 充值最小额度
     */
    private BigDecimal rechargeMin;
}
