package com.ruoyi.bussiness.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.AddMosaicSetting;
import com.ruoyi.bussiness.domain.setting.AuthLimitSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.domain.setting.TRechargeChannelSetting;
import com.ruoyi.bussiness.domain.vo.WithdrawFreezeVO;
import com.ruoyi.bussiness.mapper.TAppUserDetailMapper;
import com.ruoyi.bussiness.mapper.TAppUserMapper;
import com.ruoyi.bussiness.mapper.TWithdrawMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.utils.*;
import com.ruoyi.socket.dto.NoticeVO;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 用户提现Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-04
 */
@Service
@Slf4j
public class TWithdrawServiceImpl extends ServiceImpl<TWithdrawMapper, TWithdraw> implements ITWithdrawService {
    @Resource
    private TWithdrawMapper tWithdrawMapper;
    @Resource
    private ITAppAssetService tAppAssetService;
    @Resource
    private TAppUserMapper tAppUserMapper;
    @Resource
    private TAppUserDetailMapper tAppUserDetailMapper;
    @Resource
    private ITAppRechargeService appRechargeService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ITAppWalletRecordService appWalletRecordService;
    @Resource
    private SettingService settingService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysDictTypeService sysDictTypeService;

    @Resource
    private RedisUtil redisUtil;
    @Value("${admin-redis-stream.names}")
    private String redisStreamNames;

    /**
     * 查询用户提现
     *
     * @param id 用户提现主键
     * @return 用户提现
     */
    @Override
    public TWithdraw selectTWithdrawById(Long id) {
        return tWithdrawMapper.selectTWithdrawById(id);
    }

    /**
     * 查询用户提现列表
     *
     * @param tWithdraw 用户提现
     * @return 用户提现
     */
    @Override
    public List<TWithdraw> selectTWithdrawList(TWithdraw tWithdraw) {
        return tWithdrawMapper.selectTWithdrawList(tWithdraw);
    }

    /**
     * 新增用户提现
     *
     * @param tWithdraw 用户提现
     * @return 结果
     */
    @Override
    public int insertTWithdraw(TWithdraw tWithdraw) {
        tWithdraw.setCreateTime(DateUtils.getNowDate());
        return tWithdrawMapper.insertTWithdraw(tWithdraw);
    }

    /**
     * 修改用户提现
     *
     * @param tWithdraw 用户提现
     * @return 结果
     */
    @Override
    public int updateTWithdraw(TWithdraw tWithdraw) {
        tWithdraw.setUpdateTime(DateUtils.getNowDate());
        return tWithdrawMapper.updateTWithdraw(tWithdraw);
    }

    /**
     * 批量删除用户提现
     *
     * @param ids 需要删除的用户提现主键
     * @return 结果
     */
    @Override
    public int deleteTWithdrawByIds(Long[] ids) {
        return tWithdrawMapper.deleteTWithdrawByIds(ids);
    }

    /**
     * 删除用户提现信息
     *
     * @param id 用户提现主键
     * @return 结果
     */
    @Override
    public int deleteTWithdrawById(Long id) {
        return tWithdrawMapper.deleteTWithdrawById(id);
    }


    @Override
    public String submit(BigDecimal amount, String coinType, String pwd, String adress, String coin) {
        log.info("用户提现入参：amount：{}，coinType：{}，adress：{}，coin：{}，",amount,coinType,adress,coin);
        if(StringUtils.isEmpty(adress) || adress.trim().contains(" ")){
            return MessageUtils.message("withdraw.address.isnull");
        }
        if(StringUtils.isEmpty(coin)){
            coin = coinType.toLowerCase().replace("-erc","").replace("-trc","").trim();
        }
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("提现userId"+userId);
        log.info("id============="+userId);
        TAppUser user = tAppUserMapper.selectTAppUserByUserId(userId);
        TAppUserDetail tAppUserDetail = tAppUserDetailMapper.selectOne(new LambdaQueryWrapper<TAppUserDetail>().eq(TAppUserDetail::getUserId, userId));
        //资产
        Map<String, TAppAsset> assetMap = tAppAssetService.getAssetByUserIdList(userId);
        //实名认证限额 效验
        Boolean flag = true;
        Setting authSetting = settingService.get(SettingEnum.AUTH_LIMIT.name());
        if (null != authSetting) {
            AuthLimitSetting authLimitSetting = JSONUtil.toBean(authSetting.getSettingValue(), AuthLimitSetting.class);
            if ((Objects.nonNull(authLimitSetting.getIsOpenSenior()) && authLimitSetting.getIsOpenSenior()) || (Objects.nonNull(authLimitSetting.getIsOpenPrimary()) && authLimitSetting.getIsOpenPrimary())) {
                BigDecimal amountHs;
                TAppAsset tAppAsset = assetMap.get(coin + userId);
                if (Objects.nonNull(tAppAsset)){
                    if ("usdt".equals(coin.toLowerCase())){
                        amountHs = amount;
                    }else{
                        BigDecimal currencyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + coin.toLowerCase());
                        amountHs = amount.multiply(currencyPrice);
                    }
                }else{
                    amountHs = amount;
                }
//                else{
//                    BigDecimal currencyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + coin.toUpperCase()+"USD");
//                    if (Objects.isNull(currencyPrice)){
//                        currencyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + "USD"+coin.toUpperCase());
//                        if (Objects.isNull(currencyPrice)){
//                            return MessageUtils.message("withdraw_error_rate");
//                        }else{
//                            currencyPrice = BigDecimal.ONE.divide(currencyPrice);
//                        }
//                    }
//                    amountHs = amount.multiply(currencyPrice);
//                }

                if (Boolean.TRUE.equals(authLimitSetting.getIsOpenPrimary())) {
                    //初级限额 开启
                    //初级限额 额度
                    BigDecimal primaryLimit = (authLimitSetting.getPrimaryLimit() == null ? BigDecimal.ZERO : authLimitSetting.getPrimaryLimit());
                    if (null == tAppUserDetail.getAuditStatusPrimary() || !AuditStatusEnum.EXAMINATION_PASSED.getCode().equals(tAppUserDetail.getAuditStatusPrimary())) {
                        //未初级认证  或者认证不通过
                        if (amountHs.compareTo(primaryLimit) > 0) {
                            flag = false;
                        }
                    }
                }
                if (Boolean.TRUE.equals(authLimitSetting.getIsOpenSenior())) {
                    //高级限额 开启
                    //已初级认证  高级认证
                    BigDecimal seniorLimit = (authLimitSetting.getSeniorLimit() == null ? BigDecimal.ZERO : authLimitSetting.getSeniorLimit());
                    if (null == tAppUserDetail.getAuditStatusAdvanced() || !AuditStatusEnum.EXAMINATION_PASSED.getCode().equals(tAppUserDetail.getAuditStatusAdvanced())) {
                        //已初级认证  高级认证不通过
                        if (amountHs.compareTo(seniorLimit) > 0) {
                            flag = false;
                        }
                    }
                }
                //初级认证  高级认证  都认证
                if (null != tAppUserDetail.getAuditStatusPrimary() && null != tAppUserDetail.getAuditStatusAdvanced() && AuditStatusEnum.EXAMINATION_PASSED.getCode().equals(tAppUserDetail.getAuditStatusAdvanced()) && AuditStatusEnum.EXAMINATION_PASSED.getCode().equals(tAppUserDetail.getAuditStatusPrimary())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            return MessageUtils.message("withdraw.kyc.error");
        }
        if (StringUtils.isEmpty(tAppUserDetail.getUserTardPwd())) {
            return MessageUtils.message("user.password_notbind");
        }
        //增加密码校验
        if (!SecurityUtils.matchesPassword(pwd, tAppUserDetail.getUserTardPwd())) {
            return MessageUtils.message("tard_password.error");
        }
        Setting setting = settingService.get(SettingEnum.WITHDRAWAL_CHANNEL_SETTING.name());
        TRechargeChannelSetting set = null;
        List<TRechargeChannelSetting> tRechargeChannelSettings = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), TRechargeChannelSetting.class);
        for (TRechargeChannelSetting tRechargeChannelSetting : tRechargeChannelSettings) {
            if (tRechargeChannelSetting.getRechargeName().equals(coinType)) {
                set = tRechargeChannelSetting;
                break;
            }
        }
        if (set == null) {
            return MessageUtils.message("withdraw_error");
        }
        //当日提现次数
        int num = set.getDayWithdrawalNum();
        String today = DateUtil.today();
        Integer integer = this.baseMapper.selectCount(new LambdaQueryWrapper<TWithdraw>().eq(TWithdraw::getUserId, user.getUserId()).ge(TWithdraw::getCreateTime, today));
        if (integer >= num) {
            return MessageUtils.message("withdraw.amount_number_exceed");
        }

        if (Objects.nonNull(user.getIsLimitation())){
            if(user.getIsLimitation() == 2)
                return MessageUtils.message("user_is_limitation");
        }

        //系统免费次数
        int sysFreeNum = set.getFreeNum();
        boolean isFree = false;
        if (sysFreeNum > integer) {

            isFree = true;
            redisCache.setCacheObject(CacheConstants.WITHDRAW + today + ":" + user.getUserId(), integer + 1);
        }
        BigDecimal lowest = set.getWithdrawalMix();
        BigDecimal high = set.getWithdrawalMax();
        //提现比例
        BigDecimal ratio = BigDecimal.ZERO;
        //固定提现手续费
        BigDecimal fixedFee = BigDecimal.ZERO;
        BigDecimal fee = BigDecimal.ZERO;
        BigDecimal beforeMount = BigDecimal.ZERO;
        String remark = coinType + "提现";

        if (!isFree) {
            ratio = set.getRatio();
            fixedFee = set.getFee();
            // 新增字段 fixedFee 可能没有 没有的话默认未0
            if (fixedFee == null) {
                fixedFee = BigDecimal.ZERO;
            }
        }
        //U提现
        //不能超过钱包
        String finalCoin = coin;
        BigDecimal finalamount = amount;
        BigDecimal price = null;
        TAppAsset appAsset = assetMap.get(coin + user.getUserId());
        if (Objects.isNull(appAsset)) {
            AtomicReference<Boolean> flg = new AtomicReference<>(false);
            List<SysDictData> backCoinList = sysDictTypeService.selectDictDataByType("t_bank_coin");
            if (!CollectionUtils.isEmpty(backCoinList)){
                backCoinList.stream().forEach(a ->{
                    if (a.getDictValue().equals(finalCoin)){
                        flg.set(true);
                    }
                });
                if (flg.get()){
                    price = coin.toUpperCase().equals("USD")?BigDecimal.ONE:redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + coin.toUpperCase() + "USD");
                    if (Objects.isNull(price)){
                        price = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + "USD"+coin.toUpperCase());
                        if (Objects.isNull(price)){
                            return MessageUtils.message("withdraw_error_rate");
                        }else{
                            coin = "usdt";
                            finalamount = amount.multiply(price);
                            appAsset = assetMap.get(coin + user.getUserId());
                        }
                    }else{
                        coin = "usdt";
                        finalamount = amount.multiply(BigDecimal.ONE.divide(price,6, RoundingMode.DOWN));
                        appAsset = assetMap.get(coin + user.getUserId());
                    }
                }else{
                    return MessageUtils.message("withdraw_error_coin");
                }
            }
        }
        log.info("用户钱包：{}",appAsset.toString());
        if (Objects.isNull(appAsset)) {
            return MessageUtils.message("withdraw_error");
        }
        //最小效验
        if (appAsset.getAvailableAmount().compareTo(amount) < 0 || amount.compareTo(lowest) < 0) {
            return MessageUtils.message("withdraw.amount_error");
        }
        //超过最大值
        if (amount.compareTo(high) > 0) {
            return MessageUtils.message("withdraw.amount_error");
        }

        beforeMount = appAsset.getAvailableAmount();
        if (appAsset.getAvailableAmount().compareTo(amount) < 0) {
            return MessageUtils.message("withdraw_error");
        }

        //提现打码设置
        Setting addMosaicSetting = settingService.get(SettingEnum.ADD_MOSAIC_SETTING.name());
        if (Objects.nonNull(addMosaicSetting)) {
            AddMosaicSetting addMosaic = JSONUtil.toBean(addMosaicSetting.getSettingValue(), AddMosaicSetting.class);
            if (Objects.nonNull(addMosaic) && Objects.nonNull(addMosaic.getIsOpen())
                    && addMosaic.getIsOpen()) {
                if (Objects.isNull(user.getRechargeAmont())) user.setRechargeAmont(BigDecimal.ZERO);
                if (user.getTotleAmont().compareTo(user.getRechargeAmont()) < 0) {
                    return MessageUtils.message("withdraw_require_error");
                }
            }
        }


        TWithdraw withdraw = new TWithdraw();
        withdraw.setAmount(amount);
        withdraw.setReceiptAmount(finalamount);

        withdraw.setCoin(coin);
        withdraw.setReceiptCoin(finalCoin);

        withdraw.setType(coinType);
        withdraw.setStatus(0);
        withdraw.setUserId(user.getUserId());
        withdraw.setUsername(user.getLoginName());
        withdraw.setAddress(user.getAddress());
        Date now = new Date();
        withdraw.setUpdateTime(now);
        withdraw.setCreateTime(now);
        withdraw.setCreateBy(user.getLoginName());
        withdraw.setRatio(ratio);
        withdraw.setFixedFee(fixedFee);
        withdraw.setToAdress(adress);

        fee = amount.multiply(ratio).divide(new BigDecimal(100)).add(fixedFee);
        BigDecimal realAmount = amount.subtract(fee);
        if (Objects.nonNull(price)){
            BigDecimal receiptRealAmount = finalamount.subtract(fee.multiply(price));
            withdraw.setReceiptRealAmount(receiptRealAmount);
            withdraw.setExchangeRate(price);
        }else{
            withdraw.setReceiptRealAmount(realAmount);
            withdraw.setExchangeRate(BigDecimal.ONE);
        }
        withdraw.setFee(fee);
        withdraw.setNoticeFlag(0);
        withdraw.setRealAmount(realAmount);
        String serialId = "P" + OrderUtils.generateOrderNum();
        withdraw.setSerialId(serialId);
        withdraw.setAdminParentIds(user.getAdminParentIds());
        //增加对应u.btc,eth余额
        if (redisCache.tryLock("app:wallet:" + user.getUserId(), user.getUserId(), 1000)) {
            tAppAssetService.updateTAppAsset(
                    TAppAsset.builder()
                            .symbol(coin)
                            .userId(user.getUserId())
                            .amout(appAsset.getAmout().subtract(amount))
                            .availableAmount(appAsset.getAvailableAmount().subtract(amount))
                            .type(AssetEnum.PLATFORM_ASSETS.getCode())
                            .build());
            tWithdrawMapper.insertTWithdraw(withdraw);
            //发起提现则扣钱
            appWalletRecordService.generateRecord(user.getUserId(), amount, RecordEnum.WITHDRAW.getCode(), user.getLoginName(), withdraw.getSerialId(), remark, beforeMount, beforeMount.subtract(amount), coin, user.getAdminParentIds());
            //打码量归零 充值打码归零
            TAppUser tAppUser = new TAppUser();
            tAppUser.setUserId(user.getUserId());
            tAppUser.setRechargeAmont(BigDecimal.ZERO);
            tAppUser.setTotleAmont(BigDecimal.ZERO);
            tAppUserMapper.updateById(tAppUser);
            //socket 通知后台
            HashMap<String, Object> object = new HashMap<>();
            object.put(CacheConstants.WITHDRAW_KEY_BOT, JSON.toJSONString(withdraw));
            redisUtil.addStream(redisStreamNames, object);
            return null;
        } else {
            log.error("提现钱包锁定, userId:{}, amount:{}", user.getUserId(), amount);
            return MessageUtils.message("withdraw.refresh");
        }
    }


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public String rejectOrder(TWithdraw wi) {
        try {
            TWithdraw withdraw =this.getOne(new LambdaQueryWrapper<TWithdraw>().eq(TWithdraw::getId, wi.getId()));
            if(!redisCache.tryLock(wi.getWithdrawId()+"rejectOrder","rejectOrder",3000)){
                return "订单已操作";
            }


            if(withdraw.getStatus()==2){
                return "订单已操作";
            }


            if (!withdraw.getUpdateBy().equals("系统回收") & !SysUser.isAdmin(SecurityUtils.getUserId())
                    & !withdraw.getUpdateBy().equals(SecurityUtils.getUsername())) {
                return "订单已经被别人锁定";
            }
            BigDecimal amount = withdraw.getAmount();
            String coin = withdraw.getCoin();
            TAppUser user = tAppUserMapper.selectTAppUserByUserId(withdraw.getUserId());
            //资产
            Map<String, TAppAsset> map = tAppAssetService.getAssetByUserIdList(withdraw.getUserId());
            TAppAsset asset = map.get(withdraw.getCoin() + user.getUserId());
            BigDecimal beforeMount = asset.getAvailableAmount();

            Setting setting = settingService.get(SettingEnum.WITHDRAWAL_CHANNEL_SETTING.name());
            TRechargeChannelSetting set = new TRechargeChannelSetting();
            List<TRechargeChannelSetting> tRechargeChannelSettings = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), TRechargeChannelSetting.class);
            for (TRechargeChannelSetting tRechargeChannelSetting : tRechargeChannelSettings) {
                if (tRechargeChannelSetting.getRechargeType().equals(coin)) {
                    set = tRechargeChannelSetting;
                    break;
                }
            }
            String remark = set.getRechargeName() + "提现";
            log.debug("提现前amout ,可用余额，userId  {}-{} {}", beforeMount, asset.getAvailableAmount(), user.getUserId());
            if (redisCache.tryLock("app:wallet:" + withdraw.getUserId() + ":" + withdraw.getId(), withdraw.getUserId(),
                    1000)) {
                asset.setAmout(asset.getAmout().add(amount));
                asset.setAvailableAmount(beforeMount.add(amount));
                tAppAssetService.updateByUserId(asset);

                withdraw.setStatus(2);
                withdraw.setUpdateBy(SecurityUtils.getUsername());
                withdraw.setWithDrawRemark(wi.getWithDrawRemark());
                withdraw.setRemark(wi.getRemark());
                withdraw.setOperateTime(new Date());
                this.updateTWithdraw(withdraw);
                appWalletRecordService.generateRecord(withdraw.getUserId(), amount, RecordEnum.WITHDRAWAL_FAILED.getCode(),
                        SecurityUtils.getUsername(), withdraw.getSerialId(), remark, beforeMount,
                        beforeMount.add(amount), coin, user.getAdminParentIds());
                log.debug("账变前amout ,账变后 ，userId  {}-{} {}", beforeMount, beforeMount.add(amount), user.getUserId());
                return "成功";
            } else {
                return "操作失败，请刷新";
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败，请刷新";
        } finally {
            redisCache.deleteObject(wi.getWithdrawId()+"rejectOrder");
        }
    }

    @Override
    public NoticeVO sendMessage(Integer code, String userId) {
        NoticeVO noticeVO = new NoticeVO();
        userId = userId.split("_")[0];
        SysUser user = sysUserService.selectUserById(Long.parseLong(userId));
        if (user.isAdmin() || "0".equals(user.getUserType())) {
            userId = "";
        }
        Map<String, Integer> map = new HashMap<>();
        //查找待审核的提现订单
        List<TWithdraw> list = this.getBaseMapper().selectTWithdrawVoice(userId);

        //查找待审核的充值订单
        List<TAppRecharge> list1 = appRechargeService.selectRechargeVoice(userId);

        //查找待审核实名认证
        List<TAppUserDetail> list2 = tAppUserDetailMapper.selectVerifiedVoice(userId);

        if (0 == code) {
            noticeVO.setWithdraw(null == list ? 0 : list.size());
            noticeVO.setRecharge(null == list1 ? 0 : list1.size());
            noticeVO.setVerified(null == list2 ? 0 : list2.size());
            return noticeVO;
        }
        if (1 == code) {
            noticeVO.setWithdraw(null == list ? 0 : list.size());
            return noticeVO;
        }
        if (2 == code) {
            noticeVO.setRecharge(null == list1 ? 0 : list1.size());
            return noticeVO;
        }
        if (3 == code) {
            noticeVO.setVerified(null == list2 ? 0 : list2.size());
            return noticeVO;
        }
        if (4 == code) {
            noticeVO.setPosition(1);
            return noticeVO;
        }
        return noticeVO;
    }

    @Override
    public BigDecimal getAllWithdraw(String parentId, Integer type) {
        return tWithdrawMapper.getAllWithdraw(parentId, type);
    }

    @Override
    public List<WithdrawFreezeVO> selectFreezeList(TWithdraw appWithdraw) {
        return tWithdrawMapper.selectFreezeList(appWithdraw);
    }

    @Override
    public Boolean getWithdrawStatus(long loginIdAsLong) {
        Integer integer = tWithdrawMapper.getCountByUserId(loginIdAsLong);
        return integer>0;
    }
}
