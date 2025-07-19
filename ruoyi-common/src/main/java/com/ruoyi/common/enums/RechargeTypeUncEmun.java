package com.ruoyi.common.enums;

import java.util.Arrays;

public enum RechargeTypeUncEmun {
    USDTERC("USDT-ERC","60"),
    USDTTRC("USDT-TRC","195"),
    ETH("ETH","60"),
    BTC("BTC","0"),
    TRX("TRX","195"),
    USDCERC("USDC-ERC","60"),
    USDC("USDC","60");

    RechargeTypeUncEmun(String code, String value) {
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
        RechargeTypeUncEmun[] betResult= RechargeTypeUncEmun.values();
        RechargeTypeUncEmun result = Arrays.asList(betResult).stream()
                .filter(i -> i.getCode().equals(code))
                .findFirst().orElse(null);
        if(result==null){
            return null;
        }
        return result.getValue();
    }

}
