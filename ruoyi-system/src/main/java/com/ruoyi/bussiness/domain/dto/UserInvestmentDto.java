package com.ruoyi.bussiness.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:michael
 * @createDate: 2022/8/15 13:43
 */
@Data
public class UserInvestmentDto {
    //地址
    private String address;
    //展示收益字段
    private BigDecimal num;
    //usdt金额
    private BigDecimal amount;
    //每日收益
    private BigDecimal dayRate;
    //单次收益
    private BigDecimal singleRate;
    //总收益
    private BigDecimal totalProfit;

}
