package com.ruoyi.socket.dto;

import lombok.Data;

@Data
public class WsSubBO {
    //消息订阅类型 订阅 -subscribe  取消订阅-unsubscribe
    private String op;
    //订阅消息 规则：DETAIL   TRADE   KLINE
    private String type;
    //币种
    private String  symbol;
    private String userId;
    private String interval;
}
