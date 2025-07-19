package com.ruoyi.socket.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.DateUtil;
import com.ruoyi.socket.manager.BianaceWebSocketClient;
import com.ruoyi.socket.manager.WebSocketUserManager;
import com.ruoyi.socket.service.MarketThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.*;


@Service
@Slf4j
public class MarketThreadBinanceImpl implements MarketThread {


    @Resource
    private ITSecondCoinConfigService secondCoinConfigService;
    @Resource
    private ITContractCoinService contractCoinService;
    @Resource
    private ITCurrencySymbolService tCurrencySymbolService;
    @Resource
    private ITSymbolManageService tSymbolManageService;
    @Resource
    private IKlineSymbolService klineSymbolService;

    @Resource
    private WebSocketUserManager webSocketUserManager;
    @Resource
    private ITOwnCoinService ownCoinService;

    @Override
    public void marketThreadRun(){
        try {
            BianaceWebSocketClient klineClient = new BianaceWebSocketClient();
            Set<String> strings = new HashSet<>();
            //秒合约
            TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
            tSecondCoinConfig.setMarket("binance");
            tSecondCoinConfig.setStatus(1L);
            List<TSecondCoinConfig> tSecondCoinConfigs = secondCoinConfigService.selectTSecondCoinConfigList(tSecondCoinConfig);
            for (TSecondCoinConfig secondCoinConfig : tSecondCoinConfigs) {
                strings.add(secondCoinConfig.getSymbol());
            }
            //U本位
            TContractCoin tContractCoin =new TContractCoin();
            tContractCoin.setEnable(0L);
            tContractCoin.setMarket("binance");
            List<TContractCoin> tContractCoins = contractCoinService.selectTContractCoinList(tContractCoin);
            for (TContractCoin contractCoin : tContractCoins) {
                strings.add(contractCoin.getSymbol());
            }
            //币币
            TCurrencySymbol tCurrencySymbol = new TCurrencySymbol();
            tCurrencySymbol.setEnable("1");
            tCurrencySymbol.setMarket("binance");
            List<TCurrencySymbol> tCurrencySymbols = tCurrencySymbolService.selectTCurrencySymbolList(tCurrencySymbol);
            for (TCurrencySymbol currencySymbol : tCurrencySymbols) {
                strings.add(currencySymbol.getSymbol());
            }

            //兑换
            TSymbolManage tSymbolManage = new TSymbolManage();
            tSymbolManage.setEnable("1");
            tCurrencySymbol.setMarket("binance");
            List<TSymbolManage> tSymbolManages = tSymbolManageService.selectTSymbolManageList(tSymbolManage);
            for (TSymbolManage symbolManage : tSymbolManages) {
                strings.add(symbolManage.getSymbol()+"usdt");
            }
            //发币  和 自发币
            KlineSymbol klineSymbol = new KlineSymbol().setMarket("echo");
            List<KlineSymbol> list = klineSymbolService.selectKlineSymbolList(klineSymbol);
            for (KlineSymbol kline : list) {
                strings.add(kline.getReferCoin()+"usdt");
            }
            for (String string : strings) {
                klineClient.klineStream(string, "1m", ((event) -> {
                    webSocketUserManager.binanceKlineSendMeg(event);
                    String s =  event;
                    //kline 逻辑  分发 和 控币   异步
                    for (KlineSymbol kSymbol : list) {
                        if((string.toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                            event = ownKline(s, kSymbol);
                            webSocketUserManager.binanceKlineSendMeg(event);
                        }
                    }

                    s = createEvent(s);
                    webSocketUserManager.binanceTRADESendMeg(s);
                    for (KlineSymbol kSymbol : list) {
                        if((string.toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                            event = ownTrade(s, kSymbol);
                            webSocketUserManager.binanceTRADESendMeg(event);
                        }
                    }

                }));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createEvent(String event) {
        JSONObject jsonObject = JSONObject.parseObject(event);
        String k = jsonObject.getString("k");
        JSONObject K = JSONObject.parseObject(k);

        String s = jsonObject.getString("s");
        String E = jsonObject.getString("E");
        String T = K.getString("T");

        BigDecimal p =new  BigDecimal(K.getString("c"));
        BigDecimal q = null;
        // 根据价格 生成随机成交数量  0-1   50--2000    价格 1-10  200以内   价格 10-50     0-20    价格50-5000    0.00005000  以内随机数    5000+以上    0.000005000
        Integer randomBoolean=new Random().nextInt(2);  //这儿是生成的小于100的整数，nextInt方法的参数值要是大于0的整数
        Boolean m = randomBoolean>0?true:false;
        // 产生一个2~100的数
        if(p.compareTo(BigDecimal.ONE)<0){
            int min = 50; // 定义随机数的最小值
            int max = 2000; // 定义随机数的最大值
            Integer random = (int) min + (int) (Math.random() * (max - min));
            q=new BigDecimal(random.toString());
        }
        if(p.compareTo(new BigDecimal("1"))>0 &&  p.compareTo(new BigDecimal("10"))<0){
            int max = 2000; // 定义随机数的最大值
            Integer random =  (int) (Math.random() * (max));
            q=new BigDecimal(random.toString());
        }
        if(p.compareTo(new BigDecimal("10"))>0 &&  p.compareTo(new BigDecimal("50"))<0){
            int max = 20; // 定义随机数的最大值
            Integer random =  + (int) (Math.random() * (max));
            q=new BigDecimal(random.toString());
        }
        if(p.compareTo(new BigDecimal("50"))>0 &&  p.compareTo(new BigDecimal("5000"))<0){
            int min = 50; // 定义随机数的最小值
            int max = 5000; // 定义随机数的最大值
            Integer random = (int) min + (int) (Math.random() * (max - min));
            q=new BigDecimal(random.toString()).divide(new BigDecimal("1000000"));
        }
        if(p.compareTo(new BigDecimal("5000"))>0 ){
            int min = 50; // 定义随机数的最小值
            int max = 5000; // 定义随机数的最大值
            Integer random = (int) min + (int) (Math.random() * (max - min));
            q=new BigDecimal(random.toString()).divide(new BigDecimal("10000000"));
        }
        String str = "{'e':'aggTrade','E':'"+E+"','s':'"+s+"','p':'"+p+"','q':'"+q.toString()+"','T':'"+T+"','m':'"+m+"'}";
        return str;
    }

    @Override
    public void getAllPrice() {
    }

    @Override
    public void initMarketThreadRun() {
        BianaceWebSocketClient symbolTickerClient =  new BianaceWebSocketClient();
        Set<String> strings = new HashSet<>();
        //秒合约
        TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
        tSecondCoinConfig.setMarket("binance");
        tSecondCoinConfig.setStatus(1L);
        List<TSecondCoinConfig> tSecondCoinConfigs = secondCoinConfigService.selectTSecondCoinConfigList(tSecondCoinConfig);
        for (TSecondCoinConfig secondCoinConfig : tSecondCoinConfigs) {
            strings.add(secondCoinConfig.getSymbol());
        }
        //U本位
        TContractCoin tContractCoin =new TContractCoin();
        tContractCoin.setEnable(0L);
        tContractCoin.setMarket("binance");
        List<TContractCoin> tContractCoins = contractCoinService.selectTContractCoinList(tContractCoin);
        for (TContractCoin contractCoin : tContractCoins) {
            strings.add(contractCoin.getSymbol());
        }
        //币币
        TCurrencySymbol tCurrencySymbol = new TCurrencySymbol();
        tCurrencySymbol.setEnable("1");
        tCurrencySymbol.setMarket("binance");
        List<TCurrencySymbol> tCurrencySymbols = tCurrencySymbolService.selectTCurrencySymbolList(tCurrencySymbol);
        for (TCurrencySymbol currencySymbol : tCurrencySymbols) {
            strings.add(currencySymbol.getSymbol());
        }

        //兑换
        TSymbolManage tSymbolManage = new TSymbolManage();
        tSymbolManage.setEnable("1");
        tCurrencySymbol.setMarket("binance");
        List<TSymbolManage> tSymbolManages = tSymbolManageService.selectTSymbolManageList(tSymbolManage);
        for (TSymbolManage symbolManage : tSymbolManages) {
            strings.add(symbolManage.getSymbol()+"usdt");
        }
        //发币  和 自发币
        KlineSymbol klineSymbol = new KlineSymbol().setMarket("echo");
        List<KlineSymbol> list = klineSymbolService.selectKlineSymbolList(klineSymbol);
        for (KlineSymbol kline : list) {
            strings.add(kline.getReferCoin()+"usdt");
        }
        //对应detail
        symbolTickerClient.allTickerStream(((allEvent) -> {
            JSONArray arr = JSONArray.parseArray(allEvent);
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = JSON.parseObject(arr.get(i).toString());
                String event = obj.toString();
                webSocketUserManager.savePriceRdies(event);
                if(strings.contains(obj.getString("s").toLowerCase())){
                    webSocketUserManager.binanceDETAILSendMeg(event);
                    String s = event;
                    for (KlineSymbol kSymbol : list) {
                        if((obj.getString("s").toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                            event= ownDetail(s,kSymbol);
                            webSocketUserManager.binanceDETAILSendMeg(event);
                        }
                    }
                }
            }
        }));
    }

    private String ownDetail(String event,KlineSymbol kSymbol) {
     JSONObject jsonObject = JSONObject.parseObject(event);
        BigDecimal proportion = kSymbol.getProportion();
        if(proportion.compareTo(BigDecimal.ZERO)==0){
            proportion=new BigDecimal("100");
        }
        BigDecimal o = new BigDecimal(jsonObject.getString("o"));
        BigDecimal h = new BigDecimal(jsonObject.getString("h"));
        BigDecimal l = new BigDecimal(jsonObject.getString("l"));
        BigDecimal c = new BigDecimal(jsonObject.getString("c"));
        BigDecimal q = new BigDecimal(jsonObject.getString("q"));
        jsonObject.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        jsonObject.put("o",o.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("h",h.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("l",l.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("c",c.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("q",q.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        return jsonObject.toJSONString();
    }

    private String ownTrade(String event,KlineSymbol kSymbol ) {
        BigDecimal proportion = kSymbol.getProportion();
        if(proportion.compareTo(BigDecimal.ZERO)==0){
            proportion=new BigDecimal("100");
        }
        JSONObject jsonObject = JSONObject.parseObject(event);
        BigDecimal p = new BigDecimal(jsonObject.getString("p"));
        jsonObject.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        jsonObject.put("p",p.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        return jsonObject.toJSONString();
    }


    private String ownKline(String event,KlineSymbol kSymbol) {
        BigDecimal proportion = kSymbol.getProportion();
        if(proportion.compareTo(BigDecimal.ZERO)==0){
            proportion=new BigDecimal("100");
        }
        JSONObject jsonObject = JSONObject.parseObject(event);
        JSONObject k = jsonObject.getJSONObject("k");
        BigDecimal o = new BigDecimal(k.getString("o"));
        BigDecimal h = new BigDecimal(k.getString("h"));
        BigDecimal l = new BigDecimal(k.getString("l"));
        BigDecimal c = new BigDecimal(k.getString("c"));
        BigDecimal q = new BigDecimal(k.getString("q"));
        jsonObject.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        k.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        k.put("o",o.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("h",h.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("l",l.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("c",c.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("q",q.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("k",k);
        return jsonObject.toJSONString();
    }


}
