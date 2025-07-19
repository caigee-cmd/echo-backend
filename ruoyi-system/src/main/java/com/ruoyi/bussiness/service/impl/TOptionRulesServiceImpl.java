package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TOptionRulesMapper;
import com.ruoyi.bussiness.domain.TOptionRules;
import com.ruoyi.bussiness.service.ITOptionRulesService;

/**
 * 前台文本配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-19
 */
@Service
public class TOptionRulesServiceImpl extends ServiceImpl<TOptionRulesMapper,TOptionRules> implements ITOptionRulesService
{
    @Autowired
    private TOptionRulesMapper tOptionRulesMapper;

    /**
     * 查询前台文本配置
     * 
     * @param id 前台文本配置主键
     * @return 前台文本配置
     */
    @Override
    public TOptionRules selectTOptionRulesById(Long id)
    {
        return tOptionRulesMapper.selectTOptionRulesById(id);
    }

    /**
     * 查询前台文本配置列表
     * 
     * @param tOptionRules 前台文本配置00
     * @return 前台文本配置
     */
    @Override
    public List<TOptionRules> selectTOptionRulesList(TOptionRules tOptionRules)
    {
        return tOptionRulesMapper.selectTOptionRulesList(tOptionRules);
    }

    /**
     * 新增前台文本配置
     * 
     * @param tOptionRules 前台文本配置
     * @return 结果
     */
    @Override
    public int insertTOptionRules(TOptionRules tOptionRules)
    {
        tOptionRules.setCreateTime(DateUtils.getNowDate());
        return tOptionRulesMapper.insertTOptionRules(tOptionRules);
    }

    /**
     * 修改前台文本配置
     * 
     * @param tOptionRules 前台文本配置
     * @return 结果
     */
    @Override
    public int updateTOptionRules(TOptionRules tOptionRules)
    {
        return tOptionRulesMapper.updateTOptionRules(tOptionRules);
    }

    /**
     * 批量删除前台文本配置
     * 
     * @param ids 需要删除的前台文本配置主键
     * @return 结果
     */
    @Override
    public int deleteTOptionRulesByIds(Long[] ids)
    {
        return tOptionRulesMapper.deleteTOptionRulesByIds(ids);
    }

    /**
     * 删除前台文本配置信息
     * 
     * @param id 前台文本配置主键
     * @return 结果
     */
    @Override
    public int deleteTOptionRulesById(Long id)
    {
        return tOptionRulesMapper.deleteTOptionRulesById(id);
    }
}
