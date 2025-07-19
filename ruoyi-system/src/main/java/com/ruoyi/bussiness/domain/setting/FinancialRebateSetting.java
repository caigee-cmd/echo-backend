package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialRebateSetting {

    /**
     * 开关
     */
    private Boolean isOpen;
    /**
     * 一级返佣比例
     */
    private BigDecimal oneRatio;
    /**
     * 二级返佣比例
     */
    private BigDecimal twoRatio;
    /**
     * 三级返佣比例
     */
    private BigDecimal threeRatio;
}
