package com.ruoyi.bussiness.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.bussiness.domain.SymbolPrice;
import com.ruoyi.bussiness.domain.TBotKlineModelInfo;
import com.ruoyi.bussiness.domain.vo.TBotKlineModelVO;
import com.ruoyi.bussiness.mapper.TBotKlineModelInfoMapper;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TBotKlineModelMapper;
import com.ruoyi.bussiness.domain.TBotKlineModel;
import com.ruoyi.bussiness.service.ITBotKlineModelService;

/**
 * 控线配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-09
 */
@Service
public class TBotKlineModelServiceImpl extends ServiceImpl<TBotKlineModelMapper,TBotKlineModel> implements ITBotKlineModelService
{
    @Autowired
    private TBotKlineModelMapper tBotKlineModelMapper;
    @Autowired
    private TBotKlineModelInfoMapper botKlineModelInfoMapper;
    @Autowired
    RedisCache redisCache;
    /**
     * 查询控线配置
     * 
     * @param id 控线配置主键
     * @return 控线配置
     */
    @Override
    public TBotKlineModelVO selectTBotKlineModelById(Long id)
    {
        TBotKlineModel tBotKlineModel = tBotKlineModelMapper.selectTBotKlineModelById(id);
        TBotKlineModelVO botKlineModelVO = new TBotKlineModelVO();
        BeanUtil.copyProperties(tBotKlineModel,botKlineModelVO);
        TBotKlineModelInfo tBotKlineModelInfo = new TBotKlineModelInfo();
        tBotKlineModelInfo.setModelId(id);
        List<TBotKlineModelInfo> tBotKlineModelInfos = botKlineModelInfoMapper.selectTBotKlineModelInfoList(tBotKlineModelInfo);
        botKlineModelVO.setBotInfoList(tBotKlineModelInfos);
        return botKlineModelVO;
    }

    /**
     * 查询控线配置列表
     * 
     * @param tBotKlineModel 控线配置
     * @return 控线配置
     */
    @Override
    public List<TBotKlineModel> selectTBotKlineModelList(TBotKlineModel tBotKlineModel)
    {
        return tBotKlineModelMapper.selectTBotKlineModelList(tBotKlineModel);
    }

    /**
     * 新增控线配置
     * 
     * @param tBotKlineModel 控线配置
     * @return 结果
     */
    @Override
    public int insertTBotKlineModel(TBotKlineModel tBotKlineModel)
    {
        tBotKlineModel.setCreateTime(DateUtils.getNowDate());
        return tBotKlineModelMapper.insertTBotKlineModel(tBotKlineModel);
    }

    @Override
    public int insertTBotInfo(TBotKlineModelVO tBotKlineModelVO) {
        TBotKlineModel tBotKlineModel = new TBotKlineModel();
        BeanUtil.copyProperties(tBotKlineModelVO,tBotKlineModel);

        if(tBotKlineModelVO.getModel()==0){
            //跟随型 直接更新交易对 和缓存
            String coin = tBotKlineModelVO.getSymbol().replace("usdt", "");
            //存入增加价格。
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime lastWholeMinute = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
            long timestamp = lastWholeMinute.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
            tBotKlineModel.setBeginTime(new Date(timestamp));
            String cacheObject = redisCache.getCacheObject("con-" + coin);
            if(cacheObject==null){
                redisCache.setCacheObject("con-"+coin,tBotKlineModelVO.getConPrice()+","+tBotKlineModel.getBeginTime().getTime());
            }else{
                String[] split = cacheObject.split(",");
                redisCache.setCacheObject("con-"+coin,tBotKlineModelVO.getConPrice().add(new BigDecimal(split[0]))+","+split[1]);
            }
            tBotKlineModelMapper.insertTBotKlineModel(tBotKlineModel);
        }else {
            tBotKlineModelMapper.insertTBotKlineModel(tBotKlineModel);
            List<TBotKlineModelInfo> botInfoList = tBotKlineModelVO.getBotInfoList();
            if(botInfoList!=null&&botInfoList.size()>0){
                for (TBotKlineModelInfo tBotKlineModelInfo: botInfoList) {
                    tBotKlineModelInfo.setModelId(tBotKlineModel.getId());
                }
                botKlineModelInfoMapper.insertModelInfo(botInfoList);
            }
        }

        return 1;
    }

    /**
     * 修改控线配置
     * 
     * @param  tBotKlineModelVO
     * @return 结果
     */
    @Override
    public int updateTBotKlineModel(TBotKlineModelVO tBotKlineModelVO)
    {  TBotKlineModel tBotKlineModel = new TBotKlineModel();
        BeanUtil.copyProperties(tBotKlineModelVO,tBotKlineModel);
        tBotKlineModel.setUpdateTime(DateUtils.getNowDate());
        if(tBotKlineModelVO.getModel()==0){
            //跟随型 直接更新交易对 和缓存
            tBotKlineModelVO.setBeginTime(new Date());
            redisCache.setCacheObject("con-"+tBotKlineModelVO.getSymbol().replace("usdt",""),tBotKlineModelVO.getConPrice()+","+tBotKlineModelVO.getBeginTime().getTime());
        }
        List<TBotKlineModelInfo> botInfoList = tBotKlineModelVO.getBotInfoList();
        if(botInfoList!=null&&botInfoList.size()>0){
            for (TBotKlineModelInfo tBotKlineModelInfo: botInfoList) {
                tBotKlineModelInfo.setModelId(tBotKlineModel.getId());
            }
            TBotKlineModelInfo tBotKlineModelInfoa = new TBotKlineModelInfo();
            tBotKlineModelInfoa.setModelId(tBotKlineModelVO.getId());
            List<TBotKlineModelInfo> tBotKlineModelInfos = botKlineModelInfoMapper.selectTBotKlineModelInfoList(tBotKlineModelInfoa);
            for (TBotKlineModelInfo a: tBotKlineModelInfos) {
                botKlineModelInfoMapper.deleteTBotKlineModelInfoById(a.getId());
            }
            botKlineModelInfoMapper.insertModelInfo(botInfoList);
        }

        return tBotKlineModelMapper.updateTBotKlineModel(tBotKlineModel);
    }

    @Override
    public int updateByid(TBotKlineModel tBotKlineModel) {
        return tBotKlineModelMapper.updateTBotKlineModel(tBotKlineModel);
    }

    /**
     * 批量删除控线配置
     * 
     * @param ids 需要删除的控线配置主键
     * @return 结果
     */
    @Override
    public int deleteTBotKlineModelByIds(Long[] ids)
    {
        for (Long id: ids ) {
            TBotKlineModel tBotKlineModel = tBotKlineModelMapper.selectTBotKlineModelById(id);
            if(tBotKlineModel.getModel()==0){
                //跟随型 直接更新交易对 和缓存
                String coin = tBotKlineModel.getSymbol().replace("usdt", "");
                //存入增加价格。
                //跟随型 直接更新交易对 和缓存
                String cacheObject = redisCache.getCacheObject("con-" + coin);
                if(cacheObject==null){
                    redisCache.deleteObject("con-"+coin);
                }else{
                    String[] split = cacheObject.split(",");
                    BigDecimal subtract = new BigDecimal(split[0]).subtract(tBotKlineModel.getConPrice());
                    if(subtract.compareTo(BigDecimal.ZERO)!=0){
                        redisCache.setCacheObject("con-"+coin,subtract+","+split[1]);
                    }else {
                        redisCache.deleteObject("con-"+coin);
                    }
                }
            }
        }
        for ( Long id: ids) {
            TBotKlineModelInfo tBotKlineModelInfoa = new TBotKlineModelInfo();
            tBotKlineModelInfoa.setModelId(id);
            List<TBotKlineModelInfo> tBotKlineModelInfos = botKlineModelInfoMapper.selectTBotKlineModelInfoList(tBotKlineModelInfoa);
            if(tBotKlineModelInfos!=null&&tBotKlineModelInfos.size()>0){
            for (TBotKlineModelInfo a: tBotKlineModelInfos) {
                botKlineModelInfoMapper.deleteTBotKlineModelInfoById(a.getId());
            }
            }
        }

        return tBotKlineModelMapper.deleteTBotKlineModelByIds(ids);
    }

    /**
     * 删除控线配置信息
     * 
     * @param id 控线配置主键
     * @return 结果
     */
    @Override
    public int deleteTBotKlineModelById(Long id)
    {

        return tBotKlineModelMapper.deleteTBotKlineModelById(id);
    }

    @Override
    public List<TBotKlineModel> getBotModelListByTime(TBotKlineModel tBotKlineModel) {
        return tBotKlineModelMapper.getBotModelListByTime(tBotKlineModel);
    }

    @Override
    public List<TBotKlineModel> getBotModelPriceByTime(TBotKlineModel tBotKlineModel) {
        return tBotKlineModelMapper.getBotModelPriceByTime(tBotKlineModel);
    }

    @Override
    public List<TBotKlineModel> getBotModelListBeforTime(TBotKlineModel tBotKlineModel) {
        return tBotKlineModelMapper.getBotModelListBeforTime( tBotKlineModel);
    }

    @Override
    public List<TBotKlineModel> getBotModelListBySymbol(TBotKlineModel tBotKlineModel) {
        return tBotKlineModelMapper.getBotModelListBySymbol(tBotKlineModel);
    }

    @Override
    public HashMap<String, BigDecimal>  getyesterdayPrice() {
        List<SymbolPrice> symbolPrices = tBotKlineModelMapper.getyesterdayPrice();
        HashMap<String, BigDecimal> c =new HashMap<>();
        for (SymbolPrice symbolPrice:symbolPrices) {
            c.put(symbolPrice.getSymbol(),symbolPrice.getPrice());
        }
        return c;
    }


}
