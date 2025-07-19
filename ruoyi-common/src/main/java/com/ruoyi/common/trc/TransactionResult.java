package com.ruoyi.common.trc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResult {

    // false-确认中/TRUE-已确认
    private boolean confirmed;
    // TRUE-成功/false-失败
    private boolean success;
    // 失败描述
    private String failedMsg;
}
