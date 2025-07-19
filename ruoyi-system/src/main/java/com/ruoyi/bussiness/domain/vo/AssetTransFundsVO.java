package com.ruoyi.bussiness.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetTransFundsVO {
    /**
     *   币种
     */
    private String coin;
    /**
     * 转出账户
     * AssetEnum
     */
    private Integer transferOutAccount;
    /**
     * 转入账户
     * AssetEnum
     */
    private Integer transferInAccount;
    /**
     * 金额
     */
    private BigDecimal amount;
}
