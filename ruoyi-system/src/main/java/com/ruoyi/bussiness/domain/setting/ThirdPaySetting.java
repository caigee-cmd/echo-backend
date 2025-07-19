package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

@Data
public class ThirdPaySetting {

    //
    private String name;
    //0下线1上线
    private Integer status;
    // 地址
    private String url;

    private String key;

    //三方充值通道  0 开启  1关闭
    private String thirdPayStatu;


    //三方提现通道  0 开启  1关闭

    private String thirdWithStatu;


    //方法名
    private String companyName;
    /**
     * 编码
     */
    private String code;

    private String mechId;

    private String returnUrl;



}
