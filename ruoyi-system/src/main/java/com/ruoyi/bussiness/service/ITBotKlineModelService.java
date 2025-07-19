package com.ruoyi.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.ruoyi.bussiness.domain.TBotKlineModel;
import com.ruoyi.bussiness.domain.vo.TBotKlineModelVO;

/**
 * 控线配置Service接口
 * 
 * @author ruoyi
 * @date 2023-08-09
 */
public interface ITBotKlineModelService extends IService<TBotKlineModel> {
    /**
     * 查询控线配置
     *
     * @param id 控线配置主键
     * @return 控线配置
     */
    public TBotKlineModelVO selectTBotKlineModelById(Long id);

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

    public int insertTBotInfo(TBotKlineModelVO tBotKlineModel);

    /**
     * 修改控线配置
     *
     * @param tBotKlineModel 控线配置
     * @return 结果
     */
    public int updateTBotKlineModel(TBotKlineModelVO tBotKlineModel);

    public int updateByid(TBotKlineModel tBotKlineModel);

    /**
     * 批量删除控线配置
     *
     * @param ids 需要删除的控线配置主键集合
     * @return 结果
     */
    public int deleteTBotKlineModelByIds(Long[] ids);

    /**
     * 删除控线配置信息
     *
     * @param id 控线配置主键
     * @return 结果
     */
    public int deleteTBotKlineModelById(Long id);

    public List<TBotKlineModel> getBotModelListByTime(TBotKlineModel tBotKlineModel);

    public List<TBotKlineModel> getBotModelPriceByTime(TBotKlineModel tBotKlineModel);

    public List<TBotKlineModel> getBotModelListBeforTime(TBotKlineModel tBotKlineModel);

    public List<TBotKlineModel> getBotModelListBySymbol(TBotKlineModel tBotKlineModel);

    public HashMap<String, BigDecimal>  getyesterdayPrice();
}
