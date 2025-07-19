package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TMingProduct;

/**
 * mingProductService接口
 * 
 * @author ruoyi
 * @date 2023-08-18
 */
public interface ITMingProductService extends IService<TMingProduct>
{
    /**
     * 查询mingProduct
     * 
     * @param id mingProduct主键
     * @return mingProduct
     */
    public TMingProduct selectTMingProductById(Long id);

    /**
     * 查询mingProduct列表
     * 
     * @param tMingProduct mingProduct
     * @return mingProduct集合
     */
    public List<TMingProduct> selectTMingProductList(TMingProduct tMingProduct);

    /**
     * 新增mingProduct
     * 
     * @param tMingProduct mingProduct
     * @return 结果
     */
    public int insertTMingProduct(TMingProduct tMingProduct);

    /**
     * 修改mingProduct
     * 
     * @param tMingProduct mingProduct
     * @return 结果
     */
    public int updateTMingProduct(TMingProduct tMingProduct);

    /**
     * 批量删除mingProduct
     * 
     * @param ids 需要删除的mingProduct主键集合
     * @return 结果
     */
    public int deleteTMingProductByIds(Long[] ids);

    /**
     * 删除mingProduct信息
     * 
     * @param id mingProduct主键
     * @return 结果
     */
    public int deleteTMingProductById(Long id);
}
