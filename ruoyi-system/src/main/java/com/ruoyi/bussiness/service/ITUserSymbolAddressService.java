package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TUserSymbolAddress;
import com.ruoyi.bussiness.domain.setting.AssetCoinSetting;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;

/**
 * 用户币种充值地址Service接口
 * 
 * @author ruoyi
 * @date 2023-07-12
 */
public interface ITUserSymbolAddressService extends IService<TUserSymbolAddress>
{
    /**
     * 查询用户币种充值地址
     * 
     * @param id 用户币种充值地址主键
     * @return 用户币种充值地址
     */
    public TUserSymbolAddress selectTUserSymbolAddressById(Long id);

    /**
     * 查询用户币种充值地址列表
     * 
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 用户币种充值地址集合
     */
    public List<TUserSymbolAddress> selectTUserSymbolAddressList(TUserSymbolAddress tUserSymbolAddress);

    /**
     * 新增用户币种充值地址
     * 
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 结果
     */
    public int insertTUserSymbolAddress(TUserSymbolAddress tUserSymbolAddress);

    /**
     * 修改用户币种充值地址
     * 
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 结果
     */
    public int updateTUserSymbolAddress(TUserSymbolAddress tUserSymbolAddress);

    /**
     * 批量删除用户币种充值地址
     * 
     * @param ids 需要删除的用户币种充值地址主键集合
     * @return 结果
     */
    public int deleteTUserSymbolAddressByIds(Long[] ids);

    /**
     * 删除用户币种充值地址信息
     * 
     * @param id 用户币种充值地址主键
     * @return 结果
     */
    public int deleteTUserSymbolAddressById(Long id);

    Map<String,String> getUserRechargeAdressList(Long userId);

    Map<String,String> getAdredssByCoin(String coin,String symbol,Long userId);

    boolean check(String symbol,String adress);

    ThirdPaySetting getThirdPaySetting(String code);

}
