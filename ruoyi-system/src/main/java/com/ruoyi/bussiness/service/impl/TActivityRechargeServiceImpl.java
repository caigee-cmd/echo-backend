package com.ruoyi.bussiness.service.impl;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ruoyi.bussiness.domain.TActivityRecharge;
import com.ruoyi.bussiness.domain.TAgentActivityInfo;
import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.setting.AssetCoinSetting;
import com.ruoyi.bussiness.domain.setting.RechargeRebateSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.mapper.TActivityRechargeMapper;
import com.ruoyi.bussiness.mapper.TAgentActivityInfoMapper;
import com.ruoyi.bussiness.mapper.TAppUserMapper;
import com.ruoyi.bussiness.service.ITActivityRechargeService;
import com.ruoyi.bussiness.service.ITAppAssetService;
import com.ruoyi.bussiness.service.ITAppWalletRecordService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 充值活动Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-05
 */
@Service
public class TActivityRechargeServiceImpl extends ServiceImpl<TActivityRechargeMapper, TActivityRecharge> implements ITActivityRechargeService
{
    @Autowired
    private TActivityRechargeMapper tActivityRechargeMapper;
    @Resource
    private TAppUserMapper tAppUserMapper;
    @Resource
    private ITAppAssetService tAppAssetService;
    @Resource
    private SettingService settingService;
    @Resource
    private TAgentActivityInfoMapper tAgentActivityInfoMapper;
    @Resource
    private ITAppWalletRecordService walletRecordService;

    /**
     * 查询充值活动
     * 
     * @param id 充值活动主键
     * @return 充值活动
     */
    @Override
    public TActivityRecharge selectTActivityRechargeById(Long id)
    {
        return tActivityRechargeMapper.selectTActivityRechargeById(id);
    }

    /**
     * 查询充值活动列表
     * 
     * @param tActivityRecharge 充值活动
     * @return 充值活动
     */
    @Override
    public List<TActivityRecharge> selectTActivityRechargeList(TActivityRecharge tActivityRecharge)
    {
        return tActivityRechargeMapper.selectTActivityRechargeList(tActivityRecharge);
    }

    /**
     * 新增充值活动
     * 
     * @param tActivityRecharge 充值活动
     * @return 结果
     */
    @Override
    public int insertTActivityRecharge(TActivityRecharge tActivityRecharge)
    {
        tActivityRecharge.setCreateTime(DateUtils.getNowDate());
        return tActivityRechargeMapper.insertTActivityRecharge(tActivityRecharge);
    }

    /**
     * 修改充值活动
     * 
     * @param tActivityRecharge 充值活动
     * @return 结果
     */
    @Override
    public int updateTActivityRecharge(TActivityRecharge tActivityRecharge)
    {
        tActivityRecharge.setUpdateTime(DateUtils.getNowDate());
        return tActivityRechargeMapper.updateTActivityRecharge(tActivityRecharge);
    }

    /**
     * 批量删除充值活动
     * 
     * @param ids 需要删除的充值活动主键
     * @return 结果
     */
    @Override
    public int deleteTActivityRechargeByIds(Long[] ids)
    {
        return tActivityRechargeMapper.deleteTActivityRechargeByIds(ids);
    }

    /**
     * 删除充值活动信息
     * 
     * @param id 充值活动主键
     * @return 结果
     */
    @Override
    public int deleteTActivityRechargeById(Long id)
    {
        return tActivityRechargeMapper.deleteTActivityRechargeById(id);
    }

    @Override
    public void caseBackToFather(Long userId, BigDecimal amount, String type, String username, String serialId) {

        Setting rechargeRebatSetting = settingService.get(SettingEnum.RECHARGE_REBATE_SETTING.name());
        RechargeRebateSetting rechargeRebate = JSONUtil.toBean(rechargeRebatSetting.getSettingValue(), RechargeRebateSetting.class);
        //如果开启状态
        if(null!=rechargeRebate&&rechargeRebate.getIsOpen()){
            TAppUser massUser = tAppUserMapper.selectById(userId);
            //查询父级玩家用户是否为空 为空泽没有返利  不为空计算返利充值父级账户
            if(StringUtils.isNotBlank(massUser.getAppParentIds())){
                String appParentId = massUser.getAppParentIds();
                if (appParentId.indexOf(",")!=-1){
                    appParentId = appParentId.split(",")[0];
                }
                BigDecimal usdt;
                BigDecimal beforeMount=BigDecimal.ZERO;
                String remark="";
                String coin="";
                //获取父级
                TAppUser fatherUser = tAppUserMapper.selectById(appParentId);
                //获取余额
                Map<String, TAppAsset> assetMap=tAppAssetService.getAssetByUserIdList(fatherUser.getUserId());
                //计算返点金额
                BigDecimal rechargePro = rechargeRebate.getRatio();
                usdt=amount.multiply(rechargePro.divide(new BigDecimal("100")));
                if (rechargeRebate.getRebateMaxAmount()!=null) {
                    //是否返点大于最大金额
                    if(usdt.compareTo(rechargeRebate.getRebateMaxAmount())>=0){
                        usdt=rechargeRebate.getRebateMaxAmount();
                    }
                }
                Setting setting = settingService.get(SettingEnum.ASSET_CUSTOM_COIN.name());
                List<AssetCoinSetting> currencyList = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class);
                if (!CollectionUtils.isEmpty(currencyList)){
                    for (AssetCoinSetting dict: currencyList) {
                        if (dict.getCoinName().equals(type)){
                            TAppAsset asset=  assetMap.get(dict.getCoin()+fatherUser.getUserId());
                            beforeMount = asset.getAvailableAmount();
                            remark = "1级用户id:"+userId+dict.getCoinName()+"充值返现";
                            coin=dict.getCoin();
                            if (Objects.isNull(asset)){
                                tAppAssetService.createAsset(massUser,coin, AssetEnum.PLATFORM_ASSETS.getCode());
                            }
                            tAppAssetService.updateByUserId(TAppAsset.builder().type(asset.getType()).symbol(coin).userId(fatherUser.getUserId()).amout(asset.getAmout().add(usdt)).availableAmount(beforeMount.add(usdt)).build());
                        }
                    }
                }
                //入活动明细表
                TAgentActivityInfo agentActivityInfo = new TAgentActivityInfo();
                agentActivityInfo.setType(1);
                agentActivityInfo.setAmount(usdt);
                agentActivityInfo.setFromId(userId);
                agentActivityInfo.setUserId(fatherUser.getUserId());
                //   1usdt-erc 2 usdt-trc 3btc 4eth
                agentActivityInfo.setCoinType(coin);
                agentActivityInfo.setStatus(2);
                agentActivityInfo.setCreateTime(new Date());
                tAgentActivityInfoMapper.insertTAgentActivityInfo(agentActivityInfo);
                //入账变表
                walletRecordService.generateRecord(fatherUser.getUserId(), usdt, RecordEnum.SUBORDINATE_RECHARGE_REBATE.getCode(), username, serialId,  remark, beforeMount, beforeMount.add(usdt),coin,massUser.getAdminParentIds());

            }
        }
    }
}
