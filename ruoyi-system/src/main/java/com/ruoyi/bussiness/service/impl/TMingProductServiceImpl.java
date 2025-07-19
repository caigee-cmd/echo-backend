package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TMingProductMapper;
import com.ruoyi.bussiness.domain.TMingProduct;
import com.ruoyi.bussiness.service.ITMingProductService;

/**
 * mingProductService业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-18
 */
@Service
public class TMingProductServiceImpl extends ServiceImpl<TMingProductMapper,TMingProduct> implements ITMingProductService
{
    @Autowired
    private TMingProductMapper tMingProductMapper;

    /**
     * 查询mingProduct
     * 
     * @param id mingProduct主键
     * @return mingProduct
     */
    @Override
    public TMingProduct selectTMingProductById(Long id)
    {
        return tMingProductMapper.selectTMingProductById(id);
    }

    /**
     * 查询mingProduct列表
     * 
     * @param tMingProduct mingProduct
     * @return mingProduct
     */
    @Override
    public List<TMingProduct> selectTMingProductList(TMingProduct tMingProduct)
    {
        return tMingProductMapper.selectTMingProductList(tMingProduct);
    }

    /**
     * 新增mingProduct
     * 
     * @param tMingProduct mingProduct
     * @return 结果
     */
    @Override
    public int insertTMingProduct(TMingProduct tMingProduct)
    {
        tMingProduct.setCreateTime(DateUtils.getNowDate());
        return tMingProductMapper.insertTMingProduct(tMingProduct);
    }

    /**
     * 修改mingProduct
     * 
     * @param tMingProduct mingProduct
     * @return 结果
     */
    @Override
    public int updateTMingProduct(TMingProduct tMingProduct)
    {
        tMingProduct.setUpdateTime(DateUtils.getNowDate());
        return tMingProductMapper.updateTMingProduct(tMingProduct);
    }

    /**
     * 批量删除mingProduct
     * 
     * @param ids 需要删除的mingProduct主键
     * @return 结果
     */
    @Override
    public int deleteTMingProductByIds(Long[] ids)
    {
        return tMingProductMapper.deleteTMingProductByIds(ids);
    }

    /**
     * 删除mingProduct信息
     * 
     * @param id mingProduct主键
     * @return 结果
     */
    @Override
    public int deleteTMingProductById(Long id)
    {
        return tMingProductMapper.deleteTMingProductById(id);
    }
}
