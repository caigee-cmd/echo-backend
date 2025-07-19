package com.ruoyi.web.controller.app;


import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.domain.TCurrencySymbol;
import com.ruoyi.bussiness.domain.vo.SymbolCoinConfigVO;
import com.ruoyi.bussiness.service.ITBotKlineModelService;
import com.ruoyi.bussiness.service.ITContractCoinService;
import com.ruoyi.bussiness.service.ITCurrencySymbolService;
import com.ruoyi.bussiness.service.ITSecondCoinConfigService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.socket.config.KLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/app/market")
public class TMarketController {


    @Resource
    private ITContractCoinService tContractCoinService;
    @Resource
    private ITCurrencySymbolService tCurrencySymbolService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ITSecondCoinConfigService tSecondCoinConfigService;

    @Resource
    private ITBotKlineModelService tBotKlineModelService;

    @GetMapping("/list")
    public AjaxResult list() {
        //查询币种跟随性价格
        // LocalDateTime now = LocalDateTime.now();
        // LocalDateTime beforeTime = now.minusDays(1);
        //  List<TBotKlineModel> list = tBotKlineModelService.list(new LambdaQueryWrapper<TBotKlineModel>().between(TBotKlineModel::getBeginTime, beforeTime, now).eq(TBotKlineModel::getModel, 0));
        HashMap<String, BigDecimal> stringBigDecimalHashMap = tBotKlineModelService.getyesterdayPrice();
        List<SymbolCoinConfigVO> coinList = tSecondCoinConfigService.getSymbolList();
        for (SymbolCoinConfigVO s:coinList) {
            BigDecimal bigDecimal = stringBigDecimalHashMap.get(s.getSymbol().toLowerCase());
            if(bigDecimal==null){
                bigDecimal=BigDecimal.ZERO;
            }
            BigDecimal openPrice = KLoader.OPEN_PRICE.get(s.getCoin());
            s.setOpen(Objects.isNull(openPrice)?BigDecimal.ZERO:openPrice.add(bigDecimal));
            if(s.getMarket().equals("metal")){
                BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + s.getCoin());
                s.setAmount(Objects.isNull(currentlyPrice)?BigDecimal.ZERO:currentlyPrice);
            }else{
                BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + s.getCoin().toLowerCase());
                s.setAmount(Objects.isNull(currentlyPrice)?BigDecimal.ZERO:currentlyPrice);
            }
        }
        List<TCurrencySymbol> currencyList = tCurrencySymbolService.getSymbolList();
        for (TCurrencySymbol t:currencyList) {
            BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + t.getCoin().toLowerCase());
            t.setAmount(Objects.isNull(currentlyPrice)?BigDecimal.ZERO:currentlyPrice);
            BigDecimal bigDecimal = stringBigDecimalHashMap.get(t.getSymbol().toLowerCase());
            if(bigDecimal==null){
                bigDecimal=BigDecimal.ZERO;
            }
            BigDecimal openPrice = KLoader.OPEN_PRICE.get(t.getCoin().toLowerCase());
            t.setOpen(Objects.isNull(openPrice)?BigDecimal.ZERO:openPrice.add(bigDecimal));
        }
        List<TContractCoin> contractList = tContractCoinService.getCoinList();
        for (TContractCoin coin:contractList) {
            BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + coin.getCoin().toLowerCase());
            BigDecimal bigDecimal = stringBigDecimalHashMap.get(coin.getSymbol().toLowerCase());
            if(bigDecimal==null){
                bigDecimal=BigDecimal.ZERO;
            }
            BigDecimal openPrice = KLoader.OPEN_PRICE.get(coin.getCoin().toLowerCase());
            coin.setOpen(Objects.isNull(openPrice)?BigDecimal.ZERO:openPrice.add(bigDecimal));
            coin.setAmount(Objects.isNull(currentlyPrice)?BigDecimal.ZERO:currentlyPrice);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("coinList",coinList);
        map.put("currencyList",currencyList);
        map.put("contractList",contractList);
        return new AjaxResult(0,"success",map);
    }
}
