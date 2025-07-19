package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TNftSeries;

/**
 * nft合计Service接口
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
public interface ITNftSeriesService extends IService<TNftSeries>
{
    /**
     * 查询nft合计
     * 
     * @param id nft合计主键
     * @return nft合计
     */
    public TNftSeries selectTNftSeriesById(Long id);

    /**
     * 查询nft合计列表
     * 
     * @param tNftSeries nft合计
     * @return nft合计集合
     */
    public List<TNftSeries> selectTNftSeriesList(TNftSeries tNftSeries);

    /**
     * 新增nft合计
     * 
     * @param tNftSeries nft合计
     * @return 结果
     */
    public int insertTNftSeries(TNftSeries tNftSeries);

    /**
     * 修改nft合计
     * 
     * @param tNftSeries nft合计
     * @return 结果
     */
    public int updateTNftSeries(TNftSeries tNftSeries);

    /**
     * 批量删除nft合计
     * 
     * @param ids 需要删除的nft合计主键集合
     * @return 结果
     */
    public int deleteTNftSeriesByIds(Long[] ids);

    /**
     * 删除nft合计信息
     * 
     * @param id nft合计主键
     * @return 结果
     */
    public int deleteTNftSeriesById(Long id);
}
