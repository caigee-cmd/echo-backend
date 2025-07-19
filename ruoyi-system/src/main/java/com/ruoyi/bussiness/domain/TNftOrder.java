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
 * nft订单对象 t_nft_order
 *
 * @author ruoyi
 * @date 2023-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_nft_order")
public class TNftOrder extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 集合id
     */
    private Long seriesId;
    /**
     * 藏品id
     */
    private Long productId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 0报价 1成交 2失效
     */
    private String status;
    /**
     * $column.columnComment
     */
    private String searchValue;

}
