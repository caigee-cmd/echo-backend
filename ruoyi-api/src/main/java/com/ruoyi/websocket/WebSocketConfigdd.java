package com.ruoyi.websocket;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.socket.manager.BianaceWebSocketClient;
import com.ruoyi.socket.manager.WebSocketUserManager;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class WebSocketConfigdd {

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

    public WebSocketSubscriber webSocketSubscriberKline() {
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
        StringBuffer  sb = new StringBuffer();
        for (String coin: strings) {
            sb=sb.append(coin).append("@kline_1m/");
        }
        String substring = sb.substring(0, sb.length() - 1);
        System.out.println("substring:"+substring);


        URI socket = URI.create("wss://stream.binance.com:9443/ws/"+substring);
        // URI socket = URI.create("wss://data-stream.binance.vision/ws/"+substring);

        //int port = socket.getPort();
        //System.out.println("Port: " + port);

        return new WebSocketSubscriber(socket, new Draft_6455(),list,webSocketUserManager,strings);
    }

    public WebSocketSubscriber webSocketSubscriberDetail() {
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

        URI socket =URI.create("wss://stream.binance.com:9443/ws/!ticker@arr");
        // URI socket =URI.create("wss://data-stream.binance.vision/ws/!ticker@arr");

        return new WebSocketSubscriber(socket, new Draft_6455(),list,webSocketUserManager,strings);
    }
}
