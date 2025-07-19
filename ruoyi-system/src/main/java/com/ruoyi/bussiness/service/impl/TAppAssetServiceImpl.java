package com.ruoyi.bussiness.service.impl;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.*;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.vo.AssetTransFundsVO;
import com.ruoyi.bussiness.domain.vo.UserBonusVO;

import com.ruoyi.bussiness.mapper.TAppAssetMapper;
import com.ruoyi.bussiness.mapper.TAppUserMapper;
import com.ruoyi.bussiness.mapper.TWithdrawMapper;
import com.ruoyi.bussiness.service.ITAppAssetService;
import com.ruoyi.bussiness.service.ITAppRechargeService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 玩家资产Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
@Service
public class TAppAssetServiceImpl extends ServiceImpl<TAppAssetMapper, TAppAsset>  implements ITAppAssetService
{
    @Resource
    private TAppAssetMapper tAppAssetMapper;
    @Resource
    private TAppUserMapper tAppUserMapper;
    @Resource
    private TAppWalletRecordServiceImpl tAppWalletRecordService;
    @Resource
    private ITAppRechargeService itAppRechargeService;
    @Resource
    private TWithdrawMapper tWithdrawMapper;
    /**
     * 查询玩家资产
     * 
     * @param userId 玩家资产主键
     * @return 玩家资产
     */
    @Override
    public TAppAsset selectTAppAssetByUserId(Long userId)
    {
        return tAppAssetMapper.selectTAppAssetByUserId(userId);
    }

    /**
     * 查询玩家资产列表
     * 
     * @param tAppAsset 玩家资产
     * @return 玩家资产
     */
    @Override
    public List<TAppAsset> selectTAppAssetList(TAppAsset tAppAsset)
    {
        return tAppAssetMapper.selectTAppAssetList(tAppAsset);
    }

    /**
     * 新增玩家资产
     * 
     * @param tAppAsset 玩家资产
     * @return 结果
     */
    @Override
    public int insertTAppAsset(TAppAsset tAppAsset)
    {
        tAppAsset.setCreateTime(DateUtils.getNowDate());
        return tAppAssetMapper.insertTAppAsset(tAppAsset);
    }

    /**
     * 修改玩家资产
     * 
     * @param tAppAsset 玩家资产
     * @return 结果
     */
    @Override
    public int updateTAppAsset(TAppAsset tAppAsset)
    {
        tAppAsset.setUpdateTime(DateUtils.getNowDate());
        return tAppAssetMapper.updateTAppAsset(tAppAsset);
    }

    /**
     * 批量删除玩家资产
     * 
     * @param userIds 需要删除的玩家资产主键
     * @return 结果
     */
    @Override
    public int deleteTAppAssetByUserIds(Long[] userIds)
    {
        return tAppAssetMapper.deleteTAppAssetByUserIds(userIds);
    }

    /**
     * 删除玩家资产信息
     * 
     * @param userId 玩家资产主键
     * @return 结果
     */
    @Override
    public int deleteTAppAssetByUserId(Long userId)
    {
        return tAppAssetMapper.deleteTAppAssetByUserId(userId);
    }

    /**
     * 平台资产
     * @param userId
     * @return
     */
    @Override
    public Map<String, TAppAsset> getAssetByUserIdList(Long userId) {
        List<TAppAsset> list = tAppAssetMapper.selectList(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId,userId).eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()));
        Map<String, TAppAsset> map = new HashMap<>();
        list.stream().forEach(asset -> {
            map.put(asset.getSymbol() + asset.getUserId(), asset);
        });
        return map;
    }

    @Override
    public TAppAsset getAssetByUserIdAndType(Long userId, Integer type) {
        return tAppAssetMapper.selectOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId,userId).eq(TAppAsset::getType,type));
    }

    @Override
    public int updateByUserId(TAppAsset tAppAsset) {
        return tAppAssetMapper.updateByUserId(tAppAsset);
    }

    @Override
    @Transactional
    public int sendBouns(UserBonusVO userBounsVO) {
        TAppUser appUser = tAppUserMapper.selectById(userBounsVO.getUserId());
        TAppAsset appAsset = tAppAssetMapper.selectOne(new LambdaQueryWrapper<TAppAsset>()
                .eq(TAppAsset::getUserId, userBounsVO.getUserId())
                .eq(TAppAsset::getSymbol, userBounsVO.getSymbol())
                .eq(TAppAsset::getType, userBounsVO.getType()));
        Integer code=RecordEnum.SEND_BONUS.getCode();
        String remark=(userBounsVO.getRemark()==null?RecordEnum.SEND_BONUS.getInfo():userBounsVO.getRemark());

        String serialId = "C" + OrderUtils.generateOrderNum();

        if("0".equals(userBounsVO.getGiveType())){
            //上分
            tAppWalletRecordService.generateRecord(userBounsVO.getUserId(),userBounsVO.getAmount(), code,
                    userBounsVO.getCreateBy(),serialId,remark,
                    appAsset.getAmout(),appAsset.getAmout().add(userBounsVO.getAmount()),userBounsVO.getSymbol(),appUser.getAdminParentIds());
            appAsset.setAmout(appAsset.getAmout().add(userBounsVO.getAmount()));
            appAsset.setAvailableAmount(appAsset.getAvailableAmount().add(userBounsVO.getAmount()));
            //新增充值订单
            TAppRecharge recharge = new TAppRecharge();
            recharge.setOrderType("2");
            recharge.setSerialId(serialId);
            recharge.setUserId(appUser.getUserId());
            recharge.setUsername(appUser.getLoginName());
            recharge.setAmount(userBounsVO.getAmount());
            recharge.setBonus(0L);
            recharge.setStatus("1");
            recharge.setType(userBounsVO.getSymbol().toUpperCase());
            recharge.setAppParentIds(appUser.getAppParentIds());
            recharge.setAdminParentIds(appUser.getAdminParentIds());
            recharge.setCoin(userBounsVO.getSymbol().toLowerCase());
            recharge.setRealAmount(userBounsVO.getAmount());
            recharge.setOperateTime(new Date());
            recharge.setCreateTime(new Date());
            recharge.setCreateBy(appUser.getLoginName());
            recharge.setUpdateTime(new Date());
            itAppRechargeService.save(recharge);
        }
        if("1".equals(userBounsVO.getGiveType())){
            if(userBounsVO.getAmount().compareTo(appAsset.getAmout())>0){
                return 1;
            }
            code=RecordEnum.SUB_BONUS.getCode();
            remark=(userBounsVO.getRemark()==null?RecordEnum.SUB_BONUS.getInfo():userBounsVO.getRemark());
            tAppWalletRecordService.generateRecord(userBounsVO.getUserId(),userBounsVO.getAmount(), code,
                    userBounsVO.getCreateBy(),serialId,remark,
                    appAsset.getAmout(),appAsset.getAmout().subtract(userBounsVO.getAmount()),userBounsVO.getSymbol(),appUser.getAdminParentIds());
            appAsset.setAmout(appAsset.getAmout().subtract(userBounsVO.getAmount()));
            appAsset.setAvailableAmount(appAsset.getAvailableAmount().subtract(userBounsVO.getAmount()));
            //新增提现订单
            TWithdraw withdraw = new TWithdraw();
            withdraw.setOrderType("2");
            withdraw.setAmount(userBounsVO.getAmount());
            withdraw.setType(userBounsVO.getSymbol().toUpperCase());
            withdraw.setStatus(1);
            withdraw.setUserId(appUser.getUserId());
            withdraw.setUsername(appUser.getLoginName());
            Date now = new Date();
            withdraw.setUpdateTime(now);
            withdraw.setCreateTime(now);
            withdraw.setOperateTime(now);
            withdraw.setCreateBy(appUser.getLoginName());
            withdraw.setNoticeFlag(1);
            withdraw.setRealAmount(userBounsVO.getAmount());
            withdraw.setSerialId(serialId);
            withdraw.setCoin(userBounsVO.getSymbol().toLowerCase());
            withdraw.setAdminParentIds(appUser.getAdminParentIds());
            tWithdrawMapper.insertTWithdraw(withdraw);
        }

        tAppAssetMapper.updateByUserId(appAsset);
        return 0;
    }
    @Override
    public int subAmount(UserBonusVO userBounsVO) {
        TAppUser appUser = tAppUserMapper.selectById(userBounsVO.getUserId());
        TAppAsset appAsset = tAppAssetMapper.selectOne(new LambdaQueryWrapper<TAppAsset>()
                .eq(TAppAsset::getUserId, userBounsVO.getUserId())
                .eq(TAppAsset::getSymbol, userBounsVO.getSymbol())
                .eq(TAppAsset::getType, userBounsVO.getType()));
        Integer code=RecordEnum.SEND_AMOUNT.getCode();
        String remark=(userBounsVO.getRemark()==null?RecordEnum.SEND_AMOUNT.getInfo():userBounsVO.getRemark());
        if("0".equals(userBounsVO.getGiveType())){
            //上分
            tAppWalletRecordService.generateRecord(userBounsVO.getUserId(),userBounsVO.getAmount(), code,
                    userBounsVO.getCreateBy(),"C" + OrderUtils.generateOrderNum(),remark,
                    appAsset.getAmout(),appAsset.getAmout().add(userBounsVO.getAmount()),userBounsVO.getSymbol(),appUser.getAdminParentIds());
            appAsset.setAmout(appAsset.getAmout().add(userBounsVO.getAmount()));
            appAsset.setAvailableAmount(appAsset.getAvailableAmount().add(userBounsVO.getAmount()));
        }
        if("1".equals(userBounsVO.getGiveType())){
            if(userBounsVO.getAmount().compareTo(appAsset.getAmout())>0){
                return 1;
            }
            code=RecordEnum.SUB_AMOUNT.getCode();
            remark=(userBounsVO.getRemark()==null?RecordEnum.SUB_AMOUNT.getInfo():userBounsVO.getRemark());
            tAppWalletRecordService.generateRecord(userBounsVO.getUserId(),userBounsVO.getAmount(), code,
                    userBounsVO.getCreateBy(),"C" + OrderUtils.generateOrderNum(),remark,
                    appAsset.getAmout(),appAsset.getAmout().subtract(userBounsVO.getAmount()),userBounsVO.getSymbol(),appUser.getAdminParentIds());
            appAsset.setAmout(appAsset.getAmout().subtract(userBounsVO.getAmount()));
            appAsset.setAvailableAmount(appAsset.getAvailableAmount().subtract(userBounsVO.getAmount()));
        }



        tAppAssetMapper.updateByUserId(appAsset);
        return 0;
    }
    @Override
    public int noSettlementAssetByUserId(Long userId, String symbol, BigDecimal amout) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("symbol", symbol);
        map.put("money", amout);
        map.put("type", AssetEnum.CONTRACT_ASSETS.getCode());
        return tAppAssetMapper.noSettlementAssetByUserId(map);
    }

    @Override
    public int createAsset(TAppUser user, String coin, Integer type) {
        TAppAsset tAppAsset = new TAppAsset();
        tAppAsset.setUserId(user.getUserId());
        tAppAsset.setAdress(user.getAddress());
        tAppAsset.setSymbol(coin);
        tAppAsset.setAmout(BigDecimal.ZERO);
        tAppAsset.setOccupiedAmount(BigDecimal.ZERO);
        tAppAsset.setAvailableAmount(BigDecimal.ZERO);
        tAppAsset.setType(type);
        tAppAsset.setCreateTime(DateUtils.getNowDate());
        tAppAsset.setUpdateTime(DateUtils.getNowDate());
        return tAppAssetMapper.insertTAppAsset(tAppAsset);


    }

    @Override
    public int reduceAssetByUserId(Long userId, String symbol, BigDecimal amout) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("symbol", symbol);
        map.put("money", amout);
        map.put("type", AssetEnum.PLATFORM_ASSETS.getCode());
        return tAppAssetMapper.reduceAssetByUserId(map);
    }

    @Override
    public int addAssetByUserId(Long userId, String symbol, BigDecimal amout) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("symbol", symbol);
        map.put("money", amout);
        map.put("type", AssetEnum.PLATFORM_ASSETS.getCode());
        return tAppAssetMapper.addAssetByUserId(map);
    }

    @Override
    public int occupiedAssetByUserId(Long userId, String symbol, BigDecimal amout) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("symbol", symbol);
        map.put("money", amout);
        map.put("type", AssetEnum.PLATFORM_ASSETS.getCode());
        return tAppAssetMapper.occupiedAssetByUserId(map);
    }

    @Override
    public int settlementOccupiedCurrencyOrder(Long userId, String symbol, BigDecimal occupiedAmout, BigDecimal subtract) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("symbol", symbol);
        map.put("availableAmount", subtract);
        map.put("money", occupiedAmout);
        map.put("subtract", occupiedAmout.add(subtract));
        map.put("type", AssetEnum.PLATFORM_ASSETS.getCode());
        return tAppAssetMapper.settlementOccupiedCurrencyOrder(map);
    }

    @Override
    public String transferFunds(AssetTransFundsVO assetTransFundsVo) {
        long userId = StpUtil.getLoginIdAsLong();
        TAppUser appUser = tAppUserMapper.selectById(userId);
        if (Objects.nonNull(appUser.getIsLimitation())){
            if(appUser.getIsLimitation() == 2)
                return MessageUtils.message("user_is_limitation");
        }
        //效验转出账户余额
        String coin = assetTransFundsVo.getCoin().toLowerCase();
        TAppAsset outAsset = tAppAssetMapper.selectOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId,userId).eq(TAppAsset::getSymbol, coin).eq(TAppAsset::getType, assetTransFundsVo.getTransferOutAccount()));
        if(outAsset.getAvailableAmount().compareTo(assetTransFundsVo.getAmount())<0){
            return MessageUtils.message("asset_amount_error");
        }
        //资金充足 开始划转  1 转出 扣减余额  2 转入 增加余额
        BigDecimal availableAmount = outAsset.getAvailableAmount();
        outAsset.setAvailableAmount(availableAmount.subtract(assetTransFundsVo.getAmount()));
        outAsset.setAmout(outAsset.getAmout().subtract(assetTransFundsVo.getAmount()));
        tAppAssetMapper.updateTAppAsset(outAsset);
        //添加帐变
        tAppWalletRecordService.generateRecord(userId,assetTransFundsVo.getAmount(), RecordEnum.FUND_TRANSFER.getCode(),"", "",RecordEnum.FUND_TRANSFER.getInfo(),availableAmount,availableAmount.subtract(assetTransFundsVo.getAmount()), outAsset.getSymbol(),appUser.getAdminParentIds());



        TAppAsset inAsset = tAppAssetMapper.selectOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId,userId).eq(TAppAsset::getSymbol, coin).eq(TAppAsset::getType, assetTransFundsVo.getTransferInAccount()));
        BigDecimal availableAmount1 = inAsset.getAvailableAmount();
        inAsset.setAvailableAmount(availableAmount1.add(assetTransFundsVo.getAmount()));
        inAsset.setAmout(inAsset.getAmout().add(assetTransFundsVo.getAmount()));
        tAppAssetMapper.updateTAppAsset(inAsset);
        //添加帐变
        tAppWalletRecordService.generateRecord(userId,assetTransFundsVo.getAmount(), RecordEnum.FUND_TRANSFER.getCode(),"", "",RecordEnum.FUND_TRANSFER.getInfo(),availableAmount1,availableAmount1.add(assetTransFundsVo.getAmount()), inAsset.getSymbol(),appUser.getAdminParentIds());
        return null;
    }

    @Override
    public  List<TAppAsset> tAppAssetService() {
        List<TAppAsset> list = tAppAssetMapper.selectList(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId, StpUtil.getLoginIdAsLong()).eq(TAppAsset::getSymbol, "usdt"));
        return list;
    }
}
