package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户详细信息对象 t_app_user_detail
 *
 * @author ruoyi
 * @date 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_app_user_detail")
public class TAppUserDetail extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private Long userId;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 身份证正面照片
     */
    private String frontUrl;
    /**
     * 国际
     */
    private String country;
    /**
     * 1 身份证  2 护照  3其他
     */
    private String cardType;
    /**
     * 手持身份证照片
     */
    private String handelUrl;
    /**
     * 身份证反面照片
     */
    private String backUrl;
    /**
     * 用户交易密码
     */
    private String userTardPwd;
    /**
     *
     */
    private String searchValue;
    /**
     * AuditStatusEnum
     */
    private String auditStatusPrimary;
    /**
     * AuditStatusEnum
     */
    private String auditStatusAdvanced;
    /**
     * 信用分
     */
    private Integer credits;
    /**
     * 用户充值地址
     */
    private String userRechargeAddress;

    /**
     * 连赢场次
     */
    private Integer winNum;
    /**
     * 连输场次
     */
    private Integer loseNum;
    /**
     * 交易是否被限制 1 为限制
     */
    private Integer tradeFlag;
    /**
     * 金额是否被限制 1 为限制
     */
    private Integer amountFlag;
    /**
     * 金额限制提示语
     */
    private String pushMessage;
    /**
     * 交易限制提示语
     */
    private String tradeMessage;
    /**
     * 实名认证时间
     */
    private Date operateTime;

    //业务字段
    @TableField(exist = false)
    private String flag;

    //业务字段
    @TableField(exist = false)
    private String adminParentIds;

    //业务字段  1 初级  2  高级
    @TableField(exist = false)
    private Integer reSetFlag;
}
