package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户购买质押限制对象 t_ming_product_user
 *
 * @author ruoyi
 * @date 2023-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_ming_product_user")
public class TMingProductUser extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 玩家用户id
     */
    private Long appUserId;
    /**
     * 限购次数
     */
    private Long pledgeNum;
    /**
     *
     */
    private String searchValue;

    @TableField(exist = false)
    private TAppUser tAppUser;

    @TableField(exist = false)
    private String coin;

    @TableField(exist = false)
    private String icon;

    @TableField(exist = false)
    private String title;

}
