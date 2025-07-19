package com.ruoyi.bussiness.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.SymbolPrice;
import com.ruoyi.bussiness.domain.TBotKlineModel;

/**
 * 控线配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-09
 */
public interface TBotKlineModelMapper extends BaseMapper<TBotKlineModel>
{
    /**
     * 查询控线配置
     * 
     * @param id 控线配置主键
     * @return 控线配置
     */
    public TBotKlineModel selectTBotKlineModelById(Long id);

    /**
     * 查询控线配置列表
     * 
     * @param tBotKlineModel 控线配置
     * @return 控线配置集合
     */
    public List<TBotKlineModel> selectTBotKlineModelList(TBotKlineModel tBotKlineModel);

    /**
     * 新增控线配置
     * 
     * @param tBotKlineModel 控线配置
     * @return 结果
     */
    public int insertTBotKlineModel(TBotKlineModel tBotKlineModel);

    /**
     * 修改控线配置
     * 
     * @param tBotKlineModel 控线配置
     * @return 结果
     */
    public int updateTBotKlineModel(TBotKlineModel tBotKlineModel);

    /**
     * 删除控线配置
     * 
     * @param id 控线配置主键
     * @return 结果
     */
    public int deleteTBotKlineModelById(Long id);

    /**
     * 批量删除控线配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTBotKlineModelByIds(Long[] ids);

    public List<TBotKlineModel> getBotModelListByTime(TBotKlineModel tBotKlineModel);
    public List<TBotKlineModel> getBotModelPriceByTime(TBotKlineModel tBotKlineModel);
    List<TBotKlineModel> getBotModelListBeforTime(TBotKlineModel tBotKlineModel);
    List<TBotKlineModel> getBotModelListBySymbol(TBotKlineModel tBotKlineModel);

    List<SymbolPrice> getyesterdayPrice();
}
