package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ruoyi.bussiness.domain.TAgentActivityInfo;
import com.ruoyi.bussiness.domain.vo.TAgentActivityInfoVo;
import com.ruoyi.bussiness.mapper.TAgentActivityInfoMapper;
import com.ruoyi.bussiness.service.ITAgentActivityInfoService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 返利活动明细Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-06
 */
@Service
public class TAgentActivityInfoServiceImpl extends ServiceImpl<TAgentActivityInfoMapper, TAgentActivityInfo> implements ITAgentActivityInfoService
{
    @Autowired
    private TAgentActivityInfoMapper tAgentActivityInfoMapper;

    /**
     * 查询返利活动明细
     * 
     * @param id 返利活动明细主键
     * @return 返利活动明细
     */
    @Override
    public TAgentActivityInfo selectTAgentActivityInfoById(String id)
    {
        return tAgentActivityInfoMapper.selectTAgentActivityInfoById(id);
    }

    /**
     * 查询返利活动明细列表
     * 
     * @param tAgentActivityInfo 返利活动明细
     * @return 返利活动明细
     */
    @Override
    public List<TAgentActivityInfo> selectTAgentActivityInfoList(TAgentActivityInfo tAgentActivityInfo)
    {
        return tAgentActivityInfoMapper.selectTAgentActivityInfoList(tAgentActivityInfo);
    }

    /**
     * 新增返利活动明细
     * 
     * @param tAgentActivityInfo 返利活动明细
     * @return 结果
     */
    @Override
    public int insertTAgentActivityInfo(TAgentActivityInfo tAgentActivityInfo)
    {
        tAgentActivityInfo.setCreateTime(DateUtils.getNowDate());
        return tAgentActivityInfoMapper.insertTAgentActivityInfo(tAgentActivityInfo);
    }

    /**
     * 修改返利活动明细
     * 
     * @param tAgentActivityInfo 返利活动明细
     * @return 结果
     */
    @Override
    public int updateTAgentActivityInfo(TAgentActivityInfo tAgentActivityInfo)
    {
        tAgentActivityInfo.setUpdateTime(DateUtils.getNowDate());
        return tAgentActivityInfoMapper.updateTAgentActivityInfo(tAgentActivityInfo);
    }

    /**
     * 批量删除返利活动明细
     * 
     * @param ids 需要删除的返利活动明细主键
     * @return 结果
     */
    @Override
    public int deleteTAgentActivityInfoByIds(String[] ids)
    {
        return tAgentActivityInfoMapper.deleteTAgentActivityInfoByIds(ids);
    }

    /**
     * 删除返利活动明细信息
     * 
     * @param id 返利活动明细主键
     * @return 结果
     */
    @Override
    public int deleteTAgentActivityInfoById(String id)
    {
        return tAgentActivityInfoMapper.deleteTAgentActivityInfoById(id);
    }

    @Override
    public int selectListByLeve(TAgentActivityInfo tAgentActivityInfo) {
        return tAgentActivityInfoMapper.selectListByLeve(tAgentActivityInfo);
    }

    @Override
    public Map<Object, Object> selectUserActivityInfo(TAgentActivityInfo tAgentActivityInfo) {
        //查询1 2 3 级
        tAgentActivityInfo.setType(1);
        tAgentActivityInfo.setStatus(2);
        Map<String, Object> params = new HashMap<>();
        params.put("leve",1);
        tAgentActivityInfo.setParams(params);
        int oneCount = tAgentActivityInfoMapper.selectListByLeve(tAgentActivityInfo);
        params.put("leve",2);
        int twoCount = tAgentActivityInfoMapper.selectListByLeve(tAgentActivityInfo);
        params.put("leve",3);
        int threeCount = tAgentActivityInfoMapper.selectListByLeve(tAgentActivityInfo);
        Map<Object, Object> resultMap = new HashMap<>();
        resultMap.put("oneCount",oneCount);
        resultMap.put("twoCount",twoCount);
        resultMap.put("threeCount",threeCount);
        resultMap.put("sumCount",oneCount+twoCount+threeCount);
        BigDecimal sumAmount = tAgentActivityInfoMapper.selectAmountCountByUserId(tAgentActivityInfo);
        sumAmount = Objects.isNull(sumAmount) ? BigDecimal.ZERO : sumAmount;
        resultMap.put("sumAmount",sumAmount);
        return resultMap;
    }

    @Override
    public List<TAgentActivityInfoVo> getAgentList(TAgentActivityInfo tAgentActivityInfo) {
        return tAgentActivityInfoMapper.getAgentList(tAgentActivityInfo);
    }
}
