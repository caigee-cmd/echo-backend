package com.ruoyi.bussiness.domain.setting;


import lombok.Data;

/**
 * 理财结算实体
 */
@Data
public class FinancialSettlementSetting {

    /**
     *  结算类型  1 指定天数   2  每日  3 产品到期结算
     */
    private Integer settlementType;
    /**
     *  结算日期 1-31
     */
    private Integer settlementDay;
}
