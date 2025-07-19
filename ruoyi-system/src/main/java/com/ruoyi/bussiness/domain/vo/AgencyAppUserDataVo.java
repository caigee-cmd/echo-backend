package com.ruoyi.bussiness.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 代理下级用户统计Vo
 */
@Data
public class AgencyAppUserDataVo {

    private Long appUserId; //玩家用户id
    private BigDecimal  recharge; //充值总额
    private BigDecimal withdraw;    //体现总额
    private BigDecimal sendBonus; //赠送彩金总额
    private BigDecimal subBonus; //扣减彩金总额

    private BigDecimal btcManualScoring; // BTC人工上分+
    private BigDecimal ethManualScoring; // ETH人工上分+

    private BigDecimal subAmount; //人工下分-
    private BigDecimal sendAmount; //人工上分+
    private BigDecimal btcManualSubdivision; //BTC人工下分-
    private BigDecimal ethManualSubdivision; //ETH人工下分-

    private BigDecimal sumManualScoring; //人工下分+总额
    private BigDecimal sumManualSubdivision; //人工下分-总额

    private BigDecimal collectionAmount; //下级总归集金额

}
