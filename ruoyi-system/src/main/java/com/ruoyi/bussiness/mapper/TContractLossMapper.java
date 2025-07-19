package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TContractLoss;

/**
 * 止盈止损表Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-25
 */
public interface TContractLossMapper extends BaseMapper<TContractLoss>
{
    /**
     * 查询止盈止损表
     * 
     * @param id 止盈止损表主键
     * @return 止盈止损表
     */
    public TContractLoss selectTContractLossById(Long id);

    /**
     * 查询止盈止损表列表
     * 
     * @param tContractLoss 止盈止损表
     * @return 止盈止损表集合
     */
    public List<TContractLoss> selectTContractLossList(TContractLoss tContractLoss);

    /**
     * 新增止盈止损表
     * 
     * @param tContractLoss 止盈止损表
     * @return 结果
     */
    public int insertTContractLoss(TContractLoss tContractLoss);

    /**
     * 修改止盈止损表
     * 
     * @param tContractLoss 止盈止损表
     * @return 结果
     */
    public int updateTContractLoss(TContractLoss tContractLoss);

    /**
     * 删除止盈止损表
     * 
     * @param id 止盈止损表主键
     * @return 结果
     */
    public int deleteTContractLossById(Long id);

    /**
     * 批量删除止盈止损表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTContractLossByIds(Long[] ids);


   void  updateContractLoss(Long positionId);


}
