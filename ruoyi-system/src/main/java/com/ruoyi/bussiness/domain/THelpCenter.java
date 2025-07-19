package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帮助中心对象 t_help_center
 *
 * @author ruoyi
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_help_center")
public class THelpCenter extends BaseEntity {

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
     * 语言
     */
    private String language;
    /**
     * 1=启用 2=禁用
     */
    private String enable;
    /**
     * 0=正常 1=删除
     */
        @TableLogic
    private String delFlag;
    /**
     * $column.columnComment
     */
    private String showSymbol;

    @TableField(exist = false)
    private List<THelpCenterInfo> infoList;

}
