package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeRebateSetting {

    /**
     * 返佣比例
     */
    private BigDecimal ratio;

    /**
     * 最大返佣限制
     */
    private BigDecimal rebateMaxAmount;
    /**
     * 开关
     */
    private Boolean isOpen;
}
