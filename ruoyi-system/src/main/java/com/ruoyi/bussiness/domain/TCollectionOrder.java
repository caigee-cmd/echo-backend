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
 * 【请填写功能名称】对象 t_collection_order
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_collection_order")
public class TCollectionOrder extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 归集地址
     */
    private String address;
    /**
     * 地址类型
     */
    private String chain;
    /**
     * hash
     */
    private String hash;
    /**
     * 归集金额
     */
    private BigDecimal amount;
    /**
     * 币种
     */
    private String coin;
    /**
     * 1  进行中   2 归集成功  3 归集失败
     */
    private String status;
    /**
     * 客户端名称
     */
    private String clientName;
    /**
     * 
     */
    private String searchValue;

}
