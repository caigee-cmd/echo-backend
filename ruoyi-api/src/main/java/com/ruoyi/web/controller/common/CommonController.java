package com.ruoyi.web.controller.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import com.ruoyi.bussiness.domain.TBotKlineModel;
import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.domain.TCurrencySymbol;
import com.ruoyi.bussiness.domain.TSecondCoinConfig;
import com.ruoyi.bussiness.domain.setting.*;
import com.ruoyi.bussiness.domain.vo.SymbolCoinConfigVO;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.TranslatorUtil;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.socket.config.KLoader;
import com.ruoyi.socket.service.MarketThread;
import com.ruoyi.system.service.ISysDictTypeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;

/**
 * 通用请求处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private RedisCache redisCache;
    @Resource
    private FileService fileService;
    @Resource
    private SettingService settingService;
    @Resource
    private ITSecondCoinConfigService tSecondCoinConfigService;
    @Resource
    private ITCurrencySymbolService tCurrencySymbolService;
    @Resource
    private ITContractCoinService tContractCoinService;
    @Resource
    private ISysDictTypeService dictTypeService;
    @Resource
    private List<MarketThread> marketThread;
    @Resource
    private ITBotKlineModelService tBotKlineModelService;


    private static final String FILE_DELIMETER = ",";

    /**
     * oss通用上传请求
     */
    @PostMapping("/upload/OSS")
    public AjaxResult uploadFileOSS(MultipartFile file, String remark) {
        try {
            String filename = file.getResource().getFilename();
            //这里文件名用了uuid 防止重复，可以根据自己的需要来写
            String name = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."), filename.length());
            name = name.replace("-", "");
            String url = fileService.uploadFileOSS(file,name);
            Map ajax =new  HashMap();
            ajax.put("fileName", name);
            ajax.put("url", url);
            return   AjaxResult.success(ajax);
        } catch (Exception e) {
            e.getMessage();
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取币种列表")
    @PostMapping("/getCoinList")
    public AjaxResult getCoinList() {
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
        return AjaxResult.success(map);
    }

    @ApiOperation(value = "获取客服列表")
    @PostMapping("/getCustomerService")
    public AjaxResult getCustomerService() {
        Setting setting = settingService.get(SettingEnum.SUPPORT_STAFF_SETTING.name());
        List<SupportStaffSetting> supportStaffSettings = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), SupportStaffSetting.class);
        return AjaxResult.success(supportStaffSettings);
    }
    @ApiOperation(value = "获取登录注册开关")
    @PostMapping("/getLoginRegisterList")
    public AjaxResult getLoginRegisterList() {
        Setting setting = settingService.get(SettingEnum.LOGIN_REGIS_SETTING.name());
        LoginRegisSetting loginOrRegisList = JSONUtil.toBean(setting.getSettingValue(), LoginRegisSetting.class);
        return AjaxResult.success(loginOrRegisList);
    }


    @ApiOperation(value = "获取App侧边栏配置")
    @PostMapping("/getAppSidebarSetting")
    public AjaxResult getAppSidebarSetting() {
        Setting setting = settingService.get(SettingEnum.APP_SIDEBAR_SETTING.name());
        String settingValue = setting.getSettingValue();
        JSONArray objects = JSONUtil.parseArray(settingValue);
        List<AppSidebarSetting> list = JSONUtil.toList(objects, AppSidebarSetting.class)
                .stream().sorted(Comparator.comparing(AppSidebarSetting::getSort)).collect(Collectors.toList());
        return AjaxResult.success(list);
    }



    @ApiOperation(value = "充值通道列表")
    @PostMapping("/getAppCurrencyList")
    public AjaxResult getAppCurrencyList() {
        Setting setting = settingService.get(SettingEnum.ASSET_COIN.name());
        List<AssetCoinSetting> list = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class);
        return AjaxResult.success(list);
    }


    @ApiOperation(value = "提现通道列表")
    @PostMapping("/getWithDrawCoinList")
    public AjaxResult getWithdrawalSetting() {
        Setting setting = settingService.get(SettingEnum.WITHDRAWAL_CHANNEL_SETTING.name());
        List<TRechargeChannelSetting> list = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), TRechargeChannelSetting.class);
        return AjaxResult.success(list);
    }

    @ApiOperation(value = "白皮书")
    @PostMapping("/getWhitePaperSetting")
    public AjaxResult getWhitePaperSetting() {
        Setting setting = settingService.get(SettingEnum.WHITE_PAPER_SETTING.name());
        WhitePaperSetting  whitePaper = JSONUtil.toBean(setting.getSettingValue(), WhitePaperSetting.class);
        return AjaxResult.success(whitePaper);
    }

    @ApiOperation(value = "defi挖矿配置")
    @PostMapping("/getDefiIncomeSetting")
    public AjaxResult getDefiIncomeSetting() {
        Setting setting = settingService.get(SettingEnum.DEFI_INCOME_SETTING.name());
        DefiIncomeSetting  defiIncome = JSONUtil.toBean(setting.getSettingValue(), DefiIncomeSetting.class);
        return AjaxResult.success(defiIncome);
    }

    @ApiOperation(value = "玩法配置")
    @PostMapping("/getPlayingSetting")
    public AjaxResult getPlayingSetting() {
        Setting setting = settingService.get(SettingEnum.PLAYING_SETTING.name());
        return setting == null ?
                AjaxResult.success(new ArrayList<PlayingSetting>()) :
                AjaxResult.success(JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), PlayingSetting.class));
    }
    @ApiOperation(value = "tab头配置")
    @PostMapping("/geTabSetting")
    public AjaxResult geTabSetting() {
        Setting setting = settingService.get(SettingEnum.TAB_SETTING.name());
        return setting == null ?
                AjaxResult.success(new ArrayList<TabSetting>()) :
                AjaxResult.success(JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), TabSetting.class));
    }
    @ApiOperation(value = "底部菜单")
    @PostMapping("/getBottomMenuSetting")
    public AjaxResult getBottomMenuSetting() {
        Setting setting = settingService.get(SettingEnum.BOTTOM_MENU_SETTING.name());
        return setting == null ?
                AjaxResult.success(new ArrayList<BottomMenuSetting>()) :
                AjaxResult.success(JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), BottomMenuSetting.class));
    }

    @ApiOperation(value = "金刚区")
    @PostMapping("/getMiddleMenuSetting")
    public AjaxResult getMiddleMenuSetting() {
        Setting setting = settingService.get(SettingEnum.MIDDLE_MENU_SETTING.name());
        return setting == null ?
                AjaxResult.success(new ArrayList<MiddleMenuSetting>()) :
                AjaxResult.success(JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), MiddleMenuSetting.class));
    }

    @ApiOperation(value = "Logo配置")
    @PostMapping("/getLogoSetting")
    public AjaxResult getLogoSetting() {
        Setting setting = settingService.get(SettingEnum.LOGO_SETTING.name());
        return setting == null ?
                AjaxResult.success(new LogoSetting()) :
                AjaxResult.success(JSONUtil.toBean(setting.getSettingValue(), LogoSetting.class));
    }

    @ApiOperation(value = "首页币种配置")
    @PostMapping("/getHomeCoinSetting")
    public AjaxResult getHomeCoinSetting() {
        Setting setting = settingService.get(SettingEnum.HOME_COIN_SETTING.name());
        List<HomeCoinSetting> collect = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), HomeCoinSetting.class).stream().sorted(Comparator.comparing(HomeCoinSetting::getSort)).collect(Collectors.toList());
        return setting == null ?
                AjaxResult.success(new ArrayList<HomeCoinSetting>()) :
                AjaxResult.success(collect);
    }





    @ApiOperation(value = "字典列表")
    @PostMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType, HttpServletRequest request)
    {
        String lang = request.getHeader("Lang");
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (!CollectionUtils.isEmpty(data)){
            if ("t_repay_type".equals(dictType)){
                data.stream().forEach(d ->{
                    if (!lang.equals("zh")){
                        try {
                            d.setDictLabel(TranslatorUtil.translate("zh-CN",lang,d.getDictLabel()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                } );
            }
        }else{
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }

    @ApiOperation(value = "谷歌翻译")
    @PostMapping(value = "/googleTranslator")
    public AjaxResult googleTranslator( String langFrom,String langTo,String text)
    {
        try {
            String translate = TranslatorUtil.translate(langFrom, langTo, text);
            return AjaxResult.success(translate);
        } catch (Exception e) {
            log.error("翻译失败",e);
        }
        return AjaxResult.success("");
    }



    @ApiOperation(value = "获取所有配置")
    @PostMapping("/getAllSetting")
    public AjaxResult getAllSetting() {
        //提现
        Setting setting = settingService.get(SettingEnum.WITHDRAWAL_CHANNEL_SETTING.name());
        HashMap<String, Object> map = new HashMap<>();
        map.put("WITHDRAWAL_CHANNEL_SETTING",setting == null ? new ArrayList<TRechargeChannelSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), TRechargeChannelSetting.class));
        //首页
        setting = settingService.get(SettingEnum.HOME_COIN_SETTING.name());
        map.put("HOME_COIN_SETTING", setting == null ? new ArrayList<HomeCoinSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), HomeCoinSetting.class)
                        .stream().sorted(Comparator.comparing(HomeCoinSetting::getSort)).collect(Collectors.toList()));
        //获取客服列表
        setting = settingService.get(SettingEnum.SUPPORT_STAFF_SETTING.name());
        map.put("SUPPORT_STAFF_SETTING",setting == null ? new ArrayList<HomeCoinSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), SupportStaffSetting.class));
        //获取登录注册开关
        setting = settingService.get(SettingEnum.LOGIN_REGIS_SETTING.name());
        map.put("LOGIN_REGIS_SETTING",setting == null ? new LoginRegisSetting() :
                JSONUtil.toBean(setting.getSettingValue(), LoginRegisSetting.class));
        //获取App侧边栏配置
        setting = settingService.get(SettingEnum.APP_SIDEBAR_SETTING.name());
        map.put("APP_SIDEBAR_SETTING",setting == null ? new ArrayList<AppSidebarSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AppSidebarSetting.class)
                        .stream().sorted(Comparator.comparing(AppSidebarSetting::getSort)).collect(Collectors.toList()));
        //充值通道列表
        setting = settingService.get(SettingEnum.ASSET_COIN.name());
        map.put("ASSET_COIN",setting == null ? new ArrayList<AssetCoinSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class));
        //充值通道列表
        setting = settingService.get(SettingEnum.ASSET_CUSTOM_COIN.name());
        map.put("ASSET_CUSTOM_COIN", setting == null ? new ArrayList<AssetCoinSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class));
        //白皮书
/*        setting = settingService.get(SettingEnum.WHITE_PAPER_SETTING.name());
        map.put("WHITE_PAPER_SETTING",setting == null ? new WhitePaperSetting() :
                JSONUtil.toBean(setting.getSettingValue(), WhitePaperSetting.class));*/
        //defi挖矿配置
        setting = settingService.get(SettingEnum.DEFI_INCOME_SETTING.name());
        map.put("DEFI_INCOME_SETTING",setting == null ? new DefiIncomeSetting() :
                JSONUtil.toBean(setting.getSettingValue(), DefiIncomeSetting.class));
        //玩法配置
        setting = settingService.get(SettingEnum.PLAYING_SETTING.name());
        map.put("PLAYING_SETTING",setting == null ? new ArrayList<PlayingSetting>() :
        JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), PlayingSetting.class)
                .stream().sorted(Comparator.comparing(PlayingSetting::getSort)).collect(Collectors.toList()));

        //投部TAB_SETTING
        setting = settingService.get(SettingEnum.TAB_SETTING.name());
        map.put("TAB_SETTING",setting == null ? new ArrayList<PlayingSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), TabSetting.class)
                        .stream().sorted(Comparator.comparing(TabSetting::getSort)).collect(Collectors.toList()));
        //底部菜单
        setting = settingService.get(SettingEnum.BOTTOM_MENU_SETTING.name());
        map.put("BOTTOM_MENU_SETTING",setting == null ?new ArrayList<BottomMenuSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), BottomMenuSetting.class)
                        .stream().sorted(Comparator.comparing(BottomMenuSetting::getSort)).collect(Collectors.toList()));
        //金刚区
        setting = settingService.get(SettingEnum.MIDDLE_MENU_SETTING.name());
        map.put("MIDDLE_MENU_SETTING",setting == null ? new ArrayList<MiddleMenuSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), MiddleMenuSetting.class)
                        .stream().sorted(Comparator.comparing(MiddleMenuSetting::getSort)).collect(Collectors.toList()));
        //Logo配置
        setting = settingService.get(SettingEnum.LOGO_SETTING.name());
        map.put("LOGO_SETTING",setting == null ? new LogoSetting() :
                JSONUtil.toBean(setting.getSettingValue(), LogoSetting.class));
        //下载地址
        setting = settingService.get(SettingEnum.DOWNLOAD_SETTING.name());
        map.put("DOWNLOAD_SETTING",setting == null ? new ArrayList<DownloadSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), DownloadSetting.class)
                        .stream().sorted(Comparator.comparing(DownloadSetting::getSort)).collect(Collectors.toList()));

        //图形验证码
        setting = settingService.get(SettingEnum.MARKET_URL.name());
        map.put("MARKET_URL",setting == null ? new MarketUrlSetting() :
                JSONUtil.toBean(setting.getSettingValue(), MarketUrlSetting.class));

        //多语言
        List<SysDictData> data = dictTypeService.selectDictDataByType("t_app_language");
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
        map.put("t_app_language",data);

        //vip 等级配置
        setting = settingService.get(SettingEnum.VIP_LEVEL_SETTING.name());
        map.put("VIP_LEVEL_SETTING",setting == null ? new VipLevelSetting() :
                JSONUtil.toBean(setting.getSettingValue(), VipLevelSetting.class));

        //vip 说明
        setting = settingService.get(SettingEnum.VIP_DIRECTIONS_SETTING.name());
        map.put("VIP_DIRECTIONS_SETTING",setting == null ? new ArrayList<VipDirectionsSetting>() :
                JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), VipDirectionsSetting.class));
        //时区
        setting = settingService.get(SettingEnum.PLATFORM_SETTING.name());
        if(null == setting){
            com.ruoyi.common.core.domain.entity.TimeZone timeZone = DateUtils.getTimeZone();
            map.put("timeZone",timeZone);

        }else {
            PlatformSetting platformSetting = JSONUtil.toBean(setting.getSettingValue(), PlatformSetting.class);
            com.ruoyi.common.core.domain.entity.TimeZone timeZone = DateUtils.getTimeZone(platformSetting.getTimezone());
            map.put("timeZone",timeZone);
        }
        setting = settingService.get(SettingEnum.AUTH_LIMIT.name());
        map.put("AUTH_LIMIT",setting == null ? new AuthLimitSetting():JSONUtil.toBean(setting.getSettingValue(),AuthLimitSetting.class));
        return AjaxResult.success(map);
    }
/*    @PostMapping("/starSubMark")
    public void starSubMark() {
        for (MarketThread thread : marketThread) {
            thread.marketThreadRun();
        }
    }*/

    @PostMapping("/getMt5Amount")
    public AjaxResult getMt5Amount( String coin) {
        if(redisCache.hasKey("amount:"+coin)){
           Long amount =  redisCache.getCacheObject("amount:"+coin);
           if(amount==null){
               amount=0L;
           }
            Double v = (Math.random() * 100) + 1;
            Long add = v.longValue();
            amount+=add;
            redisCache.setCacheObject("amount:"+coin,amount);
            return AjaxResult.success(amount);
        }else {
            Double v = (Math.random() * 10000 + 5000);
            Long amount = v.longValue();
            redisCache.setCacheObject("amount:"+coin,amount);
            return AjaxResult.success(amount);
        }
    }

    @PostMapping("/getWithdrawMt5Amount")
    public AjaxResult getWithdrawMt5Amount( String coin) {
        BigDecimal price = "USD".equals(coin.toUpperCase())?BigDecimal.ONE:redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + coin.toUpperCase() + "USD");
        if (Objects.isNull(price)){
            price = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + "USD"+coin.toUpperCase());
        }else{
            price = BigDecimal.ONE.divide(price,6,RoundingMode.DOWN);
        }
        return AjaxResult.success(price);
    }


}
