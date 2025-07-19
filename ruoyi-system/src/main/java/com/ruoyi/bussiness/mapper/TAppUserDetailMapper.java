package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.bussiness.domain.TAppUserDetail;

import java.util.List;

/**
 * 用户详细信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
public interface TAppUserDetailMapper extends BaseMapper<TAppUserDetail>
{
    /**
     * 查询用户详细信息
     * 
     * @param id 用户详细信息主键
     * @return 用户详细信息
     */
    public TAppUserDetail selectTAppUserDetailById(Long id);
    public TAppUserDetail selectTAppUserDetailByUserId(Long id);

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

    int resetAppUserRealMameStatus(TAppUserDetail userDetail);
    /**
     * 删除用户详细信息
     * 
     * @param id 用户详细信息主键
     * @return 结果
     */
    public int deleteTAppUserDetailById(Long id);

    /**
     * 批量删除用户详细信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppUserDetailByIds(Long[] ids);

    List<TAppUserDetail> selectTAppUserDetailLists(TAppUserDetail tAppUserDetail);

    List<TAppUserDetail> selectVerifiedVoice(String parentId);

}
