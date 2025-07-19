package com.ruoyi.bussiness.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawFreezeVO {
    private String coin;
    private BigDecimal price;

}
