package com.ruoyi.bussiness.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.domain.TExchangeCoinRecord;

/**
 * U本位合约币种Mapper接口
 * 
 * @author michael
 * @date 2023-07-20
 */
public interface TContractCoinMapper extends BaseMapper<TContractCoin>
{
    /**
     * 查询U本位合约币种
     * 
     * @param id U本位合约币种主键
     * @return U本位合约币种
     */
    public TContractCoin selectTContractCoinById(Long id);

    /**
     * 查询U本位合约币种列表
     * 
     * @param tContractCoin U本位合约币种
     * @return U本位合约币种集合
     */
    public List<TContractCoin> selectTContractCoinList(TContractCoin tContractCoin);

    /**
     * 新增U本位合约币种
     * 
     * @param tContractCoin U本位合约币种
     * @return 结果
     */
    public int insertTContractCoin(TContractCoin tContractCoin);

    /**
     * 修改U本位合约币种
     * 
     * @param tContractCoin U本位合约币种
     * @return 结果
     */
    public int updateTContractCoin(TContractCoin tContractCoin);

    /**
     * 删除U本位合约币种
     * 
     * @param id U本位合约币种主键
     * @return 结果
     */
    public int deleteTContractCoinById(Long id);

    /**
     * 批量删除U本位合约币种
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTContractCoinByIds(Long[] ids);
}
