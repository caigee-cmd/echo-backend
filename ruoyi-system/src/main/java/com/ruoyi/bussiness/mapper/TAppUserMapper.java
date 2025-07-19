package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.bussiness.domain.TAppUser;
import org.apache.ibatis.annotations.Param;

/**
 * 玩家用户Mapper接口
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
public interface TAppUserMapper extends BaseMapper<TAppUser>
{
    /**
     * 查询玩家用户
     * 
     * @param userId 玩家用户主键
     * @return 玩家用户
     */
    public TAppUser selectTAppUserByUserId(Long userId);

    /**
     * 查询玩家用户列表
     * 
     * @param tAppUser 玩家用户
     * @return 玩家用户集合
     */
    public List<TAppUser> selectTAppUserList(TAppUser tAppUser);

    /**
     * 新增玩家用户
     * 
     * @param tAppUser 玩家用户
     * @return 结果
     */
    public int insertTAppUser(TAppUser tAppUser);

    /**
     * 修改玩家用户
     * 
     * @param tAppUser 玩家用户
     * @return 结果
     */
    public int updateTAppUser(TAppUser tAppUser);

    /**
     * 删除玩家用户
     * 
     * @param userId 玩家用户主键
     * @return 结果
     */
    public int deleteTAppUserByUserId(Long userId);

    /**
     * 批量删除玩家用户
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppUserByUserIds(Long[] userIds);

    TAppUser selectByUserLoginName(String loginName);

    TAppUser selectByAddress(String address);

    List<String> selectActiveCodeList();
    List<TAppUser> selectUnboundAppUser(TAppUser tAppUser);

    int delUpdateByAdminUserId(Long adminUserId);

    List<TAppUser> getTAppUserList(TAppUser tAppUser);

    int updateUserAppIds(@Param("appUserId") Long appUserId, @Param("adminParentIds") String adminParentIds);

    List<TAppUser> getListByPledge(TAppUser tAppUser);

    int updateTotleAmont(TAppUser appUser);

    int updateRechargeAmont(TAppUser user);
}
