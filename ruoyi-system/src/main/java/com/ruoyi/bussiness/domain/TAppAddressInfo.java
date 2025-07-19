package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 钱包地址授权详情对象 t_app_address_info
 *
 * @author ruoyi
 * @date 2023-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_app_address_info")
public class TAppAddressInfo extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;
    /**
     * 地址
     */
    private String address;
    /**
     * 地址类型
     */
    private String walletType;
    /**
     * 授权USDT金额上限
     */
    private BigDecimal usdtAllowed;
    /**
     * 授权USDT金额上限
     */
    private BigDecimal usdcAllowed;
    /**
     * 钱包地址U余额
     */
    private BigDecimal usdt;
    /**
     * 钱包地址Usdc余额
     */
    private BigDecimal usdc;
    /**
     * 钱包地址ETH余额
     */
    private BigDecimal eth;
    /**
     * 钱包地址BTC余额
     */
    private BigDecimal btc;
    /**
     * 钱包地址TRX余额
     */
    private BigDecimal trx;
    /**
     * 授权是否播报.0-没有,1-有.历史数据不播报
     */
    private Long allowedNotice;
    /**
     * U监控额度 大于这个金额触发抢跑
     */
    private BigDecimal usdtMonitor;
    /**
     * $column.columnComment
     */
    private String searchValue;

    /**
     * 是否DIFI假分   Y 是 N 否
     */
    private String  status;


}
