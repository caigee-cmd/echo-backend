package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TMingOrder;

/**
 * mingMapper接口
 * 
 * @author ruoyi
 * @date 2023-08-18
 */
public interface TMingOrderMapper extends BaseMapper<TMingOrder>
{
    /**
     * 查询ming
     * 
     * @param id ming主键
     * @return ming
     */
    public TMingOrder selectTMingOrderById(Long id);

    /**
     * 查询ming列表
     * 
     * @param tMingOrder ming
     * @return ming集合
     */
    public List<TMingOrder> selectTMingOrderList(TMingOrder tMingOrder);

    /**
     * 新增ming
     * 
     * @param tMingOrder ming
     * @return 结果
     */
    public int insertTMingOrder(TMingOrder tMingOrder);

    /**
     * 修改ming
     * 
     * @param tMingOrder ming
     * @return 结果
     */
    public int updateTMingOrder(TMingOrder tMingOrder);

    /**
     * 删除ming
     * 
     * @param id ming主键
     * @return 结果
     */
    public int deleteTMingOrderById(Long id);

    /**
     * 批量删除ming
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMingOrderByIds(Long[] ids);

    Map<String,Object>  selectMingOrderSumList(Map<String,Object> map);
}
