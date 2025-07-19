package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帮助中心问题详情对象 t_help_center_info
 *
 * @author ruoyi
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_help_center_info")
public class THelpCenterInfo extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 帮助中心主键id
     */
    private Long helpCenterId;
    /**
     * 标题
     */
    private String question;
    /**
     * 内容
     */
    private String content;
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

}
