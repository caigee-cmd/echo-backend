package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 前台文本配置对象 t_option_rules
 *
 * @author ruoyi
 * @date 2023-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_option_rules")
public class TOptionRules extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 语言 en zh
     */
    private String language;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否展示 0展示  2不展示
     */
    private Long isShow;
    /**
     * 0=服务条款 1=秒合约说明 2=币币交易说明 3=代理活动 4=U本位合约说明 5=注册隐私政策 6=注册使用条款 7=贷款规则
     */
    private Integer type;

    @TableField(exist = false)
    private String key;

}
