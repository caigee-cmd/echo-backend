package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TCurrencySymbol;

/**
 * 币币交易币种配置Service接口
 * 
 * @author ruoyi
 * @date 2023-07-25
 */
public interface ITCurrencySymbolService extends IService<TCurrencySymbol>
{
    /**
     * 查询币币交易币种配置
     * 
     * @param id 币币交易币种配置主键
     * @return 币币交易币种配置
     */
    public TCurrencySymbol selectTCurrencySymbolById(Long id);

    /**
     * 查询币币交易币种配置列表
     * 
     * @param tCurrencySymbol 币币交易币种配置
     * @return 币币交易币种配置集合
     */
    public List<TCurrencySymbol> selectTCurrencySymbolList(TCurrencySymbol tCurrencySymbol);

    /**
     * 新增币币交易币种配置
     * 
     * @param tCurrencySymbol 币币交易币种配置
     * @return 结果
     */
    public int insertTCurrencySymbol(TCurrencySymbol tCurrencySymbol);

    /**
     * 修改币币交易币种配置
     * 
     * @param tCurrencySymbol 币币交易币种配置
     * @return 结果
     */
    public int updateTCurrencySymbol(TCurrencySymbol tCurrencySymbol);

    /**
     * 批量删除币币交易币种配置
     * 
     * @param ids 需要删除的币币交易币种配置主键集合
     * @return 结果
     */
    public int deleteTCurrencySymbolByIds(Long[] ids);

    /**
     * 删除币币交易币种配置信息
     * 
     * @param id 币币交易币种配置主键
     * @return 结果
     */
    public int deleteTCurrencySymbolById(Long id);

    boolean batchSave(String[] symbols);

    List<TCurrencySymbol> getSymbolList();

    TCurrencySymbol selectByCoin(String symbol);
}
