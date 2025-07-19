package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户充值对象 t_app_recharge
 *
 * @author ruoyi
 * @date 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_app_recharge")
public class TAppRecharge extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 卡ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 所有者ID
     */
    @Excel(name = "用户id")
    private Long userId;
    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String username;
    /**
     * 充值金额
     */
    @Excel(name = "充值金额")
    private BigDecimal amount;
    //业务字段
    @Excel(name = "折合u")
    @TableField(exist = false)
    private BigDecimal uamount;
    /**
     * $column.columnComment
     */
    private Long bonus;
    /**
     * 状态
     */
    @Excel(name = "状态",dictType = "recharge_order_status")
    private String status;

    @Excel(name = "用户类型",dictType = "user_type")
    @TableField(exist = false)
    private Integer isTest;
    /**
     * 单号
     */
    @Excel(name = "订单号")
    private String serialId;

    /**
     * 订单类型 1/null=充值  2=彩金
     */
    @Excel(name = "订单类型",readConverterExp = "null=充值,''=充值,1=充值,2=彩金")
    private String orderType;
    /**
     * 第三方支付订单号
     */
    private String txId;
    /**
     * 类型
     */
    @Excel(name = "充值类型")
    private String type;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 充值地址
     */
    @Excel(name = "充值地址")
    private String address;
    /**
     * app代理ids
     */
    private String appParentIds;
    /**
     * 后台代理ids
     */
    private String adminParentIds;
    /**
     * 币总
     */
    @Excel(name = "币种")
    private String coin;
    /**
     * 入款地址
     */
    private String toAddress;
    /**
     * 区块时间
     */
    private Date blockTime;
    /**
     * $column.columnComment
     */
    private String host;
    /**
     * 实际到账金额
     */
    @Excel(name = "实际到账金额")
    private BigDecimal realAmount;
    /**
     * $column.columnComment
     */
    private String rechargeRemark;
    /**
     * 通知字段 0未通知 1通知了
     */
    private Integer noticeFlag;
    /**
     * 操作时间
     */
    @Excel(name = "操作时间")
    private Date operateTime;
    /**
     * 充值凭证
     */
    @Excel(name = "图片")
    private String fileName;

}
