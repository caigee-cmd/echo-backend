package com.ruoyi.bussiness.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author:michael
 * @createDate: 2023/7/31 14:56
 */
@Data
public class TontractRequstDto {

    private String symbol;

    private BigDecimal leverage;

    private BigDecimal delegatePrice;

    private BigDecimal delegateTotal;

    private Integer type;

    private Integer delegateType;

}
