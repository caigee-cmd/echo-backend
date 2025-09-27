package com.ruoyi.web.controller.bussiness;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.TAppRecharge;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.setting.AssetCoinSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.ITAppRechargeService;
import com.ruoyi.bussiness.service.ITUserSymbolAddressService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.*;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户充值Controller
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
@RestController
@RequestMapping("/api/recharge")
public class TAppRechargeController extends ApiBaseController
{
    private static final Logger log = LoggerFactory.getLogger(TAppRechargeController.class);
    @Resource
    private ITAppRechargeService tAppRechargeService;
    @Resource
    private SettingService settingService;
    @Resource
    private ITUserSymbolAddressService userSymbolAddressService;
    @Resource
    private RedisUtil redisUtil;
    @Value("${admin-redis-stream.names}")
    private String redisStreamNames;


    /**
     * 获取用户充值详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        TAppRecharge appRecharge = tAppRechargeService.getById(id);
        if(Objects.nonNull(appRecharge)){
            Date creatTime=appRecharge.getCreateTime();
            Map<String,Object> params=new HashMap<>();
            params.put("date",Objects.nonNull(creatTime)?creatTime.getTime():0L);
            appRecharge.setParams(params);
        }
        return AjaxResult.success(appRecharge);
    }

    /**
     * 修改用户充值
     */

    @PostMapping("/list")
    public TableDataInfo list(TAppRecharge tAppRecharge) {
        startPage();

        TAppUser user = getAppUser();
//        TAppUser user = tAppUserService.getById(7);
        tAppRecharge.setUserId(user.getUserId());
        Map<String, Object> params = new HashMap<>();
        tAppRecharge.setParams(params);
        startPage();
        List<TAppRecharge> list = tAppRechargeService.selectTAppRechargeList(tAppRecharge);
        for (TAppRecharge app:list) {
            Date creatTime=app.getCreateTime();
            Map<String,Object> map=new HashMap<>();
            map.put("createTime", Objects.nonNull(creatTime)?creatTime.getTime():0L);
            app.setParams(map);
        }
        return getDataTable(list);
    }

    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody Map<String, Object> params) {
        TAppUser user = getAppUser();
        if (null!=user&&user.getStatus() != 0) {
            log.debug("userid:{}, 非正常用户，不可充值", user.getUserId());
            return AjaxResult.success();
        }
        log.info("userid:{}, 充值参数:{}",
                user.getUserId(), JSONUtil.toJsonPrettyStr(params));
        String type = String.valueOf(params.get("type"));
        Setting setting = settingService.get(SettingEnum.ASSET_CUSTOM_COIN.name());
        log.info("userid:{}, 充值参数setting:{}",
                user.getUserId(), JSONUtil.toJsonPrettyStr(setting));
        List<AssetCoinSetting> assetCoinSettings = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class);
        List<AssetCoinSetting> list = assetCoinSettings.stream()
                .filter(assetCoinSetting -> assetCoinSetting.getCoinName().equals(type))
                .collect(Collectors.toList());
        BigDecimal amount = new BigDecimal(String.valueOf(params.get("amount")));
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            if (list.isEmpty()) {
                return AjaxResult.error("Unsupported coin type: " + type);
            }
            if (amount.compareTo(list.get(0).getRechargeMin()) < 0) {
                return AjaxResult.error(MessageUtils.message("recharge.amout.min", list.get(0).getRechargeMin()));
            }
            if (list.get(0).getRechargeMax().compareTo(amount) < 0) {
                return AjaxResult.error(MessageUtils.message("recharge.amout.max", list.get(0).getRechargeMax()));
            }
            user.setParams(params);
        }
        tAppRechargeService.insertTAppRecharge(user);

        HashMap<String, Object> object = new HashMap<>();
        object.put(CacheConstants.RECHARGE_KEY, CacheConstants.RECHARGE_KEY);
        redisUtil.addStream(redisStreamNames, object);
        return AjaxResult.success();
    }

    @PostMapping("/userRechage")
    public AjaxResult userRechage() {
        return AjaxResult.success( userSymbolAddressService.getUserRechargeAdressList(getStpUserId()));
    }
    @PostMapping("/getAdress")
    public AjaxResult getAdress() {
        Map<String,  Map<String, String>>  map = new HashMap<>();
        Setting setting = settingService.get(SettingEnum.ASSET_CUSTOM_COIN.name());
        if(setting == null){
            return AjaxResult.success();
        }
        List<AssetCoinSetting> list = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class);
        for (AssetCoinSetting assetCoinSetting : list) {
            Map<String, String> adress = userSymbolAddressService.getAdredssByCoin(assetCoinSetting.getCoin(), assetCoinSetting.getCoinName(), getStpUserId());
            map.put(assetCoinSetting.getCoinName(),adress);
        }
        return AjaxResult.success(map);
    }

    @PostMapping("/checkUadress")
    public AjaxResult checkUadress( String symbol,String adress) {
        boolean f = userSymbolAddressService.check(symbol ,adress);
        return AjaxResult.success(f);
    }
}
