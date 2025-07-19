package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TLoadProductMapper;
import com.ruoyi.bussiness.domain.TLoadProduct;
import com.ruoyi.bussiness.service.ITLoadProductService;

/**
 * 借贷产品Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@Service
public class TLoadProductServiceImpl extends ServiceImpl<TLoadProductMapper,TLoadProduct> implements ITLoadProductService
{
    @Autowired
    private TLoadProductMapper tLoadProductMapper;

    /**
     * 查询借贷产品
     * 
     * @param id 借贷产品主键
     * @return 借贷产品
     */
    @Override
    public TLoadProduct selectTLoadProductById(Long id)
    {
        return tLoadProductMapper.selectTLoadProductById(id);
    }

    /**
     * 查询借贷产品列表
     * 
     * @param tLoadProduct 借贷产品
     * @return 借贷产品
     */
    @Override
    public List<TLoadProduct> selectTLoadProductList(TLoadProduct tLoadProduct)
    {
        return tLoadProductMapper.selectTLoadProductList(tLoadProduct);
    }

    /**
     * 新增借贷产品
     * 
     * @param tLoadProduct 借贷产品
     * @return 结果
     */
    @Override
    public int insertTLoadProduct(TLoadProduct tLoadProduct)
    {
        tLoadProduct.setCreateTime(DateUtils.getNowDate());
        return tLoadProductMapper.insertTLoadProduct(tLoadProduct);
    }

    /**
     * 修改借贷产品
     * 
     * @param tLoadProduct 借贷产品
     * @return 结果
     */
    @Override
    public int updateTLoadProduct(TLoadProduct tLoadProduct)
    {
        tLoadProduct.setUpdateTime(DateUtils.getNowDate());
        return tLoadProductMapper.updateTLoadProduct(tLoadProduct);
    }

    /**
     * 批量删除借贷产品
     * 
     * @param ids 需要删除的借贷产品主键
     * @return 结果
     */
    @Override
    public int deleteTLoadProductByIds(Long[] ids)
    {
        return tLoadProductMapper.deleteTLoadProductByIds(ids);
    }

    /**
     * 删除借贷产品信息
     * 
     * @param id 借贷产品主键
     * @return 结果
     */
    @Override
    public int deleteTLoadProductById(Long id)
    {
        return tLoadProductMapper.deleteTLoadProductById(id);
    }
}
