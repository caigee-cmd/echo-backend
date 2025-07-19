package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.KlineSymbol;

/**
 * 数据源Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-10
 */
public interface KlineSymbolMapper extends BaseMapper<KlineSymbol>
{
    /**
     * 查询数据源
     * 
     * @param id 数据源主键
     * @return 数据源
     */
    public KlineSymbol selectKlineSymbolById(Long id);

    /**
     * 查询数据源列表
     * 
     * @param klineSymbol 数据源
     * @return 数据源集合
     */
    public List<KlineSymbol> selectKlineSymbolList(KlineSymbol klineSymbol);

    /**
     * 新增数据源
     * 
     * @param klineSymbol 数据源
     * @return 结果
     */
    public int insertKlineSymbol(KlineSymbol klineSymbol);

    /**
     * 修改数据源
     * 
     * @param klineSymbol 数据源
     * @return 结果
     */
    public int updateKlineSymbol(KlineSymbol klineSymbol);

    /**
     * 删除数据源
     * 
     * @param id 数据源主键
     * @return 结果
     */
    public int deleteKlineSymbolById(Long id);

    /**
     * 批量删除数据源
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKlineSymbolByIds(Long[] ids);
}
