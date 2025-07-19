package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TNftSeriesMapper;
import com.ruoyi.bussiness.domain.TNftSeries;
import com.ruoyi.bussiness.service.ITNftSeriesService;

/**
 * nft合计Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
@Service
public class TNftSeriesServiceImpl extends ServiceImpl<TNftSeriesMapper,TNftSeries> implements ITNftSeriesService
{
    @Autowired
    private TNftSeriesMapper tNftSeriesMapper;

    /**
     * 查询nft合计
     * 
     * @param id nft合计主键
     * @return nft合计
     */
    @Override
    public TNftSeries selectTNftSeriesById(Long id)
    {
        return tNftSeriesMapper.selectTNftSeriesById(id);
    }

    /**
     * 查询nft合计列表
     * 
     * @param tNftSeries nft合计
     * @return nft合计
     */
    @Override
    public List<TNftSeries> selectTNftSeriesList(TNftSeries tNftSeries)
    {
        return tNftSeriesMapper.selectTNftSeriesList(tNftSeries);
    }

    /**
     * 新增nft合计
     * 
     * @param tNftSeries nft合计
     * @return 结果
     */
    @Override
    public int insertTNftSeries(TNftSeries tNftSeries)
    {
        tNftSeries.setCreateTime(DateUtils.getNowDate());
        tNftSeries.setUpdateTime(DateUtils.getNowDate());
        tNftSeries.setCreateBy(SecurityUtils.getUsername());
        tNftSeries.setUpdateBy(SecurityUtils.getUsername());
        tNftSeries.setDelFlag("0");
        return tNftSeriesMapper.insertTNftSeries(tNftSeries);
    }

    /**
     * 修改nft合计
     * 
     * @param tNftSeries nft合计
     * @return 结果
     */
    @Override
    public int updateTNftSeries(TNftSeries tNftSeries)
    {
        tNftSeries.setUpdateTime(DateUtils.getNowDate());
        return tNftSeriesMapper.updateTNftSeries(tNftSeries);
    }

    /**
     * 批量删除nft合计
     * 
     * @param ids 需要删除的nft合计主键
     * @return 结果
     */
    @Override
    public int deleteTNftSeriesByIds(Long[] ids)
    {
        return tNftSeriesMapper.deleteTNftSeriesByIds(ids);
    }

    /**
     * 删除nft合计信息
     * 
     * @param id nft合计主键
     * @return 结果
     */
    @Override
    public int deleteTNftSeriesById(Long id)
    {
        return tNftSeriesMapper.deleteTNftSeriesById(id);
    }
}
