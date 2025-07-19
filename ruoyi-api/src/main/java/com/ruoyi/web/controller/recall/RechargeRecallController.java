package com.ruoyi.web.controller.recall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.enums.ThirdTypeUncEmun;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.udun.client.UdunClient;
import com.ruoyi.udun.domain.Coin;
import com.ruoyi.web.controller.common.ApiBaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author pc
 */
@RestController
@RequestMapping("/api/recall/pay")
@Slf4j
public class RechargeRecallController extends ApiBaseController {

    @Resource
    private ITAppRechargeService appRechargeService;

    @Resource
    private ITAppAssetService assetService;
    @Autowired
    private ITAppWalletRecordService recordService;

    @Resource
    private ITAppUserService appUserService;

    @Resource
    private ITUserSymbolAddressService userSymbolAddressService;
    @Resource
    private ITWithdrawService withdrawService;
    @Resource
    private ITAppAssetService itAppUserService;
    @Resource
    private ITAppWalletRecordService walletRecordService;
    @Resource
    private SettingService settingService;
    @PostMapping(value = "/unc")
    public void tradeCallback(@RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce,
                                @RequestParam("body") String body,
                                @RequestParam("sign") String sign) throws IOException {
        log.info("timestamp:{},nonce:{},sign:{},body:{}", timestamp, nonce, sign, body);
        onePay(body);
    }
    private synchronized  void onePay(String recallVO) throws IOException {
        log.info("unc recall post body: {}", recallVO);
        JSONObject postData = JSON.parseObject(recallVO);
        // 三方订单编号
        String fee = postData.getString("fee");
        //地址
        String address = postData.getString("address");

        String amount = postData.getString("amount");

        String decimals = postData.getString("decimals");

        String businessId = postData.getString("businessId");
        //0 	待審核1 	審核成功2 	審核駁回3 	交易成功4 	交易失敗
        Integer status = postData.getInteger("status");
        Integer tradeType = postData.getInteger("tradeType");
        String tradeId = postData.getString("tradeId");
        String coinType = postData.getString("coinType");
        String mainCoinType = postData.getString("mainCoinType");
        TUserSymbolAddress param = new TUserSymbolAddress();
        param.setAddress(address);
        List<TUserSymbolAddress> list = userSymbolAddressService.selectTUserSymbolAddressList(param);
        if (!CollectionUtils.isEmpty(list)) {
            TUserSymbolAddress tUserSymbolAddress = list.get(0);
            Double real = Math.pow(10, Double.valueOf(decimals));
            BigDecimal decimal = new BigDecimal(amount);
            BigDecimal realAmount = decimal.divide(new BigDecimal(real), 8, RoundingMode.HALF_UP);
            BigDecimal fee1 = new BigDecimal(fee).divide(new BigDecimal(real), 8, RoundingMode.HALF_UP);
            log.info("{}代付回调信息:{},{},{},{},{}", address, amount, businessId, status, tradeId);
            if (tradeType == 1) {
                TAppRecharge appRecharge = new TAppRecharge();
                appRecharge.setAddress(tUserSymbolAddress.getAddress());
                appRecharge.setUserId(tUserSymbolAddress.getUserId());

                    log.error("代收回调时，订单不存在, third_serial_id:{}", tradeId);
                    ThirdPaySetting setting = settingService.getThirdPaySetting(ThirdTypeUncEmun.UNCDUN.getValue());
                String symbol = "";
                String name = "";
                    if (Objects.nonNull(setting)) {
                            UdunClient udunClient = new UdunClient(setting.getUrl(),
                                    setting.getMechId(),
                                    setting.getKey(),
                                    setting.getReturnUrl());
                            List<Coin> coinList = udunClient.listSupportCoin(false);
                        for (Coin coin: coinList) {
                            if(mainCoinType.equals(coin.getMainCoinType())&&coinType.equals(coin.getCoinType())){
                                symbol = coin.getSymbol();
                                if(symbol.indexOf("USDT")>-1){
                                    symbol="usdt";
                                }
                                name =coin.getName();
                            }
                        }
                            //获取所有 然后获取主币，然后获取子币
                            JSONObject.toJSONString(coinList);
                            log.info(JSONObject.toJSONString(coinList));
                    }
                    //创建订单
                    if (3 == status) {
                        log.info("symbol"+symbol);
                        TAppUser tAppUser = appUserService.selectTAppUserByUserId(tUserSymbolAddress.getUserId());
                        TAppRecharge appRecharge1 = new TAppRecharge();
                        appRecharge1.setRealAmount(realAmount);
                        appRecharge1.setAddress(address);
                        appRecharge1.setUsername(tAppUser.getLoginName());
                        appRecharge1.setCreateTime(new Date());
                        appRecharge1.setSerialId(tradeId);
                        appRecharge1.setTxId(tradeId);
                        appRecharge1.setCoin(symbol.toLowerCase());
                        appRecharge1.setUpdateTime(new Date());
                        appRecharge1.setRemark("优盾充值成功");
                        appRecharge1.setAmount(realAmount);
                        appRecharge1.setStatus("1");
                        appRecharge1.setType(name);
                        appRecharge1.setUserId(tAppUser.getUserId());
                        appRecharge1.setAdminParentIds(tAppUser.getAdminParentIds());
                        appRechargeService.save(appRecharge1);
                        Map<String, TAppAsset> assetMap = itAppUserService.getAssetByUserIdList(appRecharge1.getUserId());
                        TAppAsset appAsset = assetMap.get(appRecharge1.getCoin() + appRecharge1.getUserId());
                        log.info("资产："+JSONObject.toJSONString(appAsset));
                        //增加余额
                        if(appAsset==null){
                            appAsset = new TAppAsset();
                            appAsset.setAmout(appRecharge1.getAmount());
                            appAsset.setAvailableAmount(appRecharge1.getAmount());
                            appAsset.setUserId(appRecharge1.getUserId());
                            appAsset.setAdress(tAppUser.getAddress());
                            appAsset.setType(1);
                            appAsset.setSymbol(symbol.toLowerCase());
                            itAppUserService.insertTAppAsset(appAsset);
                        }else{
                            itAppUserService.updateTAppAsset(
                                    TAppAsset.builder()
                                            .symbol(appRecharge1.getCoin())
                                            .userId(appRecharge1.getUserId())
                                            .amout(appAsset.getAmout().add(appRecharge1.getAmount()))
                                            .availableAmount(appAsset.getAvailableAmount().add(appRecharge1.getAmount()))
                                            .type(AssetEnum.PLATFORM_ASSETS.getCode())
                                            .build());
                        }


                        walletRecordService.generateRecord(appRecharge1.getUserId(),
                                appRecharge1.getRealAmount(), RecordEnum.RECHARGE.getCode(),
                                "", appRecharge1.getSerialId(),
                                "U盾自动充值",appAsset.getAmout(),
                                appAsset.getAmout().add(appRecharge1.getRealAmount()),appRecharge1.getCoin(),appRecharge1.getAdminParentIds());

                        log.info("代付回调成功: s erialId:{}",  appRecharge1.getSerialId());
                    }
                    return;
            }else if(2==tradeType){
                TWithdraw appWithdraw = new TWithdraw();
                appWithdraw.setSerialId(businessId);
                List<TWithdraw> withdraws = withdrawService.selectTWithdrawList(appWithdraw);
                if (CollectionUtils.isEmpty(withdraws)) {
                    log.error("代收提现回调时，订单不存在, third_serial_id:{}", businessId);
                    return;
                } else {
                    TWithdraw tWithdraw = withdraws.get(0);
                    if(tWithdraw.getStatus()==1 || tWithdraw.getStatus()==2){
                        log.error("代收提现回调已经处理, third_serial_id:{}  ,{}", businessId,status);
                        return;
                    }
                    if (3 == status) {
                        tWithdraw.setFee(fee1);
                        tWithdraw.setRealAmount(realAmount);
                        tWithdraw.setStatus(1);
                        withdrawService.updateTWithdraw(tWithdraw);
                        log.info("代付回调成功: s erialId:{}",  tWithdraw.getSerialId());
                        return;
                    } else {
                        log.error("回调未成功，返回status:{}", status);
                        if (2 == status || 4 == status) {
                            BigDecimal amount1 = tWithdraw.getAmount();
                            String coin = tWithdraw.getCoin();
                            TAppUser user = appUserService.selectTAppUserByUserId(tWithdraw.getUserId());
                            //资产
                            Map<String, TAppAsset> map = assetService.getAssetByUserIdList(tWithdraw.getUserId());
                            TAppAsset asset = map.get(tWithdraw.getCoin() + user.getUserId());
                            BigDecimal beforeMount = asset.getAvailableAmount();
                            asset.setAmout(asset.getAmout().add(amount1));
                            asset.setAvailableAmount(beforeMount.add(amount1));
                            assetService.updateByUserId(asset);
                            tWithdraw.setStatus(2);
                            tWithdraw.setUpdateBy(SecurityUtils.getUsername());
                            tWithdraw.setWithDrawRemark("unc提现失败");
                            tWithdraw.setRemark("unc提现失败");
                            withdrawService.updateTWithdraw(tWithdraw);
                            recordService.generateRecord(tWithdraw.getUserId(), tWithdraw.getAmount(), 8,
                                    SecurityUtils.getUsername(), tWithdraw.getSerialId(), "unc提现失败", beforeMount,
                                    beforeMount.add(tWithdraw.getAmount()), coin, user.getAdminParentIds());
                            log.debug("账变前amout ,账变后 ，userId  {}-{} {}", beforeMount, beforeMount.add(tWithdraw.getAmount()), user.getUserId());
                        }
                    }
                }
            }
        }
    }
}
