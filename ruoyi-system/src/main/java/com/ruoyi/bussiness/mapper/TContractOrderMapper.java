package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TContractOrder;

/**
 * U本位委托Mapper接口
 * 
 * @author michael
 * @date 2023-07-20
 */
public interface TContractOrderMapper extends BaseMapper<TContractOrder>
{
    /**
     * 查询U本位委托
     * 
     * @param id U本位委托主键
     * @return U本位委托
     */
    public TContractOrder selectTContractOrderById(Long id);

    /**
     * 查询U本位委托列表
     * 
     * @param tContractOrder U本位委托
     * @return U本位委托集合
     */
    public List<TContractOrder> selectTContractOrderList(TContractOrder tContractOrder);

    /**
     * 新增U本位委托
     * 
     * @param tContractOrder U本位委托
     * @return 结果
     */
    public int insertTContractOrder(TContractOrder tContractOrder);

    /**
     * 修改U本位委托
     * 
     * @param tContractOrder U本位委托
     * @return 结果
     */
    public int updateTContractOrder(TContractOrder tContractOrder);

    /**
     * 删除U本位委托
     * 
     * @param id U本位委托主键
     * @return 结果
     */
    public int deleteTContractOrderById(Long id);

    /**
     * 批量删除U本位委托
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTContractOrderByIds(Long[] ids);
}
