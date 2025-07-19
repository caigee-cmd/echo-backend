package com.ruoyi.bussiness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAppRecharge;
import org.apache.ibatis.annotations.Param;

/**
 * 用户充值Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
public interface TAppRechargeMapper extends BaseMapper<TAppRecharge>
{
    /**
     * 查询用户充值
     * 
     * @param id 用户充值主键
     * @return 用户充值
     */
    public TAppRecharge selectTAppRechargeById(Long id);

    /**
     * 查询用户充值列表
     * 
     * @param tAppRecharge 用户充值
     * @return 用户充值集合
     */
    public List<TAppRecharge> selectTAppRechargeList(TAppRecharge tAppRecharge);

    /**
     * 新增用户充值
     * 
     * @param tAppRecharge 用户充值
     * @return 结果
     */
    public int insertTAppRecharge(TAppRecharge tAppRecharge);

    /**
     * 修改用户充值
     * 
     * @param tAppRecharge 用户充值
     * @return 结果
     */
    public int updateTAppRecharge(TAppRecharge tAppRecharge);

    /**
     * 删除用户充值
     * 
     * @param id 用户充值主键
     * @return 结果
     */
    public int deleteTAppRechargeById(Long id);

    /**
     * 批量删除用户充值
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppRechargeByIds(Long[] ids);

    Map<String, Object> sumtotal(TAppRecharge tAppRecharge);

    List<TAppRecharge> selectRechargeList(TAppRecharge tAppRecharge);

    void updateStatus(TAppRecharge recharge);

    List<TAppRecharge> selectRechargeVoice(String parentId);

    BigDecimal getAllRecharge(@Param("parentId") String parentId, @Param("type") Integer type);

    List<Map<String, Object>> getWeekRecharge(String parentId);

    List<Map<String, Object>> getWeekFailedRecharge(String parentId);
}
