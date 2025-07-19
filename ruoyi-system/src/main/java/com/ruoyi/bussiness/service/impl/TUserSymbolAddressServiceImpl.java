package com.ruoyi.bussiness.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.TUserSymbolAddress;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;
import com.ruoyi.bussiness.mapper.TUserSymbolAddressMapper;
import com.ruoyi.bussiness.service.ITUserSymbolAddressService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.bussiness.service.ThirdPayFactory;
import com.ruoyi.bussiness.service.ThirdPayService;
import com.ruoyi.common.enums.ThirdTypeUncEmun;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户币种充值地址Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-12
 */
@Service
@Slf4j
public class TUserSymbolAddressServiceImpl extends ServiceImpl<TUserSymbolAddressMapper, TUserSymbolAddress> implements ITUserSymbolAddressService {
    @Autowired
    private TUserSymbolAddressMapper tUserSymbolAddressMapper;

    @Autowired
    private SettingService settingService;
    /**
     * 查询用户币种充值地址
     *
     * @param id 用户币种充值地址主键
     * @return 用户币种充值地址
     */
    @Override
    public TUserSymbolAddress selectTUserSymbolAddressById(Long id) {
        return tUserSymbolAddressMapper.selectTUserSymbolAddressById(id);
    }

    /**
     * 查询用户币种充值地址列表
     *
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 用户币种充值地址
     */
    @Override
    public List<TUserSymbolAddress> selectTUserSymbolAddressList(TUserSymbolAddress tUserSymbolAddress) {
        return tUserSymbolAddressMapper.selectTUserSymbolAddressList(tUserSymbolAddress);
    }

    /**
     * 新增用户币种充值地址
     *
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 结果
     */
    @Override
    public int insertTUserSymbolAddress(TUserSymbolAddress tUserSymbolAddress) {
        int count = tUserSymbolAddressMapper.selectCount(new LambdaQueryWrapper<TUserSymbolAddress>().eq(TUserSymbolAddress::getSymbol, tUserSymbolAddress.getSymbol()).eq(TUserSymbolAddress::getUserId,tUserSymbolAddress.getUserId()));
        if (count > 0) {
            return 10001;
        }
        return tUserSymbolAddressMapper.insertTUserSymbolAddress(tUserSymbolAddress);
    }

    /**
     * 修改用户币种充值地址
     *
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 结果
     */
    @Override
    public int updateTUserSymbolAddress(TUserSymbolAddress tUserSymbolAddress) {
        return tUserSymbolAddressMapper.updateTUserSymbolAddress(tUserSymbolAddress);
    }

    /**
     * 批量删除用户币种充值地址
     *
     * @param ids 需要删除的用户币种充值地址主键
     * @return 结果
     */
    @Override
    public int deleteTUserSymbolAddressByIds(Long[] ids) {
        return tUserSymbolAddressMapper.deleteTUserSymbolAddressByIds(ids);
    }

    /**
     * 删除用户币种充值地址信息
     *
     * @param id 用户币种充值地址主键
     * @return 结果
     */
    @Override
    public int deleteTUserSymbolAddressById(Long id) {
        return tUserSymbolAddressMapper.deleteTUserSymbolAddressById(id);
    }

    @Override
    public Map<String, String> getUserRechargeAdressList(Long userId) {
        TUserSymbolAddress tUserSymbolAddress = new TUserSymbolAddress();
        tUserSymbolAddress.setUserId(userId);
        List<TUserSymbolAddress> list = tUserSymbolAddressMapper.selectTUserSymbolAddressList(tUserSymbolAddress);
        Map<String, String> map = new HashMap<>();
        if(Objects.isNull(list)){
            return map;
        }
        for (TUserSymbolAddress userSymbolAddress : list) {
            map.put(userSymbolAddress.getSymbol(), userSymbolAddress.getAddress());
        }
        return map;
    }

    public  Map<String, String> getAdredssByCoin(String coin, String symbol, Long userId) {
        Map<String,String>  map=new HashMap<>();
        //301 U盾
        ThirdPaySetting setting= settingService.getThirdPaySetting(ThirdTypeUncEmun.UNCDUN.getValue());
        if(Objects.isNull(setting)){
           log.info("查询U盾地址为空 采取自动配置");
            TUserSymbolAddress tUserSymbolAddress = new TUserSymbolAddress();
            tUserSymbolAddress.setUserId(userId);
            tUserSymbolAddress.setSymbol(symbol);
            List<TUserSymbolAddress> list = tUserSymbolAddressMapper.selectTUserSymbolAddressList(tUserSymbolAddress);
            for (TUserSymbolAddress userSymbolAddress : list) {
                map.put(userSymbolAddress.getSymbol(), userSymbolAddress.getAddress());
            }
            return map;
        }
        ThirdPayService thirdPayService = ThirdPayFactory.getThirdpay(setting.getCompanyName());
        try {
            int count = tUserSymbolAddressMapper.selectCount(new LambdaQueryWrapper<TUserSymbolAddress>().eq(TUserSymbolAddress::getSymbol, symbol).eq(TUserSymbolAddress::getUserId, userId));
            if (count == 0) {
                if("0".equals(setting.getThirdPayStatu())){
                    JSONObject jsonObject = thirdPayService.createAdress(coin,symbol,userId,setting);
                    String adress= jsonObject.getString("adress");
                    if (StringUtils.isEmpty(adress)) {
                        return map;
                    }
                    TUserSymbolAddress btcAddress = new TUserSymbolAddress();
                    btcAddress.setAddress(adress);
                    btcAddress.setUserId(userId);
                    btcAddress.setSymbol(symbol);
                    this.insertTUserSymbolAddress(btcAddress);
                    map.put(symbol,adress);
                }
                return map;
            }else {
                TUserSymbolAddress userSymbolAddress= tUserSymbolAddressMapper.selectOne(new LambdaQueryWrapper<TUserSymbolAddress>().eq(TUserSymbolAddress::getSymbol, symbol).eq(TUserSymbolAddress::getUserId, userId));
                if(Objects.nonNull(userSymbolAddress)){
                    map.put(userSymbolAddress.getSymbol(),userSymbolAddress.getAddress());
                    return map;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean check(String symbol, String adress) {
        ThirdPaySetting setting= settingService.getThirdPaySetting(ThirdTypeUncEmun.UNCDUN.getValue());
        if(Objects.isNull(setting)){
            return false;
        }
        ThirdPayService thirdPayService = ThirdPayFactory.getThirdpay(setting.getCompanyName());
        return  thirdPayService.existAdress(symbol,adress,setting);
    }

    @Override
    public ThirdPaySetting getThirdPaySetting(String code) {
        return settingService.getThirdPaySetting("301");
    }

}
