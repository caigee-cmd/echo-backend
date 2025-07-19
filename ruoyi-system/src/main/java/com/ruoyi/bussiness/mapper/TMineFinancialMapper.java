package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.bussiness.domain.TMineFinancial;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
public interface TMineFinancialMapper extends BaseMapper<TMineFinancial>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TMineFinancial selectTMineFinancialById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineFinancial 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TMineFinancial> selectTMineFinancialList(TMineFinancial tMineFinancial);

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineFinancial 【请填写功能名称】
     * @return 结果
     */
    public int insertTMineFinancial(TMineFinancial tMineFinancial);

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineFinancial 【请填写功能名称】
     * @return 结果
     */
    public int updateTMineFinancial(TMineFinancial tMineFinancial);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTMineFinancialById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMineFinancialByIds(Long[] ids);
}
