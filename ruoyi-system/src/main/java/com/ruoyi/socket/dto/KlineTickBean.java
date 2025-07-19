package com.ruoyi.socket.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class KlineTickBean {
    private Long id;
    private BigDecimal mrid;
    private BigDecimal vol;
    private BigDecimal count;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal amount;
    private Boolean intervention;
    private BigDecimal conPrice;
    private Long conTime;
}
