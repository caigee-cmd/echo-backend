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
 * U本位委托对象 t_contract_order
 *
 * @author michael
 * @date 2023-07-20
 */
@Data
@TableName("t_contract_order")
public class TContractOrder  {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * (0 买多 1卖空)
     */
    private Integer type;
    /**
     * 委托类型（0 限价 1 市价 2 止盈止损  3 计划委托）
     */
    private Integer delegateType;
    /**
     * 状态  0 （等待成交  1 完全成交  3已撤销）
     */
    private Integer status;
    /**
     * 委托总量
     */
    private BigDecimal delegateTotal;
    /**
     * 委托价格
     */
    private BigDecimal delegatePrice;
    /**
     * 已成交量
     */
    private BigDecimal dealNum;
    /**
     * 成交价
     */
    private BigDecimal dealPrice;
    /**
     * 委托价值
     */
    private BigDecimal delegateValue;
    /**
     * 成交价值
     */
    private BigDecimal dealValue;
    /**
     * 委托时间
     */
    private Date delegateTime;
    /**
     * 成交时间
     */
    private Date dealTime;
    /**
     * 交易币种
     */
    private String coinSymbol;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 基础币种（USDT）
     */
    private String baseCoin;
    /**
     * 杠杆
     */
    private BigDecimal leverage;
    /**
     * 交易对
     */
    private String symbol;
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

    @TableField(exist=false)
    private Map<String,Object> params;
}
