package com.ruoyi.bussiness.service.impl;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.*;

import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.TAppRecharge;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.setting.AddMosaicSetting;
import com.ruoyi.bussiness.domain.setting.AssetCoinSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.mapper.TAppRechargeMapper;
import com.ruoyi.bussiness.mapper.TAppUserMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderUtils;
import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户充值Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
@Service
public class TAppRechargeServiceImpl extends ServiceImpl<TAppRechargeMapper, TAppRecharge> implements ITAppRechargeService
{
    private static final Logger log = LoggerFactory.getLogger(TAppRechargeServiceImpl.class);
    @Resource
    private TAppRechargeMapper tAppRechargeMapper;
    @Resource
    private ITAppAssetService tAppAssetService;
    @Resource
    private TAppUserMapper tAppUserMapper;
    @Resource
    private RedisUtil redisUtil;
    @Value("${admin-redis-stream.names}")
    private String redisStreamNames;
    @Resource
    private ITAppWalletRecordService walletRecordService;
    @Resource
    private ITActivityRechargeService itActivityRechargeService;
    @Resource
    private SettingService settingService;
    @Resource
    private RedisCache redisCache;

    /**
     * 查询用户充值
     * 
     * @param id 用户充值主键
     * @return 用户充值
     */
    @Override
    public TAppRecharge selectTAppRechargeById(Long id)
    {
        return tAppRechargeMapper.selectTAppRechargeById(id);
    }

    /**
     * 查询用户充值列表
     * 
     * @param tAppRecharge 用户充值
     * @return 用户充值
     */
    @Override
    public List<TAppRecharge> selectTAppRechargeList(TAppRecharge tAppRecharge)
    {
        return tAppRechargeMapper.selectTAppRechargeList(tAppRecharge);
    }

    /**
     * 新增用户充值
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTAppRecharge(TAppUser user)
    {
        Map<String, Object> params = user.getParams();
        String serialId = "W" + OrderUtils.generateOrderNum();
        TAppRecharge recharge = new TAppRecharge();
        //usdt交易的话需要乘汇率
        recharge.setAddress(String.valueOf(params.get("address")));
        recharge.setSerialId(serialId);
        recharge.setUserId(user.getUserId());
        recharge.setUsername(user.getLoginName());
        recharge.setCreateBy(user.getLoginName());
        recharge.setUpdateBy(user.getLoginName());
        recharge.setCreateTime(new Date());
        recharge.setAppParentIds(user.getAppParentIds());
        recharge.setUpdateTime(new Date());
        recharge.setBonus(0L);
        recharge.setStatus("0");
        recharge.setType(String.valueOf(params.get("type")));
        recharge.setCoin(params.get("coin")+"");
        if (params.get("amount")!=null){
            recharge.setAmount(new BigDecimal(params.get("amount")+""));
            recharge.setRealAmount(new BigDecimal(params.get("amount")+""));
        }
        recharge.setFileName(params.get("filePath")+"");
        recharge.setAdminParentIds(user.getAdminParentIds());
        recharge.setNoticeFlag(0);
        int i = tAppRechargeMapper.insert(recharge);
        //socket 通知后台
        HashMap<String, Object> object = new HashMap<>();
        object.put(CacheConstants.RECHARGE_KEY_BOT, JSON.toJSONString(recharge));
        redisUtil.addStream(redisStreamNames,object);
        return i;
    }

    /**
     * 修改用户充值
     * 
     * @param tAppRecharge 用户充值
     * @return 结果
     */
    @Override
    public int updateTAppRecharge(TAppRecharge tAppRecharge)
    {
        tAppRecharge.setUpdateTime(DateUtils.getNowDate());
        return tAppRechargeMapper.updateTAppRecharge(tAppRecharge);
    }

    /**
     * 批量删除用户充值
     * 
     * @param ids 需要删除的用户充值主键
     * @return 结果
     */
    @Override
    public int deleteTAppRechargeByIds(Long[] ids)
    {
        return tAppRechargeMapper.deleteTAppRechargeByIds(ids);
    }

    /**
     * 删除用户充值信息
     * 
     * @param id 用户充值主键
     * @return 结果
     */
    @Override
    public int deleteTAppRechargeById(Long id)
    {
        return tAppRechargeMapper.deleteTAppRechargeById(id);
    }

    @Override
    public Map<String, Object> sumtotal(TAppRecharge tAppRecharge) {
        return tAppRechargeMapper.sumtotal(tAppRecharge);
    }

    @Override
    public List<TAppRecharge> selectRechargeList(TAppRecharge tAppRecharge) {
        return tAppRechargeMapper.selectRechargeList(tAppRecharge);
    }


    @Override
    public String failedOrder(TAppRecharge tAppRecharge) {
        String username = SecurityUtils.getUsername();
        String msg = "";
        TAppRecharge recharge = tAppRechargeMapper.selectById(tAppRecharge.getId());
        if (recharge!=null){
            if (RecoenOrderStatusEmun.audit.getCode().equals(recharge.getStatus())) {
                // 审核驳回
                recharge.setStatus(RecoenOrderStatusEmun.failed.getCode());
                recharge.setUpdateBy(username);
                recharge.setUpdateTime(new Date());
                recharge.setRemark(tAppRecharge.getRemark());
                recharge.setRechargeRemark(tAppRecharge.getRechargeRemark());
                recharge.setOperateTime(new Date());
                tAppRechargeMapper.updateStatus(recharge);
            }else{
                msg = "订单状态不对，不能审核";
            }
        }else{
            msg = "没有查询到需要审核的订单";
        }
        return msg;
    }

    @Override
    public List<TAppRecharge> selectRechargeVoice(String parentId) {
        return tAppRechargeMapper.selectRechargeVoice(parentId);
    }

    @Override
    public BigDecimal getAllRecharge(String parentId, Integer type) {
        return tAppRechargeMapper.getAllRecharge(parentId,type);
    }

    @Override
    @Transactional
    public String passOrder(TAppRecharge tAppRecharge) {
        log.info("[passOrder] 开始处理充值订单审核，订单ID: {}", tAppRecharge.getId());
        String msg = "";
        String username = SecurityUtils.getUsername();
        log.info("[passOrder] 操作用户: {}", username);
        BigDecimal usdt;
        BigDecimal beforeMount = null;
        String remark = null;
        //  String coin="";
        TAppRecharge recharge = tAppRechargeMapper.selectById(tAppRecharge.getId());
        log.info("[passOrder] 查询到充值订单: userId={}, amount={}, type={}, coin={}, status={}",
                recharge != null ? recharge.getUserId() : null,
                recharge != null ? recharge.getAmount() : null,
                recharge != null ? recharge.getType() : null,
                recharge != null ? recharge.getCoin() : null,
                recharge != null ? recharge.getStatus() : null);
        if (recharge!=null){
            //资产
            Map<String, TAppAsset> assetMap=tAppAssetService.getAssetByUserIdList(recharge.getUserId());
            recharge.setRealAmount(tAppRecharge.getRealAmount());
            usdt=recharge.getAmount();
            String type=recharge.getType();
            String coin=recharge.getCoin();
            if (RecoenOrderStatusEmun.audit.getCode().equals(recharge.getStatus())) {
                recharge.setStatus(RecoenOrderStatusEmun.pass.getCode());
                recharge.setUpdateBy(username);
                recharge.setUpdateTime(new Date());
                TAppUser user = tAppUserMapper.selectTAppUserByUserId(recharge.getUserId());
                log.info("[passOrder] 查询到用户信息: userId={}, adminParentIds={}",
                        user != null ? user.getUserId() : null,
                        user != null ? user.getAdminParentIds() : null);
                Setting setting = settingService.get(SettingEnum.ASSET_CUSTOM_COIN.name());
                List<AssetCoinSetting> currencyList = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class);
                log.info("[passOrder] 币种配置列表大小: {}", currencyList != null ? currencyList.size() : 0);
                if (currencyList!=null && currencyList.size()>0){
                    for (AssetCoinSetting assetCoin: currencyList) {
                        log.debug("[passOrder] 检查币种配置: coinName={}, coin={}, type={}",
                                assetCoin.getCoinName(), assetCoin.getCoin(), type);
                        if (assetCoin.getCoinName().equals(type)){
                            log.info("[passOrder] 找到匹配币种配置: coinName={}, coin={}", assetCoin.getCoinName(), assetCoin.getCoin());
                            TAppAsset asset=  assetMap.get(assetCoin.getCoin()+recharge.getUserId());
                            if(null ==asset){
                                //充值币种不存在  去初始化资产
                                tAppAssetService.createAsset(user,assetCoin.getCoin().toLowerCase(),AssetEnum.PLATFORM_ASSETS.getCode());
                                asset=tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>()
                                        .eq(TAppAsset::getUserId,user.getUserId())
                                        .eq(TAppAsset::getType,AssetEnum.PLATFORM_ASSETS.getCode())
                                        .eq(TAppAsset::getSymbol,assetCoin.getCoin().toLowerCase())
                                );
                            }
                            beforeMount = asset.getAvailableAmount();
                            remark = assetCoin.getCoinName()+"充值";
                            coin=assetCoin.getCoin();
                            tAppAssetService.updateByUserId(TAppAsset.builder().type(asset.getType()).symbol(coin).userId(recharge.getUserId()).amout(asset.getAmout().add(usdt)).availableAmount(beforeMount.add(usdt)).build());
                        }
                    }
                }
//
                walletRecordService.generateRecord(recharge.getUserId(), recharge.getAmount(), RecordEnum.RECHARGE.getCode(), username, recharge.getSerialId(),remark,beforeMount, beforeMount.add(recharge.getAmount()),coin,user.getAdminParentIds());
                recharge.setRemark(tAppRecharge.getRemark());
                recharge.setRechargeRemark(tAppRecharge.getRechargeRemark());
                recharge.setOperateTime(new Date());
                tAppRechargeMapper.updateStatus(recharge);

                itActivityRechargeService.caseBackToFather(recharge.getUserId(), recharge.getAmount(),type,username,recharge.getSerialId());

                //充值打码累计
                Setting addMosaicSetting = settingService.get(SettingEnum.ADD_MOSAIC_SETTING.name());
                if (Objects.nonNull(addMosaicSetting)){
                    AddMosaicSetting addMosaic = JSONUtil.toBean(addMosaicSetting.getSettingValue(), AddMosaicSetting.class);
                    if (Objects.nonNull(addMosaic) && Objects.nonNull(addMosaic.getIsOpen())
                            && addMosaic.getIsOpen()){
                        BigDecimal price = BigDecimal.ONE;
                        try {
                            if (Objects.isNull(user.getRechargeAmont())) user.setRechargeAmont(BigDecimal.ZERO);
                            if (!"usdt".equals(recharge.getCoin())) {
                                price = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + recharge.getCoin().toLowerCase());
                                user.setRechargeAmont(user.getRechargeAmont().add(recharge.getAmount().multiply(price)));
                            } else {
                                user.setRechargeAmont(user.getRechargeAmont().add(recharge.getAmount()));
                            }
                            tAppUserMapper.updateRechargeAmont(user);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                log.error("[审核订单>>>]","订单状态不对，不能审核");
                msg = "订单状态不对，不能审核";
            }
        }else{
            msg = "没有查询到需要审核的订单";
        }
        return msg;
    }
}
