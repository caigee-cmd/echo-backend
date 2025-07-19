package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * 玩法配置
 */
@Data
public class PlayingSetting {

    //name  sort  isOpen
    private String name; //顺序
    private Boolean isOpen; //顺序
    private Integer sort; //顺序
}