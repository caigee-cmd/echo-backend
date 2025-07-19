package com.ruoyi.socket.manager;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huobi.wss.event.MarketDetailSubResponse;
import com.huobi.wss.event.MarketKLineSubResponse;
import com.huobi.wss.event.MarketTradeDetailSubResponse;
import com.ruoyi.bussiness.domain.TBotKlineModelInfo;
import com.ruoyi.bussiness.mapper.TBotKlineModelInfoMapper;
import com.ruoyi.bussiness.service.ITBotKlineModelService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.bussiness.service.ITBotKlineModelService;
import com.ruoyi.socket.config.KLoader;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.SpringContextUtil;
import com.ruoyi.socket.dto.*;
import com.ruoyi.socket.socketserver.WebSocketServers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertNotNull;

@Service
public class WebSocketUserManager {
    @Resource
    private RedisCache redisCache;
    @Resource
    private TBotKlineModelInfoMapper tBotKlineModelInfoMapper;
    @Resource
    private ITBotKlineModelService tBotKlineModelService;
    private static final Logger log = LoggerFactory.getLogger(WebSocketUserManager.class);
    //订阅逻辑
    @Async("socketThread")
    public void subscribeMsg(WsSubBO wsSubBO){
        //先订阅k线
        String op = wsSubBO.getOp();
        if(null!=op&&op.equals("subscribe")){
            //订阅消息处理
            //订阅消息规则：DETAIL   TRADE   KLINE
            switch (wsSubBO.getType()) {
                //DETAIL 不区分币种    TRADE KLINE区分币种
                case "DETAIL":
                    List<String> detail = WebSocketServers.detailMap.get("DETAIL");
                    if(detail==null){
                        detail= new ArrayList<>();
                    }
                    if(!detail.contains(wsSubBO.getUserId())){
                        detail.add(wsSubBO.getUserId());
                        WebSocketServers.detailMap.put("DETAIL",detail);
                    }
                    break;
                case "TRADE":
                    List<String> trade = WebSocketServers.tradeMap.get(wsSubBO.getSymbol().toLowerCase());
                    if(trade==null){
                        trade= new ArrayList<>();
                    }
                    if(!trade.contains(wsSubBO.getUserId())){
                        trade.add(wsSubBO.getUserId());
                        WebSocketServers.tradeMap.put(wsSubBO.getSymbol().toLowerCase(),trade);
                    }
                    break;
                case "KLINE":
                    List<String> kline = WebSocketServers.klineMap.get(wsSubBO.getSymbol().toLowerCase());
                    if(kline==null){
                        kline= new ArrayList<>();
                    }
                    if(!kline.contains(wsSubBO.getUserId())){
                        for (Map.Entry<String, List<String>> klineData : WebSocketServers.klineMap.entrySet()){
                            List<String> tmp_klineData = klineData.getValue();
                            tmp_klineData.remove(wsSubBO.getUserId());
                            WebSocketServers.klineMap.put(klineData.getKey(),tmp_klineData);
                        }
                        kline.add(wsSubBO.getUserId());
                        WebSocketServers.klineMap.put(wsSubBO.getSymbol().toLowerCase(),kline);
                    }
                    break;
            }
        }else{
            //取消订阅
            switch (wsSubBO.getType()) {
                //DETAIL 不区分币种    TRADE KLINE区分币种
                case "DETAIL":
                    List<String> detail = WebSocketServers.detailMap.get("DETAIL");
                    if(detail!=null){
                        detail.remove(wsSubBO.getUserId());
                        WebSocketServers.detailMap.put("DETAIL",detail);
                    }
                    break;
                case "TRADE":
                    List<String> trade = WebSocketServers.tradeMap.get(wsSubBO.getSymbol().toLowerCase());
                    if(trade!=null){
                        trade.remove(wsSubBO.getUserId());
                        WebSocketServers.tradeMap.put("TRADE",trade);
                    }
                    break;
                case "KLINE":
                    List<String> kline = WebSocketServers.klineMap.get(wsSubBO.getSymbol().toLowerCase());
                    if(kline!=null){
                        kline.remove(wsSubBO.getUserId());
                        WebSocketServers.klineMap.put("KLINE",kline);
                    }
                    break;
            }
        }
    }

    //分发逻辑
    @Async("socketThread")
    public  void buidlMsgToSend(Object  message){
        WsVO wsVO = new WsVO();
        if(message instanceof MarketTradeDetailSubResponse){
            String ch = ((MarketTradeDetailSubResponse) message).getCh();
            wsVO.setType("TRADE");
            wsVO.setSymbol(ch.replace("market.","").replace("-USDT.trade.detail","").toLowerCase());
            wsVO.setData(message);
        }else  if(message instanceof MarketKLineSubResponse){
            String ch = ((MarketKLineSubResponse) message).getCh();
            wsVO.setType("KLINE");
            wsVO.setSymbol(ch.replace("market.","").replace("-USDT.kline.1min","").toLowerCase());
            wsVO.setData( message);
        }else  {
            String ch = ((MarketDetailSubResponse) message).getCh();
            wsVO.setType("DETAIL");
            wsVO.setSymbol(ch.replace("market.","").replace("-USDT.detail","").toLowerCase());
            wsVO.setData( message);
        }
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public void toSendMessage(WsVO wsVO){
        String symbol = wsVO.getSymbol();
        switch (wsVO.getType()) {
            //DETAIL 不区分币种    TRADE KLINE区分币种
            case "DETAIL":
                //先存币种的最新价格
                BinanceDTO data = (BinanceDTO) wsVO.getData();
                DetailTickBean klineTickBean = (DetailTickBean)data.getTick();
                redisCache.setCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+symbol,klineTickBean.getClose());
              //  redisCache.setCacheObject(CachePrefix.CURRENCY_OPEN_PRICE.getPrefix()+symbol,klineTickBean.getOpen());
                KLoader.OPEN_PRICE.put(symbol,klineTickBean.getOpen());
                //在判断是否分发
                List<String> detail = WebSocketServers.detailMap.get("DETAIL");
                if(detail==null){
                    return;
                }
                for (String s : detail) {
                    WebSocketServers.sendInfo(JSONObject.toJSONString(wsVO),s);
                }
                break;
            case "TRADE":
//                log.info("================================"+wsVO);
                List<String> tradeMap = WebSocketServers.tradeMap.get(symbol.toLowerCase());
                if(tradeMap==null){
                    return;
                }
                for (String s : tradeMap) {
                    WebSocketServers.sendInfo(JSONObject.toJSONString(wsVO),s);
                }
                break;
            case "KLINE":
                if(symbol.length()>5){
                    RedisCache redis = SpringContextUtil.getBean(RedisCache.class);
                    BinanceDTO dataMt5 = (BinanceDTO) wsVO.getData();
                    KlineTickBean klineTickBeanc = (KlineTickBean)dataMt5.getTick();
                    redis.setCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+ symbol,klineTickBeanc.getClose());
                }
                List<String> klineMap = WebSocketServers.klineMap.get(symbol.toLowerCase());
                if(klineMap==null){
                    return;
                }
                for (String s : klineMap) {
                    WebSocketServers.sendInfo(JSONObject.toJSONString(wsVO),s);
                }
                break;
        }
    }


    //构建币安k线对线
    @Async("socketThread")
    public  void binanceKlineSendMeg(String event){
        JSONObject jsonObject = JSONObject.parseObject(event);
        JSONObject o = jsonObject.getJSONObject("k");
        //返回对象
        WsVO wsVO = new WsVO();
        wsVO.setType("KLINE");
        wsVO.setSymbol(String.valueOf(o.get("s")).replace("USDT","").toLowerCase());

        //构建kline对象
        BinanceDTO klineDTO = new BinanceDTO();
        KlineTickBean klineTickBean = new KlineTickBean();
        klineTickBean.setIntervention(false);
        klineDTO.setTs(jsonObject.getLong("E"));
        klineDTO.setCh(jsonObject.getString("e"));
        //k线
        klineTickBean.setId(o.getLong("t"));
        klineTickBean.setVol(new BigDecimal(o.getString("v")));
        klineTickBean.setCount(new BigDecimal(o.getString("n")));
        klineTickBean.setAmount(new BigDecimal(o.getString("q")));
        klineTickBean.setHigh(new BigDecimal(o.getString("h")));
        klineTickBean.setLow(new BigDecimal(o.getString("l")));
        klineTickBean.setOpen(new BigDecimal(o.getString("o")));
        klineTickBean.setClose(new BigDecimal(o.getString("c")));
        String coin = String.valueOf(o.get("s")).replace("USDT", "").toLowerCase();
        coin =coin.replace("usdt","");
        //获取跟随型控盘价格。
        String conPriceAndId = redisCache.getCacheObject("con-" +coin);
        if(conPriceAndId!=null){
            log.info("控线"+conPriceAndId);
            String[] split = conPriceAndId.split(",");
            BigDecimal conPrice = new BigDecimal(split[0]);
            long time = Long.parseLong(split[1]);
            BigDecimal conPriceAdd = klineTickBean.getClose().add(conPrice);
            //如果不是这一分钟插得针 开盘价不用管 管住最高价或者最低假 和封盘价，    如果是这一分钟 每一个都替换
            Long ts = klineDTO.getTs();
            //当前k线
            String ymdhm = getYMDHM(ts);
            //控制K线
            String conT = getYMDHM(time);
            log.info(coin+"控制价格为"+conPrice);
            if(conT.equals(ymdhm)){
                if(klineTickBean.getHigh().compareTo(conPriceAdd)<0){
                    klineTickBean.setHigh(conPriceAdd);
                }
                if(klineTickBean.getLow().compareTo(conPriceAdd)>0){
                    klineTickBean.setLow(conPriceAdd);
                }

                klineTickBean.setClose(conPriceAdd);
            }else {
                BigDecimal cl = klineTickBean.getClose().add(conPrice);
                BigDecimal hi = klineTickBean.getHigh().add(conPrice);
                BigDecimal lo = klineTickBean.getLow().add(conPrice);
                BigDecimal op = klineTickBean.getOpen().add(conPrice);
                klineTickBean.setHigh(hi);
                klineTickBean.setLow(lo);
                klineTickBean.setOpen(op);
                klineTickBean.setClose(cl);
            }
            KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
        }else{
            //所有控线走eth的频率,也就是说如果控线了,处理eth的同时,也会处理被控制的币种.
            if(coin.equals("bnb")) {
                controlKline(klineTickBean,klineDTO);
                klineTickBean.setHigh(new BigDecimal(o.getString("h")));
                klineTickBean.setLow(new BigDecimal(o.getString("l")));
                klineTickBean.setOpen(new BigDecimal(o.getString("o")));
                klineTickBean.setClose(new BigDecimal(o.getString("c")));
                if(KLoader.BOT_MAP.containsKey("bot-"+"bnb")){
                   return;
                }
            }else {
                if(KLoader.BOT_MAP.containsKey("bot-"+coin)){
                    Map<String, Object> cacheMap = KLoader.BOT_MAP.get("bot-"+coin);
                    //因为btc 处理了 别的k先数据 ，如果不等于null  或者 Y不等于0直接返回 不处理
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    LocalDateTime lastWholeMinute = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
                    long timestamp = lastWholeMinute.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
                    TBotKlineModelInfo tBotKlineModelInfo = new TBotKlineModelInfo();
                    tBotKlineModelInfo.setModelId((Long)cacheMap.get("id"));
                    tBotKlineModelInfo.setDateTime(timestamp);
                    TBotKlineModelInfo tBotKlineModelInfo1=tBotKlineModelInfoMapper.selectOne(new QueryWrapper<>(tBotKlineModelInfo));
                    if(tBotKlineModelInfo1!=null&&!tBotKlineModelInfo1.getY().equals("0")){
                        //控线不处理  因为走的btc的  在处理会导致价格不一致
                        return;
                    }
                }
            }
        }
        klineDTO.setTick(klineTickBean);
        wsVO.setData(klineDTO);
        //发送消息
        toSendMessage(wsVO);
    }
    private void controlKline(KlineTickBean klineTickBean,BinanceDTO klineDTO ){
        for (String  cacheMapc: KLoader.BOT_MAP.keySet()) {
            HashMap cacheMap = KLoader.BOT_MAP.get(cacheMapc);
            String coin = cacheMapc.replace("bot-", "").toLowerCase();
            klineTickBean.setIntervention(true);
            //获取整分时间戳 然后查询是否有控线数据
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime lastWholeMinute = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
            long timestamp = lastWholeMinute.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            TBotKlineModelInfo tBotKlineModelInfo = new TBotKlineModelInfo();
            tBotKlineModelInfo.setModelId((Long)cacheMap.get("id"));
            tBotKlineModelInfo.setDateTime(timestamp);
            TBotKlineModelInfo tBotKlineModelInfo1=tBotKlineModelInfoMapper.selectOne(new QueryWrapper<>(tBotKlineModelInfo));
            //不等于null  Y不等于0为控线状态
            if(null!=tBotKlineModelInfo1&&!tBotKlineModelInfo1.getY().equals("0")){
                //获取当时对比价格
                BigDecimal currentlyPrice = (BigDecimal) cacheMap.get("currentlyPrice");
                //最高价
                BigDecimal high = currentlyPrice.multiply(tBotKlineModelInfo1.getHigh().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                klineTickBean.setHigh(currentlyPrice.add(high));
                //最低价
                BigDecimal low = currentlyPrice.multiply(tBotKlineModelInfo1.getLow().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                klineTickBean.setLow(currentlyPrice.add(low));
                //放入开盘价
                BigDecimal open = currentlyPrice.multiply(tBotKlineModelInfo1.getOpen().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                klineTickBean.setOpen(currentlyPrice.add(open));

                // 获取当前时间
                LocalDateTime currentTime = LocalDateTime.now();
                // 将秒和毫秒部分设置为零，以获得当前整分的时间
                LocalDateTime currentMinute = currentTime.withSecond(0).withNano(0);
                //id要为当前分钟的整分 要不k线会混乱
                klineTickBean.setId(currentMinute.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                //获取当前秒数
                int currentSecond = currentTime.getSecond();
                int secondsToNextMinute = 60 - currentSecond;
                //时间剩余5秒不跳了 保证k线与控线一致。
                if(secondsToNextMinute<5){
                    high = currentlyPrice.multiply(tBotKlineModelInfo1.getHigh().divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
                    klineTickBean.setHigh(currentlyPrice.add(high));
                    low = currentlyPrice.multiply(tBotKlineModelInfo1.getLow().divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
                    klineTickBean.setLow(currentlyPrice.add(low));
                    BigDecimal close = currentlyPrice.multiply(tBotKlineModelInfo1.getClose().divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
                    klineTickBean.setClose(currentlyPrice.add(close));
                    klineTickBean.setOpen(currentlyPrice.add(open));
                    KLoader.BOT_PRICE.put(coin,currentlyPrice.add(close));
                }else{
                    Long ts = klineDTO.getTs();
                    //分
                    String mm = getMM(ts);
                    String ss = getSS(ts);
                    //最高价 -最低价
                    BigDecimal subtract = klineTickBean.getHigh().subtract(klineTickBean.getLow());
                    //随机次数模拟为15次 一分钟跳动的次数
                    BigDecimal mean = subtract.divide(new BigDecimal(30),8,RoundingMode.HALF_UP);
                    //涨
                    if(tBotKlineModelInfo1.getOpen().compareTo(tBotKlineModelInfo1.getClose())<0){
                        String s = KLoader.BOT_TIME_MAP.get(coin);
                        if(s.equals("0")){//第一次进来时候 第一个价格必须上一个的封盘价
                            klineTickBean.setOpen(currentlyPrice);
                            klineTickBean.setLow(currentlyPrice);
                            klineTickBean.setHigh(currentlyPrice);
                            klineTickBean.setClose(currentlyPrice);
                            HashMap<String,String> priMap = new HashMap<>();
                            priMap.put(mm,"0");
                            KLoader.BOT_PRICE_MAP.put(coin,priMap);
                            KLoader.BOT_TIME_MAP.put(coin,mm);
                            KLoader.BOT_PRICE.put(coin,currentlyPrice);
                        }else if(!s.equals(mm)){
                            //不相等就是新的一分钟
                            //新的一分钟需要获取当时的开盘价。
                            klineTickBean.setLow(klineTickBean.getOpen());
                            klineTickBean.setHigh(klineTickBean.getOpen());
                            klineTickBean.setClose(klineTickBean.getOpen());
                            HashMap<String,String> priMap = new HashMap<>();
                            priMap.put(mm,"0");
                            KLoader.BOT_PRICE_MAP.put(coin,priMap);
                            KLoader.BOT_TIME_MAP.put(coin,mm);
                            KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                        }else{
                            //相等就是同一分钟内 要缓慢增加。
                            BigDecimal price = KLoader.BOT_PRICE.get(coin);
                            BigDecimal addPrice = price.add(mean);
                            Map<String, String> bpm = KLoader.BOT_PRICE_MAP.get(coin);
                            String flag = bpm.get(mm);
                            if(flag.equals("0")){
                                //给个最低价格
                                klineTickBean.setHigh(price);
                                klineTickBean.setClose(klineTickBean.getLow());
                                KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                bpm.put(mm,ss);
                            }else{
                                if(addPrice.compareTo(klineTickBean.getHigh())>0){
                                    klineTickBean.setHigh(klineTickBean.getHigh());
                                    klineTickBean.setClose(klineTickBean.getHigh());
                                    KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                }else if(addPrice.compareTo(klineTickBean.getLow())<0){
                                    klineTickBean.setClose(klineTickBean.getLow());
                                    klineTickBean.setLow(klineTickBean.getLow());
                                    KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                }else{
                                    klineTickBean.setHigh(addPrice);
                                    klineTickBean.setClose(addPrice);
                                    KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                }
                            }
                        }
                    }else{//跌
                        String s = KLoader.BOT_TIME_MAP.get(coin);
                        if(s.equals("0")){
                            //第一次进来时候 第一个价格必须上一个的封盘价
                            klineTickBean.setOpen(currentlyPrice);
                            klineTickBean.setLow(currentlyPrice);
                            klineTickBean.setHigh(currentlyPrice);
                            klineTickBean.setClose(currentlyPrice);
                            HashMap<String,String> priMap = new HashMap<>();
                            priMap.put(mm,"0");
                            KLoader.BOT_PRICE_MAP.put(coin,priMap);
                            KLoader.BOT_TIME_MAP.put(coin,mm);
                            KLoader.BOT_PRICE.put(coin,currentlyPrice);
                        }else if(!s.equals(mm)){
                            //不相等就是新的一分钟
                            // 新的一分钟需要获取当时的开盘价。
                            klineTickBean.setLow(klineTickBean.getOpen());
                            klineTickBean.setHigh(klineTickBean.getOpen());
                            klineTickBean.setClose(klineTickBean.getOpen());
                            HashMap<String,String> priMap = new HashMap<>();
                            priMap.put(mm,"0");
                            KLoader.BOT_PRICE_MAP.put(coin,priMap);
                            KLoader.BOT_TIME_MAP.put(coin,mm);
                            KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                        }else{
                            //相等就是同一分钟内 要缓慢减少。
                            BigDecimal price = KLoader.BOT_PRICE.get(coin);
                            BigDecimal subPrice = price.subtract(mean);
                            Map<String, String> bpm = KLoader.BOT_PRICE_MAP.get(coin);
                            String flag = bpm.get(mm);
                            if(flag.equals("0")){
                                //给个最高价格
                                klineTickBean.setLow(klineTickBean.getHigh());
                                klineTickBean.setClose(klineTickBean.getHigh());
                                KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                bpm.put(mm,ss);
                            }else{
                                if(subPrice.compareTo(klineTickBean.getLow())<0){
                                    klineTickBean.setClose(klineTickBean.getLow());
                                    klineTickBean.setLow(klineTickBean.getLow());
                                    KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                }else if(subPrice.compareTo(klineTickBean.getHigh())>0){
                                    klineTickBean.setHigh(klineTickBean.getHigh());
                                    klineTickBean.setClose(klineTickBean.getHigh());
                                    KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                }else{
                                    klineTickBean.setClose(subPrice);
                                    klineTickBean.setLow(subPrice);
                                    KLoader.BOT_PRICE.put(coin,klineTickBean.getClose());
                                }
                            }
                        }
                    }
                }
                klineDTO.setTick(klineTickBean);
                WsVO wsVO = new WsVO();
                wsVO.setType("KLINE");
                wsVO.setSymbol(cacheMapc.replace("bot-",""));
                wsVO.setData(klineDTO);
                toSendMessage(wsVO);
            }
        }
    }
    public String getYMDHM(Long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用 Date 类将时间戳转换为日期对象
        Date date = new Date(timestamp);
        // 使用 SimpleDateFormat 格式化日期对象为字符串
        String formattedDate = sdf.format(date);
        // 提取分钟（mm）和秒（ss）

        String mm = formattedDate.substring(0, 16);
        return  mm;
    }
    public String getMM(Long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用 Date 类将时间戳转换为日期对象
        Date date = new Date(timestamp);
        // 使用 SimpleDateFormat 格式化日期对象为字符串
        String formattedDate = sdf.format(date);
        // 提取分钟（mm）和秒（ss）

        String mm = formattedDate.substring(14, 16);
        return  mm;
    }
    public String getSS(Long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用 Date 类将时间戳转换为日期对象
        Date date = new Date(timestamp);
        // 使用 SimpleDateFormat 格式化日期对象为字符串
        String formattedDate = sdf.format(date);
        // 提取分钟（mm）和秒（ss）
        String ss = formattedDate.substring(17, 19);
        return  ss;
    }

    @Async("socketThread")
    public  void binanceDETAILSendMeg(String event){
        JSONObject jsonObject = JSONObject.parseObject(event);
        BinanceDTO klineDTO = new BinanceDTO();
        DetailTickBean klineTickBean = new DetailTickBean();
        klineDTO.setTs(jsonObject.getLong("E"));
        klineDTO.setCh(jsonObject.getString("e"));
        klineTickBean.setId(jsonObject.getLong("E"));
        WsVO wsVO = new WsVO();
        wsVO.setType("DETAIL");
        String coin = jsonObject.getString("s").toLowerCase().replace("usdt","");
        wsVO.setSymbol(coin);
        String conPriceAndId = redisCache.getCacheObject("con-" +coin);
        if(conPriceAndId!=null){
            HashMap<String, BigDecimal> stringBigDecimalHashMap =tBotKlineModelService.getyesterdayPrice();
            BigDecimal addPrice = stringBigDecimalHashMap.get(coin + "usdt");
            if(addPrice==null){
                addPrice=BigDecimal.ZERO;
            }

            klineTickBean.setHigh(new BigDecimal(jsonObject.getString("h")));
            klineTickBean.setLow(new BigDecimal(jsonObject.getString("l")));
            klineTickBean.setOpen(new BigDecimal(jsonObject.getString("o")).add(addPrice));
            klineTickBean.setClose(KLoader.BOT_PRICE.get(coin));
            if(klineTickBean.getHigh().compareTo(klineTickBean.getClose())<0){
                klineTickBean.setHigh(klineTickBean.getClose());
            }
            if(klineTickBean.getLow().compareTo(klineTickBean.getClose())>0){
                klineTickBean.setLow(klineTickBean.getClose());
            }
        }else {
            Map<String, Object> cacheMap = KLoader.BOT_MAP.get("bot-" + coin);
            if(cacheMap!=null&&cacheMap.size()>0){
                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime lastWholeMinute = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
                long timestamp = lastWholeMinute.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
                TBotKlineModelInfo tBotKlineModelInfo = new TBotKlineModelInfo();
                tBotKlineModelInfo.setModelId((Long)cacheMap.get("id"));
                tBotKlineModelInfo.setDateTime(timestamp);
                TBotKlineModelInfo tBotKlineModelInfo1 = tBotKlineModelInfoMapper.selectOne(new QueryWrapper<>(tBotKlineModelInfo));
                if(null!=tBotKlineModelInfo1&&!tBotKlineModelInfo1.getY().equals("0")){
                    BigDecimal botPrice = KLoader.BOT_PRICE.get(coin);
                    klineTickBean.setHigh(botPrice);
                    klineTickBean.setLow(botPrice);
                    klineTickBean.setOpen(new BigDecimal(jsonObject.getString("o")));
                    klineTickBean.setClose(botPrice);
                }else{
                    klineTickBean.setHigh(new BigDecimal(jsonObject.getString("h")));
                    klineTickBean.setLow(new BigDecimal(jsonObject.getString("l")));
                    klineTickBean.setOpen(new BigDecimal(jsonObject.getString("o")));
                    klineTickBean.setClose(new BigDecimal(jsonObject.getString("c")));
                }
            }else {
                klineTickBean.setHigh(new BigDecimal(jsonObject.getString("h")));
                klineTickBean.setLow(new BigDecimal(jsonObject.getString("l")));
                klineTickBean.setOpen(new BigDecimal(jsonObject.getString("o")));
                klineTickBean.setClose(new BigDecimal(jsonObject.getString("c")));
            }
        }
        klineTickBean.setVol(new BigDecimal(jsonObject.getString("v")));
        klineTickBean.setCount(new BigDecimal(jsonObject.getString("n")));
        klineTickBean.setAmount(new BigDecimal(jsonObject.getString("q")));
        klineDTO.setTick(klineTickBean);
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void mt5DETAILSendMeg(JSONObject event,String symbol){
        BinanceDTO klineDTO = new BinanceDTO();
        DetailTickBean klineTickBean = new DetailTickBean();
        klineDTO.setTs(event.getLong("timestamp")*1000);
        klineDTO.setCh("symbol");
        RedisCache redis = SpringContextUtil.getBean(RedisCache.class);
        klineTickBean.setId(event.getLong("timestamp"));
        klineTickBean.setHigh(event.getBigDecimal("high_price"));
        klineTickBean.setLow(event.getBigDecimal("low_price"));
        klineTickBean.setOpen(event.getBigDecimal("open_price"));
        klineTickBean.setClose(event.getBigDecimal("close_price"));
//        int min = 1000; // 定义随机数的最小值
//        int max = 100000; // 定义随机数的最大值
//        // 产生一个2~100的数
//        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(event.getBigDecimal("volume"));
        klineTickBean.setCount(event.getBigDecimal("turnover"));
        redis.setCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+symbol,klineTickBean.getClose());
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("DETAIL");
        wsVO.setSymbol(symbol);
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void mt5DETAILSendMeg(String event,String symbol){
        JSONObject jsonObject = JSONObject.parseObject(event);
        BinanceDTO klineDTO = new BinanceDTO();
        DetailTickBean klineTickBean = new DetailTickBean();
        JSONArray t = jsonObject.getJSONArray("t");
        JSONArray h = jsonObject.getJSONArray("h");
        JSONArray l = jsonObject.getJSONArray("l");
        JSONArray o = jsonObject.getJSONArray("o");
        JSONArray c = jsonObject.getJSONArray("c");
        if(t.size()<=0 ||h.size()<=0 ||l.size()<=0 ||o.size()<=0 ||c.size()<=0){
            return;
        }
        klineDTO.setTs(Long.parseLong((String) jsonObject.getJSONArray("t").get(0)));
        klineDTO.setCh("symbol");
        RedisCache redis = SpringContextUtil.getBean(RedisCache.class);
        klineTickBean.setId((Long.parseLong((String) jsonObject.getJSONArray("t").get(0))));
        klineTickBean.setHigh(new BigDecimal((String) jsonObject.getJSONArray("h").get(0)));
        klineTickBean.setLow(new BigDecimal((String) jsonObject.getJSONArray("l").get(0)));
        klineTickBean.setOpen(new BigDecimal((String) jsonObject.getJSONArray("o").get(0)));
        klineTickBean.setClose(new BigDecimal((String) jsonObject.getJSONArray("c").get(0)));
        int min = 1000; // 定义随机数的最小值
        int max = 100000; // 定义随机数的最大值
        // 产生一个2~100的数
        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(new BigDecimal(s));
        redis.setCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+symbol,klineTickBean.getClose());
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("DETAIL");
        wsVO.setSymbol(symbol);
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void metalDETAILSendMeg(String event,String symbol){
        LocalDateTime currentTime = LocalDateTime.now();
        // 将秒和毫秒部分设置为零，以获得当前整分的时间
        LocalDateTime currentMinute = currentTime.withSecond(0).withNano(0);
        JSONObject jsonObject = JSONObject.parseObject(event);
        BinanceDTO klineDTO = new BinanceDTO();
        DetailTickBean klineTickBean = new DetailTickBean();
        klineDTO.setTs(Long.parseLong((String) jsonObject.getJSONArray("t").get(0))*1000);
        klineDTO.setCh("symbol");
        RedisCache redis = SpringContextUtil.getBean(RedisCache.class);

        klineTickBean.setId((currentMinute.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        klineTickBean.setHigh(new BigDecimal((String) jsonObject.getJSONArray("h").get(0)));
        klineTickBean.setLow(new BigDecimal((String) jsonObject.getJSONArray("l").get(0)));
        klineTickBean.setOpen(new BigDecimal((String) jsonObject.getJSONArray("o").get(0)));
        klineTickBean.setClose(new BigDecimal((String) jsonObject.getJSONArray("c").get(0)));
        int min = 1000; // 定义随机数的最小值
        int max = 100000; // 定义随机数的最大值
        // 产生一个2~100的数
        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(new BigDecimal(s));
        redis.setCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+symbol,klineTickBean.getClose());
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("DETAIL");
        wsVO.setSymbol(symbol);
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void metalDETAILSendMeg(JSONObject event,String symbol){
        LocalDateTime currentTime = LocalDateTime.now();
        // 将秒和毫秒部分设置为零，以获得当前整分的时间
        LocalDateTime currentMinute = currentTime.withSecond(0).withNano(0);
        //JSONObject jsonObject = JSONObject.parseObject(event);
        BinanceDTO klineDTO = new BinanceDTO();
        DetailTickBean klineTickBean = new DetailTickBean();
        klineDTO.setTs(event.getLong("timestamp")*1000);
        klineDTO.setCh("symbol");
        RedisCache redis = SpringContextUtil.getBean(RedisCache.class);

        klineTickBean.setId((currentMinute.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        klineTickBean.setHigh(event.getBigDecimal("high_price"));
        klineTickBean.setLow(event.getBigDecimal("low_price"));
        klineTickBean.setOpen(event.getBigDecimal("open_price"));
        klineTickBean.setClose(event.getBigDecimal("close_price"));
//        int min = 1000; // 定义随机数的最小值
//        int max = 100000; // 定义随机数的最大值
//        // 产生一个2~100的数
//        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(event.getBigDecimal("volume"));
        klineTickBean.setAmount(event.getBigDecimal("turnover"));
        redis.setCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+symbol,klineTickBean.getClose());
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("DETAIL");
        wsVO.setSymbol(symbol);
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void mt5KlineSendMeg(JSONObject event,String symbol){
        BinanceDTO klineDTO = new BinanceDTO();
        KlineTickBean klineTickBean = new KlineTickBean();
        klineDTO.setTs(event.getLong("timestamp"));
        klineDTO.setCh(symbol);

        klineTickBean.setId(event.getLong("timestamp"));
        klineTickBean.setHigh(event.getBigDecimal("high_price"));
        klineTickBean.setLow(event.getBigDecimal("low_price"));
        klineTickBean.setOpen(event.getBigDecimal("open_price"));
        klineTickBean.setClose(event.getBigDecimal("close_price"));
//        int min = 1000; // 定义随机数的最小值
//        int max = 100000; // 定义随机数的最大值
//        // 产生一个2~100的数
//        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(event.getBigDecimal("volume"));
        klineTickBean.setAmount(event.getBigDecimal("turnover"));
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("KLINE");
        wsVO.setSymbol(symbol);
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void mt5KlineSendMeg(String event,String symbol){
        if(null==event||event.equals("")){
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(event);
        if(jsonObject.getJSONArray("t").size()<1){
            return;
        }
        BinanceDTO klineDTO = new BinanceDTO();
        KlineTickBean klineTickBean = new KlineTickBean();
        klineDTO.setTs(Long.parseLong((String) jsonObject.getJSONArray("t").get(0)));
        klineDTO.setCh(symbol);

        klineTickBean.setId((Long.parseLong((String) jsonObject.getJSONArray("t").get(0))));
        klineTickBean.setHigh(new BigDecimal((String) jsonObject.getJSONArray("h").get(0)));
        klineTickBean.setLow(new BigDecimal((String) jsonObject.getJSONArray("l").get(0)));
        klineTickBean.setOpen(new BigDecimal((String) jsonObject.getJSONArray("o").get(0)));
        klineTickBean.setClose(new BigDecimal((String) jsonObject.getJSONArray("c").get(0)));
        int min = 1000; // 定义随机数的最小值
        int max = 100000; // 定义随机数的最大值
        // 产生一个2~100的数
        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(new BigDecimal(s));
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("KLINE");
        wsVO.setSymbol(symbol.toLowerCase());
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void metalKlineSendMeg(String event,String symbol){
        if(null==event||event.equals("")){
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(event);
        if(jsonObject.getJSONArray("t").size()<1){
            return;
        }
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 将秒和毫秒部分设置为零，以获得当前整分的时间
        LocalDateTime currentMinute = currentTime.withSecond(0).withNano(0);

        BinanceDTO klineDTO = new BinanceDTO();
        KlineTickBean klineTickBean = new KlineTickBean();
        klineDTO.setTs(Long.parseLong((String) jsonObject.getJSONArray("t").get(0))*1000);
        klineDTO.setCh(symbol);
        klineTickBean.setId((currentMinute.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        klineTickBean.setHigh(new BigDecimal((String) jsonObject.getJSONArray("h").get(0)));
        klineTickBean.setLow(new BigDecimal((String) jsonObject.getJSONArray("l").get(0)));
        klineTickBean.setOpen(new BigDecimal((String) jsonObject.getJSONArray("o").get(0)));
        klineTickBean.setClose(new BigDecimal((String) jsonObject.getJSONArray("c").get(0)));
        int min = 1000; // 定义随机数的最小值
        int max = 100000; // 定义随机数的最大值
        // 产生一个2~100的数
        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(new BigDecimal(s));
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("KLINE");
        wsVO.setSymbol(symbol.toLowerCase());
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void metalKlineSendMeg(JSONObject event,String symbol){
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 将秒和毫秒部分设置为零，以获得当前整分的时间
        LocalDateTime currentMinute = currentTime.withSecond(0).withNano(0);

        BinanceDTO klineDTO = new BinanceDTO();
        KlineTickBean klineTickBean = new KlineTickBean();
        klineDTO.setTs(event.getLong("timestamp")*1000);
        klineDTO.setCh(symbol);
        klineTickBean.setId((currentMinute.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        klineTickBean.setHigh(event.getBigDecimal("high_price"));
        klineTickBean.setLow(event.getBigDecimal("low_price"));
        klineTickBean.setOpen(event.getBigDecimal("open_price"));
        klineTickBean.setClose(event.getBigDecimal("close_price"));
//        int min = 1000; // 定义随机数的最小值
//        int max = 100000; // 定义随机数的最大值
//        // 产生一个2~100的数
//        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(event.getBigDecimal("volume"));
        klineTickBean.setAmount(event.getBigDecimal("turnover"));
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("KLINE");
        wsVO.setSymbol(symbol.toLowerCase());
        wsVO.setData(klineDTO);
        toSendMessage(wsVO);
    }
    @Async("socketThread")
    public  void binanceTRADESendMeg(String event){
        WsVO wsVO = new WsVO();
        ArrayList<TradeDataBean> dataBeans = new ArrayList<>();
        TradeTickBean tradeTickBean = new TradeTickBean();
        TradeDataBean tradeDataBean = new TradeDataBean();
        BinanceDTO tradeDTO = new BinanceDTO();
        JSONObject jsonObject = JSONObject.parseObject(event);
        Long ts = jsonObject.getLong("E");
        String coin = jsonObject.getString("s").toLowerCase().replace("usdt","");
        BigDecimal price = new BigDecimal(jsonObject.getString("p"));
        //业务数据
        wsVO.setType("TRADE");
        wsVO.setSymbol(coin);
        tradeDTO.setTs(ts);
        tradeDTO.setCh(jsonObject.getString("e"));
        tradeTickBean.setId(ts);
        tradeTickBean.setTs(ts);
        tradeDataBean.setTs(ts);
        tradeDataBean.setAmount(new BigDecimal(jsonObject.getString("q")));
        tradeDataBean.setDirection(jsonObject.getBoolean("m")?"sell":"buy");
        tradeDataBean.setPrice(price);
        String conPriceAndId = redisCache.getCacheObject("con-" +coin);
        //是否有控制
        if(conPriceAndId!=null){
            String[] split = conPriceAndId.split(",");
            tradeDataBean.setPrice(price.add(new BigDecimal(split[0])));
        }else{
            //所有控线走btc的频率,也就是说如果控线了,处理btc的同时,也会处理被控制的币种.
            if(coin.equals("bnb")) {
                conTradeKline(tradeDataBean,tradeDTO,tradeTickBean);
                wsVO.setSymbol(coin);
                tradeDataBean.setAmount(new BigDecimal(jsonObject.getString("q")));
                tradeDataBean.setDirection(jsonObject.getBoolean("m")?"sell":"buy");
                tradeDataBean.setPrice(price);
                if(KLoader.BOT_MAP.containsKey("bot-"+"bnb")){
                    return;
                }
            }else {
                if(KLoader.BOT_MAP.containsKey("bot-"+coin)){
                    Map<String, Object> cacheMap = KLoader.BOT_MAP.get("bot-"+coin);
                    //因为btc 处理了 别的k先数据 ，如果不等于null  或者 Y不等于0直接返回 不处理
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    LocalDateTime lastWholeMinute = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
                    long timestamp = lastWholeMinute.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
                    TBotKlineModelInfo tBotKlineModelInfo = new TBotKlineModelInfo();
                    tBotKlineModelInfo.setModelId((Long)cacheMap.get("id"));
                    tBotKlineModelInfo.setDateTime(timestamp);
                    TBotKlineModelInfo tBotKlineModelInfo1=tBotKlineModelInfoMapper.selectOne(new QueryWrapper<>(tBotKlineModelInfo));
                    if(tBotKlineModelInfo1!=null&&!tBotKlineModelInfo1.getY().equals("0")){
                        //控线不处理  因为走的btc的  在处理会导致价格不一致
                        return;
                    }
                }

            }
        }
        dataBeans.add(tradeDataBean);
        tradeTickBean.setData(dataBeans);
        tradeDTO.setTick(tradeTickBean);
        wsVO.setData(tradeDTO);
        toSendMessage(wsVO);
    }

    @Async("socketThread")
    public  void crudeKlineSendMeg(String event,String symbol) {
        if (StringUtils.isEmpty(event)) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(event);
        JSONArray tArray = jsonObject.getJSONArray("t");
        if (tArray == null || tArray.isEmpty()) {
            return;
        }
        // 获取当前时间 将秒和毫秒部分设置为零，以获得当前整分的时间
        LocalDateTime currentMinute = LocalDateTime.now().withSecond(0).withNano(0);
        BinanceDTO klineDTO = new BinanceDTO();
        KlineTickBean klineTickBean = new KlineTickBean();
        klineDTO.setTs(Long.parseLong((String) tArray.get(0)) * 1000);
        klineDTO.setCh(symbol);
        klineTickBean.setId((currentMinute.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        klineTickBean.setHigh(new BigDecimal((String) jsonObject.getJSONArray("h").get(0)));
        klineTickBean.setLow(new BigDecimal((String) jsonObject.getJSONArray("l").get(0)));
        klineTickBean.setOpen(new BigDecimal((String) jsonObject.getJSONArray("o").get(0)));
        klineTickBean.setClose(new BigDecimal((String) jsonObject.getJSONArray("c").get(0)));
        klineTickBean.setConPrice(new BigDecimal((String) jsonObject.getJSONArray("se").get(0)));
        int min = 1000; // 定义随机数的最小值
        int max = 100000; // 定义随机数的最大值
        // 产生一个2~100的数
        int s = (int) min + (int) (Math.random() * (max - min));
        klineTickBean.setVol(new BigDecimal(s));
        klineDTO.setTick(klineTickBean);
        WsVO wsVO = new WsVO();
        wsVO.setType("KLINE");
        wsVO.setSymbol(symbol.toLowerCase());
        wsVO.setData(klineDTO);
        log.info(JSONObject.toJSONString(wsVO));
        toSendMessage(wsVO);
    }

    private void conTradeKline(TradeDataBean tradeDataBean,BinanceDTO tradeDTO , TradeTickBean tradeTickBean){
        for (String  cacheMapc: KLoader.BOT_MAP.keySet()) {
            LocalDateTime currentTime = LocalDateTime.now();
            // 将秒和毫秒部分设置为零，以获得当前整分的时间
            LocalDateTime currentMinute = currentTime.withSecond(0).withNano(0);
            //获取当前秒数
            int currentSecond = currentTime.getSecond();
            int secondsToNextMinute = 60 - currentSecond;
            //时间剩余5秒不跳了 保证k线与控线一致。
            if(secondsToNextMinute<5){
                return;
            }
            WsVO wsVO = new WsVO();
            wsVO.setType("TRADE");
            ArrayList<TradeDataBean> dataBeans = new ArrayList<>();
            HashMap cacheMap = KLoader.BOT_MAP.get(cacheMapc);
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime lastWholeMinute = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
            long timestamp = lastWholeMinute.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            TBotKlineModelInfo tBotKlineModelInfo = new TBotKlineModelInfo();
            tBotKlineModelInfo.setModelId((Long)cacheMap.get("id"));
            tBotKlineModelInfo.setDateTime(timestamp);
            TBotKlineModelInfo info = tBotKlineModelInfoMapper.selectOne(new QueryWrapper<>(tBotKlineModelInfo));
            if(null!=info&&!info.getY().equals("0")) {
                BigDecimal botPrice = KLoader.BOT_PRICE.get(cacheMapc.replace("bot-",""));
                if(botPrice!=null){
                    tradeDataBean.setPrice(botPrice);
                }else {
                    return;
                }
            }else{
                return;
            }
            dataBeans.add(tradeDataBean);
            tradeTickBean.setData(dataBeans);
            tradeDTO.setTick(tradeTickBean);
            wsVO.setSymbol(cacheMapc.replace("bot-",""));
            wsVO.setData(tradeDTO);
            toSendMessage(wsVO);
        }
    }




    // 获取小数位个数
    public static int getNumberDecimalDigits(String number) {
        if (!number.contains(".")) {
            return 0;
        }
        return number.length() - (number.indexOf(".") + 1);
    }
    private static final String EU_BANK_XML_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    @Test
    public void addition_isCorrect() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(EU_BANK_XML_URL).build();
       Response response = client.newCall(request).execute();
        if (response != null && response.body() != null) {
            String responceString = response.body().string();
            assertNotNull(responceString);
        }
    }

    public void savePriceRdies(String event) {
        JSONObject jsonObject = JSONObject.parseObject(event);
        RedisCache redis = redisCache;
        BigDecimal decimal =new BigDecimal(jsonObject.getString("c"));
        BigDecimal openPrice =new BigDecimal(jsonObject.getString("o"));
        String s = jsonObject.getString("s").toLowerCase();
        String symbol = s.replace("usdt","");
        redis.setCacheObject(CachePrefix.CURRENCY_CLOSE_PRICE.getPrefix()+symbol,decimal);
        redis.setCacheObject(CachePrefix.CURRENCY_OPEN_PRICE.getPrefix()+symbol,openPrice);
    }
}
