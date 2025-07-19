package com.ruoyi.common.enums;

public enum ClassifyEnum {

    //（0 普通  1 vip  2 增值）

    ORDINARY("0","普通"),
    VIP("1","vip"),
    ADDED_VALUE("2","增值"),
    ;
    private final String code;
    private final String info;

    ClassifyEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

}
