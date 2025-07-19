package com.ruoyi.bussiness.domain.setting;


import lombok.Data;

/**
 * 挖矿实体
 */
@Data
public class MingSettlementSetting {

    /**
     *  结算类型   1   每日返息  2 到期一起返本金加利息
     */
    private Integer settlementType;
    /**
     *  结算日期 1-31
     */
    private Integer settlementDay;
}
