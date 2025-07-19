package com.ruoyi.socket.dto;

import lombok.Data;

@Data
public class SocketDto {
    //是否是json  0 不是 1是
    private String type;
    private String message;
}
