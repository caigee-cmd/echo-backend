package com.ruoyi.bussiness.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 钱包地址授权详情对象 t_app_address_info
 *
 * @author ruoyi
 * @date 2023-07-15
 */
@Data
public class AddressHashDTO  {

private static final long serialVersionUID=1L;

    /**
     * userId
     */

    private Long userId;
    /**
     * 地址
     */
    private String address;
    /**
     * hash
     */
    private String hash;
}
