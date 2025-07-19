package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统访问记录对象 t_appuser_login_log
 *
 * @author ruoyi
 * @date 2023-06-30
 */
@Data
//@EqualsAndHashCode(callSuper = false)
@TableName("t_appuser_login_log")
public class TAppuserLoginLog{

private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
        @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 登录用户ID
     */
    private Long userId;
    /**
     * 登录用户名
     */
    private String username;
    /**
     * 访问IP
     */
    private String ipaddr;
    /**
     * 访问位置
     */
    private String loginLocation;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 系统OS
     */
    private String os;
    /**
     * 登录状态（0成功 1失败）
     */
    private Integer status;
    /**
     * $column.columnComment
     */
    private String msg;
    /**
     * 访问时间
     */
    private Date loginTime;

}
