package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.bussiness.domain.TCollectionOrder;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
public interface TCollectionOrderMapper extends BaseMapper<TCollectionOrder>
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TCollectionOrder selectTCollectionOrderById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tCollectionOrder 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TCollectionOrder> selectTCollectionOrderList(TCollectionOrder tCollectionOrder);

    /**
     * 新增【请填写功能名称】
     * 
     * @param tCollectionOrder 【请填写功能名称】
     * @return 结果
     */
    public int insertTCollectionOrder(TCollectionOrder tCollectionOrder);

    /**
     * 修改【请填写功能名称】
     * 
     * @param tCollectionOrder 【请填写功能名称】
     * @return 结果
     */
    public int updateTCollectionOrder(TCollectionOrder tCollectionOrder);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTCollectionOrderById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCollectionOrderByIds(Long[] ids);

    BigDecimal selectCollectionAmountByUserId(Long userId);

    BigDecimal selectCollectionAmountByAgencyId(Long agencyId);

    BigDecimal selectCollectionAmountDetail(@Param("appUserId") Long appUserId, @Param("adminParentIds")String adminParentIds);

    BigDecimal getDayCollectionAmount(@Param("beginTime")String beginTime, @Param("endTime")String endTime);
}
