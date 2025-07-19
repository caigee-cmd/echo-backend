package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TSpontaneousCoin;

/**
 * 自发币种配置Service接口
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
public interface ITSpontaneousCoinService extends IService<TSpontaneousCoin>
{
    /**
     * 查询自发币种配置
     * 
     * @param id 自发币种配置主键
     * @return 自发币种配置
     */
    public TSpontaneousCoin selectTSpontaneousCoinById(Long id);

    /**
     * 查询自发币种配置列表
     * 
     * @param tSpontaneousCoin 自发币种配置
     * @return 自发币种配置集合
     */
    public List<TSpontaneousCoin> selectTSpontaneousCoinList(TSpontaneousCoin tSpontaneousCoin);

    /**
     * 新增自发币种配置
     * 
     * @param tSpontaneousCoin 自发币种配置
     * @return 结果
     */
    public int insertTSpontaneousCoin(TSpontaneousCoin tSpontaneousCoin);

    /**
     * 修改自发币种配置
     * 
     * @param tSpontaneousCoin 自发币种配置
     * @return 结果
     */
    public int updateTSpontaneousCoin(TSpontaneousCoin tSpontaneousCoin);

    /**
     * 批量删除自发币种配置
     * 
     * @param ids 需要删除的自发币种配置主键集合
     * @return 结果
     */
    public int deleteTSpontaneousCoinByIds(Long[] ids);

    /**
     * 删除自发币种配置信息
     * 
     * @param id 自发币种配置主键
     * @return 结果
     */
    public int deleteTSpontaneousCoinById(Long id);
}
