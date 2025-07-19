package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * U本位持仓表对象 t_contract_position
 *
 * @author michael
 * @date 2023-07-20
 */
@Data
@TableName("t_contract_position")
public class TContractPosition {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 购买类型(0 买多 1卖空)")
     */
    private Integer type;
    /**
     * 委托类型（0 限价 1 市价 2 止盈止损  3 计划委托）
     */
    private Integer delegateType;
    /**
     * 状态  0 （等待成交  1 完全成交
     */
    private Integer status;
    /**
     * 保证金
     */
    private BigDecimal amount;
    /**
     * 持仓数量
     */
    private BigDecimal openNum;
    /**
     * 开仓均价
     */
    private BigDecimal openPrice;
    /**
     * 预计强平价
     */
    private BigDecimal closePrice;
    /**
     * 仓位编号
     */
    private String orderNo;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 开仓手续费
     */
    private BigDecimal openFee;
    /**
     * 杠杆
     */
    private BigDecimal leverage;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 调整保证金
     */
    private BigDecimal adjustAmount;
    /**
     * 收益
     */
    private BigDecimal earn;
    /**
     * 成交价
     */
    private BigDecimal dealPrice;
    /**
     * 成交量
     */
    private BigDecimal dealNum;
    /**
     * 成交时间
     */
    private Date dealTime;
    /**
     * 卖出手续费
     */
    private BigDecimal sellFee;
    /**
     * 剩余保证金
     */
    private BigDecimal remainMargin;
    /**
     * 周期手续费
     */
    private BigDecimal assetFee;

    /**
     * 代理IDS
     */
    private String adminParentIds;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //委托价值
    private BigDecimal entrustmentValue;

    //成交价值
    private BigDecimal dealValue;
    @TableField(exist = false)
    private Map<String, Object> params;


    //审核状态  0提交   3 待审   1通过  2拒绝
    private Integer auditStatus;
    //交割时间
    private Integer  deliveryDays;

    //最小保证金
    private BigDecimal minMargin;

    //止盈率
    private BigDecimal earnRate;

    //止损率
    private BigDecimal lossRate;

    //提交平仓时间
    private Date  subTime;


    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private BigDecimal ureate;
    //补仓金额差
    @TableField(exist = false)
    private BigDecimal subAmount;

}
