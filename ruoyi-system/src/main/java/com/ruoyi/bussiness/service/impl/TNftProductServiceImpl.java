package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TNftProductMapper;
import com.ruoyi.bussiness.domain.TNftProduct;
import com.ruoyi.bussiness.service.ITNftProductService;

/**
 * nft详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
@Service
public class TNftProductServiceImpl extends ServiceImpl<TNftProductMapper,TNftProduct> implements ITNftProductService
{
    @Autowired
    private TNftProductMapper tNftProductMapper;

    /**
     * 查询nft详情
     * 
     * @param id nft详情主键
     * @return nft详情
     */
    @Override
    public TNftProduct selectTNftProductById(Long id)
    {
        return tNftProductMapper.selectTNftProductById(id);
    }

    /**
     * 查询nft详情列表
     * 
     * @param tNftProduct nft详情
     * @return nft详情
     */
    @Override
    public List<TNftProduct> selectTNftProductList(TNftProduct tNftProduct)
    {
        return tNftProductMapper.selectTNftProductList(tNftProduct);
    }

    /**
     * 新增nft详情
     * 
     * @param tNftProduct nft详情
     * @return 结果
     */
    @Override
    public int insertTNftProduct(TNftProduct tNftProduct)
    {
        tNftProduct.setCreateTime(DateUtils.getNowDate());
        tNftProduct.setUpdateTime(DateUtils.getNowDate());
        tNftProduct.setCreateBy(SecurityUtils.getUsername());
        tNftProduct.setUpdateBy(SecurityUtils.getUsername());
        tNftProduct.setDelFlag("0");
        tNftProduct.setStatus(1);
        tNftProduct.setSaleStatus("0");
        return tNftProductMapper.insertTNftProduct(tNftProduct);
    }

    /**
     * 修改nft详情
     * 
     * @param tNftProduct nft详情
     * @return 结果
     */
    @Override
    public int updateTNftProduct(TNftProduct tNftProduct)
    {
        tNftProduct.setUpdateTime(DateUtils.getNowDate());
        return tNftProductMapper.updateTNftProduct(tNftProduct);
    }

    /**
     * 批量删除nft详情
     * 
     * @param ids 需要删除的nft详情主键
     * @return 结果
     */
    @Override
    public int deleteTNftProductByIds(Long[] ids)
    {
        return tNftProductMapper.deleteTNftProductByIds(ids);
    }

    /**
     * 删除nft详情信息
     * 
     * @param id nft详情主键
     * @return 结果
     */
    @Override
    public int deleteTNftProductById(Long id)
    {
        return tNftProductMapper.deleteTNftProductById(id);
    }
}
