package com.ruoyi.common.enums;

public enum LoginOrRegisterEnum {

    /**
     * 地址登录注册
     */
    ADDRESS("0","地址登录"),

    /**
     * 邮箱登录注册
     */
    EMAIL("1","邮箱登录"),

    /**
     * 手机号登录注册
     */
    PHONE("2","手机号登录"),

    /**
     * 普通登录注册
     */
    LOGIN("3","普通登录"),


 ;
    private String code;

    private String desc;

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return desc;
    }

    LoginOrRegisterEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
