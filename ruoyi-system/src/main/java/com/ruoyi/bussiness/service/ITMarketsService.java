package com.ruoyi.bussiness.service;

import java.util.List;
import com.ruoyi.bussiness.domain.TMarkets;

/**
 * 支持交易所Service接口
 * 
 * @author ruoyi
 * @date 2023-06-26
 */
public interface ITMarketsService 
{
    /**
     * 查询支持交易所
     * 
     * @param slug 支持交易所主键
     * @return 支持交易所
     */
    public TMarkets selectTMarketsBySlug(String slug);

    /**
     * 查询支持交易所列表
     * 
     * @param tMarkets 支持交易所
     * @return 支持交易所集合
     */
    public List<TMarkets> selectTMarketsList(TMarkets tMarkets);

    /**
     * 新增支持交易所
     * 
     * @param tMarkets 支持交易所
     * @return 结果
     */
    public int insertTMarkets(TMarkets tMarkets);

    /**
     * 修改支持交易所
     * 
     * @param tMarkets 支持交易所
     * @return 结果
     */
    public int updateTMarkets(TMarkets tMarkets);

    /**
     * 批量删除支持交易所
     * 
     * @param slugs 需要删除的支持交易所主键集合
     * @return 结果
     */
    public int deleteTMarketsBySlugs(String[] slugs);

    /**
     * 删除支持交易所信息
     * 
     * @param slug 支持交易所主键
     * @return 结果
     */
    public int deleteTMarketsBySlug(String slug);
}
