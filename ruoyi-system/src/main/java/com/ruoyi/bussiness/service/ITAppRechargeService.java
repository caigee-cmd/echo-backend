package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAppRecharge;
import com.ruoyi.bussiness.domain.TAppUser;

/**
 * 用户充值Service接口
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
public interface ITAppRechargeService extends IService<TAppRecharge>
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
    public int insertTAppRecharge(TAppUser tAppRecharge);

    /**
     * 修改用户充值
     * 
     * @param tAppRecharge 用户充值
     * @return 结果
     */
    public int updateTAppRecharge(TAppRecharge tAppRecharge);

    /**
     * 批量删除用户充值
     * 
     * @param ids 需要删除的用户充值主键集合
     * @return 结果
     */
    public int deleteTAppRechargeByIds(Long[] ids);

    /**
     * 删除用户充值信息
     * 
     * @param id 用户充值主键
     * @return 结果
     */
    public int deleteTAppRechargeById(Long id);

    Map<String, Object> sumtotal(TAppRecharge tAppRecharge);

    List<TAppRecharge> selectRechargeList(TAppRecharge tAppRecharge);

    String passOrder(TAppRecharge tAppRecharge);

    String failedOrder(TAppRecharge tAppRecharge);

    List<TAppRecharge> selectRechargeVoice(String parentId);

    BigDecimal getAllRecharge(String parentId, Integer type);
}
