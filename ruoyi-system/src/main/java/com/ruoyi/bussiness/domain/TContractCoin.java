package com.ruoyi.bussiness.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * U本位合约币种对象 t_contract_coin
 * 
 * @author michael
 * @date 2023-07-20
 */
@Data
@TableName("t_contract_coin")
public class TContractCoin
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 交易对 */
    @Excel(name = "交易对")
    private String symbol;

    /** 币种 */
    @Excel(name = "币种")
    private String coin;

    /** 基础币种 */
    @Excel(name = "基础币种")
    private String baseCoin;

    /** 合约面值（1手多少 如 1手=0.01BTC） */
    @Excel(name = "合约面值", readConverterExp = "1=手多少,如=,1=手=0.01BTC")
    private BigDecimal shareNumber;

    /** 杠杆倍数 */
    @Excel(name = "杠杆倍数")
    private String leverage;

    /** 0 启用  1 禁止 */
    @Excel(name = "0 启用  1 禁止")
    private Long enable;

    /** 前端显示0启用 1 禁止 */
    @Excel(name = "前端显示0启用 1 禁止")
    private Long visible;

    /** 是否可交易（0 可以 1 禁止） */
    @Excel(name = "是否可交易", readConverterExp = "0=,可=以,1=,禁=止")
    private Long exchangeable;

    /** 开空  （0  是  1 否） */
    @Excel(name = "开空  ", readConverterExp = "0=,是=,1=,否=")
    private Long enableOpenSell;

    /** 开多  （0  是  1 否） */
    @Excel(name = "开多  ", readConverterExp = "0=,是=,1=,否=")
    private Long enableOpenBuy;

    /** 市价开空（0 是 1否） */
    @Excel(name = "市价开空", readConverterExp = "0=,是=,1=否")
    private Long enableMarketSell;

    /** 市价开多（0 是 1否） */
    @Excel(name = "市价开多", readConverterExp = "0=,是=,1=否")
    private Long enableMarketBuy;

    /** 开仓手续费 */
    @Excel(name = "开仓手续费")
    private BigDecimal openFee;

    /** 平仓手续费 */
    @Excel(name = "平仓手续费")
    private BigDecimal closeFee;

    /** 资金费率 */
    @Excel(name = "资金费率")
    private BigDecimal usdtRate;

    /** 资金周期 */
    @Excel(name = "资金周期")
    private BigDecimal intervalHour;

    /** 币种小数精度 */
    @Excel(name = "币种小数精度")
    private BigDecimal coinScale;

    /** 基础币小数精度 */
    @Excel(name = "基础币小数精度")
    private BigDecimal baseScale;

    /** 最小数（以手为单位 ） */
    @Excel(name = "最小数", readConverterExp = "以=手为单位")
    private BigDecimal minShare;

    /** 最大数（以手为单位 ） */
    @Excel(name = "最大数", readConverterExp = "以=手为单位")
    private BigDecimal maxShare;

    /** 平台收益 */
    @Excel(name = "平台收益")
    private BigDecimal totalProfit;

    /** 排序字段 */
    @Excel(name = "排序字段")
    private Long sort;

    /**
     * 显示币种
     */
    private String showSymbol;


    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 图标
     */
    private String logo;


    /**
     * 交易所
     */
    private String market;


    @TableField(exist = false)
    private Integer isCollect;

    //交割时间
    private Integer  deliveryDays;

    //最小保证金
    private BigDecimal minMargin;

    //止盈率
    private BigDecimal earnRate;

    //止损率
    private BigDecimal lossRate;

    @TableField(exist = false)
    private BigDecimal amount;

    @TableField(exist = false)
    private BigDecimal open;

    /**
     * 浮动盈利点
     */
    private BigDecimal floatProfit;

    /**
     * 浮动盈亏
     */
    private BigDecimal   profitLoss;


}
