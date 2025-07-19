package com.ruoyi.common.enums;

import java.util.Arrays;

/**
 * 三方充值
 */
public enum ThirdTypeUncEmun {
    UNCDUN("U盾","301");

    ThirdTypeUncEmun(String code, String value) {
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

    public static String getValue(String code) {
        ThirdTypeUncEmun[] betResult= ThirdTypeUncEmun.values();
        ThirdTypeUncEmun result = Arrays.asList(betResult).stream()
                .filter(i -> i.getCode().equals(code))
                .findFirst().orElse(null);
        if(result==null){
            return null;
        }
        return result.getValue();
    }

}
