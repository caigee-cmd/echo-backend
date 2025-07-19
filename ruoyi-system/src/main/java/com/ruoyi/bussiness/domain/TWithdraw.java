package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户提现对象 t_withdraw
 *
 * @author ruoyi
 * @date 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_withdraw")
public class TWithdraw extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 卡ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 用户
     */
    @Excel(name = "用户Id")
    private Long userId;
    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String username;

    @Excel(name = "用户类型",dictType = "user_type")
    @TableField(exist = false)
    private Integer isTest;
    /**
     * 提现地址
     */
    @Excel(name = "提现地址")
    private String address;
    /**
     * 提现金额
     */
    @Excel(name = "提现金额")
    private BigDecimal amount;

    @Excel(name = "折合u")
    @TableField(exist = false)
    private BigDecimal uamount;
    /**
     * 0审核中1成功2失败3 锁定
     */
    @Excel(name = "折合u",dictType = "withdraw_order_status")
    private Integer status;

    /**
     * 提现币种类型
     */
    @Excel(name = "提现类型")
    private String type;

    /**
     * 订单类型 1/null 提现  2=彩金扣减
     */
    @Excel(name = "订单类型",readConverterExp = "null=提现,''=提现,1=提现,2=彩金\"")
    private String orderType;


    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 用户名
     */
    private String fromAddr;
    /**
     * 用户名
     */
    @Excel(name = "币种")
    private String coin;
    /**
     * 手续费
     */
    @Excel(name = "手续费")
    private BigDecimal ratio;

    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String serialId;

    /**
     * 固定手续费
     */
    private BigDecimal fixedFee;
    /**
     * 手续费
     */
    private BigDecimal fee;



    /**
     * 用户名
     */
    private String withdrawId;
    /**
     * Host
     */
    private String host;
    /**
     * 实际金额
     */
    @Excel(name = "实际金额")
    private BigDecimal realAmount;
    /**
     * 收款地址
     */
    private String toAdress;
    /**
     * 通知字段 0未通知 1通知了
     */
    private Integer noticeFlag;
    /**
     * 提现说明
     */
    @Excel(name = "提现说明")
    private String withDrawRemark;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行收款人名称
     */
    private String bankUserName;
    /**
     * $column.columnComment
     */
    private String bankBranch;
    /**
     * 代理ID
     */
    private String adminParentIds;
    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 应到账金额
     */
    private BigDecimal receiptAmount;

    /**
     * 实际到账金额
     */
    private BigDecimal receiptRealAmount;
    /**
     * 到账币种
     */
    private String receiptCoin;

    //业务字段
    @TableField(exist = false)
    private BigDecimal maxAmount;
    @TableField(exist = false)
    private BigDecimal minAmount;


}
