package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * 底部菜单
 */
@Data
public class BottomMenuSetting {
    private String name;//名称
    private String key;
    private String imgUrl;//图标
    private String checkedImgUrl;//选中图标
    private String linkUrl;//选中图标
    private Integer sort; //顺序
    private Boolean isOpen; //开关
}
