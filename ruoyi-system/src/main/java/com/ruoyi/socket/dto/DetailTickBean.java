package com.ruoyi.socket.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DetailTickBean {
    private Long id;
    private Long mrid;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal amount;
    private BigDecimal vol;
    private BigDecimal count;
}
