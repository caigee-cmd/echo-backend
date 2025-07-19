package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.bussiness.domain.DefiOrder;
import com.ruoyi.bussiness.domain.dto.DefiOrderDTO;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.DefiOrderMapper;
import com.ruoyi.bussiness.service.IDefiOrderService;

/**
 * defi订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
@Service
public class DefiOrderServiceImpl extends ServiceImpl<DefiOrderMapper,DefiOrder> implements IDefiOrderService
{
    @Autowired
    private DefiOrderMapper defiOrderMapper;

    /**
     * 查询defi订单
     * 
     * @param id defi订单主键
     * @return defi订单
     */
    @Override
    public DefiOrder selectDefiOrderById(Long id)
    {
        return defiOrderMapper.selectDefiOrderById(id);
    }

    /**
     * 查询defi订单列表
     * 
     * @param defiOrder defi订单
     * @return defi订单
     */
    @Override
    public List<DefiOrder> selectDefiOrderList(DefiOrder defiOrder)
    {
        return defiOrderMapper.selectDefiOrderList(defiOrder);
    }

    @Override
    public List<DefiOrderDTO> getOrder(DefiOrder defiOrder) {
        return  defiOrderMapper.getOrder(defiOrder);
    }

    /**
     * 新增defi订单
     * 
     * @param defiOrder defi订单
     * @return 结果
     */
    @Override
    public int insertDefiOrder(DefiOrder defiOrder)
    {
        defiOrder.setCreateTime(DateUtils.getNowDate());
        return defiOrderMapper.insertDefiOrder(defiOrder);
    }

    /**
     * 修改defi订单
     * 
     * @param defiOrder defi订单
     * @return 结果
     */
    @Override
    public int updateDefiOrder(DefiOrder defiOrder)
    {
        defiOrder.setUpdateTime(DateUtils.getNowDate());
        return defiOrderMapper.updateDefiOrder(defiOrder);
    }

    /**
     * 批量删除defi订单
     * 
     * @param ids 需要删除的defi订单主键
     * @return 结果
     */
    @Override
    public int deleteDefiOrderByIds(Long[] ids)
    {
        return defiOrderMapper.deleteDefiOrderByIds(ids);
    }

    /**
     * 删除defi订单信息
     * 
     * @param id defi订单主键
     * @return 结果
     */
    @Override
    public int deleteDefiOrderById(Long id)
    {
        return defiOrderMapper.deleteDefiOrderById(id);
    }

    @Override
    public BigDecimal getAllAmount(Long id) {
        return defiOrderMapper.getAllAmount(id);
    }
}
