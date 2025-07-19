package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.ruoyi.bussiness.domain.TLoadProduct;

/**
 * 借贷产品Service接口
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public interface ITLoadProductService extends IService<TLoadProduct>
{
    /**
     * 查询借贷产品
     * 
     * @param id 借贷产品主键
     * @return 借贷产品
     */
    public TLoadProduct selectTLoadProductById(Long id);

    /**
     * 查询借贷产品列表
     * 
     * @param tLoadProduct 借贷产品
     * @return 借贷产品集合
     */
    public List<TLoadProduct> selectTLoadProductList(TLoadProduct tLoadProduct);

    /**
     * 新增借贷产品
     * 
     * @param tLoadProduct 借贷产品
     * @return 结果
     */
    public int insertTLoadProduct(TLoadProduct tLoadProduct);

    /**
     * 修改借贷产品
     * 
     * @param tLoadProduct 借贷产品
     * @return 结果
     */
    public int updateTLoadProduct(TLoadProduct tLoadProduct);

    /**
     * 批量删除借贷产品
     * 
     * @param ids 需要删除的借贷产品主键集合
     * @return 结果
     */
    public int deleteTLoadProductByIds(Long[] ids);

    /**
     * 删除借贷产品信息
     * 
     * @param id 借贷产品主键
     * @return 结果
     */
    public int deleteTLoadProductById(Long id);
}
