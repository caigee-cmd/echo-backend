package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * 冲提语音包配置
 */
@Data
public class VoiceSetting {
    /**
     * 提现语音包路径
     */
    private String withdrawalVoiceUrl;
    /**
     * 充值语音包路径
     */
    private String  rechargeVoiceUrl;

    /**
     * 实名认证
     */
    private String  verifiedVoiceUrl;
}
