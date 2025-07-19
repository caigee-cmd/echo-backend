package com.ruoyi.common.enums;

public enum WalletType {
    ETH("以太坊"),
    TRON("波场");

    private final String desc;

    WalletType(String desc) {
        this.desc = desc;
    }

    public String getDesc()
    {
        return desc;
    }

}