package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoadSetting {
    /**
     * 贷款逾期利率
     */
    private BigDecimal overdueRate;
}
