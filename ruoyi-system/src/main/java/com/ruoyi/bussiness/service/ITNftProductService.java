package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TNftProduct;

/**
 * nft详情Service接口
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
public interface ITNftProductService extends IService<TNftProduct>
{
    /**
     * 查询nft详情
     * 
     * @param id nft详情主键
     * @return nft详情
     */
    public TNftProduct selectTNftProductById(Long id);

    /**
     * 查询nft详情列表
     * 
     * @param tNftProduct nft详情
     * @return nft详情集合
     */
    public List<TNftProduct> selectTNftProductList(TNftProduct tNftProduct);

    /**
     * 新增nft详情
     * 
     * @param tNftProduct nft详情
     * @return 结果
     */
    public int insertTNftProduct(TNftProduct tNftProduct);

    /**
     * 修改nft详情
     * 
     * @param tNftProduct nft详情
     * @return 结果
     */
    public int updateTNftProduct(TNftProduct tNftProduct);

    /**
     * 批量删除nft详情
     * 
     * @param ids 需要删除的nft详情主键集合
     * @return 结果
     */
    public int deleteTNftProductByIds(Long[] ids);

    /**
     * 删除nft详情信息
     * 
     * @param id nft详情主键
     * @return 结果
     */
    public int deleteTNftProductById(Long id);
}
