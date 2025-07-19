package com.ruoyi.common.enums;

/**
 * 黑名单
 */
public enum UserBlackEnum {


    /**
     * 正常用户
     */
    NORMAL(1,"正常用户"),

    /**
     * 拉黑用户
     */
    BLOCK(2,"拉黑用户"),


    ;
    private Integer code;

    private String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    UserBlackEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }
}
