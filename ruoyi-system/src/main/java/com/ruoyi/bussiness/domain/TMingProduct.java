package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Map;

import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * mingProduct对象 t_ming_product
 *
 * @author ruoyi
 * @date 2023-08-18
 */
@Data
@TableName("t_ming_product")
public class TMingProduct     {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 图标
     */
    private String icon;
    /**
     * 启用禁用(展示在前端)1开0关
     */
    private Long status;
    /**
     * 天数(如 7,10,30)
     */
    private String days;
    /**
     * 违约利率
     */
    private BigDecimal defaultOdds;
    /**
     * 最小日利率百分比
     */
    private BigDecimal minOdds;
    /**
     * 最大日利率百分比
     */
    private BigDecimal maxOdds;
    /**
     * 每人限购次数，0表示不限
     */
    private Long timeLimit;
    /**
     * 最小金额
     */
        private BigDecimal limitMin;
    /**
     * 最大金额
     */
    private BigDecimal limitMax;
    /**
     * 排序
     */
    private Long sort;
    /**
     *  购买次数
     */
    private Long buyPurchase;
    /**
     * 币种 
     */
    private String coin;

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @LastModifiedBy
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /** 备注 */
    private String remark;



}
