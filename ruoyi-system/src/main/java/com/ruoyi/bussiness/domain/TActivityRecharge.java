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
 * 充值活动对象 t_activity_recharge
 *
 * @author ruoyi
 * @date 2023-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_activity_recharge")
public class TActivityRecharge extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 0-关闭 1-开启
     */
    private Long onOff;
    /**
     * 充值返点比例
     */
    private BigDecimal rechargePro;
    /**
     * 充值返点最大值
     */
    private BigDecimal maxRebate;

}
