package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.bussiness.domain.TActivityRecharge;

/**
 * 充值活动Service接口
 * 
 * @author ruoyi
 * @date 2023-07-05
 */
public interface ITActivityRechargeService extends IService<TActivityRecharge>
{
    /**
     * 查询充值活动
     * 
     * @param id 充值活动主键
     * @return 充值活动
     */
    public TActivityRecharge selectTActivityRechargeById(Long id);

    /**
     * 查询充值活动列表
     * 
     * @param tActivityRecharge 充值活动
     * @return 充值活动集合
     */
    public List<TActivityRecharge> selectTActivityRechargeList(TActivityRecharge tActivityRecharge);

    /**
     * 新增充值活动
     * 
     * @param tActivityRecharge 充值活动
     * @return 结果
     */
    public int insertTActivityRecharge(TActivityRecharge tActivityRecharge);

    /**
     * 修改充值活动
     * 
     * @param tActivityRecharge 充值活动
     * @return 结果
     */
    public int updateTActivityRecharge(TActivityRecharge tActivityRecharge);

    /**
     * 批量删除充值活动
     * 
     * @param ids 需要删除的充值活动主键集合
     * @return 结果
     */
    public int deleteTActivityRechargeByIds(Long[] ids);

    /**
     * 删除充值活动信息
     * 
     * @param id 充值活动主键
     * @return 结果
     */
    public int deleteTActivityRechargeById(Long id);

    void caseBackToFather(Long userId, BigDecimal realAmount, String type, String username, String serialId);
}
