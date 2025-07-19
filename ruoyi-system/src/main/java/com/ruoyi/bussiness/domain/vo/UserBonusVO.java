package com.ruoyi.bussiness.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * 赠送彩金 人工下分 VO类
 *
 * @author ruoyi
 * @date 2023-07-10
 */
@Data
public class UserBonusVO {
    //用户id
    private Long userId;
    //金额
    private BigDecimal amount;
    //资产币种
    private String symbol;

    private String type;
    //0 上分  1 下分
    private String giveType;
    private String createBy;
    //备注
    private String remark;
}
