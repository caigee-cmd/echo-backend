package com.ruoyi.common.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * k线周期
 *
 * @author clion
 */

@Getter
public enum CandlestickIntervalEnum {


    MIN1("ONE_MIN", "1min"),
    MIN5("FIVE_MIN", "5min"),
    MIN15("FIFTEEN_MIN", "15min"),
    MIN30("THIRTY_MIN", "30min"),
    MIN60("ONE_HOUR", "60min"),
    HOUR4("FOUR_HOUR  ", "4hour"),
    DAY1("ONE_DAY", "1day"),
    WEEK1("SEVEN_DAY", "1week"),
    YEAR1("ONE_YEAR", "1year");


    private String code;
    private String value;

    CandlestickIntervalEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValue(String code) {
        CandlestickIntervalEnum[] betResult = CandlestickIntervalEnum.values();
        CandlestickIntervalEnum result = Arrays.asList(betResult).stream()
                .filter(i -> i.getCode().equals(code))
                .findFirst().orElse(null);
        if (result == null) {
            return "1min";
        }
        return result.getValue();
    }


}
