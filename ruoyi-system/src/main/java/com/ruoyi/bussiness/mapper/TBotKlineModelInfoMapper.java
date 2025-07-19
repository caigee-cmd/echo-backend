package com.ruoyi.bussiness.mapper;

import cc.block.data.api.domain.market.Kline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.TBotKlineModel;
import com.ruoyi.bussiness.domain.TBotKlineModelInfo;

/**
 * 控线详情Mapper接口
 *
 * @author ruoyi
 * @date 2023-08-09
 */
public interface TBotKlineModelInfoMapper extends BaseMapper<TBotKlineModelInfo> {
    /**
     * 查询控线详情
     *
     * @param id 控线详情主键
     * @return 控线详情
     */
    public TBotKlineModelInfo selectTBotKlineModelInfoById(Long id);

    /**
     * 查询控线详情列表
     *
     * @param tBotKlineModelInfo 控线详情
     * @return 控线详情集合
     */
    public List<TBotKlineModelInfo> selectTBotKlineModelInfoList(TBotKlineModelInfo tBotKlineModelInfo);

    /**
     * 新增控线详情
     *
     * @param tBotKlineModelInfo 控线详情
     * @return 结果
     */
    public int insertTBotKlineModelInfo(TBotKlineModelInfo tBotKlineModelInfo);

    /**
     * 修改控线详情
     *
     * @param tBotKlineModelInfo 控线详情
     * @return 结果
     */
    public int updateTBotKlineModelInfo(TBotKlineModelInfo tBotKlineModelInfo);

    /**
     * 删除控线详情
     *
     * @param id 控线详情主键
     * @return 结果
     */
    public int deleteTBotKlineModelInfoById(Long id);

    /**
     * 批量删除控线详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTBotKlineModelInfoByIds(Long[] ids);

    int insertModelInfo(List<TBotKlineModelInfo> list);

    List<TBotKlineModelInfo> selectBotLineList(Map<String, Object> map);

}
