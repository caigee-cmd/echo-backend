package com.ruoyi.socket.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.domain.TCurrencySymbol;
import com.ruoyi.bussiness.domain.TSecondCoinConfig;
import com.ruoyi.bussiness.service.ITContractCoinService;
import com.ruoyi.bussiness.service.ITCurrencySymbolService;
import com.ruoyi.bussiness.service.ITSecondCoinConfigService;
import com.ruoyi.bussiness.service.ITSymbolManageService;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.socket.manager.WebSocketUserManager;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Component
public class MarketThreadMetal {

    @Value("${app.name}")
    private String clientName;
    @Resource
    private ITSecondCoinConfigService secondCoinConfigService;
    @Resource
    private ITContractCoinService contractCoinService;
    @Resource
    private ITCurrencySymbolService tCurrencySymbolService;
    @Resource
    private WebSocketUserManager webSocketUserManager;
    @Resource
    private ISysDictTypeService sysDictTypeService;
    @Async
    @Scheduled(cron = "*/10 * * * * ?")
    public void marketThreadRun() throws URISyntaxException {
        if (Objects.equals(clientName, "echo2")){
            Set<String> strings = new HashSet<>();
            getGJSString(strings);
            getWHString(strings);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info("开始获取行情数据，合计{}个", strings.size());
                    JSONArray jsonArray = new JSONArray();
                    //兑换
                    for (String string : strings) {
                        String cion_name = toCion(string);
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("code",cion_name);
                        jsonObject1.put("kline_type",8);
                        jsonObject1.put("kline_timestamp_end",0);
                        jsonObject1.put("query_kline_num",1);
                        jsonObject1.put("adjust_type",0);
                        jsonArray.add(jsonObject1);

                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("code",cion_name);
                        jsonObject2.put("kline_type",1);
                        jsonObject2.put("kline_timestamp_end",0);
                        jsonObject2.put("query_kline_num",1);
                        jsonObject2.put("adjust_type",0);
                        jsonArray.add(jsonObject2);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("trace", IdUtil.randomUUID());
                    jsonObject.putObject("data").put("data_list", jsonArray);
                    String allToken = "0a67cc1e7f97f901700c76d95b4a74b3-c-app";
                    String url = "https://quote.tradeswitcher.com/quote-b-api/batch-kline?token=" + allToken;
//                    String url = "https://quote.tradeswitcher.com/quote-b-api/batch-kline?token=alltick_token";
                    String result = HttpRequest.post(url)
                            .contentType("application/json")
                            .body(jsonObject.toJSONString())
                            .timeout(5000)
                            .execute().body();
                    jsonObject = JSONObject.parseObject(result);
                    log.info("行情数据获取完成:{}", jsonObject);
                    JSONArray datas = jsonObject.getJSONObject("data").getJSONArray("kline_list");
                    for (int i = 0; i < datas.size(); i++) {
                        JSONObject data = datas.getJSONObject(i);
                        String cion_name = toCion(data.getString("code"));
                        if (StrUtil.isBlank(cion_name)) {
                            continue;
                        }
                        String klineType = data.getString("kline_type");
                        if (StrUtil.isBlank(klineType)) {
                            continue;
                        }
                        if (klineType.equals("1")) {
                            if (cion_name.length() > 3) {
                                webSocketUserManager.mt5KlineSendMeg(data.getJSONArray("kline_data").getJSONObject(0),cion_name);
                            }else {
                                webSocketUserManager.metalKlineSendMeg(data.getJSONArray("kline_data").getJSONObject(0),cion_name);
                            }
                        }else {
                            if (cion_name.length() > 3) {
                                webSocketUserManager.mt5DETAILSendMeg(data.getJSONArray("kline_data").getJSONObject(0),cion_name);
                            }else {
                                webSocketUserManager.metalDETAILSendMeg(data.getJSONArray("kline_data").getJSONObject(0),cion_name);
                            }
                        }
                    }

                }
        });
            thread.start();
        }
    }

    public String toCion(String cion){
        switch (cion){
            case "XAU":
                return "GOLD";
            case "XAG":
                return "Silver";
            case "XPD":
                return "Palladium";
            case "XAP":
                return "Platinum";
            case "GOLD":
                return "XAU";
            case "Silver":
                return "XAG";
            case "Palladium":
                return "XPD";
            case "Platinum":
                return "XAP";
            default:
                return cion;
        }
    }

    public void getGJSString(Set<String> strings){
        //贵金属开始
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        //秒合约
        TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
        tSecondCoinConfig.setMarket("metal");
        tSecondCoinConfig.setStatus(1L);
        List<TSecondCoinConfig> tSecondCoinConfigs = secondCoinConfigService.selectTSecondCoinConfigList(tSecondCoinConfig);
        for (TSecondCoinConfig secondCoinConfig : tSecondCoinConfigs) {
            strings.add(secondCoinConfig.getCoin().toUpperCase());
        }
        //U本位
        TContractCoin tContractCoin =new TContractCoin();
        tContractCoin.setEnable(0L);
        tContractCoin.setMarket("metal");
        List<TContractCoin> tContractCoins = contractCoinService.selectTContractCoinList(tContractCoin);
        for (TContractCoin contractCoin : tContractCoins) {
            strings.add(contractCoin.getCoin().toUpperCase());
        }
        //币币
        TCurrencySymbol tCurrencySymbol = new TCurrencySymbol();
        tCurrencySymbol.setEnable("1");
        tCurrencySymbol.setMarket("metal");
        List<TCurrencySymbol> tCurrencySymbols = tCurrencySymbolService.selectTCurrencySymbolList(tCurrencySymbol);
        for (TCurrencySymbol currencySymbol : tCurrencySymbols) {
            strings.add(currencySymbol.getCoin().toUpperCase());
        }
        //贵金属结束
    }

    public void getWHString(Set<String> strings){
        //外汇开始
        //秒合约
        TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
        tSecondCoinConfig.setMarket("mt5");
        tSecondCoinConfig.setStatus(1L);
        List<TSecondCoinConfig> tSecondCoinConfigs = secondCoinConfigService.selectTSecondCoinConfigList(tSecondCoinConfig);
        for (TSecondCoinConfig secondCoinConfig : tSecondCoinConfigs) {
            strings.add(secondCoinConfig.getSymbol().toUpperCase());
        }
        //U本位
        TContractCoin tContractCoin =new TContractCoin();
        tContractCoin.setEnable(0L);
        tContractCoin.setMarket("mt5");
        List<TContractCoin> tContractCoins = contractCoinService.selectTContractCoinList(tContractCoin);
        for (TContractCoin contractCoin : tContractCoins) {
            strings.add(contractCoin.getSymbol().toUpperCase());
        }
        //币币
        TCurrencySymbol tCurrencySymbol = new TCurrencySymbol();
        tCurrencySymbol.setEnable("1");
        tCurrencySymbol.setMarket("mt5");
        List<TCurrencySymbol> tCurrencySymbols = tCurrencySymbolService.selectTCurrencySymbolList(tCurrencySymbol);
        for (TCurrencySymbol currencySymbol : tCurrencySymbols) {
            strings.add(currencySymbol.getSymbol().toUpperCase());
        }

        //字典银行卡绑定币种
        List<SysDictData> backCoinList = sysDictTypeService.selectDictDataByType("t_bank_coin");
        if (!CollectionUtils.isEmpty(backCoinList)){
            for (SysDictData sysDictData : backCoinList) {
                if ("USD".equalsIgnoreCase(sysDictData.getDictValue())) continue;
                strings.add(sysDictData.getDictValue().toUpperCase()+"USD");
                strings.add("USD"+sysDictData.getDictValue().toUpperCase());
            }
        }
        //外汇结束
    }

    public void marketThreadRun_old() throws URISyntaxException {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        Set<String> strings = new HashSet<>();
        //秒合约
        TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
        tSecondCoinConfig.setMarket("metal");
        tSecondCoinConfig.setStatus(1L);
        List<TSecondCoinConfig> tSecondCoinConfigs = secondCoinConfigService.selectTSecondCoinConfigList(tSecondCoinConfig);
        for (TSecondCoinConfig secondCoinConfig : tSecondCoinConfigs) {
            strings.add(secondCoinConfig.getCoin().toUpperCase());
        }
        //U本位
        TContractCoin tContractCoin =new TContractCoin();
        tContractCoin.setEnable(0L);
        tContractCoin.setMarket("metal");
        List<TContractCoin> tContractCoins = contractCoinService.selectTContractCoinList(tContractCoin);
        for (TContractCoin contractCoin : tContractCoins) {
            strings.add(contractCoin.getCoin().toUpperCase());
        }
        //币币
        TCurrencySymbol tCurrencySymbol = new TCurrencySymbol();
        tCurrencySymbol.setEnable("1");
        tCurrencySymbol.setMarket("metal");
        List<TCurrencySymbol> tCurrencySymbols = tCurrencySymbolService.selectTCurrencySymbolList(tCurrencySymbol);
        for (TCurrencySymbol currencySymbol : tCurrencySymbols) {
            strings.add(currencySymbol.getCoin().toUpperCase());
        }
        //兑换
        for (String string : strings) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    double v = random.nextDouble();
                    String url = "";
                    url = "https://api-q.fx678img.com/getQuote.php?exchName=WGJS&symbol="+string.toUpperCase()+"&st="+v;
                    String result = HttpRequest.get(url)
                            .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("g943.kdlfps.com", 18866)))
                            .header(Header.USER_AGENT,"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Mobile Safari/537.36 Edg/132.0.0.0")
                            .header("referer", "https://quote.fx678.com/")
                            .header("Host","api-q.fx678img.com")
                            .header("Origin","https://quote.fx678.com")
                            .timeout(20000)
                            .execute().body();
                    webSocketUserManager.metalKlineSendMeg(result,string);
                    webSocketUserManager.metalDETAILSendMeg(result,string);
                }
            });
            thread.start();
        }
    }
}
