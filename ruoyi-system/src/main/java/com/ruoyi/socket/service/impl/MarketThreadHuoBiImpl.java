package com.ruoyi.socket.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.wss.event.MarketDetailSubResponse;
import com.huobi.wss.event.MarketKLineSubResponse;
import com.huobi.wss.event.MarketTradeDetailSubResponse;
import com.huobi.wss.handle.WssMarketHandle;
import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.domain.TCurrencySymbol;
import com.ruoyi.bussiness.domain.TSecondCoinConfig;
import com.ruoyi.bussiness.domain.TSymbolManage;
import com.ruoyi.bussiness.service.ITContractCoinService;
import com.ruoyi.bussiness.service.ITCurrencySymbolService;
import com.ruoyi.bussiness.service.ITSecondCoinConfigService;
import com.ruoyi.bussiness.service.ITSymbolManageService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.socket.service.MarketThread;
import com.ruoyi.socket.manager.WebSocketUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Slf4j
public class MarketThreadHuoBiImpl implements MarketThread {

    @Autowired
    private RedisCache redisCache;
    @Resource
    private ITSecondCoinConfigService secondCoinConfigService;
    @Resource
    private ITContractCoinService contractCoinService;
    @Resource
    private ITCurrencySymbolService tCurrencySymbolService;
    @Resource
    private ITSymbolManageService tSymbolManageService;
    @Resource
    private WebSocketUserManager webSocketUserManager;
    private String URL = "wss://api.hbdm.com/linear-swap-ws";//合约站行情请求以及订阅地址

    @Override
    public void marketThreadRun()  {

        try {
            Set<String> strings  = new HashSet<>();

        //秒合约
            TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
            tSecondCoinConfig.setMarket("huobi");
            tSecondCoinConfig.setStatus(1L);
            List<TSecondCoinConfig> tSecondCoinConfigs = secondCoinConfigService.selectTSecondCoinConfigList(tSecondCoinConfig);
            for (TSecondCoinConfig secondCoinConfig : tSecondCoinConfigs) {
                strings.add(secondCoinConfig.getSymbol());
            }
            //U本位
            TContractCoin tContractCoin =new TContractCoin();
            tContractCoin.setEnable(0L);
            tContractCoin.setMarket("huobi");
            List<TContractCoin> tContractCoins = contractCoinService.selectTContractCoinList(tContractCoin);
            for (TContractCoin contractCoin : tContractCoins) {
                strings.add(contractCoin.getSymbol());
            }
            //币币
            TCurrencySymbol tCurrencySymbol = new TCurrencySymbol();
            tCurrencySymbol.setEnable("1");
            tCurrencySymbol.setMarket("huobi");
            List<TCurrencySymbol> tCurrencySymbols = tCurrencySymbolService.selectTCurrencySymbolList(tCurrencySymbol);
            for (TCurrencySymbol currencySymbol : tCurrencySymbols) {
                strings.add(currencySymbol.getSymbol());
            }
            //兑换
            TSymbolManage tSymbolManage = new TSymbolManage();
            tSymbolManage.setEnable("1");
            tCurrencySymbol.setMarket("huobi");
            List<TSymbolManage> tSymbolManages = tSymbolManageService.selectTSymbolManageList(tSymbolManage);
            for (TSymbolManage symbolManage : tSymbolManages) {
                strings.add(symbolManage.getSymbol()+"usdt");
            }
            WssMarketHandle klinesMarketHandle = new WssMarketHandle(URL);
            List<String> subList = new ArrayList<>(strings);
            klinesMarketHandle.sub(subList, response -> {
                log.debug(response);
                //要写分发逻辑 和  控线逻辑  有三个 数据结构  1detail  2kline  3trade
                JSONObject parse = (JSONObject) JSONObject.parse(response);
                String o = (String)parse.get("ch");
                //判断属于哪种类型
                if(o.indexOf("trade")>-1){
                    //trade
                    MarketTradeDetailSubResponse event = JSON.parseObject(response, MarketTradeDetailSubResponse.class);
                    webSocketUserManager.buidlMsgToSend(event);

                }else if(o.indexOf("kline")>-1){
                    //kline
                    MarketKLineSubResponse event = JSON.parseObject(response, MarketKLineSubResponse.class);
                    webSocketUserManager.buidlMsgToSend(event);
                }else{
                    //detail
                    MarketDetailSubResponse event = JSON.parseObject(response, MarketDetailSubResponse.class);
                    webSocketUserManager.buidlMsgToSend(event);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllPrice() {

    }

    @Override
    public void initMarketThreadRun() {

    }

}
