package com.ruoyi.bussiness.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

/**
 * 秒合约币种配置对象 t_second_coin_config
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@Data
public class SymbolCoinConfigVO {

private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 合约交易对
     */
    private String symbol;
    /**
     * 是否启用 2关闭 1启用
     */
    private Long status;
    /**
     * 是否展示 2不展示 1展示
     */
    private Long showFlag;
    /**
     * 币种
     */
    private String coin;
    /**
     * 结算币种
     */
    private String baseCoin;
    private String market;

    /**
     * 排序
     */
    private Long sort;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 图标
     */
    private String logo;
    /**
     * 展示交易对
     */
    private String showSymbol;
    /**
     * 类型  0-秒合约 1-u本位  2-币币
     */
    private Integer type;
    /**
     * 类型  1 外汇 2 虚拟币
     */
    private Integer coinType;
    private BigDecimal amount;
    @TableField(exist = false)
    private BigDecimal open;
    private Integer isCollect;
}
