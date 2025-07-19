package com.ruoyi.task;

import com.alibaba.fastjson2.JSONObject;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.binance.connector.client.utils.httpclient.WebSocketStreamHttpClientSingleton;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.socket.manager.BianaceWebSocketClient;
import com.ruoyi.socket.manager.WebSocketUserManager;
import com.ruoyi.socket.service.impl.MarketThreadBinanceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 查询地址的usdt余额定时任务。
 */
@Component
@Slf4j
public class BianaceWSTask {

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
    @Scheduled(cron = "0 0 1 * * ?")
    public void queryAddreUsdt(){
      /*  log.info("全部断开");
       try {
           log.info("重连");
           BianaceWebSocketClient klineClient = new BianaceWebSocketClient();
           BianaceWebSocketClient aggTradeClient = new BianaceWebSocketClient();
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
               }));
               //对应trade
               aggTradeClient.aggTradeStream(string, ((event) -> {
                   webSocketUserManager.binanceTRADESendMeg(event);
                   String s =  event;
                   for (KlineSymbol kSymbol : list) {
                       if((string.toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                           event = ownTrade(s, kSymbol);
                           webSocketUserManager.binanceTRADESendMeg(event);
                       }
                   }
               }));
               //对应detail
               symbolTickerClient.symbolTicker(string, ((event) -> {
                   String s =  event;
                   webSocketUserManager.binanceDETAILSendMeg(event);
                   for (KlineSymbol kSymbol : list) {
                       if((string.toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                           event= ownDetail(s,kSymbol);
                           webSocketUserManager.binanceDETAILSendMeg(event);
                       }
                   }
               }));
           }
           MarketThreadBinanceImpl.klineClient  = klineClient;
           MarketThreadBinanceImpl.aggTradeClient  = aggTradeClient;
           MarketThreadBinanceImpl.symbolTickerClient = symbolTickerClient;
           log.info("重连结束");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
        k.put("o",o.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("h",h.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("l",l.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("c",c.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("q",q.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("k",k);
        return jsonObject.toJSONString();
    }
}
