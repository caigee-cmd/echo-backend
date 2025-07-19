package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.ruoyi.bussiness.domain.TUserSymbolAddress;

/**
 * 用户币种充值地址Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-12
 */
public interface TUserSymbolAddressMapper extends BaseMapper<TUserSymbolAddress>
{
    /**
     * 查询用户币种充值地址
     * 
     * @param id 用户币种充值地址主键
     * @return 用户币种充值地址
     */
    public TUserSymbolAddress selectTUserSymbolAddressById(Long id);

    /**
     * 查询用户币种充值地址列表
     * 
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 用户币种充值地址集合
     */
    public List<TUserSymbolAddress> selectTUserSymbolAddressList(TUserSymbolAddress tUserSymbolAddress);

    /**
     * 新增用户币种充值地址
     * 
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 结果
     */
    public int insertTUserSymbolAddress(TUserSymbolAddress tUserSymbolAddress);

    /**
     * 修改用户币种充值地址
     * 
     * @param tUserSymbolAddress 用户币种充值地址
     * @return 结果
     */
    public int updateTUserSymbolAddress(TUserSymbolAddress tUserSymbolAddress);

    /**
     * 删除用户币种充值地址
     * 
     * @param id 用户币种充值地址主键
     * @return 结果
     */
    public int deleteTUserSymbolAddressById(Long id);

    /**
     * 批量删除用户币种充值地址
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTUserSymbolAddressByIds(Long[] ids);
}
