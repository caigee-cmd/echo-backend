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
 * 返利活动明细对象 t_agent_activity_info
 *
 * @author ruoyi
 * @date 2023-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_agent_activity_info")
public class TAgentActivityInfo extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
        @TableId(value = "id",type = IdType.AUTO)
    private String id;
    /**
     * 1 充值返利 2挖矿返利
     */
    private Integer type;
    /**
     * 返利金额
     */
    private BigDecimal amount;
    /**
     * 1usdt-erc 2 usdt-trc 3btc 4eth
     */
    private String coinType;
    /**
     * 返利用户
     */
    private Long fromId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 1  待返  2  已返
     */
    private Integer status;
    /**
     * $column.columnComment
     */
    private String loginName;
    /**
     * $column.columnComment
     */
    private String serialId;

}
