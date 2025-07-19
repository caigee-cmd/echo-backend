package com.ruoyi.bussiness.domain;

import lombok.Data;
@Data
public class SendPhoneDto {

    private String account;
    //API账号对应密钥
    private String password;
    //短信内容
    private String msg;
    //手机号码
    private String mobile;
    //客户自定义批次号
    private String uid;
    //用户收到短信之后显示的发件人国内不支持自定义，国外支持，但是需要提前和运营商沟通注册，具体请与NODE对接人员确定。选填
    private String senderId;
    //退订开启标识
    private String tdFlag;
}
