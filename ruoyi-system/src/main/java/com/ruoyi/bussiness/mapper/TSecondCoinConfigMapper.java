package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TSecondCoinConfig;

import java.util.List;


/**
 * 秒合约币种配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface TSecondCoinConfigMapper extends BaseMapper<TSecondCoinConfig>
{
    /**
     * 查询秒合约币种配置
     * 
     * @param id 秒合约币种配置主键
     * @return 秒合约币种配置
     */
    public TSecondCoinConfig selectTSecondCoinConfigById(Long id);

    /**
     * 查询秒合约币种配置列表
     * 
     * @param tSecondCoinConfig 秒合约币种配置
     * @return 秒合约币种配置集合
     */
    public List<TSecondCoinConfig> selectTSecondCoinConfigList(TSecondCoinConfig tSecondCoinConfig);

    /**
     * 新增秒合约币种配置
     * 
     * @param tSecondCoinConfig 秒合约币种配置
     * @return 结果
     */
    public int insertTSecondCoinConfig(TSecondCoinConfig tSecondCoinConfig);

    /**
     * 修改秒合约币种配置
     * 
     * @param tSecondCoinConfig 秒合约币种配置
     * @return 结果
     */
    public int updateTSecondCoinConfig(TSecondCoinConfig tSecondCoinConfig);

    /**
     * 删除秒合约币种配置
     * 
     * @param id 秒合约币种配置主键
     * @return 结果
     */
    public int deleteTSecondCoinConfigById(Long id);

    /**
     * 批量删除秒合约币种配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSecondCoinConfigByIds(Long[] ids);

    List<TSecondCoinConfig> selectBathCopySecondCoinConfigList();
}
