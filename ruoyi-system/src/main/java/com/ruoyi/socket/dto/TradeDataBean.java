package com.ruoyi.socket.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TradeDataBean {
    private BigDecimal amount;
    private Long ts;
    private Long id;
    private BigDecimal price;
    private String direction;
}
