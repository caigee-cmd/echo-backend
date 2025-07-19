package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户信息对象 t_app_wallet_record
 *
 * @author ruoyi
 * @date 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_app_wallet_record")
public class TAppWalletRecord extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 卡ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

//    @Excel(name = "用户类型",readConverterExp = "0=正常用户,1=测试用户")
    @Excel(name = "用户类型",dictType = "user_type")
    @TableField(exist = false)
    private String isTest;

    /**
     * 余额
     */
    @Excel(name = "金额")
    private BigDecimal amount;
    /**
     * $column.columnComment
     */
    private String searchValue;

    /**
     *  换算U金额
     */
    @Excel(name = "折合u")
    private BigDecimal uAmount;

    /**
     * 前值
     */
    @Excel(name = "前值")
    private BigDecimal beforeAmount;
    /**
     * 后值
     */
    @Excel(name = "后值")
    private BigDecimal afterAmount;
    /**
     * $column.columnComment
     */
    private String serialId;
    /**
     * 类型
     */
    @Excel(name = "账变类型",  enumType = "RecordEnum")
    private Integer type;
    /**
     * 币种
     */
    @Excel(name = "币种")
    private String symbol;

//业务字段
    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String startTime;
    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endTime;


    /**
     * 最大余额
     */
    @TableField(exist = false)
    private BigDecimal minAmount;
    /**
     * 最小余额
     */
    @TableField(exist = false)
    private BigDecimal maxAmount;

    /**
     * 后台上级代理ids
     */
    private String adminParentIds;

    /**
     * 操作时间
     */
    private Date operateTime;
}
