package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 规则说明对象 t_home_setter
 *
 * @author ruoyi
 * @date 2023-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_home_setter")
public class THomeSetter extends BaseEntity {

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
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private String content;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 是否展示 0展示  2不展示
     */
    private String isShow;
    /**
     * 语言
     */
    private String languageName;
    /**
     * 点赞数
     */
    private Integer likesNum;
    /**
     * 类型（0 首页文本  1 问题列表）
     */
    private Integer homeType;
    /**
     * 功能（0=首页  1=defi挖矿 2=助力贷）
     */
    private Integer modelType;
    /**
     * $column.columnComment
     */
    private String searchValue;

}
