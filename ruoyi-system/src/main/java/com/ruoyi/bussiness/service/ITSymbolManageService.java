package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.ruoyi.bussiness.domain.TSymbolManage;

/**
 * 币种管理Service接口
 * 
 * @author ruoyi
 * @date 2023-07-12
 */
public interface ITSymbolManageService extends IService<TSymbolManage>
{
    /**
     * 查询币种管理
     * 
     * @param id 币种管理主键
     * @return 币种管理
     */
    public TSymbolManage selectTSymbolManageById(Long id);

    /**
     * 查询币种管理列表
     * 
     * @param tSymbolManage 币种管理
     * @return 币种管理集合
     */
    public List<TSymbolManage> selectTSymbolManageList(TSymbolManage tSymbolManage);

    /**
     * 新增币种管理
     * 
     * @param tSymbolManage 币种管理
     * @return 结果
     */
    public int insertTSymbolManage(TSymbolManage tSymbolManage);

    /**
     * 修改币种管理
     * 
     * @param tSymbolManage 币种管理
     * @return 结果
     */
    public int updateTSymbolManage(TSymbolManage tSymbolManage);

    /**
     * 批量删除币种管理
     * 
     * @param ids 需要删除的币种管理主键集合
     * @return 结果
     */
    public int deleteTSymbolManageByIds(Long[] ids);

    /**
     * 删除币种管理信息
     * 
     * @param id 币种管理主键
     * @return 结果
     */
    public int deleteTSymbolManageById(Long id);

    List<String> selectSymbolList(TSymbolManage tSymbolManage);

    boolean addBatch(String[] symbols);
}
