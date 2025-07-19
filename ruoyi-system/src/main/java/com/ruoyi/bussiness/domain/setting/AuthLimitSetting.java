package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuthLimitSetting {
    /**
     * 初级限额
     */
    private BigDecimal primaryLimit;
    /**
     * 初级限额 开启
     */
    private Boolean isOpenPrimary;
    /**
     * 高级限额
     */
    private BigDecimal seniorLimit;

    /**
     * 高级限额 开启
     */
    private Boolean isOpenSenior;
}
