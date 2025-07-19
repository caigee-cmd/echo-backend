package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.vo.AssetTransFundsVO;
import com.ruoyi.bussiness.domain.vo.UserBonusVO;


/**
 * 玩家资产Service接口
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
public interface ITAppAssetService extends IService<TAppAsset> {
    /**
     * 查询玩家资产
     *
     * @param userId 玩家资产主键
     * @return 玩家资产
     */
    public TAppAsset selectTAppAssetByUserId(Long userId);

    /**
     * 查询玩家资产列表
     *
     * @param tAppAsset 玩家资产
     * @return 玩家资产集合
     */
    public List<TAppAsset> selectTAppAssetList(TAppAsset tAppAsset);

    /**
     * 新增玩家资产
     *
     * @param tAppAsset 玩家资产
     * @return 结果
     */
    public int insertTAppAsset(TAppAsset tAppAsset);

    /**
     * 修改玩家资产
     *
     * @param tAppAsset 玩家资产
     * @return 结果
     */
    public int updateTAppAsset(TAppAsset tAppAsset);

    /**
     * 批量删除玩家资产
     *
     * @param userIds 需要删除的玩家资产主键集合
     * @return 结果
     */
    public int deleteTAppAssetByUserIds(Long[] userIds);

    /**
     * 删除玩家资产信息
     *
     * @param userId 玩家资产主键
     * @return 结果
     */
    public int deleteTAppAssetByUserId(Long userId);

    /**
     * 平台资产
     *
     * @param userId
     * @return
     */
    Map<String, TAppAsset> getAssetByUserIdList(Long userId);

    /**
     * 理财资产/合约资产
     *
     * @param userId
     * @param type
     * @return
     */
    TAppAsset getAssetByUserIdAndType(Long userId, Integer type);

    int updateByUserId(TAppAsset tAppAsset);

    int sendBouns(UserBonusVO userBounsVO);

    int subAmount(UserBonusVO userBounsVO);

    int noSettlementAssetByUserId(Long userId, String symbol, BigDecimal amout);

    int createAsset(TAppUser user, String coin, Integer type);

    int reduceAssetByUserId(Long userId, String symbol, BigDecimal amout);

    int addAssetByUserId(Long userId, String symbol, BigDecimal amout);

    int occupiedAssetByUserId(Long userId, String symbol, BigDecimal amout);

    int settlementOccupiedCurrencyOrder(Long userId, String symbol, BigDecimal occupiedAmout, BigDecimal subtract);

    String transferFunds(AssetTransFundsVO assetTransFundsVo);

    List<TAppAsset> tAppAssetService();
}
