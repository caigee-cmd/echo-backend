package com.ruoyi.util;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.common.enums.RecordEnum;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class BotMessageBuildUtils {

    public static SendMessage buildUsdtText(TAppAddressInfo tAppAddressInfo , BigDecimal diffUsdt){

        SendMessage sendMessage = new SendMessage();
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (tAppAddressInfo.getUsdt() != null && Objects.nonNull(diffUsdt) && diffUsdt.compareTo(BigDecimal.ZERO) != 0) {

            if (diffUsdt.compareTo(BigDecimal.ZERO) > 0) {
                sb.append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("USDT余额增加").append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("\n");
            } else {
                sb.append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("⚠️⚠️⚠️USDT余额减少⚠️⚠️⚠️").append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("\n");
            }
            sb.append("\n\n");
            //sb.append("\uD83E\uDDD1\uD83C\uDFFB\u200D\uD83E\uDDB0用户名：").append(remark).append("\n");
            sb.append("\uD83C\uDD94用户ID：").append(tAppAddressInfo.getUserId()).append("\n");
            sb.append("\n\n");
            sb.append("\uD83C\uDF17历史余额：").append(diffUsdt.stripTrailingZeros().toPlainString()).append("\n");
            sb.append("\uD83C\uDF16变动金额：").append(tAppAddressInfo.getUsdt().subtract(diffUsdt).stripTrailingZeros().toPlainString()).append("\n");
            sb.append("\uD83C\uDF15当前余额：").append(tAppAddressInfo.getUsdt().stripTrailingZeros().toPlainString()).append("\n");
            sb.append("⏰变动时间： ").append(formatter.format(tAppAddressInfo.getUpdateTime())).append("\n");
            sb.append("\n\n");
            sb.append("\uD83D\uDCB0钱包地址：").append(tAppAddressInfo.getAddress()).append("\n");
            sb.append("\n\n");
            sb.append("\uD83C\uDF96\uD83C\uDF96\uD83C\uDF96数据统计\uD83C\uDF96\uD83C\uDF96\uD83C\uDF96").append("\n");
            sb.append("\n\n");
            sendMessage.setText(sb.toString());
        }
        return sendMessage;

    }


    public static SendMessage buildText(Integer code, TAppRecharge recharge, TWithdraw withdraw ) {
        SendMessage sendMessage = new SendMessage();
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        if(Objects.equals(RecordEnum.RECHARGE.getCode(), code)){
            sb.append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("⚠️⚠️⚠️有新的充值订单，请尽快审核⚠️⚠️⚠️").append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("\n");
            sb.append("\n\n");
            sb.append("\uD83C\uDD94订单号：").append(recharge.getSerialId()).append("\n");
            sb.append("\uD83C\uDD94用户ID：").append(recharge.getUserId()).append("\n");
            sb.append("\n\n");
            sb.append("\uD83C\uDF17充值地址：").append(recharge.getAddress()).append("\n");
            sb.append("\uD83C\uDF16充值金额：").append(recharge.getAmount().stripTrailingZeros().toPlainString()).append("\n");
            sb.append("⏰时间： ").append(formatter.format(recharge.getCreateTime())).append("\n");
            sb.append("\n\n");
            sb.append("\uD83D\uDCB0币种：").append(recharge.getCoin().toUpperCase()).append("\n");
            sb.append("\n\n");
        }
        if(Objects.equals(RecordEnum.WITHDRAW.getCode(), code)){
            sb.append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("⚠️⚠️⚠️有新的提现订单，请尽快审核⚠️⚠️⚠️").append("\uD83D\uDCB8\uD83D\uDCB8\uD83D\uDCB8").append("\n");
            sb.append("\n\n");
            sb.append("\uD83C\uDD94订单号：").append(withdraw.getSerialId()).append("\n");
            sb.append("\uD83C\uDD94用户ID：").append(withdraw.getUserId()).append("\n");
            sb.append("\n\n");
            sb.append("\uD83C\uDF17钱包地址：").append(withdraw.getAddress()).append("\n");
            sb.append("\uD83C\uDF17收款地址：").append(withdraw.getToAdress()).append("\n");
            sb.append("\uD83C\uDF16提现金额：").append(withdraw.getAmount().stripTrailingZeros().toPlainString()).append("\n");
            sb.append("⏰时间： ").append(formatter.format(withdraw.getCreateTime())).append("\n");
            sb.append("\n\n");
            sb.append("\uD83D\uDCB0币种：").append(withdraw.getCoin().toUpperCase()).append("\n");
            sb.append("\n\n");
        }
        sb.append("\uD83C\uDF96\uD83C\uDF96\uD83C\uDF96平台充提通知\uD83C\uDF96\uD83C\uDF96\uD83C\uDF96").append("\n");
        sb.append("\n\n");
        sendMessage.setText(sb.toString());

        return sendMessage;
    }
}
