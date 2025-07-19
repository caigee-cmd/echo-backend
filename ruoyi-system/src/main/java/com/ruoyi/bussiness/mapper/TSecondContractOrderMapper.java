package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TSecondContractOrder;

import java.util.List;

/**
 * 秒合约订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public interface TSecondContractOrderMapper extends BaseMapper<TSecondContractOrder>
{
    /**
     * 查询秒合约订单
     * 
     * @param id 秒合约订单主键
     * @return 秒合约订单
     */
    public TSecondContractOrder selectTSecondContractOrderById(Long id);

    /**
     * 查询秒合约订单列表
     * 
     * @param tSecondContractOrder 秒合约订单
     * @return 秒合约订单集合
     */
    public List<TSecondContractOrder> selectTSecondContractOrderList(TSecondContractOrder tSecondContractOrder);

    /**
     * 新增秒合约订单
     * 
     * @param tSecondContractOrder 秒合约订单
     * @return 结果
     */
    public int insertTSecondContractOrder(TSecondContractOrder tSecondContractOrder);

    /**
     * 修改秒合约订单
     * 
     * @param tSecondContractOrder 秒合约订单
     * @return 结果
     */
    public int updateTSecondContractOrder(TSecondContractOrder tSecondContractOrder);

    /**
     * 删除秒合约订单
     * 
     * @param id 秒合约订单主键
     * @return 结果
     */
    public int deleteTSecondContractOrderById(Long id);

    /**
     * 批量删除秒合约订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSecondContractOrderByIds(Long[] ids);
}
