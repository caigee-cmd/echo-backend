package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.KlineSymbolMapper;
import com.ruoyi.bussiness.domain.KlineSymbol;
import com.ruoyi.bussiness.service.IKlineSymbolService;

/**
 * 数据源Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-10
 */
@Service
public class KlineSymbolServiceImpl extends ServiceImpl<KlineSymbolMapper,KlineSymbol> implements IKlineSymbolService
{
    @Autowired
    private KlineSymbolMapper klineSymbolMapper;

    /**
     * 查询数据源
     * 
     * @param id 数据源主键
     * @return 数据源
     */
    @Override
    public KlineSymbol selectKlineSymbolById(Long id)
    {
        return klineSymbolMapper.selectKlineSymbolById(id);
    }

    /**
     * 查询数据源列表
     * 
     * @param klineSymbol 数据源
     * @return 数据源
     */
    @Override
    public List<KlineSymbol> selectKlineSymbolList(KlineSymbol klineSymbol)
    {
        return klineSymbolMapper.selectKlineSymbolList(klineSymbol);
    }

    /**
     * 新增数据源
     * 
     * @param klineSymbol 数据源
     * @return 结果
     */
    @Override
    public int insertKlineSymbol(KlineSymbol klineSymbol)
    {
        return klineSymbolMapper.insertKlineSymbol(klineSymbol);
    }

    /**
     * 修改数据源
     * 
     * @param klineSymbol 数据源
     * @return 结果
     */
    @Override
    public int updateKlineSymbol(KlineSymbol klineSymbol)
    {
        return klineSymbolMapper.updateKlineSymbol(klineSymbol);
    }

    /**
     * 批量删除数据源
     * 
     * @param ids 需要删除的数据源主键
     * @return 结果
     */
    @Override
    public int deleteKlineSymbolByIds(Long[] ids)
    {
        return klineSymbolMapper.deleteKlineSymbolByIds(ids);
    }

    /**
     * 删除数据源信息
     * 
     * @param id 数据源主键
     * @return 结果
     */
    @Override
    public int deleteKlineSymbolById(Long id)
    {
        return klineSymbolMapper.deleteKlineSymbolById(id);
    }
}
