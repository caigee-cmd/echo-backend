package com.ruoyi.socket.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WsCoinSubVO {

    private Long userId;
    private String subscribeId;
    private Long ownId;
    private String ownCoin;
    private BigDecimal amountLimit;
    private Long numLimit;
    private BigDecimal price;
    private String status;
    private String msg;
}
