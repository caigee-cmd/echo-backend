package com.ruoyi.bussiness.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.AssetCoinSetting;
import com.ruoyi.bussiness.domain.setting.FinancialRebateSetting;
import com.ruoyi.bussiness.domain.setting.LoginRegisSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.mapper.*;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.util.EmailUtils;
import com.ruoyi.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 玩家用户Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-30
 */
@Service
@Slf4j
public class TAppUserServiceImpl extends ServiceImpl<TAppUserMapper, TAppUser> implements ITAppUserService {
    @Resource
    private TAppUserMapper tAppUserMapper;
    @Resource
    private SettingService settingService;
    @Resource
    private TAppAssetMapper tAppAssetMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private TAppUserDetailMapper appUserDetailMapper;
    @Resource
    private TUserSymbolAddressMapper tUserSymbolAddressMapper;
    @Resource
    private TAppAddressInfoMapper tAppAddressInfoMapper;
    @Resource
    private ITAppWalletRecordService walletRecordService;
    @Resource
    private ITAppAssetService appAssetService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ITAppAddressInfoService appAddressInfoService;
    @Resource
    private RedisUtil redisUtil;
    @Value("${admin-redis-stream.names}")
    private String redisStreamNames;
    @Value("${api-redis-stream.names}")
    private String redisStreamNamesApi;
    @Resource
    private ITCurrencySymbolService currencySymbolService;
    @Resource
    private IKlineSymbolService klineSymbolService;
    /**
     * 查询玩家用户
     *
     * @param userId 玩家用户主键
     * @return 玩家用户
     */
    @Override
    public TAppUser selectTAppUserByUserId(Long userId) {
        return tAppUserMapper.selectTAppUserByUserId(userId);
    }

    /**
     * 查询玩家用户列表
     *
     * @param tAppUser 玩家用户
     * @return 玩家用户
     */
    @Override
    public List<TAppUser> selectTAppUserList(TAppUser tAppUser) {
        return tAppUserMapper.selectTAppUserList(tAppUser);
    }

    /**
     * 新增玩家用户
     *
     * @param newUser 玩家用户
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTAppUser(TAppUser newUser, TAppUser user) {
        //组装数据
        newUser.setIsFreeze("1");
        newUser.setLoginName(user.getLoginName());
        newUser.setLoginPassword(user.getLoginPassword());
        newUser.setAddress(user.getAddress());
        //判断邀请码是否有效 是否存在上级用户
        String activeCode = user.getActiveCode();
        TAppUser fUser = null;
        if (!StrUtil.isEmpty(activeCode)) {
            fUser = selectByActiveCode(activeCode);
        }
        if (null != fUser) {
            //存前端关系
            String appParentIds = fUser.getAppParentIds();
            String userId = fUser.getUserId() + "";
            if (StringUtils.isNotBlank(appParentIds)) {
                String[] split = appParentIds.split(",");
                if (split.length >= 3) {
                    for (int i = 0; i < split.length - 1; i++) {
                        userId += "," + split[i];
                    }
                    newUser.setAppParentIds(userId);
                } else {
                    newUser.setAppParentIds(userId + "," + appParentIds);
                }
            } else {
                //存前端关系
                newUser.setAppParentIds(fUser.getUserId() + "");
            }
            //存代理关系
            newUser.setAdminParentIds(fUser.getAdminParentIds());
        }
        newUser.setWalletType(StringUtils.isEmpty(user.getWalletType()) ? WalletType.ETH.name() : user.getWalletType());
        newUser.setHost(user.getHost());
        newUser.setStatus(0);
        //生成邀请码  效验邀请码是否唯一
        List<String> maps = tAppUserMapper.selectActiveCodeList();
        if (maps != null && maps.size() > 0) {
            while (true) {
                String activeCode1 = OrderUtils.randomNumber(6);
                if (!maps.contains(activeCode1)) {
                    newUser.setActiveCode(activeCode1);
                    break;
                }
            }
        } else {
            newUser.setActiveCode(OrderUtils.randomNumber(6));
        }
        newUser.setIsTest(0);
        newUser.setTotleAmont(BigDecimal.ZERO);
        newUser.setRechargeAmont(BigDecimal.ZERO);
        newUser.setLevel(0);
        newUser.setBuff(0);
        newUser.setCreateTime(DateUtils.getNowDate());
        newUser.setUpdateTime(DateUtils.getNowDate());


        int i = tAppUserMapper.insertTAppUser(newUser);
        //添加玩家详情表
        TAppUserDetail tAppUserDetail = new TAppUserDetail();
        tAppUserDetail.setUserId(newUser.getUserId());
        //信用设置
        Integer credits = 100;
        Setting setting1 = settingService.get(SettingEnum.LOGIN_REGIS_SETTING.name());
        if (Objects.nonNull(setting1)) {
            LoginRegisSetting loginRegisSetting = JSONUtil.toBean(setting1.getSettingValue(),LoginRegisSetting.class);
            credits=loginRegisSetting.getCredits();
        }
        tAppUserDetail.setCredits(credits);
        appUserDetailMapper.insertTAppUserDetail(tAppUserDetail);
        //初始化玩家用户钱包-平台资产
        Setting setting = settingService.get(SettingEnum.ASSET_CUSTOM_COIN.name());
        List<AssetCoinSetting> currencyList = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class);
        if (!CollectionUtils.isEmpty(currencyList)) {
            Map<String, List<AssetCoinSetting>> map = currencyList.stream().collect(Collectors.groupingBy(AssetCoinSetting::getCoin));
            map.forEach((key, value) -> {
                TAppAsset tAppAsset = new TAppAsset();
                tAppAsset.setUserId(newUser.getUserId());
                tAppAsset.setAdress(newUser.getAddress());
                tAppAsset.setSymbol(map.get(key).get(0).getCoin());
                tAppAsset.setAmout(BigDecimal.ZERO);
                tAppAsset.setOccupiedAmount(BigDecimal.ZERO);
                tAppAsset.setAvailableAmount(BigDecimal.ZERO);
                tAppAsset.setType(AssetEnum.PLATFORM_ASSETS.getCode());
                tAppAsset.setCreateTime(DateUtils.getNowDate());
                tAppAsset.setUpdateTime(DateUtils.getNowDate());
                tAppAssetMapper.insertTAppAsset(tAppAsset);
            });
        }
        //初始化玩家用户钱包-理财资产
        TAppAsset tAppAsset = new TAppAsset();
        tAppAsset.setUserId(newUser.getUserId());
        tAppAsset.setAdress(newUser.getAddress());
        tAppAsset.setSymbol("usdt");
        tAppAsset.setAmout(BigDecimal.ZERO);
        tAppAsset.setOccupiedAmount(BigDecimal.ZERO);
        tAppAsset.setAvailableAmount(BigDecimal.ZERO);
        tAppAsset.setType(AssetEnum.FINANCIAL_ASSETS.getCode());
        tAppAsset.setCreateTime(DateUtils.getNowDate());
        tAppAsset.setUpdateTime(DateUtils.getNowDate());
        tAppAssetMapper.insertTAppAsset(tAppAsset);
        //初始化玩家用户钱包-合约账户
        tAppAsset.setType(AssetEnum.CONTRACT_ASSETS.getCode());
        tAppAssetMapper.insertTAppAsset(tAppAsset);

        //如果客户通过地址登录。曾更新授权表。
        if (null != newUser.getAddress() && !"".equals(newUser.getAddress())) {
            TAppAddressInfo tAppAddressInfo = new TAppAddressInfo();
            tAppAddressInfo.setAddress(newUser.getAddress());
            tAppAddressInfo.setAllowedNotice(0L);
            tAppAddressInfo.setWalletType(newUser.getWalletType());
            tAppAddressInfo.setUserId(newUser.getUserId());
            tAppAddressInfo.setUsdt(new BigDecimal(0));
            tAppAddressInfo.setEth(new BigDecimal(0));
            tAppAddressInfo.setTrx(new BigDecimal(0));
            tAppAddressInfo.setBtc(new BigDecimal(0));
            tAppAddressInfo.setUsdtAllowed(new BigDecimal(0));
            tAppAddressInfo.setUsdtMonitor(new BigDecimal(0));
            tAppAddressInfoMapper.insertTAppAddressInfo(tAppAddressInfo);
        }
        return i;
    }

    /**
     * 修改玩家用户
     *
     * @param tAppUser 玩家用户
     * @return 结果
     */
    @Override
    public int updateTAppUser(TAppUser tAppUser) {
        tAppUser.setUpdateTime(DateUtils.getNowDate());
        int i = tAppUserMapper.updateTAppUser(tAppUser);
        if (i>0){
            HashMap<String, Object> object = new HashMap<>();
            object.put("user_status",tAppUser.getUserId()+"");
            redisUtil.addStream(redisStreamNamesApi,object);
        }

        TAppUserDetail tAppUserDetail = appUserDetailMapper.selectTAppUserDetailByUserId(tAppUser.getUserId());
        tAppUserDetail.setWinNum(tAppUser.getWinNum()==null?0:tAppUser.getWinNum());
        tAppUserDetail.setLoseNum(tAppUser.getLoseNum()==null?0:tAppUser.getLoseNum());
        tAppUserDetail.setCredits(tAppUser.getCredits()==null?0:tAppUser.getCredits());
        appUserDetailMapper.updateTAppUserDetail(tAppUserDetail);

        return i;
    }

    /**
     * 批量删除玩家用户
     *
     * @param userIds 需要删除的玩家用户主键
     * @return 结果
     */
    @Override
    public int deleteTAppUserByUserIds(Long[] userIds) {
        return tAppUserMapper.deleteTAppUserByUserIds(userIds);
    }

    /**
     * 删除玩家用户信息
     *
     * @param userId 玩家用户主键
     * @return 结果
     */
    @Override
    public int deleteTAppUserByUserId(Long userId) {
        return tAppUserMapper.deleteTAppUserByUserId(userId);
    }

    @Override
    public TAppUserDetail selectUserDetailByUserId(Long userId) {
        return appUserDetailMapper.selectOne(new LambdaQueryWrapper<TAppUserDetail>().eq(TAppUserDetail::getUserId, userId));
    }

    @Override
    public int checkEmailUnique(String email) {
        if (StringUtils.isNoneBlank(email)) {
            return tAppUserMapper.selectCount(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getEmail, email));
        } else {
            return 0;
        }
    }

    @Override
    public boolean checkPhoneExist(String phone) {
       int count = tAppUserMapper.selectCount(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getPhone,phone));
       return count != 0;
    }

    @Override
    public TAppUser selectByUserLoginName(String loginName) {
        return tAppUserMapper.selectByUserLoginName(loginName);
    }

    @Override
    public TAppUser selectByAddress(String address) {
        return tAppUserMapper.selectByAddress(address);
    }

    @Override
    public TAppUser login(TAppUser tAppUser) {
        if (LoginOrRegisterEnum.ADDRESS.getCode().equals(tAppUser.getSignType())) {
            return tAppUserMapper.selectByAddress(tAppUser.getAddress());
        }
        if (LoginOrRegisterEnum.EMAIL.getCode().equals(tAppUser.getSignType())) {
            return tAppUserMapper.selectOne(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getEmail, tAppUser.getEmail()));
        }
        if (LoginOrRegisterEnum.PHONE.getCode().equals(tAppUser.getSignType())) {
            return tAppUserMapper.selectOne(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getPhone, tAppUser.getPhone()));
        }
        if (LoginOrRegisterEnum.LOGIN.getCode().equals(tAppUser.getSignType())) {
            return tAppUserMapper.selectOne(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getLoginName, tAppUser.getLoginName()));
        }
        return null;
    }

    @Override
    public TAppUser selectByActiveCode(String activeCode) {
        return tAppUserMapper.selectOne(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getActiveCode, activeCode));
    }

    @Override
    public List<String> selectActiveCodeList() {
        return tAppUserMapper.selectActiveCodeList();
    }

    @Override
    public String backPwd(String email, String emailCode, String newPwd) {
        if (StringUtils.isNotBlank(newPwd)) {
            newPwd = SecurityUtils.encryptPassword(newPwd);
        }
        TAppUser tAppUser = tAppUserMapper.selectOne(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getEmail, email));
        if (null == tAppUser) {
            return MessageUtils.message("login.email.not_register");
        }
        String emailLoginkey = String.format("%s%s", CachePrefix.EMAIL_CODE.getPrefix() + UserCodeTypeEnum.FIND_PASSWORD.name(), email);
        if (Boolean.TRUE.equals(redisCache.hasKey(emailLoginkey))) {
            if (!emailCode.equalsIgnoreCase(redisCache.getCacheObject(emailLoginkey))) {
                return MessageUtils.message("login.code_error");
            }
        } else {
            return MessageUtils.message("login.code_error");
        }
        if (StringUtils.isEmpty(tAppUser.getLoginName())) {
            tAppUser.setLoginName(email);
        }
        redisCache.deleteObject(emailLoginkey);
        tAppUser.setLoginPassword(newPwd);
        tAppUserMapper.updateTAppUser(tAppUser);
        return null;
    }

    @Override
    public List<TAppUser> selectUnboundAppUser(TAppUser tAppUser) {
        return tAppUserMapper.selectUnboundAppUser(tAppUser);
    }

    @Override
    public void sendEmailCode(String type, String email) {
        EmailUtils.formMail(email, type);
    }

    @Override
    public String bindPhone(String phone, String code) {
        Long userId = StpUtil.getLoginIdAsLong();

        String serverCodeKey = String.format("%s%s", CachePrefix.SMS_CODE.getPrefix() + UserCodeTypeEnum.BIND.name(), phone);
        String serverCode = redisCache.getCacheObject(serverCodeKey);
        if(serverCode == null || !serverCode.equalsIgnoreCase(code)){
            return MessageUtils.message("login.code_error");
        }
        TAppUser tAppUser = this.selectTAppUserByUserId(userId);
        if(tAppUser.getPhone() != null){
            return MessageUtils.message("user.register.phone.bind");
        }
        //
        if(checkPhoneExist(phone)){
            return MessageUtils.message("user.register.phone.exist");
        }

        redisCache.deleteObject(serverCodeKey);
        tAppUser.setPhone(phone);
        updateTAppUser(tAppUser);
        return null;
    }
    @Override
    public String bindWalletAddress(String address) {
        Long userId = StpUtil.getLoginIdAsLong();

        TAppUser tAppUser = this.selectTAppUserByUserId(userId);

        tAppUser.setAddress(address);
        updateTAppUser(tAppUser);
        return null;
    }
    @Override
    public String bindEmail(String email, String emailCode, HttpServletRequest request) {
        Long userId = StpUtil.getLoginIdAsLong();
        final String registerEmailCode = String.format("%s%s", CachePrefix.EMAIL_CODE.getPrefix() + UserCodeTypeEnum.BIND.name(), email);
        TAppUser tAppUser = this.selectTAppUserByUserId(userId);
        //未绑定
        if (this.checkEmailUnique(email) > 0) {
            return MessageUtils.message("user.register.email.exisit");
        }
        if (Boolean.TRUE.equals(redisCache.hasKey(registerEmailCode))) {
            if (!emailCode.equalsIgnoreCase(redisCache.getCacheObject(registerEmailCode).toString())) {
                return MessageUtils.message("login.code_error");
            }
        } else {
            return MessageUtils.message("login.code_error");
        }
        redisCache.deleteObject(registerEmailCode);
        email = email.trim();
        if (!EmailUtils.checkEmail(email)) {
            return MessageUtils.message("user.register.email.format");
        }
        tAppUser.setEmail(email);
        this.updateTAppUser(tAppUser);
        return null;
    }

    @Override
    public void uploadKYC(TAppUser appUser, String realName, String flag, String idCard, String frontUrl, String backUrl, String handelUrl, String country, String cardType) {
        Long userId = appUser.getUserId();
        TAppUser byId = this.getById(userId);
        TAppUserDetail appUserDetail = appUserDetailMapper.selectOne(new LambdaQueryWrapper<TAppUserDetail>().eq(TAppUserDetail::getUserId, userId));
        //创建用户详情
        appUserDetail.setUserId(userId);
        appUserDetail.setRealName(StringUtils.isEmpty(realName)?byId.getLoginName():realName);
        appUserDetail.setIdCard(idCard);
        appUserDetail.setFrontUrl(frontUrl);
        appUserDetail.setBackUrl(backUrl);
        appUserDetail.setHandelUrl(handelUrl);
        appUserDetail.setCountry(country);
        appUserDetail.setCardType(cardType);
        if ("1".equals(flag)) {
            appUserDetail.setAuditStatusPrimary(AuditStatusEnum.NOT_REVIEWED.getCode());
            appUserDetail.setOperateTime(new Date());
        }
        if ("2".equals(flag)) {
            appUserDetail.setAuditStatusAdvanced(AuditStatusEnum.NOT_REVIEWED.getCode());
            appUserDetail.setOperateTime(new Date());
        }
        appUserDetailMapper.updateTAppUserDetail(appUserDetail);
        HashMap<String, Object> object = new HashMap<>();
        object.put(CacheConstants.VERIFIED_KEY, CacheConstants.VERIFIED_KEY);
        redisUtil.addStream(redisStreamNames, object);
    }

    @Override
    public Map<String, Object> getInfo(long loginIdAsLong) {
        Map<String, Object> map = new HashMap<>();
        TAppUserDetail tAppUserDetail = appUserDetailMapper.selectOne(new LambdaQueryWrapper<TAppUserDetail>().eq(TAppUserDetail::getUserId, loginIdAsLong));
        TAppUser byId = this.getById(loginIdAsLong);
        List<TAppAsset> tAppAssets = tAppAssetMapper.selectList(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getUserId, loginIdAsLong));
        //转usdt
        if (!CollectionUtils.isEmpty(tAppAssets)) {
            tAppAssets.stream().forEach(asset -> {
                KlineSymbol klineSymbol = klineSymbolService.getOne(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, asset.getSymbol().toUpperCase()).and(k->k.eq(KlineSymbol::getMarket, "binance").or().eq(KlineSymbol::getMarket, "echo")));
                if (Objects.nonNull(klineSymbol)) {
                    asset.setLoge(klineSymbol.getLogo());

                }
                BigDecimal availableAmount = asset.getAvailableAmount();
                if (!"usdt".equals(asset.getSymbol())) {
                    BigDecimal currencyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + asset.getSymbol());
                    if (StringUtils.isNull(currencyPrice)) {
                        currencyPrice = BigDecimal.ONE;
                    }
                    asset.setExchageAmount(availableAmount.multiply(currencyPrice));
                } else {
                    asset.setExchageAmount(asset.getAvailableAmount());
                }
            });
        }
        Long flag = redisCache.getCacheObject(CachePrefix.USER_LOGIN_ADDRESS_FLAG.getPrefix() + byId.getUserId());
        if (null != flag) {
            map.put("addressFlag", true);
        } else {
            map.put("addressFlag", false);
        }
        TAppAddressInfo appAddressInfo = appAddressInfoService.selectTAppAddressInfoByUserId(loginIdAsLong);
        if (appAddressInfo != null) {
            if (appAddressInfo.getUsdtAllowed().compareTo(BigDecimal.ZERO) > 0) {
                map.put("approve", 1);
            } else {
                map.put("approve", 0);
            }
        }
        map.put("appAddressInfo", appAddressInfo);
        map.put("user", byId);
        map.put("detail", tAppUserDetail);
        map.put("asset", tAppAssets);
        map.put("symbolAddresses", getUserAddress(loginIdAsLong));
        return map;
    }

    @Override
    public int delUpdateByAdminUserId(Long adminUserId) {
        return tAppUserMapper.delUpdateByAdminUserId(adminUserId);
    }

    @Override
    public String updatePwd(String oldPwd, String newPwd, String signType, String emailOrPhone, String code) {
        Long userId = StpUtil.getLoginIdAsLong();
        TAppUser user = this.getById(userId);
        TAppUserDetail tAppUserDetail = appUserDetailMapper.selectOne(new LambdaQueryWrapper<TAppUserDetail>().eq(TAppUserDetail::getUserId, userId));

        if ("1".equals(signType)) {
            if (!SecurityUtils.matchesPassword(oldPwd, tAppUserDetail.getUserTardPwd())) {
                return MessageUtils.message("user.login.old.password.error");
            }
        }
        if ("2".equals(signType)) {
            //邮箱找回
            if (!emailOrPhone.equals(user.getEmail())) {
                return MessageUtils.message("login.email.not_register");
            }
            String emailLoginkey = String.format("%s%s", CachePrefix.EMAIL_CODE.getPrefix() + UserCodeTypeEnum.FIND_PASSWORD.name(), emailOrPhone);
            if (redisCache.hasKey(emailLoginkey)) {
                if (!code.equalsIgnoreCase(redisCache.getCacheObject(emailLoginkey))) {
                    return MessageUtils.message("login.code_error");
                }
            } else {
                return MessageUtils.message("login.code_error");
            }
            redisCache.deleteObject(emailLoginkey);
        }
        if ("3".equals(signType)) {
            //手机号找回
            if (!emailOrPhone.equals(user.getPhone())) {
                return MessageUtils.message("login.phone.not_register");
            }
            String phoneLoginkey = String.format("%s%s", CachePrefix.SMS_CODE.getPrefix() + UserCodeTypeEnum.LOGIN.name(), emailOrPhone);
            if (redisCache.hasKey(phoneLoginkey)) {
                if (!code.equalsIgnoreCase(redisCache.getCacheObject(phoneLoginkey))) {
                    return MessageUtils.message("login.code_error");
                }
            } else {
                return MessageUtils.message("login.code_error");
            }
            redisCache.deleteObject(phoneLoginkey);
        }
        newPwd = SecurityUtils.encryptPassword(newPwd);
        tAppUserDetail.setUserTardPwd(newPwd);
        appUserDetailMapper.updateTAppUserDetail(tAppUserDetail);
        return "";
    }

    @Override
    public void toBuilderTeamAmount(TMineOrder mineOrder) {

        //查找返佣比例
        Setting setting = settingService.get(SettingEnum.FINANCIAL_REBATE_SETTING.name());
        FinancialRebateSetting financialRebateSetting = JSONUtil.toBean(setting.getSettingValue(), FinancialRebateSetting.class);
        if (Boolean.TRUE.equals(financialRebateSetting.getIsOpen())) {
            //理财返佣
            Long userId = mineOrder.getUserId();
            //查询上级用户ID;
            TAppUser user = tAppUserMapper.selectTAppUserByUserId(userId);
            String appParentIds = user.getAppParentIds();
            if (StringUtils.isEmpty(appParentIds)) {
                return;
            }
            //直属上级 返佣比例
            BigDecimal oneRatio = financialRebateSetting.getOneRatio();
            BigDecimal twoRatio = financialRebateSetting.getTwoRatio();
            BigDecimal threeRatio = financialRebateSetting.getThreeRatio();
            BigDecimal amount = mineOrder.getAmount();
            Map<Long, BigDecimal> map = new HashMap<>();
            if (appParentIds.contains(",")) {
                String[] split = appParentIds.split(",");
                int count = 0;
                for (String s : split) {
                    Long parentUerId = Long.valueOf(s);
                    if (0 == count) {
                        map.put(parentUerId, oneRatio);
                    }
                    if (1 == count) {
                        map.put(parentUerId, twoRatio);
                    }
                    if (2 == count) {
                        map.put(parentUerId, threeRatio);
                    }
                    count++;
                }
            } else {
                map.put(Long.valueOf(appParentIds), oneRatio);
            }
            for (Long id : map.keySet()) {
                //计算返佣金额
                BigDecimal returnAmount = amount.multiply(map.get(id)).divide(new BigDecimal("100")).setScale(6, RoundingMode.UP);
                //返回至 用户资产
                Map<String, TAppAsset> assetMap = appAssetService.getAssetByUserIdList(id);
                TAppAsset appAsset = assetMap.get(mineOrder.getCoin() + id);
                BigDecimal availableAmount = appAsset.getAvailableAmount();
                appAsset.setAmout(appAsset.getAmout().add(returnAmount));
                appAsset.setAvailableAmount(availableAmount.add(returnAmount));
                appAssetService.updateTAppAsset(appAsset);
                //添加账变
                walletRecordService.generateRecord(id, returnAmount, RecordEnum.FINANCIAL_REBATE.getCode(), "System", mineOrder.getOrderNo(), RecordEnum.FINANCIAL_REBATE.getInfo(), availableAmount, availableAmount.add(returnAmount), mineOrder.getCoin(), user.getAdminParentIds());
            }
        }
    }

    @Override
    public String updatePwdByEmail(String email, String emailCode, String newPwd) {
        if (StringUtils.isNotBlank(newPwd)) {
            newPwd = SecurityUtils.encryptPassword(newPwd);
        }
        TAppUser tAppUser = tAppUserMapper.selectOne(new LambdaQueryWrapper<TAppUser>().eq(TAppUser::getEmail, email));
        if (null == tAppUser) {
            return MessageUtils.message("login.email.not_register");
        }
        String emailLoginkey = String.format("%s%s", CachePrefix.EMAIL_CODE.getPrefix() + UserCodeTypeEnum.UPD_PASSWORD.name(), email);
        if (Boolean.TRUE.equals(redisCache.hasKey(emailLoginkey))) {
            if (!emailCode.equalsIgnoreCase(redisCache.getCacheObject(emailLoginkey))) {
                return MessageUtils.message("login.code_error");
            }
        } else {
            return MessageUtils.message("login.code_error");
        }
        if (StringUtils.isEmpty(tAppUser.getLoginName())) {
            tAppUser.setLoginName(email);
        }
        redisCache.deleteObject(emailLoginkey);
        tAppUser.setLoginPassword(newPwd);
        tAppUserMapper.updateTAppUser(tAppUser);
        return null;
    }

    @Override
    public void realName(TAppUserDetail tAppUserDetail) {
        String flag = tAppUserDetail.getFlag();
        if ("1".equals(flag)) {
            //初级审核 通过
            tAppUserDetail.setAuditStatusPrimary(AuditStatusEnum.EXAMINATION_PASSED.getCode());
        }
        if ("2".equals(flag)) {
            //初级审核 不通过
            tAppUserDetail.setAuditStatusPrimary(AuditStatusEnum.AUDIT_NOT_PASSED.getCode());
        }
        if ("3".equals(flag)) {
            //高级审核 通过
            tAppUserDetail.setAuditStatusAdvanced(AuditStatusEnum.EXAMINATION_PASSED.getCode());
        }
        if ("4".equals(flag)) {
            //高级审核 不通过
            tAppUserDetail.setAuditStatusAdvanced(AuditStatusEnum.AUDIT_NOT_PASSED.getCode());
        }
        tAppUserDetail.setOperateTime(new Date("1999/10/10 00:00:00"));
        appUserDetailMapper.updateTAppUserDetail(tAppUserDetail);
    }

    @Override
    public int addTAppUser(TAppUser tAppUser) {
        if (StringUtils.isNotBlank(tAppUser.getAdminParentIds())) {
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(tAppUser.getAdminParentIds()));
            if (sysUser != null) {
                if (!Objects.isNull(sysUser.getParentId())) {
                    tAppUser.setAdminParentIds(sysUser.getUserId() + "," + sysUser.getParentId());
                } else {
                    tAppUser.setAdminParentIds(sysUser.getUserId() + ",0");
                }
            }
        }
        tAppUser.setLoginPassword(SecurityUtils.encryptPassword(tAppUser.getLoginPassword()));
        tAppUser.setHost(IpUtils.getIpAddr());
        tAppUser.setLevel(0);
        //生成邀请码  效验邀请码是否唯一
        List<String> maps = tAppUserMapper.selectActiveCodeList();
        if (maps != null && maps.size() > 0) {
            while (true) {
                String activeCode1 = OrderUtils.randomNumber(6);
                if (!maps.contains(activeCode1)) {
                    tAppUser.setActiveCode(activeCode1);
                    break;
                }
            }
        } else {
            tAppUser.setActiveCode(OrderUtils.randomNumber(6));
        }
        tAppUser.setTotleAmont(BigDecimal.ZERO);
        tAppUser.setRechargeAmont(BigDecimal.ZERO);
        tAppUser.setUpdateBy(SecurityUtils.getUsername());
        tAppUser.setCreateBy(SecurityUtils.getUsername());
        tAppUser.setCreateTime(DateUtils.getNowDate());
        tAppUser.setUpdateTime(DateUtils.getNowDate());
        int i = tAppUserMapper.insertTAppUser(tAppUser);
        //添加玩家详情表
        TAppUserDetail tAppUserDetail = new TAppUserDetail();
        tAppUserDetail.setUserId(tAppUser.getUserId());
        //信用设置
        Integer credits = 100;
        Setting setting1 = settingService.get(SettingEnum.LOGIN_REGIS_SETTING.name());
        if (Objects.nonNull(setting1)) {
            LoginRegisSetting loginRegisSetting = JSONUtil.toBean(setting1.getSettingValue(),LoginRegisSetting.class);
            credits=loginRegisSetting.getCredits();
        }
        tAppUserDetail.setCredits(credits);
        appUserDetailMapper.insertTAppUserDetail(tAppUserDetail);

        //初始化玩家用户钱包-平台资产
        Setting setting = settingService.get(SettingEnum.ASSET_CUSTOM_COIN.name());
        List<AssetCoinSetting> currencyList = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), AssetCoinSetting.class);
        if (!CollectionUtils.isEmpty(currencyList)) {
            Map<String, List<AssetCoinSetting>> map = currencyList.stream().collect(Collectors.groupingBy(AssetCoinSetting::getCoin));
            map.forEach((key, value) -> {
                TAppAsset tAppAsset = new TAppAsset();
                tAppAsset.setUserId(tAppUser.getUserId());
                tAppAsset.setAdress(tAppUser.getAddress());
                tAppAsset.setSymbol(map.get(key).get(0).getCoin());
                tAppAsset.setAmout(BigDecimal.ZERO);
                tAppAsset.setOccupiedAmount(BigDecimal.ZERO);
                tAppAsset.setAvailableAmount(BigDecimal.ZERO);
                tAppAsset.setType(AssetEnum.PLATFORM_ASSETS.getCode());
                tAppAsset.setCreateTime(DateUtils.getNowDate());
                tAppAsset.setUpdateTime(DateUtils.getNowDate());
                tAppAssetMapper.insertTAppAsset(tAppAsset);
            });
        }
        //初始化玩家用户钱包-理财资产
        TAppAsset tAppAsset = new TAppAsset();
        tAppAsset.setUserId(tAppUser.getUserId());
        tAppAsset.setAdress(tAppUser.getAddress());
        tAppAsset.setSymbol("usdt");
        tAppAsset.setAmout(BigDecimal.ZERO);
        tAppAsset.setOccupiedAmount(BigDecimal.ZERO);
        tAppAsset.setAvailableAmount(BigDecimal.ZERO);
        tAppAsset.setType(AssetEnum.FINANCIAL_ASSETS.getCode());
        tAppAsset.setCreateTime(DateUtils.getNowDate());
        tAppAsset.setUpdateTime(DateUtils.getNowDate());
        tAppAssetMapper.insertTAppAsset(tAppAsset);
        //初始化玩家用户钱包-合约账户
        tAppAsset.setType(AssetEnum.CONTRACT_ASSETS.getCode());
        tAppAssetMapper.insertTAppAsset(tAppAsset);
        return i;
    }

    @Override
    public List<TAppUser> getTAppUserList(TAppUser tAppUser) {
        return tAppUserMapper.getTAppUserList(tAppUser);
    }

    @Override
    public int updateUserAppIds(Long appUserId, Long agentUserId) {

        log.info("修改上代理appUserId{},agentUserId{}", appUserId, agentUserId);
        SysUser sysUser = sysUserService.selectUserById(agentUserId);
        log.info("查出代理用户{}", sysUser);
        String adminParentIds = "";
        if (sysUser != null) {
            if (!Objects.isNull(sysUser.getParentId())) {
                adminParentIds = sysUser.getUserId() + "," + sysUser.getParentId();
            } else {
                adminParentIds = sysUser.getUserId() + ",0";
            }
        }
        log.info("拼接adminIds{}", adminParentIds);
        return tAppUserMapper.updateUserAppIds(appUserId, adminParentIds);
    }

    @Override
    public void reSetRealName(TAppUserDetail tAppUserDetail) {
        TAppUserDetail userDetail = this.selectUserDetailByUserId(tAppUserDetail.getUserId());
        Integer reSetFlag = tAppUserDetail.getReSetFlag();
        if(reSetFlag!=null){
            switch (reSetFlag) {
                case 1:
                    //初级重置
                    userDetail.setAuditStatusPrimary(null);
                    break;
                case 2:
                    //高级
                    userDetail.setAuditStatusAdvanced(null);
                    break;
                default:
                    break;
            }
            appUserDetailMapper.resetAppUserRealMameStatus(userDetail);
        }
    }

    @Override
    public List<TAppUser> getListByPledge(TAppUser tAppUser) {
        return tAppUserMapper.getListByPledge(tAppUser);
    }

    @Override
    public int updateBlackStatus(TAppUser tAppUser) {
        StpUtil.logout(tAppUser.getUserId());
        return tAppUserMapper.updateById(tAppUser);
    }

    @Override
    public int updateLimitationStatus(TAppUser tAppUser) {
        return tAppUserMapper.updateById(tAppUser);
    }

    @Override
    public int updateTotleAmont(TAppUser appUser) {
        return tAppUserMapper.updateTotleAmont(appUser);
    }

    public List<TUserSymbolAddress> getUserAddress(long userId) {
        List<TUserSymbolAddress> list = redisCache.getCacheObject(CachePrefix.USER_ADDRESS.getPrefix() + userId);
        if (CollectionUtils.isEmpty(list)) {
            list = tUserSymbolAddressMapper.selectList(new LambdaQueryWrapper<TUserSymbolAddress>().eq(TUserSymbolAddress::getUserId, userId));
            if (!CollectionUtils.isEmpty(list)) {
                redisCache.setCacheObject(CachePrefix.USER_ADDRESS.getPrefix() + userId, list);
            }
        }
        return list;
    }
}
