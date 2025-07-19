package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

import javax.swing.*;
import java.security.Key;

/**
 * 玩法配置
 */
@Data
public class TabSetting {

    //name  sort  isOpen
    private String name; //顺序
    private Boolean isOpen; //顺序
    private Integer sort; //顺序
    private String keyStr; // 语言
}