package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * 首页币种配置
 */
@Data
public class HomeCoinSetting {

    private String  isOpen; //开关
    private String  coin;   //币种
    private String  sort;    //排序
}
