package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Map;

import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 止盈止损表对象 t_contract_loss
 *
 * @author ruoyi
 * @date 2023-07-25
 */
@Data
@TableName("t_contract_loss")
public class TContractLoss  {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 委托类型（0 限价 1 市价）
     */
    private Integer delegateType;
    /**
     * 状态  0  正常 1 删除  2 撤销
     */
    private Integer status;
    /**
     * 仓位ID
     */
    private Long positionId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 止盈触发价
     */
    private BigDecimal earnPrice;
    /**
     * 止损触发价
     */
    private BigDecimal losePrice;
    /**
     * 止盈委托价
     */
    private BigDecimal earnDelegatePrice;
    /**
     * 止损委托价
     */
    private BigDecimal loseDelegatePrice;
    /**
     * 止盈数量
     */
    private BigDecimal earnNumber;
    /**
     * 止损数量
     */
    private BigDecimal loseNumber;
    /**
     * 0 止盈    1止损
     */
    private Long lossType;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String symbol;

    private BigDecimal leverage;

    private Integer type;
    @TableField(exist=false)
    private Map<String,Object> params;

}
