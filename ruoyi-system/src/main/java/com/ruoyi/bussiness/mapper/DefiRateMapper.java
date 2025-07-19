package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.bussiness.domain.DefiRate;

/**
 * defi挖矿利率配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
public interface DefiRateMapper extends BaseMapper<DefiRate>
{
    /**
     * 查询defi挖矿利率配置
     * 
     * @param id defi挖矿利率配置主键
     * @return defi挖矿利率配置
     */
    public DefiRate selectDefiRateById(Long id);

    /**
     * 查询defi挖矿利率配置列表
     * 
     * @param defiRate defi挖矿利率配置
     * @return defi挖矿利率配置集合
     */
    public List<DefiRate> selectDefiRateList(DefiRate defiRate);

    /**
     * 新增defi挖矿利率配置
     * 
     * @param defiRate defi挖矿利率配置
     * @return 结果
     */
    public int insertDefiRate(DefiRate defiRate);

    /**
     * 修改defi挖矿利率配置
     * 
     * @param defiRate defi挖矿利率配置
     * @return 结果
     */
    public int updateDefiRate(DefiRate defiRate);

    /**
     * 删除defi挖矿利率配置
     * 
     * @param id defi挖矿利率配置主键
     * @return 结果
     */
    public int deleteDefiRateById(Long id);

    /**
     * 批量删除defi挖矿利率配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDefiRateByIds(Long[] ids);

    public List<DefiRate> selectAllOrderBy();
    public List<DefiRate> getDefiRateByAmount(BigDecimal amount);
}
