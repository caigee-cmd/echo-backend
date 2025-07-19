package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 t_mine_user
 *
 * @author ruoyi
 * @date 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_mine_user")
public class TMineUser extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 挖矿产品id
     */
    private Long id;
    /**
     * 限购次数
     */
    private Long timeLimit;

}
