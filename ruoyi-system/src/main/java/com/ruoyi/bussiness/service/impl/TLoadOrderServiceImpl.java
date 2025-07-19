package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TLoadProduct;
import com.ruoyi.bussiness.mapper.*;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.domain.TLoadOrder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 贷款订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-14
 */
@Service
@Slf4j
public class TLoadOrderServiceImpl extends ServiceImpl<TLoadOrderMapper,TLoadOrder> implements ITLoadOrderService
{
    @Autowired
    private TLoadOrderMapper tLoadOrderMapper;
    @Resource
    private TLoadProductMapper tLoadProductMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private TAppUserMapper tAppUserMapper;
    @Resource
    private TAppAssetMapper assetMapper;
    @Autowired
    private ITAppWalletRecordService tAppWalletRecordService;

    /**
     * 查询贷款订单
     * 
     * @param id 贷款订单主键
     * @return 贷款订单
     */
    @Override
    public TLoadOrder selectTLoadOrderById(Long id)
    {
        return tLoadOrderMapper.selectTLoadOrderById(id);
    }

    /**
     * 查询贷款订单列表
     * 
     * @param tLoadOrder 贷款订单
     * @return 贷款订单
     */
    @Override
    public List<TLoadOrder> selectTLoadOrderList(TLoadOrder tLoadOrder)
    {
        return tLoadOrderMapper.selectTLoadOrderList(tLoadOrder);
    }

    /**
     * 新增贷款订单
     * 
     * @param tLoadOrder 贷款订单
     * @return 结果
     */
    @Override
    public int insertTLoadOrder(TLoadOrder tLoadOrder)
    {
        tLoadOrder.setCreateTime(DateUtils.getNowDate());
        return tLoadOrderMapper.insertTLoadOrder(tLoadOrder);
    }

    /**
     * 修改贷款订单
     * 
     * @param tLoadOrder 贷款订单
     * @return 结果
     */
    @Override
    public int updateTLoadOrder(TLoadOrder tLoadOrder)
    {
        tLoadOrder.setUpdateTime(DateUtils.getNowDate());
        return tLoadOrderMapper.updateTLoadOrder(tLoadOrder);
    }

    /**
     * 批量删除贷款订单
     * 
     * @param ids 需要删除的贷款订单主键
     * @return 结果
     */
    @Override
    public int deleteTLoadOrderByIds(Long[] ids)
    {
        return tLoadOrderMapper.deleteTLoadOrderByIds(ids);
    }

    /**
     * 删除贷款订单信息
     * 
     * @param id 贷款订单主键
     * @return 结果
     */
    @Override
    public int deleteTLoadOrderById(Long id)
    {
        return tLoadOrderMapper.deleteTLoadOrderById(id);
    }

    @Override
    @Transactional
    public AjaxResult saveTLoadOrder(TLoadOrder loadOrder, TAppUser user) {
        TLoadProduct tLoadProduct=  tLoadProductMapper.selectTLoadProductById(loadOrder.getProId());
        loadOrder.setOrderNo("Z"+ OrderUtils.generateOrderNum());
        loadOrder.setStatus(0);
        loadOrder.setCreateTime(new Date());
        loadOrder.setRate(tLoadProduct.getOdds());
        loadOrder.setInterest(loadOrder.getAmount().multiply(tLoadProduct.getOdds()).multiply(new BigDecimal(loadOrder.getCycleType()).divide(new BigDecimal(100)).setScale(2, RoundingMode.UP)));
        loadOrder.setUpdateTime(new Date());
        loadOrder.setUserId(user.getUserId());
        loadOrder.setAdminParentIds(user.getAdminParentIds());
        //增加对应u.btc,eth余额
        if (redisCache.tryLock(CachePrefix.APP_LOADORDER.getPrefix() + user.getUserId(), user.getUserId(), 1000)) {
            tLoadOrderMapper.insertTLoadOrder(loadOrder);
            //发起提现则扣钱
            return AjaxResult.success();
        }else {
            log.error("下单锁定, userId:{}, loadOrder:{}", user.getUserId(), loadOrder.getAmount());
            return AjaxResult.error(MessageUtils.message("withdraw.refresh"));
        }
    }

    /**
     * 审核通过
     * @param tLoadOrder
     * @return
     */
    @Transactional
    @Override
    public AjaxResult passTLoadOrder(TLoadOrder tLoadOrder) {
        tLoadOrder.setStatus(1);
        tLoadOrder.setDisburseTime(new Date());
        Integer cycType = tLoadOrder.getCycleType();
        Date endTime = DateUtils.dateFormatDay(tLoadOrder.getDisburseTime(), cycType);
//        TLoadProduct loadOrder=tLoadProductMapper.selectTLoadProductById(tLoadOrder.getProId());

        TAppUser user = tAppUserMapper.selectById(tLoadOrder.getUserId());
        TAppAsset asset = assetMapper.selectOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId,tLoadOrder.getUserId()).eq(TAppAsset::getSymbol,"usdt").eq(TAppAsset::getType,"1"));
        Map<String, Object> map = DateUtils.getWeek(endTime);
        if(tLoadOrder.getDisburseAmount().compareTo(BigDecimal.ZERO)<=0){
            return AjaxResult.error("审批金额必须大于0");
        }
        if(tLoadOrder.getDisburseAmount().compareTo(tLoadOrder.getAmount())>0){
            return AjaxResult.error("审批金额不能大于申请金额");
        }
        BigDecimal beforeAmount = asset.getAmout();
        //获取day和week
        tLoadOrder.setFinalRepayTime(endTime);
        tLoadOrder.setInterest(tLoadOrder.getDisburseAmount().multiply(tLoadOrder.getRate()).multiply(new BigDecimal(tLoadOrder.getCycleType()).divide(new BigDecimal(100)).setScale(2, RoundingMode.UP)));
        tLoadOrderMapper.updateTLoadOrder(tLoadOrder);
        assetMapper.updateByUserId(TAppAsset.builder().symbol("usdt").userId(tLoadOrder.getUserId()).amout(asset.getAmout().add(tLoadOrder.getDisburseAmount())).availableAmount(asset.getAvailableAmount().add(tLoadOrder.getDisburseAmount())).type(AssetEnum.PLATFORM_ASSETS.getCode()).build());
        tAppWalletRecordService.generateRecord(tLoadOrder.getUserId(), tLoadOrder.getDisburseAmount(), RecordEnum.LOAD_ORDER.getCode(), SecurityUtils.getUsername(), tLoadOrder.getOrderNo(), "贷款", beforeAmount, beforeAmount.add(tLoadOrder.getDisburseAmount()), "USDT",user.getAdminParentIds());
        user.setIsFreeze("1");
        tAppUserMapper.updateTAppUser(user);
        return AjaxResult.success();
    }

    /**
     * 还款
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int repayment(Long id) {
        TLoadOrder tLoadOrder = tLoadOrderMapper.selectTLoadOrderById(id);
        tLoadOrder.setStatus(3);
        tLoadOrder.setReturnTime(new Date());
        tLoadOrder.setUpdateTime(new Date());
        int i=tLoadOrderMapper.updateTLoadOrder(tLoadOrder);
        TLoadOrder serch = new TLoadOrder();
        serch.setUserId(tLoadOrder.getUserId());
        List<TLoadOrder> list =  tLoadOrderMapper.selectListByUserId(serch);
        if (CollectionUtils.isEmpty(list)){
            TAppUser user=tAppUserMapper.selectById(tLoadOrder.getUserId());
            user.setIsFreeze("2");
            i=tAppUserMapper.updateTAppUser(user);
        }
        return i;
    }
}
