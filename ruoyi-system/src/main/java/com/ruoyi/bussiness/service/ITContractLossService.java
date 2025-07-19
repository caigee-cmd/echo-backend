package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TContractLoss;

/**
 * 止盈止损表Service接口
 * 
 * @author ruoyi
 * @date 2023-07-25
 */
public interface ITContractLossService extends IService<TContractLoss> {
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
     * 批量删除止盈止损表
     *
     * @param ids 需要删除的止盈止损表主键集合
     * @return 结果
     */
    public int deleteTContractLossByIds(Long[] ids);

    /**
     * 删除止盈止损表信息
     *
     * @param id 止盈止损表主键
     * @return 结果
     */
    public int deleteTContractLossById(Long id);

    String cntractLossSett( TContractLoss contractLoss);


    void  updateContractLoss(Long positionId);

}
