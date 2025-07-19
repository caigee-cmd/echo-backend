package com.ruoyi.bussiness.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.AddMosaicSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.mapper.TContractOrderMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderUtils;
import com.ruoyi.common.utils.ucontract.ContractComputerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * U本位委托Service业务层处理
 *
 * @author michael
 * @date 2023-07-20
 */
@Service
@Slf4j
public class TContractOrderServiceImpl extends ServiceImpl<TContractOrderMapper, TContractOrder> implements ITContractOrderService {
    @Resource
    private TContractOrderMapper tContractOrderMapper;

    @Resource
    private ITContractCoinService contractCoinService;
    @Resource
    private ITContractPositionService contractPositionService;

    @Resource
    private ITAppAssetService appAssetService;

    @Resource
    private ITAppWalletRecordService appWalletRecordService;

    @Resource
    private ITAppUserService appUserService;

    @Resource
    private RedisCache redisCache;
    @Resource
    private SettingService settingService;

    /**
     * 查询U本位委托
     *
     * @param id U本位委托主键
     * @return U本位委托
     */
    @Override
    public TContractOrder selectTContractOrderById(Long id) {
        return tContractOrderMapper.selectTContractOrderById(id);
    }

    /**
     * 查询U本位委托列表
     *
     * @param tContractOrder U本位委托
     * @return U本位委托
     */
    @Override
    public List<TContractOrder> selectTContractOrderList(TContractOrder tContractOrder) {
        return tContractOrderMapper.selectTContractOrderList(tContractOrder);
    }

    /**
     * 新增U本位委托
     *
     * @param tContractOrder U本位委托
     * @return 结果
     */
    @Override
    public int insertTContractOrder(TContractOrder tContractOrder) {
        tContractOrder.setCreateTime(DateUtils.getNowDate());
        return tContractOrderMapper.insertTContractOrder(tContractOrder);
    }

    /**
     * 修改U本位委托
     *
     * @param tContractOrder U本位委托
     * @return 结果
     */
    @Override
    public int updateTContractOrder(TContractOrder tContractOrder) {
        tContractOrder.setUpdateTime(DateUtils.getNowDate());
        return tContractOrderMapper.updateTContractOrder(tContractOrder);
    }

    /**
     * 批量删除U本位委托
     *
     * @param ids 需要删除的U本位委托主键
     * @return 结果
     */
    @Override
    public int deleteTContractOrderByIds(Long[] ids) {
        return tContractOrderMapper.deleteTContractOrderByIds(ids);
    }

    /**
     * 删除U本位委托信息
     *
     * @param id U本位委托主键
     * @return 结果
     */
    @Override
    public int deleteTContractOrderById(Long id) {
        return tContractOrderMapper.deleteTContractOrderById(id);
    }

    /**
     * @param symbol
     * @param leverage
     * @param delegatePrice
     * @param delegateTotal
     * @param userId
     * @param type
     * @param delegateType
     * @return
     */
    @Override
    public String buyContractOrder(String symbol, BigDecimal leverage, BigDecimal delegatePrice, BigDecimal delegateTotal, Long userId, Integer type, Integer delegateType) {
        if(delegateType==1){
            log.info("前端传入实时价格"+delegatePrice);
            delegatePrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + symbol);
            log.info("获取实时价格"+delegatePrice);
        }
       //校验
        String result=verifySubmit(symbol, leverage, delegatePrice, delegateTotal, userId.longValue());
        if (!"success".equals(result)) {
            return result;
        }
        String serialId = "U" + OrderUtils.generateOrderNum();
        TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(symbol);
        TAppUser user = appUserService.selectTAppUserByUserId(userId);
        TAppAsset tAppAsset = appAssetService.getAssetByUserIdAndType(userId, AssetEnum.CONTRACT_ASSETS.getCode());
        BigDecimal beforeMount = tAppAsset.getAvailableAmount();
        BigDecimal shareNumber = tContractCoin.getShareNumber();
        BigDecimal num = shareNumber.multiply(delegateTotal);
        BigDecimal amount = ContractComputerUtil.getAmount(delegatePrice, num, leverage);
        BigDecimal openFee = tContractCoin.getOpenFee().multiply(amount).setScale(6, RoundingMode.HALF_UP);

        // 判断是否限制
        if (Objects.nonNull(user.getIsLimitation())){
            if(user.getIsLimitation() == 2)
                return MessageUtils.message("user_is_limitation");
        }

        BigDecimal closePrice = ContractComputerUtil.getStrongPrice(leverage, type, delegatePrice, num, amount,openFee);
        BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + symbol);
         //市价
        if (delegateType == 1) {
            //扣除金额
            tAppAsset.setAmout(tAppAsset.getAmout().subtract(amount));
            tAppAsset.setAvailableAmount(tAppAsset.getAvailableAmount().subtract(amount));
            createPosition(symbol, delegatePrice, closePrice, amount, num, leverage, type, delegateType, openFee, userId, serialId,user.getAdminParentIds(),0);
            appAssetService.updateTAppAsset(tAppAsset);
            appWalletRecordService.generateRecord(userId, amount, RecordEnum.CONTRACT_TRANSACTIONSUB.getCode(), user.getLoginName(), serialId, "合约交易", beforeMount, beforeMount.subtract(amount), tContractCoin.getBaseCoin().toLowerCase(),user.getAdminParentIds());
        }else{
            if (comparePrice(type, currentlyPrice, delegatePrice)) {
                //扣除金额
                amount=ContractComputerUtil.getAmount(currentlyPrice, num, leverage);
                openFee=tContractCoin.getOpenFee().multiply(amount).setScale(6, RoundingMode.HALF_UP);
                closePrice = ContractComputerUtil.getStrongPrice(leverage, type, currentlyPrice, num, amount, openFee);
                tAppAsset.setAmout(tAppAsset.getAmout().subtract(amount));
                tAppAsset.setAvailableAmount(tAppAsset.getAvailableAmount().subtract(amount));
                createPosition(symbol, currentlyPrice, closePrice, amount, num, leverage, type, delegateType, openFee, userId, serialId, user.getAdminParentIds(),0);
                appAssetService.updateTAppAsset(tAppAsset);
                appWalletRecordService.generateRecord(userId, amount, RecordEnum.CONTRACT_TRANSACTIONSUB.getCode(), user.getLoginName(), serialId, "合约交易", beforeMount, beforeMount.subtract(amount), tContractCoin.getBaseCoin().toLowerCase(),user.getAdminParentIds());
            }else {
                createContractOrder(symbol,delegatePrice,amount,num,leverage,type,delegateType,openFee,userId,serialId,user.getAdminParentIds());
            }
        }
        return "success";
    }

    /**
     * @param symbol
     * @param leverage
     * @param delegatePrice
     * @param delegateTotal
     * @param userId
     * @return
     */
    @Override
    public String verifySubmit(String symbol, BigDecimal leverage, BigDecimal delegatePrice, BigDecimal delegateTotal, Long userId) {

        TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(symbol);
        BigDecimal shareNumber = tContractCoin.getShareNumber();
        BigDecimal minShare = tContractCoin.getMinShare();
        BigDecimal maxShare = tContractCoin.getMaxShare();
        BigDecimal num = shareNumber.multiply(delegateTotal);
        //合约账户
        TAppAsset tAppAsset = appAssetService.getAssetByUserIdAndType(userId, AssetEnum.CONTRACT_ASSETS.getCode());
        if (Objects.isNull(tAppAsset)) {
            return MessageUtils.message("contract.accont.error");
        }
        BigDecimal availableAmount = tAppAsset.getAvailableAmount();
        BigDecimal amount = ContractComputerUtil.getAmount(delegatePrice, num, leverage);
        //判断余额
        if (amount.compareTo(availableAmount) > 0) {
            return MessageUtils.message("contract.accont.error");
        }
        if (delegateTotal.compareTo(minShare) < 0) {
            return MessageUtils.message("contract.min.share",minShare);
        }
        if (delegateTotal.compareTo(maxShare) > 0) {
            return MessageUtils.message("contract.max.share",maxShare);
        }
        return "success";
    }

    /**
     * @param id
     * @return
     */
    @Override
    public String canCelOrder(Long id) {

        TContractOrder tContractOrder = tContractOrderMapper.selectOne(new LambdaQueryWrapper<TContractOrder>()
                .eq(TContractOrder::getId,id).eq(TContractOrder::getUserId,StpUtil.getLoginIdAsLong())
                .eq(TContractOrder::getStatus,0));
        if(tContractOrder == null){
            return MessageUtils.message("order.status.error");
        }

//        if(tContractOrder.getStatus()==3){
//            return MessageUtils.message("order.status.error");
//        }

        TAppAsset tAppAsset = appAssetService.getAssetByUserIdAndType(tContractOrder.getUserId(), AssetEnum.CONTRACT_ASSETS.getCode());
        tAppAsset.setAvailableAmount(tAppAsset.getAvailableAmount().add(tContractOrder.getDelegateValue()));
        tAppAsset.setOccupiedAmount(tAppAsset.getOccupiedAmount().subtract(tContractOrder.getDelegateValue()));
        appAssetService.updateTAppAsset(tAppAsset);
        tContractOrder.setStatus(3);
        tContractOrderMapper.updateTContractOrder(tContractOrder);
        return "success";
    }

    //创建仓位对象
    private TContractPosition createPosition(String symbol, BigDecimal openPrice, BigDecimal closePrice, BigDecimal amount, BigDecimal num, BigDecimal level, Integer type, Integer delegateType, BigDecimal openFee, Long uerId, String serialId, String adminParentIds,Integer deliveryDays) {
        TContractPosition tContractPosition = new TContractPosition();
        BigDecimal bigDecimal = amount.subtract(openFee);
        tContractPosition.setSymbol(symbol);
        tContractPosition.setAmount(bigDecimal);
        tContractPosition.setAdjustAmount(bigDecimal);
        tContractPosition.setLeverage(level);
        tContractPosition.setClosePrice(closePrice);
        tContractPosition.setOpenNum(num);
        tContractPosition.setOpenPrice(openPrice);
        tContractPosition.setOpenFee(openFee);
        tContractPosition.setStatus(0);
        tContractPosition.setType(type);
        tContractPosition.setDelegateType(delegateType);
        tContractPosition.setCreateTime(new Date());
        tContractPosition.setRemainMargin(bigDecimal);
        tContractPosition.setOrderNo(serialId);
        tContractPosition.setUserId(uerId);
        tContractPosition.setEntrustmentValue(openPrice.multiply(num).setScale(6,RoundingMode.HALF_UP));
        tContractPosition.setAdminParentIds(adminParentIds);
        tContractPosition.setDeliveryDays(0);
        tContractPosition.setSubTime(new Date());
        contractPositionService.save(tContractPosition);

        //u本位打码
        TAppUser tAppUser = appUserService.getById(uerId);
        Setting setting = settingService.get(SettingEnum.ADD_MOSAIC_SETTING.name());
        if (Objects.nonNull(setting)){
            AddMosaicSetting addMosaic = JSONUtil.toBean(setting.getSettingValue(), AddMosaicSetting.class);
            if (Objects.nonNull(addMosaic) && Objects.nonNull(addMosaic.getIsOpen()) && addMosaic.getIsOpen() && Objects.nonNull(addMosaic.getContractIsOpen()) && addMosaic.getContractIsOpen()){
                tAppUser.setTotleAmont(tAppUser.getTotleAmont().add(tContractPosition.getAmount()));
                appUserService.updateTotleAmont(tAppUser);
            }
        }

        return tContractPosition;
    }
    //创建委托对象
    private void createContractOrder(String symbol, BigDecimal openPrice, BigDecimal amount, BigDecimal num, BigDecimal level, Integer type, Integer delegateType, BigDecimal openFee, Long uerId, String serialId, String adminParentIds) {
        TContractOrder tContractOrder = new TContractOrder();
        tContractOrder.setSymbol(symbol);
        tContractOrder.setDelegatePrice(openPrice);
        tContractOrder.setDelegateValue(amount);
        tContractOrder.setOrderNo(serialId);
        tContractOrder.setDelegateTotal(num);
        tContractOrder.setDelegateType(delegateType);
        tContractOrder.setType(type);
        tContractOrder.setStatus(0);
        tContractOrder.setFee(openFee);
        tContractOrder.setCreateTime(new Date());
        tContractOrder.setDelegateTime(new Date());
        tContractOrder.setLeverage(level);
        tContractOrder.setUserId(uerId);
        tContractOrder.setAdminParentIds(adminParentIds);
        tContractOrderMapper.insert(tContractOrder);
        appAssetService.noSettlementAssetByUserId(uerId,"usdt",amount);
     }
    private boolean comparePrice(Integer type, BigDecimal currentlyPrice, BigDecimal delegatePrice) {
        if (0 == type) {
            //限价于大于当前价
            if (delegatePrice.compareTo(currentlyPrice) >= 0) {
                return true;
            }
        } else {
            //限价小于等于当前价
            if (delegatePrice.compareTo(currentlyPrice) <= 0) {
                return true;
            }
        }
        return false;
    }
}
