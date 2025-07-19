package com.ruoyi.bussiness.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TMarketsMapper;
import com.ruoyi.bussiness.domain.TMarkets;
import com.ruoyi.bussiness.service.ITMarketsService;

/**
 * 支持交易所Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-26
 */
@Service
public class TMarketsServiceImpl implements ITMarketsService 
{
    @Autowired
    private TMarketsMapper tMarketsMapper;

    /**
     * 查询支持交易所
     * 
     * @param slug 支持交易所主键
     * @return 支持交易所
     */
    @Override
    public TMarkets selectTMarketsBySlug(String slug)
    {
        return tMarketsMapper.selectTMarketsBySlug(slug);
    }

    /**
     * 查询支持交易所列表
     * 
     * @param tMarkets 支持交易所
     * @return 支持交易所
     */
    @Override
    public List<TMarkets> selectTMarketsList(TMarkets tMarkets)
    {
        return tMarketsMapper.selectTMarketsList(tMarkets);
    }

    /**
     * 新增支持交易所
     * 
     * @param tMarkets 支持交易所
     * @return 结果
     */
    @Override
    public int insertTMarkets(TMarkets tMarkets)
    {
        return tMarketsMapper.insertTMarkets(tMarkets);
    }

    /**
     * 修改支持交易所
     * 
     * @param tMarkets 支持交易所
     * @return 结果
     */
    @Override
    public int updateTMarkets(TMarkets tMarkets)
    {
        return tMarketsMapper.updateTMarkets(tMarkets);
    }

    /**
     * 批量删除支持交易所
     * 
     * @param slugs 需要删除的支持交易所主键
     * @return 结果
     */
    @Override
    public int deleteTMarketsBySlugs(String[] slugs)
    {
        return tMarketsMapper.deleteTMarketsBySlugs(slugs);
    }

    /**
     * 删除支持交易所信息
     * 
     * @param slug 支持交易所主键
     * @return 结果
     */
    @Override
    public int deleteTMarketsBySlug(String slug)
    {
        return tMarketsMapper.deleteTMarketsBySlug(slug);
    }
}
