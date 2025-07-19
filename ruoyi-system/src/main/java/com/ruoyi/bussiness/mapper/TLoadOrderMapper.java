package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TLoadOrder;

/**
 * 贷款订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-14
 */
public interface TLoadOrderMapper extends BaseMapper<TLoadOrder>
{
    /**
     * 查询贷款订单
     * 
     * @param id 贷款订单主键
     * @return 贷款订单
     */
    public TLoadOrder selectTLoadOrderById(Long id);

    /**
     * 查询贷款订单列表
     * 
     * @param tLoadOrder 贷款订单
     * @return 贷款订单集合
     */
    public List<TLoadOrder> selectTLoadOrderList(TLoadOrder tLoadOrder);

    /**
     * 新增贷款订单
     * 
     * @param tLoadOrder 贷款订单
     * @return 结果
     */
    public int insertTLoadOrder(TLoadOrder tLoadOrder);

    /**
     * 修改贷款订单
     * 
     * @param tLoadOrder 贷款订单
     * @return 结果
     */
    public int updateTLoadOrder(TLoadOrder tLoadOrder);

    /**
     * 删除贷款订单
     * 
     * @param id 贷款订单主键
     * @return 结果
     */
    public int deleteTLoadOrderById(Long id);

    /**
     * 批量删除贷款订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTLoadOrderByIds(Long[] ids);

    List<TLoadOrder> selectListByUserId(TLoadOrder serch);
}
