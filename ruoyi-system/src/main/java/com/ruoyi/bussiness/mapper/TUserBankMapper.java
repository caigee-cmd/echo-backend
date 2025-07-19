package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TUserBank;

/**
 * 银行卡Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-21
 */
public interface TUserBankMapper extends BaseMapper<TUserBank>
{
    /**
     * 查询银行卡
     * 
     * @param id 银行卡主键
     * @return 银行卡
     */
    public TUserBank selectTUserBankById(Long id);

    /**
     * 查询银行卡列表
     * 
     * @param tUserBank 银行卡
     * @return 银行卡集合
     */
    public List<TUserBank> selectTUserBankList(TUserBank tUserBank);

    /**
     * 新增银行卡
     * 
     * @param tUserBank 银行卡
     * @return 结果
     */
    public int insertTUserBank(TUserBank tUserBank);

    /**
     * 修改银行卡
     * 
     * @param tUserBank 银行卡
     * @return 结果
     */
    public int updateTUserBank(TUserBank tUserBank);

    /**
     * 删除银行卡
     * 
     * @param id 银行卡主键
     * @return 结果
     */
    public int deleteTUserBankById(Long id);

    /**
     * 批量删除银行卡
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTUserBankByIds(Long[] ids);
}
