package com.ruoyi.bussiness.domain.setting;

import lombok.Data;

@Data
public class AppSidebarSetting {
//    /**
//     * 实名认证（初级）
//     */
//    private Boolean primary;
//    /**
//     * 实名认证（高级）
//     */
//    private Boolean advanced;
//    /**
//     * 绑定银行卡
//     */
//    private Boolean bank;
//    /**
//     * 设置资金密码
//     */
//    private Boolean tardPwd;
//    /**
//     * 设置登陆密码
//     */
//    private Boolean loginPwd;
//    /**
//     * 邮箱认证
//     */
//    private Boolean certified;
//    /**
//     * 服务条款
//     */
//    private Boolean termsService;
//    /**
//     * 白皮书
//     */
//    private Boolean paper;
//    /**
//     * 语言
//     */
//    private Boolean language;

    private String key;
    private String name;    //名称
    private String jumpUrl; //跳转地址
    private String jumpType; //跳转类型
    private Integer sort; //顺序
    private Boolean isOpen; //开关
    private String logoUrl; //图标


}
