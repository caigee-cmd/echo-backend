package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 币种兑换记录对象 t_exchange_coin_record
 *
 * @author ruoyi
 * @date 2023-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_exchange_coin_record")
public class TExchangeCoinRecord extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * $column.columnComment
     */
    private String fromCoin;
    /**
     * $column.columnComment
     */
    private String toCoin;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 
用户名称
     */
    private String username;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 兑换状态0:已提交;1:成功;2失败
     */
    private Integer status;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 三方汇率
     */
    private BigDecimal thirdRate;
    /**
     * 系统汇率
     */
    private BigDecimal systemRate;
    /**
     * $column.columnComment
     */
    private String searchValue;

    /**
     * 后台代理id
     */
    private String adminParentIds;


    public String getExchangeType(){
        return this.fromCoin+this.toCoin;
    }

    @JsonIgnore
    public String getFromToRemark() {
        return String.format("%s转%s,三方汇率:%s,系统汇率:%s", fromCoin, toCoin, thirdRate, systemRate);
    }
}
