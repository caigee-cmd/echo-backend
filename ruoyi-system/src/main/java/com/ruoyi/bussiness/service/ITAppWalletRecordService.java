package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAppWalletRecord;
import com.ruoyi.bussiness.domain.vo.AgencyAppUserDataVo;
import com.ruoyi.bussiness.domain.vo.AgencyDataVo;
import com.ruoyi.bussiness.domain.vo.DailyDataVO;
import com.ruoyi.bussiness.domain.vo.UserDataVO;

/**
 * 用户信息Service接口
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
public interface ITAppWalletRecordService extends IService<TAppWalletRecord>
{
    /**
     * 查询用户信息
     * 
     * @param id 用户信息主键
     * @return 用户信息
     */
    public TAppWalletRecord selectTAppWalletRecordById(Long id);

    /**
     * 查询用户信息列表
     * 
     * @param tAppWalletRecord 用户信息
     * @return 用户信息集合
     */
    public List<TAppWalletRecord> selectTAppWalletRecordList(TAppWalletRecord tAppWalletRecord);

    /**
     * 新增用户信息
     * 
     * @param tAppWalletRecord 用户信息
     * @return 结果
     */
    public int insertTAppWalletRecord(TAppWalletRecord tAppWalletRecord);

    /**
     * 修改用户信息
     * 
     * @param tAppWalletRecord 用户信息
     * @return 结果
     */
    public int updateTAppWalletRecord(TAppWalletRecord tAppWalletRecord);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteTAppWalletRecordByIds(Long[] ids);

    /**
     * 删除用户信息信息
     * 
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteTAppWalletRecordById(Long id);

    void generateRecord(Long userId, BigDecimal amount, int type, String createBy, String serialId, String remark, BigDecimal before, BigDecimal after,String coin,String adminParentIds);

    List<UserDataVO> selectUserDataList(TAppWalletRecord tAppWalletRecord);

    List<AgencyDataVo> selectAgencyList(TAppWalletRecord appWalletRecord);

    List<DailyDataVO> dailyData(TAppWalletRecord appWalletRecord);

    List<AgencyAppUserDataVo> selectAgencyAppUserList(TAppWalletRecord appWalletRecord);

    Map<String,BigDecimal> statisticsAmount();
}
