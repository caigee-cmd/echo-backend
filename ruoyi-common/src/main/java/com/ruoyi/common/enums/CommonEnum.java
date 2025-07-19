package com.ruoyi.common.enums;

public enum CommonEnum {

    TRUE(1,true),
    FALSE(0,false),
    ZERO(0,false),
    ONE(1,false),
    TWO(2,false),
    THREE(3,false),
    ;

    private final Integer code;
    private final Boolean desc;

    CommonEnum(Integer code, Boolean info)
    {
        this.code = code;
        this.desc = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public Boolean getInfo()
    {
        return desc;
    }
}
