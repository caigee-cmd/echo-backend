package com.ruoyi.common.enums;

public enum RecoenOrderStatusEmun {
    audit("0","待审核"),
    pass("1","通过"),
    failed("2","不通过");

    RecoenOrderStatusEmun(String code, String value) {
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
