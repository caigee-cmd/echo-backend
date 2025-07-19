package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TNftOrder;

/**
 * nft订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
public interface TNftOrderMapper extends BaseMapper<TNftOrder>
{
    /**
     * 查询nft订单
     * 
     * @param id nft订单主键
     * @return nft订单
     */
    public TNftOrder selectTNftOrderById(Long id);

    /**
     * 查询nft订单列表
     * 
     * @param tNftOrder nft订单
     * @return nft订单集合
     */
    public List<TNftOrder> selectTNftOrderList(TNftOrder tNftOrder);

    /**
     * 新增nft订单
     * 
     * @param tNftOrder nft订单
     * @return 结果
     */
    public int insertTNftOrder(TNftOrder tNftOrder);

    /**
     * 修改nft订单
     * 
     * @param tNftOrder nft订单
     * @return 结果
     */
    public int updateTNftOrder(TNftOrder tNftOrder);

    /**
     * 删除nft订单
     * 
     * @param id nft订单主键
     * @return 结果
     */
    public int deleteTNftOrderById(Long id);

    /**
     * 批量删除nft订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTNftOrderByIds(Long[] ids);
}
