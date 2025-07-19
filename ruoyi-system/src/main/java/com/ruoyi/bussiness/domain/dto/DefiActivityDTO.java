package com.ruoyi.bussiness.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 空投活动对象 t_defi_activity
 *
 * @author ruoyi
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_defi_activity")
public class DefiActivityDTO extends BaseEntity {

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
    private Long endTimeS;
    private Long beginTimeS;
}
