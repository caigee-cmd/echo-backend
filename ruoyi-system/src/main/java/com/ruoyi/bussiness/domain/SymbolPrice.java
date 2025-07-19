package com.ruoyi.bussiness.domain;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class SymbolPrice {
    private String symbol;
    private BigDecimal price;

}