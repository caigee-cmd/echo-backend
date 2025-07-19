package com.ruoyi.bussiness.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TSymbolsMapper;
import com.ruoyi.bussiness.domain.TSymbols;
import com.ruoyi.bussiness.service.ITSymbolsService;

/**
 * 支持币种Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-06-26
 */
@Service
public class TSymbolsServiceImpl implements ITSymbolsService 
{
    @Autowired
    private TSymbolsMapper tSymbolsMapper;

    /**
     * 查询支持币种
     * 
     * @param slug 支持币种主键
     * @return 支持币种
     */
    @Override
    public TSymbols selectTSymbolsBySlug(String slug)
    {
        return tSymbolsMapper.selectTSymbolsBySlug(slug);
    }

    /**
     * 查询支持币种列表
     * 
     * @param tSymbols 支持币种
     * @return 支持币种
     */
    @Override
    public List<TSymbols> selectTSymbolsList(TSymbols tSymbols)
    {
        return tSymbolsMapper.selectTSymbolsList(tSymbols);
    }

    /**
     * 新增支持币种
     * 
     * @param tSymbols 支持币种
     * @return 结果
     */
    @Override
    public int insertTSymbols(TSymbols tSymbols)
    {
        return tSymbolsMapper.insertTSymbols(tSymbols);
    }

    /**
     * 修改支持币种
     * 
     * @param tSymbols 支持币种
     * @return 结果
     */
    @Override
    public int updateTSymbols(TSymbols tSymbols)
    {
        return tSymbolsMapper.updateTSymbols(tSymbols);
    }

    /**
     * 批量删除支持币种
     * 
     * @param slugs 需要删除的支持币种主键
     * @return 结果
     */
    @Override
    public int deleteTSymbolsBySlugs(String[] slugs)
    {
        return tSymbolsMapper.deleteTSymbolsBySlugs(slugs);
    }

    /**
     * 删除支持币种信息
     * 
     * @param slug 支持币种主键
     * @return 结果
     */
    @Override
    public int deleteTSymbolsBySlug(String slug)
    {
        return tSymbolsMapper.deleteTSymbolsBySlug(slug);
    }
}
