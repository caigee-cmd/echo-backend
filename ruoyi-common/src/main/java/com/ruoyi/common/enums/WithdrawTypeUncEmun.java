package com.ruoyi.common.enums;

import java.util.Arrays;

public enum WithdrawTypeUncEmun {
    USDTERC("USDT-ERC","USDT-ERC20"),
    USDTTRC("USDT-TRC","USDT-TRC20"),
    ETH("ETH","ETH"),
    BTC("BTC","BTC");

    WithdrawTypeUncEmun(String code, String value) {
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
        WithdrawTypeUncEmun[] betResult= WithdrawTypeUncEmun.values();
        WithdrawTypeUncEmun result = Arrays.asList(betResult).stream()
                .filter(i -> i.getCode().equals(code))
                .findFirst().orElse(null);
        if(result==null){
            return null;
        }
        return result.getValue();
    }

}
