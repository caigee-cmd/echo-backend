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
 * 借贷产品对象 t_load_product
 *
 * @author ruoyi
 * @date 2023-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_load_product")
public class TLoadProduct extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 贷款最小额度
     */
    private BigDecimal amountMin;
    /**
     * 贷款最大额度
     */
    private BigDecimal amountMax;
    /**
     * 周期类型  0-7天 1-14天 2-30天 ,,,,待补充
     */
    private Long cycleType;
    /**
     * 还款类型 0-到期一次换本息...待补充
     */
    private Long repayType;
    /**
     * 状态 0 未开启 1已开启
     */
    private Long status;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 日利率（%）
     */
    private BigDecimal odds;
    /**
     * 还款机构
     */
    private String repayOrg;
    /**
     * 是否冻结  1=正常 2=冻结
     */
    private String isFreeze;

}
