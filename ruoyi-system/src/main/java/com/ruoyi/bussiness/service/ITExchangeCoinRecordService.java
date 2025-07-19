package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TExchangeCoinRecord;

/**
 * 币种兑换记录Service接口
 * 
 * @author ruoyi
 * @date 2023-07-07
 */
public interface ITExchangeCoinRecordService extends IService<TExchangeCoinRecord>
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
     * 批量删除币种兑换记录
     * 
     * @param ids 需要删除的币种兑换记录主键集合
     * @return 结果
     */
    public int deleteTExchangeCoinRecordByIds(Long[] ids);

    /**
     * 删除币种兑换记录信息
     * 
     * @param id 币种兑换记录主键
     * @return 结果
     */
    public int deleteTExchangeCoinRecordById(Long id);

    Integer countBySubmittedRecord(Long userId, String fromSymbol, String toSymbol);

    int insertRecord(TAppUser user, Map<String, Object> params);

    Map<String, Object> getCurrencyPrice(String[] currency);

    List<TExchangeCoinRecord> getListByLimit(int size);
}
