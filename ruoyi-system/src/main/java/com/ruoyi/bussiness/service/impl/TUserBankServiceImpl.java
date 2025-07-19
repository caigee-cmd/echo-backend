package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TUserBankMapper;
import com.ruoyi.bussiness.domain.TUserBank;
import com.ruoyi.bussiness.service.ITUserBankService;

/**
 * 银行卡Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-21
 */
@Service
public class TUserBankServiceImpl extends ServiceImpl<TUserBankMapper,TUserBank> implements ITUserBankService
{
    @Autowired
    private TUserBankMapper tUserBankMapper;

    /**
     * 查询银行卡
     * 
     * @param id 银行卡主键
     * @return 银行卡
     */
    @Override
    public TUserBank selectTUserBankById(Long id)
    {
        return tUserBankMapper.selectTUserBankById(id);
    }

    /**
     * 查询银行卡列表
     * 
     * @param tUserBank 银行卡
     * @return 银行卡
     */
    @Override
    public List<TUserBank> selectTUserBankList(TUserBank tUserBank)
    {
        return tUserBankMapper.selectTUserBankList(tUserBank);
    }

    /**
     * 新增银行卡
     * 
     * @param tUserBank 银行卡
     * @return 结果
     */
    @Override
    public int insertTUserBank(TUserBank tUserBank)
    {
        tUserBank.setCreateTime(DateUtils.getNowDate());
        return tUserBankMapper.insertTUserBank(tUserBank);
    }

    /**
     * 修改银行卡
     * 
     * @param tUserBank 银行卡
     * @return 结果
     */
    @Override
    public int updateTUserBank(TUserBank tUserBank)
    {
        tUserBank.setUpdateTime(DateUtils.getNowDate());
        return tUserBankMapper.updateTUserBank(tUserBank);
    }

    /**
     * 批量删除银行卡
     * 
     * @param ids 需要删除的银行卡主键
     * @return 结果
     */
    @Override
    public int deleteTUserBankByIds(Long[] ids)
    {
        return tUserBankMapper.deleteTUserBankByIds(ids);
    }

    /**
     * 删除银行卡信息
     * 
     * @param id 银行卡主键
     * @return 结果
     */
    @Override
    public int deleteTUserBankById(Long id)
    {
        return tUserBankMapper.deleteTUserBankById(id);
    }
}
