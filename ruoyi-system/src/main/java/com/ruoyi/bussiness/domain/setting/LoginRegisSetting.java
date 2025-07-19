package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

@Data
public class LoginRegisSetting {


    //邮箱
    private boolean emailIsOpen;
    //手机注册
    private boolean phoneIsOpen;
    //普通
    private boolean ordinaryIsOpen;
    //地址
    private boolean addressIsOpen;
    //信用分
    private Integer credits=100;
}
