package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 贷款订单对象 t_load_order
 *
 * @author ruoyi
 * @date 2023-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_load_order")
public class TLoadOrder extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 贷款商品表id
     */
    private Long proId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 贷款金额
     */
    private BigDecimal amount;
    /**
     * 贷款利率
     */
    private BigDecimal rate;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 0=待审核 1=审核通过  2=审核拒绝  3=已结清  4=已逾期
     */
    private Integer status;
    /**
     * 最后还款日
     */
    private Date finalRepayTime;
    /**
     * 放款日期
     */
    private Date disburseTime;
    /**
     * 还款日期
     */
    private Date returnTime;
    /**
     * 审批金额
     */
    private BigDecimal disburseAmount;
    /**
     * 后台代理ids
     */
    private String adminParentIds;
    /**
     * 手持身份证
     */
    private String cardUrl;
    /**
     * 身份证正面
     */
    private String cardBackUrl;
    /**
     * 身份证反面
     */
    private String capitalUrl;
    /**
     * $column.columnComment
     */
    private String licenseUrl;
    /**
     * $column.columnComment
     */
    private String orderNo;
    /**
     * 还款周期
     */
    private Integer cycleType;
    /**
     * $column.columnComment
     */
    private String searchValue;

    /**
     * 逾期利息
     */
    @TableField(exist = false)
    private BigDecimal lastInstets;
    /**
     *逾期天数
     */
    private Integer days;

    @TableField(exist = false)
    private TLoadProduct tLoadProduct;
}
