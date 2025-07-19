package com.ruoyi.bussiness.service.impl;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.AddMosaicSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.mapper.TMineFinancialMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
@Service
@Slf4j
public class TMineFinancialServiceImpl extends ServiceImpl<TMineFinancialMapper, TMineFinancial> implements ITMineFinancialService
{
    @Resource
    private TMineFinancialMapper tMineFinancialMapper;
    @Resource
    private ITAppUserService appUserService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ITAppAssetService tAppAssetService;
    @Resource
    private ITMineOrderService orderService;
    @Resource
    private ITMineUserService mineUserService;
    @Resource
    private ITAppWalletRecordService walletRecordService;
    @Resource
    private ITMineOrderDayService mineOrderDayService;
    @Resource
    private SettingService settingService;
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TMineFinancial selectTMineFinancialById(Long id)
    {
        return tMineFinancialMapper.selectTMineFinancialById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineFinancial 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TMineFinancial> selectTMineFinancialList(TMineFinancial tMineFinancial)
    {
        return tMineFinancialMapper.selectTMineFinancialList(tMineFinancial);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineFinancial 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTMineFinancial(TMineFinancial tMineFinancial)
    {
        tMineFinancial.setCreateTime(DateUtils.getNowDate());
        return tMineFinancialMapper.insertTMineFinancial(tMineFinancial);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineFinancial 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTMineFinancial(TMineFinancial tMineFinancial)
    {
        tMineFinancial.setUpdateTime(DateUtils.getNowDate());
        return tMineFinancialMapper.updateTMineFinancial(tMineFinancial);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineFinancialByIds(Long[] ids)
    {
        return tMineFinancialMapper.deleteTMineFinancialByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineFinancialById(Long id)
    {
        return tMineFinancialMapper.deleteTMineFinancialById(id);
    }

    @Override
    public String submit(Long planId, BigDecimal money, Long days) {
        {
            long userId = StpUtil.getLoginIdAsLong();
            TAppUser user = appUserService.selectTAppUserByUserId(userId);
            TAppUserDetail tAppUserDetail = appUserService.selectUserDetailByUserId(userId);
            TMineFinancial mineFinancial = tMineFinancialMapper.selectTMineFinancialById(planId);
            //用户等级
            Integer level = Objects.isNull(user.getLevel()) ? 0 : user.getLevel();
            //VIP等级
            if (ClassifyEnum.VIP.getCode().equals(mineFinancial.getClassify())) {
                if (level < mineFinancial.getLevel()) {
                    return MessageUtils.message("mine.level.error");
                }
            }
            if (Objects.nonNull(user.getIsLimitation())){
                if(user.getIsLimitation() == 2)
                    return MessageUtils.message("user_is_limitation");
            }
            // 交易限制
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
            // 1. 时间判断， 不能太频繁
            if (Boolean.FALSE.equals(redisCache.hasKey(CachePrefix.MINE_FINANCIAL.getPrefix() + user.getUserId()))) {
                redisCache.setCacheObject(CachePrefix.MINE_FINANCIAL.getPrefix()+user.getUserId(), System.currentTimeMillis(), 5000,
                        TimeUnit.MILLISECONDS);
            } else {
                return MessageUtils.message("order.10s_retry");
            }
            //资产
            TAppAsset tAppAsset = tAppAssetService.getAssetByUserIdAndType(user.getUserId(), AssetEnum.FINANCIAL_ASSETS.getCode());
            // 2. 金额判断
            if (Objects.isNull(tAppAsset)) {
                return MessageUtils.message("order.amount_error");
            }
            if (tAppAsset.getAvailableAmount().compareTo(money) < 0) {
                log.debug("钱包金额不足，无法下单, userId:{}, money:{}, wallet:{}", user.getUserId().toString(), money.toPlainString(), tAppAsset.getAvailableAmount().toPlainString());
                return MessageUtils.message("order.amount_error");
            }
            // 单个用户单笔下注最小金额 和最大金额
            if (null != mineFinancial.getLimitMin()) {
                if (money.compareTo(mineFinancial.getLimitMin()) < 0) {
                    return MessageUtils.message("order.single.min", mineFinancial.getLimitMin());
                }
            }
            if (null != mineFinancial.getLimitMax()) {
                if (mineFinancial.getLimitMax().compareTo(money) < 0) {
                    return MessageUtils.message("order.single.max", mineFinancial.getLimitMax());
                }
            }
            //剩余金额
            if(mineFinancial.getRemainAmount().compareTo(money)<0){
                return MessageUtils.message("order.buy.insufficient", mineFinancial.getRemainAmount());
            }

            if (mineFinancial.getStatus() == 0) {
                log.error("此用户异常,已下架，不可下注, userId:{}, 挖矿id:{},挖矿名称:{}", user.getUserId(), mineFinancial.getId(),
                        mineFinancial.getTitle());
                return MessageUtils.message("product.removed");
            }
            TMineOrder order = new TMineOrder();
            order.setAdress(user.getAddress());
            order.setPlanId(planId);
            //购买次数
            int count = orderService.count(new LambdaQueryWrapper<TMineOrder>().eq(TMineOrder::getUserId,userId).eq(TMineOrder::getPlanId,planId));
            //用户购买次数限制
            if(null == mineFinancial.getTimeLimit()){
                TMineUser result = mineUserService.getOne(new LambdaQueryWrapper<TMineUser>().eq(TMineUser::getUserId,userId).eq(TMineUser::getId,planId));
                if (Objects.nonNull(result)) {
                    mineFinancial.setTimeLimit(result.getTimeLimit());
                }
            }
            if (count >= mineFinancial.getTimeLimit()) {
                log.error("此用户已到达限制次数,不允许购买, userId:{}, 挖矿id:{},挖矿名称:{}", user.getUserId(), mineFinancial.getId(),
                        mineFinancial.getTitle());
                return MessageUtils.message("financial.count.max");
            }
            Date now = new DateTime();
            if (Objects.isNull(days)) {
                return MessageUtils.message("days.not.null");
            }
            String day = mineFinancial.getDays();
            if (!day.contains(days.toString())) {
                return MessageUtils.message("days.not.error");
            }
            // 下单
            String serialId = "E" + OrderUtils.generateOrderNum();
            Date endTime = DateUtils.dateFormatDay(now, days.intValue());
            //当日利率从接口获取，现在取最小利率
            TMineOrder mineOrder = new TMineOrder();
            mineOrder.setOrderNo(serialId);
            mineOrder.setAdress(user.getAddress());
            mineOrder.setPlanId(mineFinancial.getId());
            mineOrder.setCreateTime(new Date());
            mineOrder.setDays(days);
            mineOrder.setAmount(money);
            mineOrder.setAccumulaEarn(BigDecimal.ZERO);
            mineOrder.setEndTime(endTime);
            mineOrder.setPlanTitle(mineFinancial.getTitle());
            mineOrder.setMinOdds(mineFinancial.getMinOdds());
            mineOrder.setMaxOdds(mineFinancial.getMaxOdds());
            mineOrder.setDefaultOdds(mineFinancial.getDefaultOdds());
            mineOrder.setAdminUserIds(user.getAdminParentIds());
            mineOrder.setUserId(user.getUserId());
            mineOrder.setType(0L);
            mineOrder.setStatus(0L);
            mineOrder.setCoin(mineFinancial.getCoin());
            mineOrder.setAvgRate(mineFinancial.getAvgRate());
            mineFinancial.setPurchasedAmount(mineFinancial.getPurchasedAmount().add(money));
            //剩余金额
            mineFinancial.setRemainAmount(mineFinancial.getRemainAmount().subtract(money));
            BigDecimal bigDecimal=mineFinancial.getTotalInvestAmount().subtract(mineFinancial.getRemainAmount());
            mineFinancial.setProcess(bigDecimal.divide(mineFinancial.getTotalInvestAmount(),4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            if (redisCache.tryLock(CachePrefix.MINE_FINANCIAL_ORDER.getPrefix() + user.getUserId(), user.getUserId(), 1000)) {
                //  先扣钱，再下单
                BigDecimal amout = tAppAsset.getAmout();
                BigDecimal availableAmount = tAppAsset.getAvailableAmount();
                tAppAsset.setAvailableAmount(availableAmount.subtract(money));
                tAppAsset.setAmout(amout.subtract(money));
                tAppAssetService.updateTAppAsset(tAppAsset);
                //账变记录
                walletRecordService.generateRecord(user.getUserId(), money, RecordEnum.FINANCIAL_PURCHASE.getCode(), user.getLoginName(), serialId, RecordEnum.FINANCIAL_PURCHASE.getInfo(), availableAmount, availableAmount.subtract(money), tAppAsset.getSymbol(),user.getAdminParentIds());
                //添加订单
                orderService.insertTMineOrder(mineOrder);
                //修改产品
                this.updateTMineFinancial(mineFinancial);
                //当日收益保存
                log.debug("理财购买提交成功, userId:{}, planId:{},理财产品名字:{} money:{}", user.getUserId(), planId, mineFinancial.getTitle(), money);
                //升级vip 和 增加团队金额 操作
                appUserService.toBuilderTeamAmount(mineOrder);

                //理财打码
                Setting setting = settingService.get(SettingEnum.ADD_MOSAIC_SETTING.name());
                if (Objects.nonNull(setting)){
                    AddMosaicSetting addMosaic = JSONUtil.toBean(setting.getSettingValue(), AddMosaicSetting.class);
                    if (Objects.nonNull(addMosaic) && Objects.nonNull(addMosaic.getIsOpen()) && addMosaic.getIsOpen() && Objects.nonNull(addMosaic.getFinancialIsOpen()) && addMosaic.getFinancialIsOpen()){
                        user.setTotleAmont(user.getTotleAmont().add(mineOrder.getAmount()));
                        appUserService.updateTotleAmont(user);
                    }
                }

                return "";
            } else {
                return MessageUtils.message("withdraw.refresh");
            }
        }
    }

    @Transactional
    @Override
    public String reCall(String id) {
        TMineOrder mineOrder = orderService.getOne(new LambdaQueryWrapper<TMineOrder>().eq(TMineOrder::getId, id));
        if(CommonEnum.ONE.getCode()==mineOrder.getStatus().intValue()){
            return "订单状态不正确！！";
        }
        TAppUser user = appUserService.selectTAppUserByUserId(mineOrder.getUserId());
        mineOrder.setStatus(2L);
        TMineFinancial mineFinancial  =  this.selectTMineFinancialById(mineOrder.getPlanId());

        BigDecimal  odds=mineFinancial.getDefaultOdds().divide(new BigDecimal(100));
        BigDecimal  amout=mineOrder.getAmount();
        //增加收益
        TMineOrderDay mineOrderDay = mineOrderDayService.getOne(new LambdaQueryWrapper<TMineOrderDay>()
                .eq(TMineOrderDay::getOrderNo, mineOrder.getOrderNo())
                .eq(TMineOrderDay::getType, mineOrder.getType())
                .eq(TMineOrderDay::getStatus, CommonEnum.ONE.getCode())
        );
        if (Objects.nonNull(mineOrderDay) && Objects.nonNull(mineOrderDay.getEarn())){
            amout = amout.add(mineOrderDay.getEarn());
        }
        int days= DateUtil.daydiff(new Date(),mineOrder.getEndTime());
        //违约金
        BigDecimal bigDecimal=amout.multiply(odds).multiply(new BigDecimal(days));
        //减去违约金
        BigDecimal result=amout.subtract(bigDecimal).setScale(6, RoundingMode.UP);
        TAppAsset asset = tAppAssetService.getAssetByUserIdAndType(mineOrder.getUserId(), AssetEnum.FINANCIAL_ASSETS.getCode());
        //减去违约金
        BigDecimal availableAmount = asset.getAvailableAmount();
        asset.setAvailableAmount(availableAmount.add(result));
        asset.setAmout(asset.getAmout().add(result));
        tAppAssetService.updateTAppAsset(asset);
        walletRecordService.generateRecord(user.getUserId(), result, RecordEnum.FINANCIAL_REDEMPTION.getCode(), user.getLoginName(), mineOrder.getOrderNo(), RecordEnum.FINANCIAL_REDEMPTION.getInfo(), availableAmount, asset.getAvailableAmount(), asset.getSymbol(),user.getAdminParentIds());
        mineOrder.setUpdateTime(new Date());
        orderService.updateTMineOrder(mineOrder);
        //修改每日收益状态
        if (Objects.nonNull(mineOrderDay)){
            mineOrderDay.setStatus(CommonEnum.TWO.getCode());
            mineOrderDay.setUpdateTime(DateUtils.getNowDate());
            mineOrderDay.setUpdateBy(SecurityUtils.getUsername());
            mineOrderDayService.updateById(mineOrderDay);
        }
        return "";
    }

    /**
     * 个人收益
     * @return
     */
    @Override
    public Map<String, Object> personalIncome() {
        Map<String, Object> map = new HashMap<>();
        //查看个人 总投入     累计收益
        QueryWrapper<TMineOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.select("sum(amount) as sumAmount,sum(accumula_earn) as sumEarn");
        orderWrapper.eq("user_id",StpUtil.getLoginIdAsLong());
        Map<String, Object> map1 = orderService.getMap(orderWrapper);
        if(!CollectionUtils.isEmpty(map1)){
            map.put("sumAmount",map1.get("sumAmount")==null?BigDecimal.ZERO:map1.get("sumAmount"));
            map.put("sumEarn",map1.get("sumEarn")==null?BigDecimal.ZERO:map1.get("sumEarn"));
        }else {
            map.put("sumAmount",BigDecimal.ZERO);
            map.put("sumEarn",BigDecimal.ZERO);
        }
        //查看个人   持仓数量
        QueryWrapper<TMineOrder> orderWrapper1 = new QueryWrapper<>();
        orderWrapper1.select("sum(amount) as position");
        orderWrapper1.eq("status",0);
        orderWrapper1.eq("user_id",StpUtil.getLoginIdAsLong());
        Map<String, Object> map2 = orderService.getMap(orderWrapper1);
        if(!CollectionUtils.isEmpty(map2)){
            map.put("position",map2.get("position")==null?BigDecimal.ZERO:map2.get("position"));
        }else {
            map.put("position",BigDecimal.ZERO);
        }
        //当日赚取
        QueryWrapper<TAppWalletRecord> orderWrapper2 = new QueryWrapper<>();
        orderWrapper2.select("sum(amount) as commission");
        orderWrapper2.eq("user_id",StpUtil.getLoginIdAsLong());
        orderWrapper2.eq("type",RecordEnum.FINANCIAL_SETTLEMENT.getCode());
        Map<String, Object> map3 = walletRecordService.getMap(orderWrapper2);
        if(!CollectionUtils.isEmpty(map3)){
            map.put("commission",map3.get("commission")==null?BigDecimal.ZERO:map3.get("commission"));
        }else {
            map.put("commission",BigDecimal.ZERO);
        }
        return map;
    }
}
