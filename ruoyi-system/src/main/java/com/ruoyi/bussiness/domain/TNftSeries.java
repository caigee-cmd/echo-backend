package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * nft合计对象 t_nft_series
 *
 * @author ruoyi
 * @date 2023-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_nft_series")
public class TNftSeries extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 所属链 -btc -eth -doge 等等  
     */
    private String chainType;
    /**
     * 所属链图标
     */
    private String coinUrl;
    /**
     * 交易总价格
     */
    private BigDecimal tradeAmount;
    /**
     * 交易次数
     */
    private Long tradeNum;
    /**
     * 地板价格
     */
    private BigDecimal aveAmount;
    /**
     * 封面
     */
    private String logoUrl;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 0=正常 1=删除
     */
        @TableLogic
    private String delFlag;

}
