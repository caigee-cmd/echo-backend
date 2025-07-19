package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

@Data
public class MarketUrlSetting {

    /**
     * 谷歌验证码
     */
    private Boolean url;

    /**
     * 运营端验证码
     */
    private Boolean adminCode;

    /**
     * H5验证码
     */
    private Boolean h5Code;
}
