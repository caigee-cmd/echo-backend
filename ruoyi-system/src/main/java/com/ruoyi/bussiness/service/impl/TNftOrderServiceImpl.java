package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TNftOrderMapper;
import com.ruoyi.bussiness.domain.TNftOrder;
import com.ruoyi.bussiness.service.ITNftOrderService;

/**
 * nft订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
@Service
public class TNftOrderServiceImpl extends ServiceImpl<TNftOrderMapper,TNftOrder> implements ITNftOrderService
{
    @Autowired
    private TNftOrderMapper tNftOrderMapper;

    /**
     * 查询nft订单
     * 
     * @param id nft订单主键
     * @return nft订单
     */
    @Override
    public TNftOrder selectTNftOrderById(Long id)
    {
        return tNftOrderMapper.selectTNftOrderById(id);
    }

    /**
     * 查询nft订单列表
     * 
     * @param tNftOrder nft订单
     * @return nft订单
     */
    @Override
    public List<TNftOrder> selectTNftOrderList(TNftOrder tNftOrder)
    {
        return tNftOrderMapper.selectTNftOrderList(tNftOrder);
    }

    /**
     * 新增nft订单
     * 
     * @param tNftOrder nft订单
     * @return 结果
     */
    @Override
    public int insertTNftOrder(TNftOrder tNftOrder)
    {
        tNftOrder.setCreateTime(DateUtils.getNowDate());
        return tNftOrderMapper.insertTNftOrder(tNftOrder);
    }

    /**
     * 修改nft订单
     * 
     * @param tNftOrder nft订单
     * @return 结果
     */
    @Override
    public int updateTNftOrder(TNftOrder tNftOrder)
    {
        tNftOrder.setUpdateTime(DateUtils.getNowDate());
        return tNftOrderMapper.updateTNftOrder(tNftOrder);
    }

    /**
     * 批量删除nft订单
     * 
     * @param ids 需要删除的nft订单主键
     * @return 结果
     */
    @Override
    public int deleteTNftOrderByIds(Long[] ids)
    {
        return tNftOrderMapper.deleteTNftOrderByIds(ids);
    }

    /**
     * 删除nft订单信息
     * 
     * @param id nft订单主键
     * @return 结果
     */
    @Override
    public int deleteTNftOrderById(Long id)
    {
        return tNftOrderMapper.deleteTNftOrderById(id);
    }
}
