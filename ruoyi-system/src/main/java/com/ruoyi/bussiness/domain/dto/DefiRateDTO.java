package com.ruoyi.bussiness.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import jnr.ffi.annotations.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * defi挖矿利率配置对象 t_defi_rate
 *
 * @author ruoyi
 * @date 2023-08-17
 */
@Data
public class DefiRateDTO {

    private static final long serialVersionUID=1L;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 金额区间
     */
    private String amountTotle;
    /**
     * 利率
     */
    private String rate;


}
