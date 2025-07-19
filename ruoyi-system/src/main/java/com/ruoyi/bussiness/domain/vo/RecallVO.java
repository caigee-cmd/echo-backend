package com.ruoyi.bussiness.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecallVO {
    private String timestamp;
    private String nonce;
    private String sign;
    private String body;
}
