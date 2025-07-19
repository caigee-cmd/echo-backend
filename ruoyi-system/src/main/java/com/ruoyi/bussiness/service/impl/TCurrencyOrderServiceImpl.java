package com.ruoyi.bussiness.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.AddMosaicSetting;
import com.ruoyi.bussiness.domain.setting.AppSidebarSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.mapper.TAppUserMapper;
import com.ruoyi.bussiness.mapper.TCurrencySymbolMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TCurrencyOrderMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 币币交易订单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-25
 */
@Service
public class TCurrencyOrderServiceImpl extends ServiceImpl<TCurrencyOrderMapper, TCurrencyOrder> implements ITCurrencyOrderService {
    @Resource
    private TCurrencyOrderMapper tCurrencyOrderMapper;
    @Resource
    private ITAppAssetService tAppAssetService;
    @Resource
    private ITAppUserService tAppUserService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private TCurrencySymbolMapper tCurrencySymbolMapper;
    @Resource
    private ITAppWalletRecordService appWalletRecordService;
    @Resource
    private SettingService settingService;

    /**
     * 查询币币交易订单
     *
     * @param id 币币交易订单主键
     * @return 币币交易订单
     */
    @Override
    public TCurrencyOrder selectTCurrencyOrderById(Long id) {
        return tCurrencyOrderMapper.selectTCurrencyOrderById(id);
    }

    /**
     * 查询币币交易订单列表
     *
     * @param tCurrencyOrder 币币交易订单
     * @return 币币交易订单
     */
    @Override
    public List<TCurrencyOrder> selectTCurrencyOrderList(TCurrencyOrder tCurrencyOrder) {
        return tCurrencyOrderMapper.selectTCurrencyOrderList(tCurrencyOrder);
    }

    /**
     * 新增币币交易订单
     *
     * @param tCurrencyOrder 币币交易订单
     * @return 结果
     */
    @Override
    public int insertTCurrencyOrder(TCurrencyOrder tCurrencyOrder) {
        tCurrencyOrder.setCreateTime(DateUtils.getNowDate());
        return tCurrencyOrderMapper.insertTCurrencyOrder(tCurrencyOrder);
    }

    /**
     * 修改币币交易订单
     *
     * @param tCurrencyOrder 币币交易订单
     * @return 结果
     */
    @Override
    public int updateTCurrencyOrder(TCurrencyOrder tCurrencyOrder) {
        tCurrencyOrder.setUpdateTime(DateUtils.getNowDate());
        return tCurrencyOrderMapper.updateTCurrencyOrder(tCurrencyOrder);
    }

    /**
     * 批量删除币币交易订单
     *
     * @param ids 需要删除的币币交易订单主键
     * @return 结果
     */
    @Override
    public int deleteTCurrencyOrderByIds(Long[] ids) {
        return tCurrencyOrderMapper.deleteTCurrencyOrderByIds(ids);
    }

    /**
     * 删除币币交易订单信息
     *
     * @param id 币币交易订单主键
     * @return 结果
     */
    @Override
    public int deleteTCurrencyOrderById(Long id) {
        return tCurrencyOrderMapper.deleteTCurrencyOrderById(id);
    }

    @Transactional
    @Override
    public String submitCurrencyOrder(TAppUser user, TCurrencyOrder tCurrencyOrder) {
        String symbol = tCurrencyOrder.getSymbol();
        String coin = tCurrencyOrder.getCoin();
        Integer delegateType = tCurrencyOrder.getDelegateType();
        TCurrencySymbol currencySymbol = tCurrencySymbolMapper.selectOne(new LambdaQueryWrapper<TCurrencySymbol>().eq(TCurrencySymbol::getCoin, symbol).eq(TCurrencySymbol::getBaseCoin, coin).eq(TCurrencySymbol::getEnable, "1"));
        if (Objects.isNull(currencySymbol)) {
            return MessageUtils.message("currency.coin.setting.nonexistent", (tCurrencyOrder.getSymbol() + tCurrencyOrder.getCoin()).toUpperCase());
        }
        //  111
        BigDecimal symolSettle = tCurrencyOrder.getSymbol().equals("usdt") ? BigDecimal.ONE : redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + tCurrencyOrder.getSymbol());
        BigDecimal coinSettle = tCurrencyOrder.getCoin().equals("usdt") ? BigDecimal.ONE : redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + tCurrencyOrder.getCoin());
        BigDecimal settlePrice = symolSettle.divide(coinSettle, 8, RoundingMode.DOWN);

        //校验


        String result = checkOrder(tCurrencyOrder, currencySymbol, settlePrice);
        if (!result.equals("success")) {
            return result;
        }
        if (Objects.nonNull(user.getIsLimitation())){
            if(user.getIsLimitation() == 2)
                return MessageUtils.message("user_is_limitation");
        }


        Long userId = user.getUserId();
        //判断是否需要创建资产账户
        if (tCurrencyOrder.getType() == 0) {
            TAppAsset tAppAsset = tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()).eq(TAppAsset::getUserId, userId).eq(TAppAsset::getSymbol, tCurrencyOrder.getSymbol()));
            if (Objects.isNull(tAppAsset)) {
                tAppAssetService.createAsset(user, tCurrencyOrder.getSymbol(), AssetEnum.PLATFORM_ASSETS.getCode());
            }
        } else {
            TAppAsset tAppAsset = tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()).eq(TAppAsset::getUserId, userId).eq(TAppAsset::getSymbol, tCurrencyOrder.getCoin()));
            //判断是否需要创建资产账户
            if (Objects.isNull(tAppAsset)) {
                tAppAssetService.createAsset(user, tCurrencyOrder.getCoin(), AssetEnum.PLATFORM_ASSETS.getCode());
            }
        }
        //查询交易币种资产
        TAppAsset addAsset = tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId, userId).eq(TAppAsset::getSymbol, tCurrencyOrder.getSymbol()).eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()));
        //查询结算币种资产
        TAppAsset subtractAsset = tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId, userId).eq(TAppAsset::getSymbol, tCurrencyOrder.getCoin()).eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()));

        BigDecimal delegatePrice = tCurrencyOrder.getDelegatePrice();
        BigDecimal delegateTotal = tCurrencyOrder.getDelegateTotal();
        BigDecimal delegateValue = tCurrencyOrder.getDelegateValue();
        BigDecimal ratio = currencySymbol.getFeeRate().divide(new BigDecimal("100"));
        BigDecimal dealNum = BigDecimal.ZERO;

        //可用资产
        BigDecimal availableAsset = BigDecimal.ZERO;
        String msgsSymbol = "";
        if (tCurrencyOrder.getType() == 0) {
            availableAsset = subtractAsset.getAvailableAmount();
            msgsSymbol = subtractAsset.getSymbol();
            if (availableAsset.compareTo(delegateValue) < 0) {
                return MessageUtils.message("currency.balance.deficiency", msgsSymbol.toUpperCase());
            }
        } else {
            availableAsset = addAsset.getAvailableAmount();
            msgsSymbol = addAsset.getSymbol();
            if (availableAsset.compareTo(delegateTotal) < 0) {
                return MessageUtils.message("currency.balance.deficiency", msgsSymbol.toUpperCase());
            }
        }
        //根据type 和 delegateType 整合数据
        String serialNo = "Y" + OrderUtils.generateOrderNum();
        tCurrencyOrder.setAdminParentIds(user.getAdminParentIds());
        tCurrencyOrder.setUserId(userId);
        tCurrencyOrder.setOrderNo(serialNo);
        tCurrencyOrder.setDealPrice(settlePrice);
        if (tCurrencyOrder.getType() == 0) {
            if (delegateType == 0) {
                if (checkPrice(tCurrencyOrder.getType(), settlePrice, delegatePrice)) {
                    dealNum = delegateTotal;
                    tCurrencyOrder.setDealNum(delegateTotal);
                    tCurrencyOrder.setDealValue(settlePrice.multiply(delegateTotal).setScale(6, RoundingMode.DOWN));
                    tCurrencyOrder.setFee(delegateTotal.multiply(ratio));

                    BigDecimal subtractPrice = tCurrencyOrder.getDealValue();
                    BigDecimal addPrice = dealNum.subtract(dealNum.multiply(ratio));
                    assembleCurrencyOrder(tCurrencyOrder, symbol, coin, userId, subtractPrice, addPrice, subtractAsset, addAsset);
                } else {
                    combinationCurrencyOrder(tCurrencyOrder, ratio, userId);
                }
            } else {
                dealNum = delegateValue.divide(settlePrice, 6, RoundingMode.DOWN);
                tCurrencyOrder.setDelegateTotal(dealNum);
                tCurrencyOrder.setDelegatePrice(settlePrice);
                tCurrencyOrder.setDelegateValue(delegateValue);
                tCurrencyOrder.setDealNum(dealNum);
                tCurrencyOrder.setDealValue(delegateValue);
                tCurrencyOrder.setFee(dealNum.multiply(ratio));
                BigDecimal subtractPrice = delegateValue;
                BigDecimal addPrice = dealNum.subtract(tCurrencyOrder.getFee());
                assembleCurrencyOrder(tCurrencyOrder, symbol, coin, userId, subtractPrice, addPrice, subtractAsset, addAsset);
            }
        } else {
            if (delegateType == 0) {
                if (checkPrice(tCurrencyOrder.getType(), settlePrice, delegatePrice)) {
                    tCurrencyOrder.setDealNum(delegateTotal);
                    tCurrencyOrder.setDealValue(settlePrice.multiply(delegateTotal).setScale(6, RoundingMode.DOWN));
                    tCurrencyOrder.setFee(tCurrencyOrder.getDealValue().multiply(ratio));
                    BigDecimal subtractPrice = tCurrencyOrder.getDealNum();
                    BigDecimal addPrice = tCurrencyOrder.getDealValue().subtract(tCurrencyOrder.getFee());
                    assembleCurrencyOrder(tCurrencyOrder, coin, symbol, userId, subtractPrice, addPrice, addAsset, subtractAsset);
                } else {
                    combinationCurrencyOrder(tCurrencyOrder, ratio, userId);
                }
            } else {
                tCurrencyOrder.setDealNum(delegateTotal);
                tCurrencyOrder.setDealValue(settlePrice.multiply(delegateTotal).setScale(6, RoundingMode.DOWN));
                tCurrencyOrder.setDelegatePrice(settlePrice);
                tCurrencyOrder.setDelegateValue(tCurrencyOrder.getDealValue());
                tCurrencyOrder.setFee(tCurrencyOrder.getDealValue().multiply(ratio));
                BigDecimal subtractPrice = delegateTotal;
                BigDecimal addPrice = tCurrencyOrder.getDealValue().subtract(tCurrencyOrder.getFee());
                assembleCurrencyOrder(tCurrencyOrder, coin, symbol, userId, subtractPrice, addPrice, addAsset, subtractAsset);
            }
        }
        return "success";
    }

    /**
     * 币种校验
     *
     * @param tCurrencyOrder
     * @param currencySymbol
     * @param settlePrice
     * @return
     */
    @Override
    public String checkOrder(TCurrencyOrder tCurrencyOrder, TCurrencySymbol currencySymbol, BigDecimal settlePrice) {


        Integer type = tCurrencyOrder.getType();

        Integer deleType=tCurrencyOrder.getDelegateType();
        //
        BigDecimal minSell = Objects.isNull(currencySymbol.getMinSell())?BigDecimal.ZERO:currencySymbol.getMinSell();

        if (type == 1) {
            if (tCurrencyOrder.getDelegateTotal().compareTo(minSell) < 0) {
                return MessageUtils.message("order.sell.min.error", minSell);
            }
        } else {
            if(deleType==1){
                tCurrencyOrder.setDelegateTotal(tCurrencyOrder.getDelegateValue().divide(settlePrice, 6, RoundingMode.DOWN));
            }
            //最小下单量
            if (tCurrencyOrder.getDelegateTotal().compareTo(currencySymbol.getOrderMin()) < 0) {
                return MessageUtils.message("currency.order.min.error", currencySymbol.getOrderMin());
            }
            //最大下单量
            if (tCurrencyOrder.getDelegateTotal().compareTo(currencySymbol.getOrderMax()) > 0) {
                MessageUtils.message("currency.order.max.error", currencySymbol.getOrderMax());
            }
        }
//        Integer delegateType = tCurrencyOrder.getDelegateType();
//        if (currencySymbol.getIsDeal().equals("2")){
//            return MessageUtils.message("currency.deal.error");
//        }
//        if (type==0){
//            if (delegateType==0){
//                //限价买 1=可以 2=不可以
//                if (currencySymbol.getLimitedBuy().equals("2")){
//                    return MessageUtils.message("currency.limited.buy.error");
//                }
//                //最高买单价
//                if (tCurrencyOrder.getDelegatePrice().compareTo(currencySymbol.getBuyMax())>0){
//
//                    return MessageUtils.message("currency.buy.max.error",currencySymbol.getBuyMax());
//                }
//                //最小下单量
//                if (tCurrencyOrder.getDelegateTotal().compareTo(currencySymbol.getOrderMin())<0 ){
//
//                    return MessageUtils.message("currency.order.min.error",currencySymbol.getOrderMin());
//                }
//                //最大下单量
//                if (tCurrencyOrder.getDelegateTotal().compareTo(currencySymbol.getOrderMax())>0){
//                    MessageUtils.message("currency.order.max.error",currencySymbol.getOrderMax());
//                }


//                //市价买 1=可以 2=不可以
//                if (currencySymbol.getMarketBuy().equals("2")){
//
//                    return MessageUtils.message("currency.market.buy.error");
//                }
//                //最高买单价
////                if (settlePrice.compareTo(currencySymbol.getBuyMax())>0){
////                    return MessageUtils.message("currency.buy.max.error",currencySymbol.getBuyMax());
////                }
//            }
//        }else{
//            //最小下单量
//            if (tCurrencyOrder.getDelegateTotal().compareTo(currencySymbol.getOrderMin())<0 ){
//                return MessageUtils.message("currency.order.min.error",currencySymbol.getOrderMin());
//            }
//            //最大下单量
//            if (tCurrencyOrder.getDelegateTotal().compareTo(currencySymbol.getOrderMax())>0){
//                MessageUtils.message("currency.order.max.error",currencySymbol.getOrderMax());
//            }
//            if (delegateType==0){
//                //限价卖 1=可以 2=不可以
//                if (currencySymbol.getLimitedBuy().equals("2")){
//                    return MessageUtils.message("currency.limited.sell.error");
//                }
//                //最底卖单价
//                if (tCurrencyOrder.getDelegatePrice().compareTo(currencySymbol.getSellMin())<0){
//
//                    return MessageUtils.message("currency.sell.min.error",currencySymbol.getSellMin());
//                }
//            }else{
//                //市价卖 1=可以 2=不可以
//                if (currencySymbol.getMarketSell().equals("2")){
//
//                    return MessageUtils.message("currency.market.sell.error");
//                }
//                //最底卖单价
////                if (settlePrice.compareTo(currencySymbol.getSellMin())<0){
////
////                    return MessageUtils.message("currency.sell.min.error",currencySymbol.getSellMin());
////                }
//            }
//        }
        return "success";
    }

    /**
     * @param symbol
     * @param coin
     * @param userId        用户id
     * @param subtractPrice 交易币种要➖的金额
     * @param addPrice      结算币种要➕的金额
     * @param subtractAsset 用户交易币种资产
     * @param addAsset      用户结算币种资产
     */
    @Override
    public void assembleCurrencyOrder(TCurrencyOrder tCurrencyOrder, String symbol, String coin, Long
            userId, BigDecimal subtractPrice, BigDecimal addPrice, TAppAsset subtractAsset, TAppAsset addAsset) {
        TAppUser appUser = tAppUserService.getById(userId);
        tCurrencyOrder.setUserId(userId);
        tCurrencyOrder.setDelegateTime(DateUtils.getNowDate());
        tCurrencyOrder.setDealTime(DateUtils.getNowDate());
        tCurrencyOrder.setCreateTime(DateUtils.getNowDate());
        tCurrencyOrder.setUpdateTime(DateUtils.getNowDate());
        tCurrencyOrder.setStatus(1);
        tCurrencyOrderMapper.insert(tCurrencyOrder);
        tAppAssetService.reduceAssetByUserId(userId, coin, subtractPrice);
        tAppAssetService.addAssetByUserId(userId, symbol, addPrice);
        appWalletRecordService.generateRecord(userId, subtractPrice, RecordEnum.CURRENCY_TRADINGSUB.getCode(), null, tCurrencyOrder.getOrderNo(), "币币交易-", subtractAsset.getAvailableAmount(), subtractAsset.getAvailableAmount().subtract(subtractPrice), coin, appUser.getAdminParentIds());
        appWalletRecordService.generateRecord(userId, addPrice, RecordEnum.CURRENCY_TRADINGADD.getCode(), null, tCurrencyOrder.getOrderNo(), "币币交易+", addAsset.getAvailableAmount(), addAsset.getAvailableAmount().add(addPrice), symbol, appUser.getAdminParentIds());

        //币币打码
        Setting setting = settingService.get(SettingEnum.ADD_MOSAIC_SETTING.name());
        if (Objects.nonNull(setting)){
            AddMosaicSetting addMosaic = JSONUtil.toBean(setting.getSettingValue(), AddMosaicSetting.class);
            if (Objects.nonNull(addMosaic) && Objects.nonNull(addMosaic.getIsOpen()) && addMosaic.getIsOpen() && Objects.nonNull(addMosaic.getCurrencyIsOpen()) && addMosaic.getCurrencyIsOpen()){
                BigDecimal price = BigDecimal.ONE;
                try {
                    if (!coin.equals("usdt")){
                        price = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + coin.toLowerCase());
                        appUser.setTotleAmont(appUser.getTotleAmont().add(subtractPrice.multiply(price)));
                    }else{
                        appUser.setTotleAmont(appUser.getTotleAmont().add(subtractPrice));
                    }
                    tAppUserService.updateTotleAmont(appUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 限价没满足条件的操作
     *
     * @param tCurrencyOrder
     * @param ratio
     * @param userId
     */
    public void combinationCurrencyOrder(TCurrencyOrder tCurrencyOrder, BigDecimal ratio, Long userId) {
        tCurrencyOrder.setCreateTime(DateUtils.getNowDate());
        tCurrencyOrder.setUpdateTime(DateUtils.getNowDate());
        tCurrencyOrder.setDelegateTime(DateUtils.getNowDate());
        tCurrencyOrder.setStatus(0);
        tCurrencyOrder.setFee(ratio);
        tCurrencyOrder.setDelegateTotal(tCurrencyOrder.getDelegateTotal());
        tCurrencyOrder.setDelegatePrice(tCurrencyOrder.getDelegatePrice());
        tCurrencyOrder.setDelegateValue(tCurrencyOrder.getDelegateValue());
        tCurrencyOrder.setDealNum(BigDecimal.ZERO);
        tCurrencyOrder.setDealValue(BigDecimal.ZERO);
        tCurrencyOrder.setDealPrice(BigDecimal.ZERO);
        tCurrencyOrderMapper.insert(tCurrencyOrder);
        tAppAssetService.occupiedAssetByUserId(userId, tCurrencyOrder.getType() == 0 ? tCurrencyOrder.getCoin() : tCurrencyOrder.getSymbol(), tCurrencyOrder.getType() == 0 ? tCurrencyOrder.getDelegateValue() : tCurrencyOrder.getDelegateTotal());
    }

    /**
     * 扯单
     *
     * @param currencyOrder
     * @return
     */
    @Override
    @Transactional
    public int canCelOrder(TCurrencyOrder currencyOrder) {
        //买入撤单
        TAppAsset asset = tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>()
                .eq(TAppAsset::getUserId, currencyOrder.getUserId())
                .eq(TAppAsset::getSymbol, currencyOrder.getSymbol())
                .eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()));

        TAppAsset usdtAsset = tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>()
                .eq(TAppAsset::getUserId, currencyOrder.getUserId())
                .eq(TAppAsset::getSymbol, currencyOrder.getCoin())
                .eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()));

        Integer type = currencyOrder.getType();

        BigDecimal delegateTotal = currencyOrder.getDelegateTotal();

        BigDecimal delegateVale = currencyOrder.getDelegateValue();

        if (0 == type) {
            usdtAsset.setAvailableAmount(usdtAsset.getAvailableAmount().add(delegateVale));
            usdtAsset.setOccupiedAmount(usdtAsset.getOccupiedAmount().subtract(delegateVale));
            tAppAssetService.updateByUserId(usdtAsset);
        } else if (1 == type) {
            asset.setAvailableAmount(asset.getAvailableAmount().add(delegateTotal));
            asset.setOccupiedAmount(asset.getOccupiedAmount().subtract(delegateTotal));
            tAppAssetService.updateByUserId(asset);
        }
        currencyOrder.setStatus(3);
        int i = tCurrencyOrderMapper.updateTCurrencyOrder(currencyOrder);
        return i;
    }

    @Override
    public List<TCurrencyOrder> selectOrderList(TCurrencyOrder tCurrencyOrder) {
        List<TCurrencyOrder> tCurrencyOrders = tCurrencyOrderMapper.selectOrderList(tCurrencyOrder);
        for (TCurrencyOrder currencyOrder : tCurrencyOrders) {
            QueryWrapper<TCurrencySymbol> queryWrapper = new QueryWrapper<TCurrencySymbol>();
            queryWrapper.eq("UPPER(coin)", currencyOrder.getSymbol().toUpperCase());
            TCurrencySymbol tCurrencySymbol = tCurrencySymbolMapper.selectOne(queryWrapper);
        }

        return tCurrencyOrders;
    }


    /**
     * 判断是否符合限价的操作
     *
     * @param type
     * @param settlePrice
     * @param delegatePrice
     * @return
     */
    @Override
    public boolean checkPrice(Integer type, BigDecimal settlePrice, BigDecimal delegatePrice) {
        if (0 == type) {
            //限价于小于当前价
            if (delegatePrice.compareTo(settlePrice) >= 0) {
                return true;
            }
        } else {
            //限价大于等于当前价
            if (delegatePrice.compareTo(settlePrice) <= 0) {
                return true;
            }
        }
        return false;
    }
}
