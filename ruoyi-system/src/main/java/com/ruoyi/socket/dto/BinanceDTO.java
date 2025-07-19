package com.ruoyi.socket.dto;

import com.huobi.wss.event.MarketKLineSubResponse;
import lombok.Data;

@Data
public class BinanceDTO {
    private String ch;
    private Long ts;
    private Object tick;
}
