package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 币种管理对象 t_symbol_manage
 *
 * @author ruoyi
 * @date 2023-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_symbol_manage")
public class TSymbolManage extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 币种
     */
    private String symbol;
    /**
     * 最小兑换数量
     */
    private BigDecimal minChargeNum;
    /**
     * 最大兑换数量
     */
    private BigDecimal maxChargeNum;
    /**
     * 手续费(%)
     */
    private BigDecimal commission;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 1 启用 2 禁用
     */
    private String enable;
    /**
     * 0正常  2删除
     */
        @TableLogic
    private String delFlag;

    /**
     * 图标
     */
    private String logo;

    /**
     * 交易所
     */
    private String market;
}
