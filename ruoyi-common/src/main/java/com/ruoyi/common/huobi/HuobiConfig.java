package com.ruoyi.common.huobi;

import com.google.common.collect.Lists;

import java.util.List;

public class HuobiConfig {
    //火币订阅格式
    public static List<String> getSubList(String symbol){
        List<String> channels = Lists.newArrayList();
        channels.add("market."+symbol+"-USDT.kline.1min");
        channels.add("market."+symbol+"-USDT.trade.detail");
        channels.add("market."+symbol+"-USDT.detail");
        return channels;
    }
    public static String getKline(String symbol){
        String kilne = "market."+symbol+"-USDT.kline.1min";
        return kilne;
    }
    public static String getTrade(String symbol){
        String kilne = "market."+symbol+"-USDT.trade.detail";
        return kilne;
    }
    public static String getDetail(String symbol){
        String kilne = "market."+symbol+"-USDT.detail";
        return kilne;
    }
}
