package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * 金刚区
 */
@Data
public class MiddleMenuSetting {

    private String name;    //名字
    private String key;
    private String imgUrl;  //图标
    private String linkUrl; //跳转地址
    private Integer sort; //顺序
    private Boolean isOpen; //开关
}
