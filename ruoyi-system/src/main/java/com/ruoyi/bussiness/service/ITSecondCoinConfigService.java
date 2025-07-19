package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.TSecondCoinConfig;
import com.ruoyi.bussiness.domain.vo.SecondCoinCopyVO;
import com.ruoyi.bussiness.domain.vo.SymbolCoinConfigVO;

import java.util.List;
import java.util.Map;


/**
 * 秒合约币种配置Service接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface ITSecondCoinConfigService extends IService<TSecondCoinConfig>
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
    public int insertSecondCoin(TSecondCoinConfig tSecondCoinConfig);

    /**
     * 修改秒合约币种配置
     * 
     * @param tSecondCoinConfig 秒合约币种配置
     * @return 结果
     */
    public int updateTSecondCoinConfig(TSecondCoinConfig tSecondCoinConfig);

    /**
     * 批量删除秒合约币种配置
     * 
     * @param ids 需要删除的秒合约币种配置主键集合
     * @return 结果
     */
    public int deleteTSecondCoinConfigByIds(Long[] ids);

    /**
     * 删除秒合约币种配置信息
     * 
     * @param id 秒合约币种配置主键
     * @return 结果
     */
    public int deleteTSecondCoinConfigById(Long id);

    public boolean batchSave(String[] coins);

    List<SymbolCoinConfigVO>  getSymbolList();

    List<TSecondCoinConfig>  selectBathCopySecondCoinConfigList();

    int bathCopyIng(SecondCoinCopyVO secondCoinCopyVO);
}
