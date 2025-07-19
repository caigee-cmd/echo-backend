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
 * 秒合约币种周期配置对象 t_second_period_config
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_second_period_config")
public class TSecondPeriodConfig extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 秒合约币种配置id
     */
    private Long secondId;
    /**
     * 时间周期  单位秒
     */
    private Long period;

    /**
     * 赔率
     */
    private BigDecimal odds;
    /**
     * 最大金额
     */
    private BigDecimal maxAmount;
    /**
     * 最小金额
     */
    private BigDecimal minAmount;
    /**
     * 1开启 2关闭
     */
    private Long status;
    /**
     * 全输标识
     */
    private Boolean flag;
    /**
     * $column.columnComment
     */
    private String searchValue;


}
