package com.ruoyi.socket.dto;

import lombok.Data;

@Data
public class WsVO {
    //trade kline detail
    private String type;
    private String symbol;
    private Object data;
}
