package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.bussiness.domain.TAppUserDetail;

import java.util.List;

/**
 * 用户详细信息Service接口
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
public interface ITAppUserDetailService extends IService<TAppUserDetail>
{
    /**
     * 查询用户详细信息
     * 
     * @param id 用户详细信息主键
     * @return 用户详细信息
     */
    public TAppUserDetail selectTAppUserDetailById(Long id);

    /**
     * 查询用户详细信息列表
     * 
     * @param tAppUserDetail 用户详细信息
     * @return 用户详细信息集合
     */
    public List<TAppUserDetail> selectTAppUserDetailList(TAppUserDetail tAppUserDetail);

    /**
     * 新增用户详细信息
     * 
     * @param tAppUserDetail 用户详细信息
     * @return 结果
     */
    public int insertTAppUserDetail(TAppUserDetail tAppUserDetail);

    /**
     * 修改用户详细信息
     * 
     * @param tAppUserDetail 用户详细信息
     * @return 结果
     */
    public int updateTAppUserDetail(TAppUserDetail tAppUserDetail);

    /**
     * 批量删除用户详细信息
     * 
     * @param ids 需要删除的用户详细信息主键集合
     * @return 结果
     */
    public int deleteTAppUserDetailByIds(Long[] ids);

    /**
     * 删除用户详细信息信息
     * 
     * @param id 用户详细信息主键
     * @return 结果
     */
    public int deleteTAppUserDetailById(Long id);

    List<TAppUserDetail> selectTAppUserDetailLists(TAppUserDetail tAppUserDetail);
}
