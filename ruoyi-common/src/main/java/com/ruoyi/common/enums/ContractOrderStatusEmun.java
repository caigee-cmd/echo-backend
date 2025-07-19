package com.ruoyi.common.enums;

public enum ContractOrderStatusEmun {
    DEAL("0","待成交"),
    ALLDEAl("1","完全成交"),
    CANCAL("2","撤销");

    ContractOrderStatusEmun(String code, String value) {
        this.code = code;
        this.value = value;
    }
    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
