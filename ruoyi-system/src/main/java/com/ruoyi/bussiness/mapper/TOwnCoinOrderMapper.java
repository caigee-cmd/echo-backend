package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TOwnCoinOrder;
import org.apache.ibatis.annotations.Param;

/**
 * 申购订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-20
 */
public interface TOwnCoinOrderMapper extends BaseMapper<TOwnCoinOrder>
{
    /**
     * 查询申购订单
     * 
     * @param id 申购订单主键
     * @return 申购订单
     */
    public TOwnCoinOrder selectTOwnCoinOrderById(Long id);

    /**
     * 查询申购订单列表
     * 
     * @param tOwnCoinOrder 申购订单
     * @return 申购订单集合
     */
    public List<TOwnCoinOrder> selectTOwnCoinOrderList(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 新增申购订单
     * 
     * @param tOwnCoinOrder 申购订单
     * @return 结果
     */
    public int insertTOwnCoinOrder(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 修改申购订单
     * 
     * @param tOwnCoinOrder 申购订单
     * @return 结果
     */
    public int updateTOwnCoinOrder(TOwnCoinOrder tOwnCoinOrder);

    /**
     * 删除申购订单
     * 
     * @param id 申购订单主键
     * @return 结果
     */
    public int deleteTOwnCoinOrderById(Long id);

    /**
     * 批量删除申购订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOwnCoinOrderByIds(Long[] ids);

    Integer getAllAmountByUserIdAndCoinId(@Param("ownId") Long ownId, @Param("userId") Long userId);
}
