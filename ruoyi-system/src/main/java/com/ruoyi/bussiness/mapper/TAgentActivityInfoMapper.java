package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TAgentActivityInfo;
import com.ruoyi.bussiness.domain.vo.TAgentActivityInfoVo;

/**
 * 返利活动明细Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-06
 */
public interface TAgentActivityInfoMapper extends BaseMapper<TAgentActivityInfo>
{
    /**
     * 查询返利活动明细
     * 
     * @param id 返利活动明细主键
     * @return 返利活动明细
     */
    public TAgentActivityInfo selectTAgentActivityInfoById(String id);

    /**
     * 查询返利活动明细列表
     * 
     * @param tAgentActivityInfo 返利活动明细
     * @return 返利活动明细集合
     */
    public List<TAgentActivityInfo> selectTAgentActivityInfoList(TAgentActivityInfo tAgentActivityInfo);

    /**
     * 新增返利活动明细
     * 
     * @param tAgentActivityInfo 返利活动明细
     * @return 结果
     */
    public int insertTAgentActivityInfo(TAgentActivityInfo tAgentActivityInfo);

    /**
     * 修改返利活动明细
     * 
     * @param tAgentActivityInfo 返利活动明细
     * @return 结果
     */
    public int updateTAgentActivityInfo(TAgentActivityInfo tAgentActivityInfo);

    /**
     * 删除返利活动明细
     * 
     * @param id 返利活动明细主键
     * @return 结果
     */
    public int deleteTAgentActivityInfoById(String id);

    /**
     * 批量删除返利活动明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAgentActivityInfoByIds(String[] ids);

    int selectListByLeve(TAgentActivityInfo tAgentActivityInfo);

    BigDecimal selectAmountCountByUserId(TAgentActivityInfo tAgentActivityInfo);

    List<TAgentActivityInfoVo> getAgentList(TAgentActivityInfo tAgentActivityInfo);
}
