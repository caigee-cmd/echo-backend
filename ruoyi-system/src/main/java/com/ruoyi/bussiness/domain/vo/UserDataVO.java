package com.ruoyi.bussiness.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author:michael 玩家数据统计
 * @createDate: 2023/8/11 14:09
 */

@Data
public class UserDataVO {

    //用户id
    private Long userId;

    private String userName;
    //总充值
    private BigDecimal totalRechargeAmount;
    //总提现
    private BigDecimal totalWithdrawAmount;
    //总赠送彩金
    private BigDecimal totalWingAmount;
    //总扣减彩金
    private BigDecimal totalSubBousAmount;
    //总下分
    private BigDecimal totalSubAmount;
    //总上分
    private BigDecimal totalAddAmount;
    //总归集金额
    private BigDecimal totalCollectAmount;
    //秒合约输赢金额
    private BigDecimal totalCurrencyAmount;
    //U本位输赢总金额
    private BigDecimal totalContractAmount;
    //理财总金额
    private BigDecimal totalMattersAmount;
    //
    private BigDecimal  betAmount;

    private BigDecimal  rewardAmount;

}
