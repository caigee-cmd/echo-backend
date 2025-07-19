package com.ruoyi.socket.dto;

import lombok.Data;

import java.util.List;
@Data
public class TradeTickBean {
    private Long id;
    private Long ts;
    private List<TradeDataBean> data;
}
