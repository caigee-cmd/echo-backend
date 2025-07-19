package com.ruoyi.socket.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KLoader {
    //控线币种
    public static Map<String, HashMap> BOT_MAP =new ConcurrentHashMap<>();
    //控线实时价格
    public static Map<String ,BigDecimal> BOT_PRICE = new ConcurrentHashMap<>();
    //控线时间控制map
    public static Map<String,String> BOT_TIME_MAP =new ConcurrentHashMap<>();
    //控线最高最低价格控制map
    public static Map<String ,Map<String,String>> BOT_PRICE_MAP = new ConcurrentHashMap<>();
    //跟随型  key  是 交易对   value 的map是  key是时间戳  arrayList是价格
    public static Map<String,BigDecimal> OPEN_PRICE = new ConcurrentHashMap<>();
}
