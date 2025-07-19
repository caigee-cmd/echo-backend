package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TAppAddressInfo;

import java.util.List;


/**
 * 钱包地址授权详情Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-15
 */
public interface TAppAddressInfoMapper extends BaseMapper<TAppAddressInfo>
{
    /**
     * 查询钱包地址授权详情
     * 
     * @param userId 钱包地址授权详情主键
     * @return 钱包地址授权详情
     */
    public TAppAddressInfo selectTAppAddressInfoByUserId(Long userId);

    /**
     * 查询钱包地址授权详情列表
     * 
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 钱包地址授权详情集合
     */
    public List<TAppAddressInfo> selectTAppAddressInfoList(TAppAddressInfo tAppAddressInfo);

    /**
     * 新增钱包地址授权详情
     * 
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 结果
     */
    public int insertTAppAddressInfo(TAppAddressInfo tAppAddressInfo);

    /**
     * 修改钱包地址授权详情
     * 
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 结果
     */
    public int updateTAppAddressInfo(TAppAddressInfo tAppAddressInfo);

    /**
     * 删除钱包地址授权详情
     * 
     * @param userId 钱包地址授权详情主键
     * @return 结果
     */
    public int deleteTAppAddressInfoByUserId(Long userId);

    /**
     * 批量删除钱包地址授权详情
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppAddressInfoByUserIds(Long[] userIds);

    List<TAppAddressInfo> getAllowedUser();
}
