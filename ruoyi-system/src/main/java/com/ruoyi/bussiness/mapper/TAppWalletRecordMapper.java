package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.bussiness.domain.TAppWalletRecord;
import com.ruoyi.bussiness.domain.vo.AgencyAppUserDataVo;
import com.ruoyi.bussiness.domain.vo.AgencyDataVo;
import com.ruoyi.bussiness.domain.vo.DailyDataVO;
import com.ruoyi.bussiness.domain.vo.UserDataVO;

/**
 * 用户信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
public interface TAppWalletRecordMapper extends BaseMapper<TAppWalletRecord>
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
     * 删除用户信息
     * 
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteTAppWalletRecordById(Long id);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppWalletRecordByIds(Long[] ids);


    List<DailyDataVO> dailyData(TAppWalletRecord tAppWalletRecord);

   List<UserDataVO> selectUserDataList(TAppWalletRecord tAppWalletRecord);

    List<AgencyDataVo> selectAgencyList(TAppWalletRecord appWalletRecord);

    List<AgencyAppUserDataVo> selectAgencyAppUserList(TAppWalletRecord appWalletRecord);

    BigDecimal statisticsAmount();

    BigDecimal getAllWithdrawOfUser(Long userId);
}
