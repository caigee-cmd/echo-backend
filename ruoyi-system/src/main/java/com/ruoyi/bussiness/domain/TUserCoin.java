package com.ruoyi.bussiness.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("t_user_coin")
public class TUserCoin {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String coin;

    private String icon;
}
