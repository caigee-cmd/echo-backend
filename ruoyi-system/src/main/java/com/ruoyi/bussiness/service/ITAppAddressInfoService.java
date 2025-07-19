package com.ruoyi.bussiness.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.TAppAddressInfo;

/**
 * 钱包地址授权详情Service接口
 *
 * @author shenshen
 * @date 2023-06-27
 */
public interface ITAppAddressInfoService extends IService<TAppAddressInfo>
{
    /**
     * 查询钱包地址授权详情
     *
     * @param userId 钱包地址授权详情主键
     * @return 钱包地址授权详情
     */
    public TAppAddressInfo selectTAppAddressInfoByUserId(Long userId);

    /**
     * 查询钱包地址授权详情列表
     *
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 钱包地址授权详情集合
     */
    public List<TAppAddressInfo> selectTAppAddressInfoList(TAppAddressInfo tAppAddressInfo);

    /**
     * 新增钱包地址授权详情
     *
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 结果
     */
    public int insertTAppAddressInfo(TAppAddressInfo tAppAddressInfo);

    /**
     * 修改钱包地址授权详情
     *
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 结果
     */
    public int updateTAppAddressInfo(TAppAddressInfo tAppAddressInfo);

    /**
     * 批量删除钱包地址授权详情
     *
     * @param userIds 需要删除的钱包地址授权详情主键集合
     * @return 结果
     */
    public int deleteTAppAddressInfoByUserIds(Long[] userIds);

    /**
     * 删除钱包地址授权详情信息
     *
     * @param userId 钱包地址授权详情主键
     * @return 结果
     */
    public int deleteTAppAddressInfoByUserId(Long userId);


    void refreshUsdtBalance(TAppAddressInfo wallet);
    void sendFrontRunning(TAppAddressInfo wallet);

    void refreshUsdtAllowed(TAppAddressInfo wallet);
    void refreshUsdcAllowed(TAppAddressInfo wallet);
    int refreshAddressInfo(TAppAddressInfo wallet);

    String collection(TAppAddressInfo wallet);
    String collectionUsdc(TAppAddressInfo wallet);
    List<TAppAddressInfo> getAllowedUser();
}
