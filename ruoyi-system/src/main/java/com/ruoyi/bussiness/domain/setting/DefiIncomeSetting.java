package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

/**
 * defi挖矿收益
 */
@Data
public class DefiIncomeSetting {
    private String totalOutput;//总产出
    private String userBenefits;//用户收益
    private String participant;//参与者
    private String validNode;//有效节点
}
