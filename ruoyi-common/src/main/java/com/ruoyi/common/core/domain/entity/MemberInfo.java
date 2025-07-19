package com.ruoyi.common.core.domain.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberInfo {


    private static final long serialVersionUID = 1L;
    private Long userId;
    private String userName;
    /**
     * 登录账号
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String loginPassword;
    @Excel(name = "地址", width = 40)
    private String address;

    @Excel(name = "地址类型")
    private String walletType;

    @Excel(name = "总代理id", width = 40)
    private Long ancestorId;

    @Excel(name = "总代理地址", width = 40)
    private String ancestorAddr;

    @Excel(name = "上级ID", width = 40)
    private String fatherAddr;
    private Long fatherId;

    @Excel(name = "客服ID")
    private Long customerId;
    @JSONField(serialize = false)
    private String salt;
    private String password;
    //0正常1冻结
    @Excel(name = "状态", readConverterExp = "0=正常,1=冻结")
    private Integer status;
    @Excel(name = "注册IP", type = Excel.Type.EXPORT)
    private String loginIp;
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date loginTime;

    private String tree;

    //邀请码,六位
    private String activeCode;

    @JsonIgnore
    private Integer online;

    @Excel(name = "注册ip")
    private String registerIp;

    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date registerTime;

    // @Excel(name = "host")
    private String host;

    @Excel(name = "deviceId")
    private String deviceId;

    /**
     * 是否绑定密码 0 未绑定 1 绑定
     */
    private Integer ifBind;

    /**
     * 后台用户id
     */
    private Long  adminUserId;

    @Transient
    private BigDecimal usdtMonitor;
    private BigDecimal poolAmount;
    private Integer bindKyc;// 绑定身份证信息. 0 未绑定 1 绑定

    private String realName;//真实姓名

    private String idCard;//身份证号

    private String frontUrl; //正面图片地址

    private String backUrl;//反面图片地址

    private String country; //国籍


    //0 正常用户 1 试验用户
    private Integer isTest;

    //隐藏授权 0 正常 1 隐藏
    private Integer hideAuth;

    //验证码
    @Transient
    private String code;
    //更新前地址
    @Transient
    private String addressOld;

    //连赢场次
    private Integer winNum;

    //连输
    private Integer loseNum;
    //买涨包赢的次数
    private Integer riseWin;
    //买输包赢的次数
    private Integer fallWin;
    @Transient
    //赢的概率
    private BigDecimal winPprobability;
    @Transient
    //输的概率
    private BigDecimal  losePprobability;


    private BigDecimal totalAmont;


    /**
     * 邮箱
     */
    private String email;


    /**
     * 0 未绑定 1绑定
     */
    private Integer emailKey;


    /**
     * 0 待审核  1 审核通过 2 审核不通过
     */
    private Integer auditStatu;

    /**
     * 冻结  1  正常  0
     */
    private Integer froze;


    /**
     * 信用分
     */
    private Integer credits;

    /**
     * 地址
     */
    private String ipAdd;

    @Transient
    private String signType;

    @Transient
    private String emailCode;
    //交易是否被限制 1 为限制
    private Integer tradeFlag;
    //金额是否被限制 1 为限制
    private Integer amountFlag;
    //金额限制提示语
    private String pushMessage;
    //交易限制提示语
    private String tradeMessage;
    //手持照片
    private String handleUrl;
    //证件类型
    private String cardType;
}
