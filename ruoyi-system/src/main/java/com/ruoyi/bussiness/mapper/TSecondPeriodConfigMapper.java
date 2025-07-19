package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.bussiness.domain.TSecondPeriodConfig;

/**
 * 秒合约币种周期配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface TSecondPeriodConfigMapper extends BaseMapper<TSecondPeriodConfig>
{
    /**
     * 查询秒合约币种周期配置
     * 
     * @param id 秒合约币种周期配置主键
     * @return 秒合约币种周期配置
     */
    public TSecondPeriodConfig selectTSecondPeriodConfigById(Long id);

    /**
     * 查询秒合约币种周期配置列表
     * 
     * @param tSecondPeriodConfig 秒合约币种周期配置
     * @return 秒合约币种周期配置集合
     */
    public List<TSecondPeriodConfig> selectTSecondPeriodConfigList(TSecondPeriodConfig tSecondPeriodConfig);

    /**
     * 新增秒合约币种周期配置
     * 
     * @param tSecondPeriodConfig 秒合约币种周期配置
     * @return 结果
     */
    public int insertTSecondPeriodConfig(TSecondPeriodConfig tSecondPeriodConfig);

    /**
     * 修改秒合约币种周期配置
     * 
     * @param tSecondPeriodConfig 秒合约币种周期配置
     * @return 结果
     */
    public int updateTSecondPeriodConfig(TSecondPeriodConfig tSecondPeriodConfig);

    /**
     * 删除秒合约币种周期配置
     * 
     * @param id 秒合约币种周期配置主键
     * @return 结果
     */
    public int deleteTSecondPeriodConfigById(Long id);

    /**
     * 批量删除秒合约币种周期配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSecondPeriodConfigByIds(Long[] ids);
}
