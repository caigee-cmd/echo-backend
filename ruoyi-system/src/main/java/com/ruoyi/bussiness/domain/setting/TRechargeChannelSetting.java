package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现通道配置
 */
@Data
public class TRechargeChannelSetting {

    /**
     *  0-关闭 1-开启
     */
    private String status;
    /**
     * 展示名称
     */
    private String rechargeName;
    /**
     * 提现币种
     */
    private String rechargeType;
    /**
     * 0 为数据货币 1为银行卡
     */
    private String type;
    /**
     *固定手续费
     */

    private BigDecimal fee ;

    /**
     * 手续费
     */
    private BigDecimal ratio;
    /**
     * 每日提现次数限制
     */
    private Integer dayWithdrawalNum;
    /**
     * 系统免费提现次数
     */
    private Integer freeNum;
    /**
     * 最大限制
     */
    private BigDecimal withdrawalMax;
    /**
     * 最小限制
     */
    private BigDecimal withdrawalMix;
}
