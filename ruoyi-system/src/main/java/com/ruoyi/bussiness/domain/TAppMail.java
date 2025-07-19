package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 1v1站内信对象 t_app_mail
 *
 * @author ruoyi
 * @date 2023-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_app_mail")
public class TAppMail extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * $column.columnComment
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * $column.columnComment
     */
    private Long userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 消息类型 =普通消息 2=全站消息
     */
    private String type;
    /**
     * 状态（0 未读 1已读）
     */
    private Integer status;
    /**
     * 操作人
     */
    private String opertorId;
    /**
     * $column.columnComment
     */
    private String searchValue;
    /**
     * 0正常 2删除
     */
        @TableLogic
    private String delFlag;

    @TableField(exist = false)
    private String userIds;

}
