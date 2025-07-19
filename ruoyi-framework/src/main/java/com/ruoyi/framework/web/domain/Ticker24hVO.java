package com.ruoyi.framework.web.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Ticker24hVO {

    private  String  symbol ;
    private  BigDecimal highPrice ;
    private  BigDecimal  lowPrice ;
    private  BigDecimal  volume ;

}

