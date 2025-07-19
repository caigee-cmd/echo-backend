package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.bussiness.domain.TExchangeCoinRecord;

/**
 * 币种兑换记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-07
 */
public interface TExchangeCoinRecordMapper extends BaseMapper<TExchangeCoinRecord>
{
    /**
     * 查询币种兑换记录
     * 
     * @param id 币种兑换记录主键
     * @return 币种兑换记录
     */
    public TExchangeCoinRecord selectTExchangeCoinRecordById(Long id);

    /**
     * 查询币种兑换记录列表
     * 
     * @param tExchangeCoinRecord 币种兑换记录
     * @return 币种兑换记录集合
     */
    public List<TExchangeCoinRecord> selectTExchangeCoinRecordList(TExchangeCoinRecord tExchangeCoinRecord);

    /**
     * 新增币种兑换记录
     * 
     * @param tExchangeCoinRecord 币种兑换记录
     * @return 结果
     */
    public int insertTExchangeCoinRecord(TExchangeCoinRecord tExchangeCoinRecord);

    /**
     * 修改币种兑换记录
     * 
     * @param tExchangeCoinRecord 币种兑换记录
     * @return 结果
     */
    public int updateTExchangeCoinRecord(TExchangeCoinRecord tExchangeCoinRecord);

    /**
     * 删除币种兑换记录
     * 
     * @param id 币种兑换记录主键
     * @return 结果
     */
    public int deleteTExchangeCoinRecordById(Long id);

    /**
     * 批量删除币种兑换记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTExchangeCoinRecordByIds(Long[] ids);

    Integer countByExchangeCoinRecord(TExchangeCoinRecord exchangeCoinRecord);

    List<TExchangeCoinRecord> getListByLimit(int size);
}
