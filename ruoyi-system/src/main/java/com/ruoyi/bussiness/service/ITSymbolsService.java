package com.ruoyi.bussiness.service;

import java.util.List;
import com.ruoyi.bussiness.domain.TSymbols;

/**
 * 支持币种Service接口
 * 
 * @author ruoyi
 * @date 2023-06-26
 */
public interface ITSymbolsService 
{
    /**
     * 查询支持币种
     * 
     * @param slug 支持币种主键
     * @return 支持币种
     */
    public TSymbols selectTSymbolsBySlug(String slug);

    /**
     * 查询支持币种列表
     * 
     * @param tSymbols 支持币种
     * @return 支持币种集合
     */
    public List<TSymbols> selectTSymbolsList(TSymbols tSymbols);

    /**
     * 新增支持币种
     * 
     * @param tSymbols 支持币种
     * @return 结果
     */
    public int insertTSymbols(TSymbols tSymbols);

    /**
     * 修改支持币种
     * 
     * @param tSymbols 支持币种
     * @return 结果
     */
    public int updateTSymbols(TSymbols tSymbols);

    /**
     * 批量删除支持币种
     * 
     * @param slugs 需要删除的支持币种主键集合
     * @return 结果
     */
    public int deleteTSymbolsBySlugs(String[] slugs);

    /**
     * 删除支持币种信息
     * 
     * @param slug 支持币种主键
     * @return 结果
     */
    public int deleteTSymbolsBySlug(String slug);
}
