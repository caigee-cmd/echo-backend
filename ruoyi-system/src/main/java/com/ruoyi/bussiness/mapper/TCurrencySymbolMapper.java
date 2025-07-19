package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TCurrencySymbol;

/**
 * 币币交易币种配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-25
 */
public interface TCurrencySymbolMapper extends BaseMapper<TCurrencySymbol>
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
     * 删除币币交易币种配置
     * 
     * @param id 币币交易币种配置主键
     * @return 结果
     */
    public int deleteTCurrencySymbolById(Long id);

    /**
     * 批量删除币币交易币种配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCurrencySymbolByIds(Long[] ids);

    TCurrencySymbol selectInfo(TCurrencySymbol tCurrencySymbol);

    TCurrencySymbol selectByCoin(String symbol);
}
