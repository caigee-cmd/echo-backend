package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 银行卡对象 t_user_bank
 *
 * @author ruoyi
 * @date 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_bank")
public class TUserBank extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 银行卡号
     */
    private String cardNumber;
    /**
     * 开户银行名称
     */
    private String bankName;
    /**
     * 开户省市
     */
    private String bankAddress;
    /**
     * 开户网点
     */
    private String bankBranch;
    /**
     * 用户名称
     */
    private Long userId;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 银行编码
     */
    private String bankCode;
    /**
     * 用户地址
     */
    private String userAddress;

    private String adminParentIds;

//    /**
//     * 币种
//     */
//    private String coin;

}
