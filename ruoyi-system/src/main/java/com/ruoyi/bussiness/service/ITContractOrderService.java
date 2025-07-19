package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.TContractOrder;
import java.math.BigDecimal;
import java.util.List;

/**
 * U本位委托Service接口
 * 
 * @author michael
 * @date 2023-07-20
 */
public interface ITContractOrderService extends IService<TContractOrder>
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
     * 批量删除U本位委托
     * 
     * @param ids 需要删除的U本位委托主键集合
     * @return 结果
     */
    public int deleteTContractOrderByIds(Long[] ids);

    /**
     * 删除U本位委托信息
     * 
     * @param id U本位委托主键
     * @return 结果
     */
    public int deleteTContractOrderById(Long id);


    /**
     * U本位提交接口
     * @param symbol
     * @param leverage
     * @param delegatePrice
     * @param delegateTotal
     * @param userId
     * @param type
     * @param delegateType
     * @return
     */
    String buyContractOrder(String symbol, BigDecimal leverage, BigDecimal delegatePrice,BigDecimal delegateTotal,Long userId,Integer type, Integer delegateType);


    /**
     * 提交验证
     * @param symbol
     * @param leverage
     * @param delegatePrice
     * @param delegateTotal
     * @param userId
     * @return
     */

    String verifySubmit(String symbol, BigDecimal leverage, BigDecimal delegatePrice,BigDecimal delegateTotal,Long userId);


    /**
     * 撤销委托订单
     * @param id
     * @return
     */
    String canCelOrder(Long id);





}
