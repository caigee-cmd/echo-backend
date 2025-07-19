package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 空投活动对象 t_defi_activity
 *
 * @author ruoyi
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_defi_activity")
public class DefiActivity extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 需要金额
     */
    private BigDecimal totleAmount;

    private Long userId;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 奖励金额
     */
    private BigDecimal amount;
    /**
     * 0-usdt 1-eth
     */
    private Long type;

    private Integer status;
    /**
     * $column.columnComment
     */
    private String searchValue;

}
