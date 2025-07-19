package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.mapper.TAppWalletRecordMapper;
import com.ruoyi.bussiness.mapper.TExchangeCoinRecordMapper;
import com.ruoyi.bussiness.mapper.TSymbolManageMapper;
import com.ruoyi.bussiness.service.ITAppAssetService;
import com.ruoyi.bussiness.service.ITExchangeCoinRecordService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 币种兑换记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-07
 */
@Service
@Slf4j
public class TExchangeCoinRecordServiceImpl extends ServiceImpl<TExchangeCoinRecordMapper, TExchangeCoinRecord> implements ITExchangeCoinRecordService
{
    @Autowired
    private TExchangeCoinRecordMapper tExchangeCoinRecordMapper;
    @Autowired
    private RedisCache redisCache;
    @Resource
    private ITAppAssetService assetService;
    @Resource
    private TAppWalletRecordMapper appWalletRecordMapper;
    @Resource
    private TSymbolManageMapper tSymbolManageMapper;



    /**
     * 查询币种兑换记录
     * 
     * @param id 币种兑换记录主键
     * @return 币种兑换记录
     */
    @Override
    public TExchangeCoinRecord selectTExchangeCoinRecordById(Long id)
    {
        return tExchangeCoinRecordMapper.selectTExchangeCoinRecordById(id);
    }

    /**
     * 查询币种兑换记录列表
     * 
     * @param tExchangeCoinRecord 币种兑换记录
     * @return 币种兑换记录
     */
    @Override
    public List<TExchangeCoinRecord> selectTExchangeCoinRecordList(TExchangeCoinRecord tExchangeCoinRecord)
    {
        return tExchangeCoinRecordMapper.selectTExchangeCoinRecordList(tExchangeCoinRecord);
    }

    /**
     * 新增币种兑换记录
     * 
     * @param tExchangeCoinRecord 币种兑换记录
     * @return 结果
     */
    @Override
    public int insertTExchangeCoinRecord(TExchangeCoinRecord tExchangeCoinRecord)
    {
        tExchangeCoinRecord.setCreateTime(DateUtils.getNowDate());
        return tExchangeCoinRecordMapper.insertTExchangeCoinRecord(tExchangeCoinRecord);
    }

    /**
     * 修改币种兑换记录
     * 
     * @param tExchangeCoinRecord 币种兑换记录
     * @return 结果
     */
    @Override
    public int updateTExchangeCoinRecord(TExchangeCoinRecord tExchangeCoinRecord)
    {
        tExchangeCoinRecord.setUpdateTime(DateUtils.getNowDate());
        return tExchangeCoinRecordMapper.updateTExchangeCoinRecord(tExchangeCoinRecord);
    }

    /**
     * 批量删除币种兑换记录
     * 
     * @param ids 需要删除的币种兑换记录主键
     * @return 结果
     */
    @Override
    public int deleteTExchangeCoinRecordByIds(Long[] ids)
    {
        return tExchangeCoinRecordMapper.deleteTExchangeCoinRecordByIds(ids);
    }

    /**
     * 删除币种兑换记录信息
     * 
     * @param id 币种兑换记录主键
     * @return 结果
     */
    @Override
    public int deleteTExchangeCoinRecordById(Long id)
    {
        return tExchangeCoinRecordMapper.deleteTExchangeCoinRecordById(id);
    }

    @Override
    public Integer countBySubmittedRecord(Long userId, String fromCoin, String toCoin) {
        TExchangeCoinRecord exchangeCoinRecord = new TExchangeCoinRecord();
        exchangeCoinRecord.setUserId(userId);
        exchangeCoinRecord.setFromCoin(fromCoin);
        exchangeCoinRecord.setToCoin(toCoin);
        exchangeCoinRecord.setStatus(0);
        return tExchangeCoinRecordMapper.countByExchangeCoinRecord(exchangeCoinRecord);
    }

    @Override
    public int insertRecord(TAppUser user, Map<String, Object> params) {
        TExchangeCoinRecord record = new TExchangeCoinRecord();
        record.setAddress(user.getAddress());
        record.setUserId(user.getUserId());
        record.setUsername(user.getLoginName());
        record.setFromCoin(String.valueOf(params.get("fromSymbol")));
        record.setToCoin(String.valueOf(params.get("toSymbol")));
        record.setAmount(new BigDecimal(String.valueOf(params.get("total"))));
        record.setStatus(1);
        record.setCreateTime(DateUtils.getNowDate());
        record.setAdminParentIds(user.getAdminParentIds());
        int i = tExchangeCoinRecordMapper.insert(record);
        if (i==1){
            //兑换
            exchangeStart(record,user);
        }
        return i;


    }

    @Override
    public Map<String, Object> getCurrencyPrice(String[] currency) {
        Map<String, Object> resultMap = new HashMap<>();
        if (currency!=null && currency.length>0){
            for (int i = 0; i < currency.length; i++) {
                resultMap.put(currency[i],redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+currency[i]));
            }
        }
        return resultMap;
    }

    @Override
    public List<TExchangeCoinRecord> getListByLimit(int size) {
        return tExchangeCoinRecordMapper.getListByLimit(size);
    }

    /**
     * 开始兑换
     *
     * @param record
     * @param user
     */
    public void exchangeStart(TExchangeCoinRecord record, TAppUser user) {
        log.debug(">>> 币种兑换开始 >>>");

        // 系统配置的兑换汇率
        List<TSymbolManage> list = tSymbolManageMapper.selectList(new LambdaQueryWrapper<TSymbolManage>().eq(TSymbolManage::getEnable, "1").eq(TSymbolManage::getDelFlag, "0"));
            BigDecimal from =record.getFromCoin().toLowerCase().equals("usdt")?new BigDecimal(1): redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+record.getFromCoin().toLowerCase());
            BigDecimal to = record.getToCoin().toLowerCase().equals("usdt")?new BigDecimal(1):redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+record.getToCoin().toLowerCase());
            if(from==null&&to==null){
                log.error("未匹配到三方提供的汇率! origin:{}", record.getExchangeType());
                return;
            }
            BigDecimal thirdRate=  from.divide(to, 8, RoundingMode.DOWN);
            //获取自己的应有币种资源对象
            Map<String, TAppAsset>  assetMapper=assetService.getAssetByUserIdList(record.getUserId());
            TAppAsset asset =assetMapper.get(record.getFromCoin().toLowerCase()+record.getUserId());
            if (!validateAmount(record, asset)) {
                // 余额不足将任务更新为失败
                updateExchangeRecordStatus(record, 2, "余额不足!");
                return;
            }
            //手续费汇率
            if (StringUtils.isNotEmpty(list) && list.size()>0){
                for (TSymbolManage s :list) {
                    if (s.getSymbol().equals(record.getToCoin().toLowerCase())){
                        record.setSystemRate(s.getCommission().divide(new BigDecimal("100")));
                    }
                }
            }
            record.setThirdRate(thirdRate);
            // 对金额进行过滤,后期此处可通过策略模式进行重构
            updateAndSaveRecord(asset,  record, resolveComputeAmount(record),assetMapper,user);

            log.debug(">>> 币种兑换结束 >>>");
    }

    private void updateExchangeRecordStatus(TExchangeCoinRecord record, Integer status, String remark) {
        record.setStatus(status);
        record.setRemark(remark);
        tExchangeCoinRecordMapper.updateById(record);
    }

    private TAppWalletRecord buildWalletRecord(TAppAsset asset, BigDecimal beforeAmount, BigDecimal afterAmount,
                                               BigDecimal amount, Integer type, String coin, String remark, String adminParentIds) {

        TAppWalletRecord walletRecord = new TAppWalletRecord();
        walletRecord.setUserId(asset.getUserId());
        walletRecord.setAmount(amount);
        walletRecord.setSymbol(coin);
        walletRecord.setRemark(remark);
        walletRecord.setBeforeAmount(beforeAmount);
        walletRecord.setAfterAmount(afterAmount);
        walletRecord.setCreateTime(new Date());
        walletRecord.setSerialId("C" + OrderUtils.generateOrderNum());
        walletRecord.setType(type);
        walletRecord.setAdminParentIds(adminParentIds);
        BigDecimal price = amount;
        try {
            if (!coin.equals("usdt")){
                log.debug("帐变记录币种获取汇率：  币种：{}",coin);
                price = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + coin.toLowerCase());
                log.debug("帐变记录获取汇率：  币种：{}  汇率：{}",coin,price);
                walletRecord.setUAmount(amount.multiply(price));
            }else{
                walletRecord.setUAmount(amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return walletRecord;
    }

    private boolean validateAmount(TExchangeCoinRecord record, TAppAsset asset) {
        boolean flag = false;
        if ( asset.getBtcDefaultIfNull(BigDecimal.ZERO).compareTo(record.getAmount()) >= 0) {
            flag =  true;
        }
        return flag;

    }

    /**
     * 决定最终的金额
     *
     * @param record 兑换记录
     * @return 金额
     */
    private BigDecimal resolveComputeAmount(TExchangeCoinRecord record) {
        // 三方提供的汇率计算结果
        BigDecimal amount = record.getAmount().multiply(record.getThirdRate());
        // 系统提供的汇率计算结果
        amount = amount.subtract(amount.multiply(record.getSystemRate()));
        return amount;
    }

    private void updateAndSaveRecord(TAppAsset asset, TExchangeCoinRecord record, BigDecimal computeAmount, Map<String, TAppAsset> map, TAppUser user) {
        TAppWalletRecord walletRecordFrom = null;
        TAppWalletRecord walletRecordTo = null;
        TAppAsset to = map.get(record.getToCoin().toLowerCase()+record.getUserId());
        TAppAsset from=map.get(record.getFromCoin().toLowerCase()+record.getUserId());
        String toCoin = record.getToCoin().toLowerCase();
        String fromCoin= record.getFromCoin().toLowerCase();

        //判断是否已有需要兑换的币种资源  没有的话新增兑换金额  有的话直接修改金额
        if(to==null){
            TAppAsset tAppAsset = new TAppAsset();
            tAppAsset.setType(AssetEnum.PLATFORM_ASSETS.getCode());
            tAppAsset.setAmout(computeAmount);
            tAppAsset.setSymbol(toCoin);
            tAppAsset.setUserId(record.getUserId());
            tAppAsset.setAdress(record.getAddress());
            tAppAsset.setAvailableAmount(computeAmount);
            tAppAsset.setOccupiedAmount(BigDecimal.ZERO);
            tAppAsset.setCreateTime(DateUtils.getNowDate());
            tAppAsset.setUpdateTime(DateUtils.getNowDate());
            assetService.save(tAppAsset);

            assetService.updateByUserId(TAppAsset.builder().symbol(fromCoin)
                    .type(AssetEnum.PLATFORM_ASSETS.getCode())
                    .userId(record.getUserId())
                    .amout(from.getAmout().subtract(record.getAmount()))
                    .availableAmount(from.getAvailableAmount().subtract(record.getAmount())).build());
            walletRecordFrom = buildWalletRecord(asset, from.getAmout(),from.getAmout().subtract(record.getAmount()),  record.getAmount(),RecordEnum.CURRENCY_CONVERSION_SUB.getCode(), record.getFromCoin(), record.getFromToRemark(),user.getAdminParentIds());
            walletRecordTo =  buildWalletRecord(asset, new BigDecimal(0), computeAmount, computeAmount, RecordEnum.CURRENCY_EXCHANGE_ADD.getCode(), record.getToCoin(), record.getFromToRemark(), user.getAdminParentIds());

        }else{
            assetService.updateByUserId(TAppAsset.builder().symbol(toCoin)
                    .type(AssetEnum.PLATFORM_ASSETS.getCode())
                    .userId(record.getUserId())
                    .amout(to.getAmout().add(computeAmount))
                    .availableAmount(to.getAvailableAmount().add(computeAmount)).build());
            assetService.updateByUserId(TAppAsset.builder().symbol(fromCoin)
                    .type(AssetEnum.PLATFORM_ASSETS.getCode())
                    .userId(record.getUserId())
                    .amout(from.getAmout().subtract(record.getAmount()))
                    .availableAmount(from.getAvailableAmount().subtract(record.getAmount())).build());
            walletRecordFrom = buildWalletRecord(asset, from.getAmout(),from.getAmout().subtract(record.getAmount()),  record.getAmount(),RecordEnum.CURRENCY_CONVERSION_SUB.getCode(), record.getFromCoin(), record.getFromToRemark(), user.getAdminParentIds());
            walletRecordTo =  buildWalletRecord(asset,  to.getAmout(), to.getAmout().add(computeAmount), computeAmount, RecordEnum.CURRENCY_EXCHANGE_ADD.getCode(), record.getToCoin(), record.getFromToRemark(), user.getAdminParentIds());
        }

        // 保存钱包变更记录
        appWalletRecordMapper.insert(walletRecordFrom);
        appWalletRecordMapper.insert(walletRecordTo);
        // 兑换记录更新为成功
        updateExchangeRecordStatus(record, 1, null);
    }

}
