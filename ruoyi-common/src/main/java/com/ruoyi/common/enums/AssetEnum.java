package com.ruoyi.common.enums;

public enum AssetEnum {



    /**
     * 平台资产
     */
    PLATFORM_ASSETS(1,"平台资产"),

    /**
     * 理财资产
     */
    FINANCIAL_ASSETS(2,"理财资产"),
    /**
     * 合约资产
     */
    CONTRACT_ASSETS(3,"合约账户"),




    ;
    private Integer code;

    private String desc;

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return desc;
    }

    AssetEnum(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
