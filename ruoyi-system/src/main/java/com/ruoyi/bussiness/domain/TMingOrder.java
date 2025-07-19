package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * ming对象 t_ming_order
 *
 * @author ruoyi
 * @date 2023-08-18
 */
@Data
@TableName("t_ming_order")
public class TMingOrder  {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 投资金额（分）
     */
    private BigDecimal amount;
    /**
     * 投资期限（天）
     */
    private Integer days;
    /**
     * 0 收益  1 结算 3 赎回
     */
    private Integer status;
    /**
     * 项目id
     */
    private Long planId;
    /**
     * 项目名称
     */
    private String planTitle;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 到期时间
     */
    private Date endTime;
    /**
     * 结算时间
     */
    private Date settleTime;
    /**
     * 累计收益
     */
    private BigDecimal accumulaEarn;
    /**
     * 最小利率
     */
    private BigDecimal minOdds;
    /**
     * 最大利率
     */
    private BigDecimal maxOdds;
    /**
     * 后台用户id
     */
    private String adminUserIds;
    /**
     * $column.columnComment
     */
    private String collectionOrder;
    /**
     * $column.columnComment
     */
    private Long userId;
    /**
     * $column.columnComment
     */
    private BigDecimal orderAmount;

    @TableField(exist = false)
    private Integer settlementType;
    /**
     *  结算日期 1-31
     */
    @TableField(exist = false)
    private Integer settlementDay;


    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @TableField(exist=false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;
}
