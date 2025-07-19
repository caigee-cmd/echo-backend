package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TOptionRules;

/**
 * 前台文本配置Service接口
 * 
 * @author ruoyi
 * @date 2023-07-19
 */
public interface ITOptionRulesService extends IService<TOptionRules>
{
    /**
     * 查询前台文本配置
     * 
     * @param id 前台文本配置主键
     * @return 前台文本配置
     */
    public TOptionRules selectTOptionRulesById(Long id);

    /**
     * 查询前台文本配置列表
     * 
     * @param tOptionRules 前台文本配置
     * @return 前台文本配置集合
     */
    public List<TOptionRules> selectTOptionRulesList(TOptionRules tOptionRules);

    /**
     * 新增前台文本配置
     * 
     * @param tOptionRules 前台文本配置
     * @return 结果
     */
    public int insertTOptionRules(TOptionRules tOptionRules);

    /**
     * 修改前台文本配置
     * 
     * @param tOptionRules 前台文本配置
     * @return 结果
     */
    public int updateTOptionRules(TOptionRules tOptionRules);

    /**
     * 批量删除前台文本配置
     * 
     * @param ids 需要删除的前台文本配置主键集合
     * @return 结果
     */
    public int deleteTOptionRulesByIds(Long[] ids);

    /**
     * 删除前台文本配置信息
     * 
     * @param id 前台文本配置主键
     * @return 结果
     */
    public int deleteTOptionRulesById(Long id);
}
