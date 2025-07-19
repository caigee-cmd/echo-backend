package com.ruoyi.bussiness.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author:michael 玩家数据统计
 * @createDate: 2023/8/11 14:09
 */

@Data
public class DailyDataVO {

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
    //总下分
    private BigDecimal totalAddAmount;
    //总归集金额
    private BigDecimal totalCollectAmount;
    //
    private BigDecimal  betAmount;
    private BigDecimal  rewardAmount;
    //U本位输赢总金额
    private BigDecimal totalContractAmount;
    //理财总金额
    private BigDecimal totalMattersAmount;


}
