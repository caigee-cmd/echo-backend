package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TAppAddressInfo;
import com.ruoyi.bussiness.domain.TAppAsset;

/**
 * 玩家资产Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
public interface TAppAssetMapper extends BaseMapper<TAppAsset>
{
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
     * 删除玩家资产
     * 
     * @param userId 玩家资产主键
     * @return 结果
     */
    public int deleteTAppAssetByUserId(Long userId);

    /**
     * 批量删除玩家资产
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppAssetByUserIds(Long[] userIds);

    int updateByUserId(TAppAsset tAppAsset);

    int  noSettlementAssetByUserId(Map<String,Object> params);

    int reduceAssetByUserId(Map<String, Object> params);

    int addAssetByUserId(Map<String, Object> params);

    int occupiedAssetByUserId(Map<String, Object> params);

    int settlementOccupiedCurrencyOrder(Map<String, Object> params);
}
