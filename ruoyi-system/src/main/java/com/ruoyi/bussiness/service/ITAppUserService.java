package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TAppUserDetail;
import com.ruoyi.bussiness.domain.TMineOrder;

import javax.servlet.http.HttpServletRequest;

/**
 * 玩家用户Service接口
 * 
 * @author ruoyi
 * @date 2023-06-30
 */
public interface ITAppUserService extends IService<TAppUser>
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
    public int insertTAppUser(TAppUser tAppUser,TAppUser user);

    /**
     * 修改玩家用户
     * 
     * @param tAppUser 玩家用户
     * @return 结果
     */
    public int updateTAppUser(TAppUser tAppUser);

    /**
     * 批量删除玩家用户
     * 
     * @param userIds 需要删除的玩家用户主键集合
     * @return 结果
     */
    public int deleteTAppUserByUserIds(Long[] userIds);

    /**
     * 删除玩家用户信息
     * 
     * @param userId 玩家用户主键
     * @return 结果
     */
    public int deleteTAppUserByUserId(Long userId);

    /**
     * 获取用户详情
     */
    TAppUserDetail selectUserDetailByUserId(Long userId);

    int checkEmailUnique(String email);
    boolean checkPhoneExist(String phone);

    TAppUser selectByUserLoginName(String email);

    TAppUser selectByAddress(String address);

    TAppUser login(TAppUser tAppUser);

    TAppUser selectByActiveCode(String activeCode);

    List<String> selectActiveCodeList();

    String backPwd(String email, String emailCode, String newPwd);

    List<TAppUser> selectUnboundAppUser(TAppUser tAppUser);

    void sendEmailCode(String type, String email);

    String bindEmail(String email, String emailCode, HttpServletRequest request);


    String bindWalletAddress(String address);

    String bindPhone(String phone,String code);
    void uploadKYC(TAppUser appUser, String realName, String flag, String idCard, String frontUrl, String backUrl, String handelUrl, String country, String cardType);

    Map<String,Object> getInfo(long loginIdAsLong);

    int delUpdateByAdminUserId(Long userId);

    String updatePwd(String oldPwd, String newPwd, String signType, String emailOrPhone, String code);

    void toBuilderTeamAmount(TMineOrder mineOrder);

    String updatePwdByEmail(String email, String emailCode, String newPwd);

    void realName(TAppUserDetail tAppUserDetail);

    int addTAppUser(TAppUser tAppUser);

    List<TAppUser> getTAppUserList(TAppUser tAppUser);

    int updateUserAppIds(Long appUserId, Long agentUserId);

    void reSetRealName(TAppUserDetail tAppUserDetail);

    List<TAppUser> getListByPledge(TAppUser tAppUser);

    int updateBlackStatus(TAppUser tAppUser);

    int updateLimitationStatus(TAppUser tAppUser);

    int updateTotleAmont(TAppUser appUser);
}
