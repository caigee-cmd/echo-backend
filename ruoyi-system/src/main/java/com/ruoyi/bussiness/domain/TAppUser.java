package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 玩家用户对象 t_app_user
 *
 * @author ruoyi
 * @date 2023-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_app_user")
public class TAppUser extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "user_id",type = IdType.AUTO)
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 0-正常 1-测试
     */
    @Excel(name = "用户id",dictType = "user_type")
    private Integer isTest;

    @TableField(exist=false)
    private String code;
    /**
     * 姓名
     */
    @Excel(name = "登录名")
    private String loginName;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    private String email;

    /**
     * 登陆密码
     */
    private String loginPassword;
    /**
     * 地址
     */
    @Excel(name = "地址")
    private String address;
    /**
     * 地址类型 ETH TRC
     */
    @Excel(name = "地址类型")
    private String walletType;
    /**
     * 0正常1冻结
     */
    @Excel(name = "是否冻结",dictType = "user_status")
    private Integer status;
    /**
     * 总打码量
     */
    private BigDecimal totleAmont;

    /**
     * 总充值打码量
     */
    private BigDecimal rechargeAmont;

    /**
     * 0正常 1包赢 2包输
     */
    private Integer buff;
    /**
     * app代理ids
     */
    @Excel(name = "玩家代理")
    private String appParentIds;
    @Excel(name = "玩家代理用户名")
    @TableField(exist = false)
    private String appParentNames;
    /**
     * 后台代理ids
     */
    @Excel(name = "后台代理")
    private String adminParentIds;
    @TableField(exist = false)
    @Excel(name = "后台代理用户名")
    private String adminParentNames;
    /**
     * 邀请码
     */
    private String activeCode;
    /**
     * 注册ip
     */
    @Excel(name = "注册ip")
    private String registerIp;
    /**
     * 注册域名
     */
    @Excel(name = "注册域名")
    private String host;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    private String phone;
    /**
     * vip等级 
     */
    private Integer level;
    /**
     * $column.columnComment
     */
    private String searchValue;

    /**
     *是否冻结  1=正常 2=冻结
     */
    private String isFreeze;

    /**
     *黑名单 1=正常 2拉黑
     */
    private Integer isBlack;

    /**
     *限制 1=正常 2限制
     */
    private Integer isLimitation;

    @TableField(exist=false)
    private String signType;

    @TableField(exist=false)
    private String flag;

    @TableField(exist=false)
    private Integer productId;

    @TableField(exist=false)
    private Integer winNum;

    @TableField(exist=false)
    private Integer loseNum;
    @TableField(exist=false)
    private Integer credits;

}
