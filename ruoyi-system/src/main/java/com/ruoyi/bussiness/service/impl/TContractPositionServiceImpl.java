package com.ruoyi.bussiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.ucontract.ContractComputerUtil;
import jnr.ffi.annotations.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TContractPositionMapper;
import com.ruoyi.bussiness.domain.TContractPosition;

import javax.annotation.Resource;

/**
 * U本位持仓表Service业务层处理
 *
 * @author michael
 * @date 2023-07-20
 */
@Service
@Slf4j
public class TContractPositionServiceImpl extends ServiceImpl<TContractPositionMapper, TContractPosition> implements ITContractPositionService {
    @Autowired
    private TContractPositionMapper tContractPositionMapper;

    @Autowired
    private ITContractCoinService contractCoinService;

    @Autowired
    private ITAppAssetService appAssetService;

    @Autowired
    private ITAppUserService appUserService;

    @Autowired
    private ITAppWalletRecordService appWalletRecordService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ITContractLossService contractLossService;

    @Resource
    private RedisUtil redisUtil;
    @Value("${api-redis-stream.names}")
    private String redisStreamNames;

    /**
     * 查询U本位持仓表
     *
     * @param id U本位持仓表主键
     * @return U本位持仓表
     */
    @Override
    public TContractPosition selectTContractPositionById(Long id) {
        return tContractPositionMapper.selectTContractPositionById(id);
    }

    /**
     * 查询U本位持仓表列表
     *
     * @param tContractPosition U本位持仓表
     * @return U本位持仓表
     */
    @Override
    public List<TContractPosition> selectTContractPositionList(TContractPosition tContractPosition) {
        return tContractPositionMapper.selectTContractPositionList(tContractPosition);
    }

    /**
     * 新增U本位持仓表
     *
     * @param tContractPosition U本位持仓表
     * @return 结果
     */
    @Override
    public int insertTContractPosition(TContractPosition tContractPosition) {
        tContractPosition.setCreateTime(DateUtils.getNowDate());
        return tContractPositionMapper.insertTContractPosition(tContractPosition);
    }

    /**
     * 修改U本位持仓表
     *
     * @param tContractPosition U本位持仓表
     * @return 结果
     */
    @Override
    public int updateTContractPosition(TContractPosition tContractPosition) {
        HashMap<String, Object> object = new HashMap<>();
        object.put("position", "1");
        redisUtil.addStream(redisStreamNames, object);
        return tContractPositionMapper.updateTContractPosition(tContractPosition);
    }

    /**
     * 批量删除U本位持仓表
     *
     * @param ids 需要删除的U本位持仓表主键
     * @return 结果
     */
    @Override
    public int deleteTContractPositionByIds(Long[] ids) {
        return tContractPositionMapper.deleteTContractPositionByIds(ids);
    }

    /**
     * 删除U本位持仓表信息
     *
     * @param id U本位持仓表主键
     * @return 结果
     */
    @Override
    public int deleteTContractPositionById(Long id) {
        return tContractPositionMapper.deleteTContractPositionById(id);
    }

    @Override
    public String allClosePosition(Long id) {
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(id);
        TAppUser appUser = appUserService.getById(contractPosition.getUserId());
        if (contractPosition.getStatus() != 0) {
            return MessageUtils.message("order.status.error");
        }
        String symol = contractPosition.getSymbol();
        //类型0  买入买多 1 卖出做空
        Integer type = contractPosition.getType();
        //开仓均价
        BigDecimal openPrice = contractPosition.getOpenPrice();
        BigDecimal num = contractPosition.getOpenNum();
        BigDecimal amount = contractPosition.getAdjustAmount();

        TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(symol);
        //当前价
        BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + contractPosition.getSymbol().toLowerCase());
        BigDecimal sellFee = tContractCoin.getCloseFee();
        contractPosition.setDealNum(num);
        contractPosition.setStatus(1);
        contractPosition.setAuditStatus(1);
        contractPosition.setDealPrice(currentlyPrice);
        contractPosition.setSellFee(contractPosition.getAdjustAmount().multiply(sellFee).setScale(4, RoundingMode.HALF_UP));
        BigDecimal earn=BigDecimal.ZERO;

        BigDecimal floatProit = Objects.isNull(tContractCoin.getFloatProfit()) ? BigDecimal.ZERO : tContractCoin.getFloatProfit();
        //止损率
        BigDecimal profitLoss = Objects.isNull(tContractCoin.getProfitLoss()) ? BigDecimal.ZERO : tContractCoin.getProfitLoss();

        if("ok".equals(checkProfit(tContractCoin))){
              earn = ContractComputerUtil.getPositionEarn(openPrice, num, currentlyPrice, type);
        }else{
            // -0.5* 4*0.001*10*/0.0001
            BigDecimal sub= ContractComputerUtil.getRate(openPrice,currentlyPrice,type);

            contractPosition.setSellFee(contractPosition.getAdjustAmount().multiply(sellFee).setScale(4, RoundingMode.HALF_UP));

            earn=  sub.multiply(profitLoss).multiply(contractPosition.getOpenNum()).multiply(contractPosition.getLeverage()).divide(floatProit,4,RoundingMode.DOWN);
        }
        contractPosition.setEarn(earn);
        contractPosition.setDealTime(new Date());
        updateTContractPosition(contractPosition);
        //撤销止盈止损
        contractLossService.updateContractLoss(contractPosition.getId());
        TAppAsset asset = appAssetService.getAssetByUserIdAndType(contractPosition.getUserId(), AssetEnum.CONTRACT_ASSETS.getCode());
        BigDecimal amont = asset.getAmout();
        BigDecimal availAsset = asset.getAvailableAmount();
        BigDecimal money = amount.add(earn).subtract(contractPosition.getSellFee());
        BigDecimal subReturn=availAsset.add(money);
        if(subReturn.compareTo(BigDecimal.ZERO)<0){
            asset.setAmout(BigDecimal.ZERO);
            asset.setAvailableAmount(BigDecimal.ZERO);
        }else {
            asset.setAmout(amont.add(money));
            asset.setAvailableAmount(availAsset.add(money));
        }
        appAssetService.updateTAppAsset(asset);
        appWalletRecordService.generateRecord(contractPosition.getUserId(), money, RecordEnum.CONTRACT_TRANSACTION_CLOSING.getCode(), null, contractPosition.getOrderNo(), "合约交易平仓", amont, amont.add(money), tContractCoin.getBaseCoin(), appUser.getAdminParentIds());
        return "success";
    }

    @Override
    public String adjustAmout(Long id, BigDecimal money, String flag) {
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(id);
        TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(contractPosition.getSymbol());

        TAppAsset asset = appAssetService.getAssetByUserIdAndType(contractPosition.getUserId(), AssetEnum.CONTRACT_ASSETS.getCode());

        //增加保证金
        if ("0".equals(flag)) {
            if (money.compareTo(asset.getAvailableAmount()) > 0) {
                return MessageUtils.message("contract.asset.error");
            }
        }
        BigDecimal amont = contractPosition.getAmount();

        BigDecimal adjust = contractPosition.getAdjustAmount();

        BigDecimal level = contractPosition.getLeverage();

        Integer type = contractPosition.getType();

        BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + contractPosition.getSymbol().toLowerCase());

        if (contractPosition.getStatus() != 0) {
            return MessageUtils.message("order.status.error");
        }
        BigDecimal adjustAmount = BigDecimal.ZERO;

        BigDecimal afterAmount = BigDecimal.ZERO;
        //增加保证金
        if ("0".equals(flag)) {
            adjustAmount = adjust.add(money);
            afterAmount = asset.getAvailableAmount().subtract(money);
            //减少保证金
        } else if ("1".equals(flag)) {
            adjustAmount = adjust.subtract(money);
            afterAmount = asset.getAvailableAmount().add(money);
            BigDecimal earn = ContractComputerUtil.getPositionEarn(contractPosition.getOpenPrice(), contractPosition.getOpenNum(), currentlyPrice, type);
            if (earn.compareTo(BigDecimal.ZERO) < 0) {
                if (adjust.add(earn).subtract(money).compareTo(amont) < 0) {
                    return MessageUtils.message("adjust.min.error");
                }
            } else {
                if (adjust.subtract(money).compareTo(amont) < 0) {
                    return MessageUtils.message("adjust.min.error");
                }
            }
        }
        BigDecimal closePrice = ContractComputerUtil.getStrongPrice(level, type, contractPosition.getOpenPrice(), contractPosition.getOpenNum(), adjustAmount, BigDecimal.ZERO);
        contractPosition.setAdjustAmount(adjustAmount);
        contractPosition.setRemainMargin(adjustAmount);
        contractPosition.setClosePrice(closePrice);
        tContractPositionMapper.updateTContractPosition(contractPosition);
        BigDecimal assetAmout = asset.getAmout();
        if ("0".equals(flag)) {
            asset.setAmout(assetAmout.subtract(money));
            asset.setAvailableAmount(asset.getAvailableAmount().subtract(money));
            appAssetService.updateTAppAsset(asset);
        } else if ("1".equals(flag)) {
            asset.setAmout(assetAmout.add(money));
            asset.setAvailableAmount(asset.getAvailableAmount().add(money));
            appAssetService.updateTAppAsset(asset);
        }
        TAppUser appUser = appUserService.getById(contractPosition.getUserId());
        appWalletRecordService.generateRecord(contractPosition.getUserId(), money, RecordEnum.CONTRACT_TRADING_ADJUSTMENT_MARGIN.getCode(), null, contractPosition.getOrderNo(), "调整保证金", asset.getAvailableAmount(), afterAmount, tContractCoin.getCoin(), appUser.getAdminParentIds());
        return "success";
    }

    @Override
    public String verifyStopPostion(Long id) {
        String result = "success";
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(id);
        Integer auditStaus = Objects.isNull(contractPosition.getAuditStatus()) ? 0 : contractPosition.getAuditStatus();

        String check=checkPositon(contractPosition);
        if (auditStaus == 1) {
            if("ok".equals(check) || "okday".equals(check)){
                return MessageUtils.message("order.audit.pass");
            }
        }
        if("ok".equals(check) || "okday".equals(check)){
            return MessageUtils.message("order.audit.reject");
        }

        if (auditStaus == 3) {
            return MessageUtils.message("order.audit.error");
        }
        if (auditStaus != 5 && auditStaus != 2) {
            //已提交
            contractPosition.setAuditStatus(5);
            updateTContractPosition(contractPosition);
        }
        //当前价
        BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + contractPosition.getSymbol().toLowerCase());
        Date subTime = contractPosition.getSubTime();
        //交割时间
        Integer deliveryDays = Objects.isNull(contractPosition.getDeliveryDays()) ? 0 : contractPosition.getDeliveryDays();
        //止盈率
        BigDecimal earnRate = Objects.isNull(contractPosition.getEarnRate()) ? BigDecimal.ZERO : contractPosition.getEarnRate();
        //止损率
        BigDecimal lossRate = Objects.isNull(contractPosition.getLossRate()) ? BigDecimal.ZERO : contractPosition.getLossRate();

        //开仓均价
        BigDecimal openPrice = contractPosition.getOpenPrice();
        //调整保证金
        BigDecimal adjustAmount = contractPosition.getAdjustAmount();

        BigDecimal remin=contractPosition.getRemainMargin();
        //杠杆
        BigDecimal level = contractPosition.getLeverage();
        //
        int type = contractPosition.getType();
        //止盈价
        BigDecimal earnPrice=ContractComputerUtil.getEarnDealPrice(openPrice,type,earnRate);
        BigDecimal rate = ContractComputerUtil.getPositionRate(openPrice, currentlyPrice, type);
//        if(type==0) {            //当前价>开仓价
//            //  开盘   16000   止盈价 18000    当前  17000
//            if(currentlyPrice.compareTo(earnPrice)>=0){
//                redisCache.setCacheObject(CachePrefix.POSITION_PRICE.getPrefix() + contractPosition.getOrderNo(),earnPrice);
//            }
//            BigDecimal  flag = redisCache.getCacheObject(CachePrefix.POSITION_PRICE.getPrefix() + contractPosition.getOrderNo());
//            if(Objects.nonNull(flag) && currentlyPrice.compareTo(flag)<0){
//                if(currentlyPrice.compareTo(openPrice)>0  && currentlyPrice.compareTo(earnPrice) < 0) {
//                    rate= ContractComputerUtil.getPositionRate(earnPrice, currentlyPrice, type);
//                }
//            }
//        }else if(type==1){
//            if(currentlyPrice.compareTo(earnPrice)<=0){
//                redisCache.setCacheObject(CachePrefix.POSITION_PRICE.getPrefix() + contractPosition.getOrderNo(),earnPrice);
//            }
//            BigDecimal  flag = redisCache.getCacheObject(CachePrefix.POSITION_PRICE.getPrefix() + contractPosition.getOrderNo());
//            if(Objects.nonNull(flag) && currentlyPrice.compareTo(flag)>0){
//                if (currentlyPrice.compareTo(openPrice) < 0 && currentlyPrice.compareTo(earnPrice) > 0) {
//                    rate = ContractComputerUtil.getPositionRate(earnPrice, currentlyPrice, type);
//                }
//            }
//        }
        //收益率
        BigDecimal bigDecimal =adjustAmount.add((adjustAmount.multiply(level).multiply(earnRate).setScale(4, RoundingMode.UP)));
//        if (rate.compareTo(BigDecimal.ZERO) < 0 ) {
//            bigDecimal = bigDecimal.subtract((adjustAmount.multiply(rate).multiply(level).setScale(4, RoundingMode.UP)));
//        }
        BigDecimal loss = adjustAmount.multiply(level).multiply(lossRate).setScale(4, RoundingMode.UP);
        int sub = 0;

        if (deliveryDays > 0) {
            int days = DateUtil.daysBetween(subTime, new Date());
            sub=deliveryDays-days;
            if (sub > 0) {
                result = MessageUtils.message("contract.delivery.day", sub);
            }
        }
        if (remin.compareTo(BigDecimal.ZERO) > 0 && lossRate.compareTo(BigDecimal.ZERO) > 0) {
            if (remin.compareTo(bigDecimal) >= 0) {
                closePosition(id);
                return "success";
            }
            if (remin.compareTo(bigDecimal) < 0 ||  remin.compareTo(loss) > 0) {
                result = MessageUtils.message("contract.delivery.margan", new Object[]{sub, bigDecimal, loss});
            }
            if (remin.compareTo(loss) <= 0) {
                closePosition(id);
                return "success";
            }
        }
        if (earnRate.compareTo(BigDecimal.ZERO) > 0 && lossRate.compareTo(BigDecimal.ZERO) == 0) {
            if (remin.compareTo(bigDecimal) < 0) {
                result = MessageUtils.message("contract.delivery.earn", new Object[]{sub, bigDecimal});
            } else {
                closePosition(id);
                return "success";
            }
        }
        if (lossRate.compareTo(BigDecimal.ZERO) > 0 && earnRate.compareTo(BigDecimal.ZERO) == 0) {
            if (remin.compareTo(loss) > 0) {
                result = MessageUtils.message("contract.delivery.loss", new Object[]{sub, loss});
            }else {
                closePosition(id);
                return "success";
            }
        }

        return result;
    }

    @Override
    public String pass(TContractPosition tContractPosition) {
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(tContractPosition.getId());
        Integer auditStaus = Objects.isNull(contractPosition.getAuditStatus()) ? 0 : contractPosition.getAuditStatus();
        Integer status = Objects.isNull(contractPosition.getStatus()) ? 0 : contractPosition.getStatus();
        if (status == 1) {
            return "不是待成交状态";
        }
        if (auditStaus == 1) {
            return "请勿重复审核";
        }
//        if (auditStaus != 3) {
//            return "不是待审核状态";
//        }
//        TAppUser appUser = appUserService.getById(contractPosition.getUserId());
//        if (contractPosition.getStatus() != 0) {
//            return MessageUtils.message("order.status.error");
//        }
        String symol = contractPosition.getSymbol();
        //  TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(symol);
        //当前价
        BigDecimal amount = contractPosition.getAdjustAmount();
        BigDecimal earn = contractPosition.getEarn();
        //    TAppAsset asset = appAssetService.getAssetByUserIdAndType(contractPosition.getUserId(), AssetEnum.CONTRACT_ASSETS.getCode());
        //  BigDecimal amont = asset.getAmout();
        //  BigDecimal availAsset = asset.getAvailableAmount();
        // BigDecimal money = amount.add(earn).subtract(contractPosition.getSellFee());
        //   asset.setAmout(amont.add(money));
        //  asset.setAvailableAmount(availAsset.add(money));
        //  appAssetService.updateTAppAsset(asset);
        // appWalletRecordService.generateRecord(contractPosition.getUserId(), money, RecordEnum.CONTRACT_TRANSACTION_CLOSING.getCode(), null, contractPosition.getOrderNo(), "合约交易平仓", amont, amont.add(money), tContractCoin.getBaseCoin(), appUser.getAdminParentIds());
        //contractPosition.setStatus(1);
        contractPosition.setAuditStatus(1);
        contractPosition.setUpdateTime(new Date());
        updateTContractPosition(contractPosition);
        return "success";
    }

    @Override
    public String reject(TContractPosition tContractPosition) {
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(tContractPosition.getId());
        Integer status = Objects.isNull(contractPosition.getStatus()) ? 0 : contractPosition.getStatus();
        if (status == 1) {
            return "不是待成交状态";
        }
        contractPosition.setAuditStatus(2);
        contractPosition.setUpdateTime(new Date());
        updateTContractPosition(contractPosition);
        return "success";
    }

    //追加保证金
    @Override
    public String adjustPositionMargn(Long id, BigDecimal money) {
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(id);
        //平台账户
   //     TAppAsset outAsset = appAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId, contractPosition.getUserId()).eq(TAppAsset::getSymbol, "usdt").eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()));
        TAppUser appUser = appUserService.selectTAppUserByUserId(contractPosition.getUserId());

        //合约账户
        TAppAsset inAsset = appAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId, contractPosition.getUserId()).eq(TAppAsset::getSymbol, "usdt").eq(TAppAsset::getType, AssetEnum.CONTRACT_ASSETS.getCode()));
        if (inAsset.getAvailableAmount().compareTo(money) < 0) {
            return MessageUtils.message("asset_amount_error");
        }
//        BigDecimal availableAmount = outAsset.getAvailableAmount();
//        outAsset.setAvailableAmount(availableAmount.subtract(money));
//        outAsset.setAmout(outAsset.getAmout().subtract(money));
//        appAssetService.updateTAppAsset(outAsset);
      //  appWalletRecordService.generateRecord(contractPosition.getUserId(), money, RecordEnum.CONTRACT_ADD_AMOUT.getCode(), "", "", "平台资产-", availableAmount, availableAmount.subtract(money), "usdt", appUser.getAdminParentIds());

        contractPosition.setRemainMargin(contractPosition.getRemainMargin().add(money));
        this.updateTContractPosition(contractPosition);
        BigDecimal availableAmount1 = inAsset.getAvailableAmount();
        inAsset.setAvailableAmount(availableAmount1.subtract(money));
        inAsset.setAmout(inAsset.getAmout().subtract(money));
        appAssetService.updateTAppAsset(inAsset);
        //添加帐变
        appWalletRecordService.generateRecord(contractPosition.getUserId(), money, RecordEnum.CONTRACT_ADD_AMOUT.getCode(), "", "", "合约资产-", availableAmount1, availableAmount1.subtract(money), "usdt", appUser.getAdminParentIds());
        return "success";
    }

    //追加本金
    @Override
    public String adjustPositionAmout(Long id, BigDecimal money) {

        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(id);
        TAppAsset asset = appAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId, contractPosition.getUserId()).eq(TAppAsset::getSymbol, "usdt").eq(TAppAsset::getType, AssetEnum.CONTRACT_ASSETS.getCode()));
        TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(contractPosition.getSymbol());

        if (money.compareTo(asset.getAvailableAmount()) > 0) {
            return MessageUtils.message("contract.asset.error");
        }
        String symbol = contractPosition.getSymbol().toLowerCase();
        //杠杆
        BigDecimal leverage = contractPosition.getLeverage();

        //BigDecimal openFee = tContractCoin.getOpenFee().multiply(money).setScale(6, RoundingMode.HALF_UP);

        BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + symbol);
        TAppUser appUser = appUserService.selectTAppUserByUserId(contractPosition.getUserId());
        BigDecimal availableAmount = asset.getAvailableAmount();
        //开仓均价

        //计算数量

        BigDecimal num = money.multiply(leverage).divide(currentlyPrice, 6, RoundingMode.DOWN);
        contractPosition.setAmount(contractPosition.getAmount().add(money));
        contractPosition.setAdjustAmount(contractPosition.getAdjustAmount().add(money));
        contractPosition.setRemainMargin(contractPosition.getRemainMargin().add(money));
        contractPosition.setOpenNum(contractPosition.getOpenNum().add(num));
        contractPosition.setOpenPrice(currentlyPrice);
        contractPosition.setEntrustmentValue(contractPosition.getEntrustmentValue().add((currentlyPrice.multiply(num).setScale(6, RoundingMode.HALF_UP))));
        this.updateTContractPosition(contractPosition);
        asset.setAmout(asset.getAmout().subtract(money));
        asset.setAvailableAmount(asset.getAvailableAmount().subtract(money));
        appAssetService.updateTAppAsset(asset);

        appWalletRecordService.generateRecord(contractPosition.getUserId(), money, RecordEnum.CONTRACT_ADD.getCode(), "", contractPosition.getOrderNo(), RecordEnum.CONTRACT_ADD.getInfo(), availableAmount, availableAmount.subtract(money), "usdt", appUser.getAdminParentIds());


        return "success";
    }

    @Override
    public String closePosition(Long id) {
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(id);
        Integer auditStaus = Objects.isNull(contractPosition.getAuditStatus()) ? 0 : contractPosition.getAuditStatus();
        if (auditStaus == 3) {
            return MessageUtils.message("order.audit.error");
        }
        TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(contractPosition.getSymbol());
        BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + contractPosition.getSymbol().toLowerCase());
        BigDecimal sellFee = tContractCoin.getCloseFee();
        contractPosition.setDealNum(contractPosition.getOpenNum());
        contractPosition.setStatus(0);
        contractPosition.setAuditStatus(3);
        contractPosition.setDealPrice(currentlyPrice);
        contractPosition.setSellFee(contractPosition.getAdjustAmount().multiply(sellFee).setScale(4, RoundingMode.HALF_UP));
        BigDecimal earn = ContractComputerUtil.getPositionEarn(contractPosition.getOpenPrice(), contractPosition.getOpenNum(), currentlyPrice, contractPosition.getType());
        contractPosition.setEarn(earn);
        updateTContractPosition(contractPosition);
        return "success";
    }

    @Override
    public String stopPosition(Long id) {
        TContractPosition contractPosition = tContractPositionMapper.selectTContractPositionById(id);
        Integer status = Objects.isNull(contractPosition.getStatus()) ? 0 : contractPosition.getStatus();
        if (status == 1) {
            return "不是待成交状态";
        }
//        if (auditStaus == 3) {
//            return MessageUtils.message("order.audit.error");
//
//        }
        BigDecimal dealPrice = Objects.isNull(contractPosition.getDealPrice()) ? BigDecimal.ZERO : contractPosition.getDealPrice();
        TAppUser appUser = appUserService.getById(contractPosition.getUserId());
        if (contractPosition.getStatus() != 0) {
            return MessageUtils.message("order.status.error");
        }
        String symol = contractPosition.getSymbol();
        //类型0  买入买多 1 卖出做空
        Integer type = contractPosition.getType();
        //开仓均价
        BigDecimal openPrice = contractPosition.getOpenPrice();
        BigDecimal num = contractPosition.getOpenNum();
        BigDecimal amount = contractPosition.getRemainMargin();
        TContractCoin tContractCoin = contractCoinService.selectContractCoinBySymbol(symol);
        //当前价
        if (dealPrice.compareTo(BigDecimal.ZERO) == 0) {
            dealPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + contractPosition.getSymbol().toLowerCase());

        }
        BigDecimal sellFee = tContractCoin.getCloseFee();
        contractPosition.setDealNum(num);
        contractPosition.setStatus(1);
        contractPosition.setAuditStatus(1);
        contractPosition.setDealPrice(dealPrice);
        contractPosition.setSellFee(contractPosition.getAdjustAmount().multiply(sellFee).setScale(4, RoundingMode.HALF_UP));
        BigDecimal earn = ContractComputerUtil.getPositionEarn(openPrice, num, dealPrice, type);
        contractPosition.setEarn(earn);
        contractPosition.setDealTime(new Date());
        updateTContractPosition(contractPosition);
        //撤销止盈止损
        //tContractPositionMapper.updateContractLoss(contractPosition.getId());
        TAppAsset asset = appAssetService.getAssetByUserIdAndType(contractPosition.getUserId(), AssetEnum.CONTRACT_ASSETS.getCode());
        BigDecimal amont = asset.getAmout();
        BigDecimal availAsset = asset.getAvailableAmount();
        BigDecimal money = amount.add(earn).subtract(contractPosition.getSellFee());
        asset.setAmout(amont.add(money));
        asset.setAvailableAmount(availAsset.add(money));
        appAssetService.updateTAppAsset(asset);
        appWalletRecordService.generateRecord(contractPosition.getUserId(), money, RecordEnum.CONTRACT_TRANSACTION_CLOSING.getCode(), null, contractPosition.getOrderNo(), "合约交易平仓", amont, amont.add(money), tContractCoin.getBaseCoin(), appUser.getAdminParentIds());
        return "success";
    }

    @Override
    public String stopAllPosition(Long id) {
        TContractPosition contractPosition=this.getById(id);
        Integer status = Objects.isNull(contractPosition.getStatus()) ? 0 : contractPosition.getStatus();
        if (status == 1) {
            return "不是待成交状态";
        }
        BigDecimal earn = ContractComputerUtil.getPositionEarn(contractPosition.getOpenPrice(), contractPosition.getOpenNum(), contractPosition.getClosePrice(), contractPosition.getType());
        contractPosition.setStatus(1);
        contractPosition.setAuditStatus(1);
        contractPosition.setDealTime(new Date());
        contractPosition.setDealNum(contractPosition.getOpenNum());
        contractPosition.setDealValue(contractPosition.getOpenNum().multiply(contractPosition.getDealPrice()).setScale(6,RoundingMode.HALF_UP));
        contractPosition.setEarn(earn);
        this.updateTContractPosition(contractPosition);
        return "success";
    }


    public String checkProfit(TContractCoin tContractCoin) {
        String result = "ok";
          //止盈率
        BigDecimal floatProit = Objects.isNull(tContractCoin.getFloatProfit()) ? BigDecimal.ZERO : tContractCoin.getFloatProfit();
        //止损率
        BigDecimal profitLoss = Objects.isNull(tContractCoin.getProfitLoss()) ? BigDecimal.ZERO : tContractCoin.getProfitLoss();
        if (floatProit.compareTo(BigDecimal.ZERO) > 0) {
            result = result + "earn";
        }
        if (profitLoss.compareTo(BigDecimal.ZERO) > 0) {
            result = result + "loss";
        }
        return result;
    }

    public String checkPositon(TContractPosition tContractPosition) {
        String result = "ok";

        Integer deliverDays = Objects.isNull(tContractPosition.getDeliveryDays()) ? 0 : tContractPosition.getDeliveryDays();
        //止盈率
        BigDecimal earnRate = Objects.isNull(tContractPosition.getEarnRate()) ? BigDecimal.ZERO : tContractPosition.getEarnRate();
        //止盈率
        BigDecimal lossRate = Objects.isNull(tContractPosition.getLossRate()) ? BigDecimal.ZERO : tContractPosition.getLossRate();
        if (deliverDays > 0) {
            result = result + "day";
        }
        if (earnRate.compareTo(BigDecimal.ZERO) > 0) {
            result = result + "earn";
        }
        if (lossRate.compareTo(BigDecimal.ZERO) > 0) {
            result = result + "loss";
        }
        return result;
    }
}
