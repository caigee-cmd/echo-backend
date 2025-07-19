package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户币种充值地址对象 t_user_symbol_address
 *
 * @author ruoyi
 * @date 2023-07-12
 */
@Data
@TableName("t_user_symbol_address")
public class TUserSymbolAddress {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
        @TableId(value = "id")
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 币种
     */
    private String symbol;
    /**
     * 充值地址
     */
    private String address;

}
