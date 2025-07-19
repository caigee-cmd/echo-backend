package com.ruoyi.bussiness.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.AddMosaicSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.*;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TMingOrderMapper;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.management.ObjectName;

/**
 * mingService业务层处理
 *
 * @author ruoyi
 * @date 2023-08-18
 */
@Service
public class TMingOrderServiceImpl extends ServiceImpl<TMingOrderMapper, TMingOrder> implements ITMingOrderService {
    @Autowired
    private TMingOrderMapper tMingOrderMapper;

    @Resource
    private ITAppUserService appUserService;

    @Resource
    private ITAppAssetService appAssetService;

    @Resource
    private ITAppWalletRecordService appWalletRecordService;

    @Resource
    private ITMingProductService mingProductService;
    @Resource
    private ITMingProductUserService mingProductUserService;
    @Resource
    private SettingService settingService;

    /**
     * 查询ming
     *
     * @param id ming主键
     * @return ming
     */
    @Override
    public TMingOrder selectTMingOrderById(Long id) {
        return tMingOrderMapper.selectTMingOrderById(id);
    }

    /**
     * 查询ming列表
     *
     * @param tMingOrder ming
     * @return ming
     */
    @Override
    public List<TMingOrder> selectTMingOrderList(TMingOrder tMingOrder) {
        return tMingOrderMapper.selectTMingOrderList(tMingOrder);
    }

    /**
     * 新增ming
     *
     * @param tMingOrder ming
     * @return 结果
     */
    @Override
    public int insertTMingOrder(TMingOrder tMingOrder) {
        tMingOrder.setCreateTime(DateUtils.getNowDate());
        return tMingOrderMapper.insertTMingOrder(tMingOrder);
    }

    /**
     * 修改ming
     *
     * @param tMingOrder ming
     * @return 结果
     */
    @Override
    public int updateTMingOrder(TMingOrder tMingOrder) {
        tMingOrder.setUpdateTime(DateUtils.getNowDate());
        return tMingOrderMapper.updateTMingOrder(tMingOrder);
    }

    /**
     * 批量删除ming
     *
     * @param ids 需要删除的ming主键
     * @return 结果
     */
    @Override
    public int deleteTMingOrderByIds(Long[] ids) {
        return tMingOrderMapper.deleteTMingOrderByIds(ids);
    }

    /**
     * 删除ming信息
     *
     * @param id ming主键
     * @return 结果
     */
    @Override
    public int deleteTMingOrderById(Long id) {
        return tMingOrderMapper.deleteTMingOrderById(id);
    }

    @Override
    public String bugMingOrder(Long planId, BigDecimal amount, Long userId) {
        TAppUser user = appUserService.selectTAppUserByUserId(userId);
        TAppUserDetail tAppUserDetail = appUserService.selectUserDetailByUserId(userId);
        Integer tradeFlag = tAppUserDetail.getTradeFlag();
        if (null != tradeFlag) {
            if (1 == tradeFlag) {
                if (tAppUserDetail.getTradeMessage() == null || tAppUserDetail.getTradeMessage().equals("")) {
                    return MessageUtils.message("user.push.message");
                } else {
                    return tAppUserDetail.getTradeMessage();
                }
            }
        }
        if (Objects.nonNull(user.getIsLimitation())){
            if (user.getIsLimitation() == 2){
                return MessageUtils.message("user_is_limitation");
            }
        }
        TAppAsset tAppAsset = appAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()).eq(TAppAsset::getUserId, userId).eq(TAppAsset::getSymbol, "usdt"));
        TMingProduct tMingProduct = mingProductService.selectTMingProductById(planId);
        TMingProductUser tMingProductUser = mingProductUserService.getOne(new LambdaQueryWrapper<TMingProductUser>()
                .eq(TMingProductUser::getProductId, planId).eq(TMingProductUser::getAppUserId, userId));
        //判断有没有个人购买限制
        if (Objects.nonNull(tMingProductUser) && Objects.nonNull(tMingProductUser.getPledgeNum())) tMingProduct.setTimeLimit(tMingProductUser.getPledgeNum());
        // 2. 金额判断
        if (Objects.isNull(tAppAsset)) {
            return MessageUtils.message("order_amount_error");
        }
        if (tAppAsset.getAvailableAmount().compareTo(amount) < 0) {
            return MessageUtils.message("order_amount_error");
        }
        // 单个用户单笔下注最小金额 和最大金额
        if (null != tMingProduct.getLimitMin()) {
            if (amount.compareTo(tMingProduct.getLimitMin()) < 0) {
                return MessageUtils.message("order.single.min");
            }
        }
        if (null != tMingProduct.getLimitMax()) {
            if (tMingProduct.getLimitMax().compareTo(amount) < 0) {
                return MessageUtils.message("order.single.max");
            }
        }
        int count = this.count(new LambdaQueryWrapper<TMingOrder>().eq(TMingOrder::getPlanId, tMingProduct.getId()).eq(TMingOrder::getUserId, userId));
        if (count >= tMingProduct.getTimeLimit()) {
            return MessageUtils.message("financial.count.max");
        }
        String days = tMingProduct.getDays();
        TMingOrder mineOrder = new TMingOrder();
        mineOrder.setUserId(userId);
        mineOrder.setPlanId(planId);
        Date now = new DateTime();
        // 下单
        String serialId = "E" + OrderUtils.generateOrderNum();
        Date endTime = DateUtil.dateFormatDay(now, Integer.valueOf(days));
        //当日利率从接口获取，现在取最小利率
        mineOrder.setOrderNo(serialId);
        mineOrder.setPlanId(tMingProduct.getId());
        mineOrder.setCreateTime(new Date());
        mineOrder.setDays(Integer.valueOf(days));
        mineOrder.setAmount(amount);
        mineOrder.setAccumulaEarn(BigDecimal.ZERO);
        mineOrder.setEndTime(endTime);
        mineOrder.setPlanTitle(tMingProduct.getTitle());
        mineOrder.setMinOdds(tMingProduct.getMinOdds());
        mineOrder.setMaxOdds(tMingProduct.getMaxOdds());
        mineOrder.setAdminUserIds(user.getAdminParentIds());
        mineOrder.setUserId(user.getUserId());
        mineOrder.setStatus(0);
        //  先扣钱，再下单
        BigDecimal bigDecimal = tAppAsset.getAvailableAmount();
        tAppAsset.setAmout(tAppAsset.getAmout().subtract(amount));
        tAppAsset.setAvailableAmount(bigDecimal.subtract(amount));
        appAssetService.updateTAppAsset(tAppAsset);
        appWalletRecordService.generateRecord(user.getUserId(), amount, RecordEnum.MINING_TO_BUY.getCode(), user.getLoginName(), serialId, "", bigDecimal, bigDecimal.subtract(amount), "USDT", user.getAdminParentIds());
        tMingOrderMapper.insert(mineOrder);
        //质押打码
        Setting setting = settingService.get(SettingEnum.ADD_MOSAIC_SETTING.name());
        if (Objects.nonNull(setting)){
            AddMosaicSetting addMosaic = JSONUtil.toBean(setting.getSettingValue(), AddMosaicSetting.class);
            if (Objects.nonNull(addMosaic) && Objects.nonNull(addMosaic.getIsOpen()) && addMosaic.getIsOpen() && Objects.nonNull(addMosaic.getPledgeIsOpen()) && addMosaic.getPledgeIsOpen()){
                user.setTotleAmont(user.getTotleAmont().add(mineOrder.getAmount()));
                appUserService.updateTotleAmont(user);
            }
        }
        return "success";
    }

    @Override
    public Map<String, Object> selectMingOrderSumList(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        return tMingOrderMapper.selectMingOrderSumList(params);
    }

    @Override
    public String redemption(Long id) {
        //质押赎回
        TMingOrder tMingOrder = tMingOrderMapper.selectTMingOrderById(id);
        //项目id
        Long planId = tMingOrder.getPlanId();
        //投资金额
        BigDecimal amount = tMingOrder.getAmount();
        TMingProduct product = mingProductService.getById(planId);
        //违约利率
        BigDecimal defaultOdds = product.getDefaultOdds();
        //违约金
        BigDecimal multiply = amount.multiply(defaultOdds);
        //退还金额
        BigDecimal subtract = amount.subtract(multiply);

        TAppUser user = appUserService.selectTAppUserByUserId(tMingOrder.getUserId());
        TAppAsset tAppAsset = appAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()).eq(TAppAsset::getUserId, tMingOrder.getUserId()).eq(TAppAsset::getSymbol, "usdt"));
        BigDecimal bigDecimal = tAppAsset.getAvailableAmount();
        tAppAsset.setAmout(tAppAsset.getAmout().add(subtract));
        tAppAsset.setAvailableAmount(bigDecimal.add(subtract));
        appAssetService.updateTAppAsset(tAppAsset);
        appWalletRecordService.generateRecord(tMingOrder.getUserId(), subtract, RecordEnum.MINING_REDEMPTION.getCode(), user.getLoginName(), tMingOrder.getOrderNo(), "", bigDecimal, bigDecimal.add(subtract), "USDT", user.getAdminParentIds());
        tMingOrder.setStatus(2);
        this.updateTMingOrder(tMingOrder);
        return "";
    }

    @Override
    public String redemption(Long id, String flag) {
        //质押赎回
        TMingOrder tMingOrder = tMingOrderMapper.selectTMingOrderById(id);
        //投资金额
        BigDecimal amount = tMingOrder.getAmount();

        TAppUser user = appUserService.selectTAppUserByUserId(tMingOrder.getUserId());
        TAppAsset tAppAsset = appAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()).eq(TAppAsset::getUserId, tMingOrder.getUserId()).eq(TAppAsset::getSymbol, "usdt"));
        BigDecimal bigDecimal = tAppAsset.getAvailableAmount();
        tAppAsset.setAmout(tAppAsset.getAmout().add(amount));
        tAppAsset.setAvailableAmount(bigDecimal.add(amount));
        appAssetService.updateTAppAsset(tAppAsset);
        appWalletRecordService.generateRecord(tMingOrder.getUserId(), amount, RecordEnum.MINING_REDEMPTION.getCode(), user.getLoginName(), tMingOrder.getOrderNo(), "", bigDecimal, bigDecimal.add(amount), "USDT", user.getAdminParentIds());
        tMingOrder.setStatus(2);
        tMingOrder.setAccumulaEarn(BigDecimal.ZERO);
        this.updateTMingOrder(tMingOrder);
        return "";
    }
}

