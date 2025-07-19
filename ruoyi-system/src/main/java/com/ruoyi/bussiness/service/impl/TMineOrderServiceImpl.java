package com.ruoyi.bussiness.service.impl;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Objects;

import com.ruoyi.bussiness.domain.TMineOrder;
import com.ruoyi.bussiness.domain.setting.FinancialSettlementSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.mapper.TMineOrderMapper;
import com.ruoyi.bussiness.service.ITMineOrderService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.enums.CommonEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
@Service
public class TMineOrderServiceImpl extends ServiceImpl<TMineOrderMapper, TMineOrder> implements ITMineOrderService
{
    @Resource
    private TMineOrderMapper tMineOrderMapper;
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TMineOrder selectTMineOrderById(Long id)
    {
        return tMineOrderMapper.selectTMineOrderById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tMineOrder 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TMineOrder> selectTMineOrderList(TMineOrder tMineOrder)
    {
        return tMineOrderMapper.selectTMineOrderList(tMineOrder);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param tMineOrder 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTMineOrder(TMineOrder tMineOrder)
    {
        tMineOrder.setCreateTime(DateUtils.getNowDate());
        return tMineOrderMapper.insertTMineOrder(tMineOrder);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param tMineOrder 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTMineOrder(TMineOrder tMineOrder)
    {
        tMineOrder.setUpdateTime(DateUtils.getNowDate());
        return tMineOrderMapper.updateTMineOrder(tMineOrder);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineOrderByIds(Long[] ids)
    {
        return tMineOrderMapper.deleteTMineOrderByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTMineOrderById(Long id)
    {
        return tMineOrderMapper.deleteTMineOrderById(id);
    }
}
