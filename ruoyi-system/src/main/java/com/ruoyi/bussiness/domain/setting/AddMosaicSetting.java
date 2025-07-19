package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * 底部菜单
 */
@Data
public class AddMosaicSetting {
    private Boolean isOpen; //总开关
    private Boolean sencordIsOpen; //秒合约开关
    private Boolean currencyIsOpen; //币币开关
    private Boolean contractIsOpen; //u本位开关
    private Boolean financialIsOpen; //理财开关
    private Boolean pledgeIsOpen; //质押开关
}
