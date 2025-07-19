package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.DefiOrder;
import com.ruoyi.bussiness.domain.dto.DefiOrderDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * defi订单Service接口
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
public interface IDefiOrderService extends IService<DefiOrder>
{
    /**
     * 查询defi订单
     * 
     * @param id defi订单主键
     * @return defi订单
     */
    public DefiOrder selectDefiOrderById(Long id);

    /**
     * 查询defi订单列表
     * 
     * @param defiOrder defi订单
     * @return defi订单集合
     */
    public List<DefiOrder> selectDefiOrderList(DefiOrder defiOrder);
    public List<DefiOrderDTO> getOrder(DefiOrder defiOrder);
    /**
     * 新增defi订单
     * 
     * @param defiOrder defi订单
     * @return 结果
     */
    public int insertDefiOrder(DefiOrder defiOrder);

    /**
     * 修改defi订单
     * 
     * @param defiOrder defi订单
     * @return 结果
     */
    public int updateDefiOrder(DefiOrder defiOrder);

    /**
     * 批量删除defi订单
     * 
     * @param ids 需要删除的defi订单主键集合
     * @return 结果
     */
    public int deleteDefiOrderByIds(Long[] ids);

    /**
     * 删除defi订单信息
     * 
     * @param id defi订单主键
     * @return 结果
     */
    public int deleteDefiOrderById(Long id);

    public BigDecimal getAllAmount(Long id);
}
