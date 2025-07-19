package com.ruoyi.bussiness.service.impl;

import cc.block.data.api.domain.market.Kline;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.*;

import com.ruoyi.bussiness.domain.TBotKlineModel;
import com.ruoyi.bussiness.domain.vo.TBotKlineModelVO;
import com.ruoyi.bussiness.service.ITBotKlineModelService;
import com.ruoyi.socket.config.KLoader;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TBotKlineModelInfoMapper;
import com.ruoyi.bussiness.domain.TBotKlineModelInfo;
import com.ruoyi.bussiness.service.ITBotKlineModelInfoService;
import org.springframework.util.CollectionUtils;

/**
 * 控线详情Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Service
@Slf4j
public class TBotKlineModelInfoServiceImpl extends ServiceImpl<TBotKlineModelInfoMapper, TBotKlineModelInfo> implements ITBotKlineModelInfoService {
    @Autowired
    private TBotKlineModelInfoMapper tBotKlineModelInfoMapper;
    @Autowired
    private ITBotKlineModelService botKlineModelService;
    @Autowired
    RedisCache redisCache;
    /**
     * 查询控线详情
     *
     * @param id 控线详情主键
     * @return 控线详情
     */
    @Override
    public TBotKlineModelInfo selectTBotKlineModelInfoById(Long id) {
        return tBotKlineModelInfoMapper.selectTBotKlineModelInfoById(id);
    }

    /**
     * 查询控线详情列表
     *
     * @param tBotKlineModelInfo 控线详情
     * @return 控线详情
     */
    @Override
    public List<TBotKlineModelInfo> selectTBotKlineModelInfoList(TBotKlineModelInfo tBotKlineModelInfo) {
        return tBotKlineModelInfoMapper.selectTBotKlineModelInfoList(tBotKlineModelInfo);
    }

    /**
     * 新增控线详情
     *
     * @param tBotKlineModelInfo 控线详情
     * @return 结果
     */
    @Override
    public int insertTBotKlineModelInfo(TBotKlineModelInfo tBotKlineModelInfo) {
        return tBotKlineModelInfoMapper.insertTBotKlineModelInfo(tBotKlineModelInfo);
    }

    /**
     * 修改控线详情
     *
     * @param tBotKlineModelInfo 控线详情
     * @return 结果
     */
    @Override
    public int updateTBotKlineModelInfo(TBotKlineModelInfo tBotKlineModelInfo) {
        return tBotKlineModelInfoMapper.updateTBotKlineModelInfo(tBotKlineModelInfo);
    }

    /**
     * 批量删除控线详情
     *
     * @param ids 需要删除的控线详情主键
     * @return 结果
     */
    @Override
    public int deleteTBotKlineModelInfoByIds(Long[] ids) {
        return tBotKlineModelInfoMapper.deleteTBotKlineModelInfoByIds(ids);
    }

    /**
     * 删除控线详情信息
     *
     * @param id 控线详情主键
     * @return 结果
     */
    @Override
    public int deleteTBotKlineModelInfoById(Long id) {
        return tBotKlineModelInfoMapper.deleteTBotKlineModelInfoById(id);
    }

    @Override
    public List<Kline> selectBotLineList(String symbol, List<Kline> list,String inter) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<Kline>();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("symbol", symbol);
        param.put("nowTime", System.currentTimeMillis());
        List<TBotKlineModelInfo> tBotKlineModelInfos = tBotKlineModelInfoMapper.selectBotLineList(param);
        log.info(JSONObject.toJSONString(tBotKlineModelInfos));
        HashMap<Long,TBotKlineModelInfo> kmap = new HashMap();
        for (TBotKlineModelInfo kline: tBotKlineModelInfos ) {
            kmap.put(kline.getDateTime(),kline);
        }
        if (CollectionUtils.isEmpty(tBotKlineModelInfos)) {
            return list;
        } else {
            Long startTime = tBotKlineModelInfos.get(0).getDateTime();
            Long endTime = tBotKlineModelInfos.get(tBotKlineModelInfos.size() - 1).getDateTime();
            //最大时间小于历史最小时间 直接返回
            if (endTime <= list.get(0).getTimestamp()) {
                return list;
            }
            boolean confFlag= true;
            for (Kline kline : list) {
                //判断当前时段， 和开始时间，然后找出 map中所有数据，在拼接。
                //需要几个字段 map（存储控线）  2  当前k线的开始时间结束时间，1分钟不用管  3  参考价格  传到里面后
                if(inter.equals("ONE_MIN")){
                    if(startTime<=kline.getTimestamp()&&endTime>=kline.getTimestamp()){
                        Double open =0.0;
                        TBotKlineModelInfo kline1 = kmap.get(kline.getTimestamp());
                        if(kline1==null){
                            continue;
                        }
                        log.info("查询k线"+JSONObject.toJSONString(kline1));
                        TBotKlineModelVO botKlineModelVO = botKlineModelService.selectTBotKlineModelById(kline1.getModelId());
                        log.info("查询k线"+JSONObject.toJSONString(kline1));
                        if(botKlineModelVO!=null){
                            open=botKlineModelVO.getConPrice().doubleValue();
                            log.info("参考价格:"+open);
                        }
                        getKine(kline,kline1,open);
                        if(confFlag){
                            kline.setOpen(open);
                            confFlag=false;
                        }
                    }
                }else{
                    Long endTime1 = getEndTime(kline.getTimestamp(), inter);
                    List<TBotKlineModelInfo> timeKline = getTimeKline(kline.getTimestamp(), endTime1, kmap);
                    if(null!=timeKline&&timeKline.size()!=0){
                        Collections.sort(timeKline, new Comparator<TBotKlineModelInfo>() {
                            @Override
                            public int compare(TBotKlineModelInfo o1, TBotKlineModelInfo o2) {
                                return Long.compare(o1.getDateTime(), o2.getDateTime());
                            }
                        });
                        TBotKlineModelInfo kline1 = new TBotKlineModelInfo();
                        Double open =0.0;

                        if(timeKline.get(0).getDateTime().equals(kline.getTimestamp())){
                            kline1.setOpen(timeKline.get(0).getOpen());
                        }
                        if(timeKline.get(timeKline.size()-1).getDateTime().equals(endTime1)){
                            kline1.setClose(timeKline.get(timeKline.size()-1).getClose());
                        }
                        TBotKlineModelVO botKlineModelVO = botKlineModelService.selectTBotKlineModelById(timeKline.get(0).getModelId());
                        if(botKlineModelVO!=null){
                            open=botKlineModelVO.getConPrice().doubleValue();
                            log.info("参考价格:"+open);
                        }
                        BigDecimal low = getLow(timeKline);
                        BigDecimal high = getHigh(timeKline);
                        kline1.setHigh(high);
                        kline1.setLow(low);
                        getKine(kline,kline1,open);
                        if(confFlag){
                            kline.setOpen(open);
                            confFlag=false;
                        }

                    }
                }

            }

            Kline kline = list.get(list.size() - 1);
            BigDecimal cacheObject = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + symbol.replace("usdt", "").toLowerCase());
            kline.setClose(cacheObject.doubleValue());
            kline.setHigh(cacheObject.doubleValue());
            kline.setLow(cacheObject.doubleValue());
            log.info("最后一个属性："+JSONObject.toJSONString(kline));
            return list;
        }
    }

    private List<TBotKlineModelInfo> getTimeKline(Long start,Long end, HashMap<Long,TBotKlineModelInfo> map){
        List<TBotKlineModelInfo> rtn = new ArrayList<>();
        for (Long times: map.keySet()) {
            if(times>=start&&times<=end){
                rtn.add(map.get(times));
            }
        }
        return rtn;
    }
    private Long getEndTime(Long startEnd,String inter){
        //["ONE_MIN","FIVE_MIN","FIFTEEN_MIN","THIRTY_MIN","ONE_HOUR","TWO_HOUR","SIX_HOUR","ONE_DAY","TWO_DAY","SEVEN_DAY"]
        if(inter.equals("FIVE_MIN")){
            return  startEnd+60000*4;

        }else if(inter.equals("FIFTEEN_MIN")){
            return  startEnd+60000*14;

        }else if(inter.equals("THIRTY_MIN")){
            return  startEnd+60000*29;

        }else if(inter.equals("ONE_HOUR")){
            return  startEnd+60000*59;

        }else if(inter.equals("TWO_HOUR")){

            return  startEnd+60000*59*2;
        }
        return 0L;
    }


    private BigDecimal getHigh(List<TBotKlineModelInfo> amounts){
        BigDecimal amount =BigDecimal.ZERO;
        for (TBotKlineModelInfo am: amounts) {
            if(amount.compareTo(BigDecimal.ZERO)==0){
                amount = am.getHigh();
            }else if(am.getHigh().compareTo(amount)>0){
                amount = am.getHigh();
            }
        }
        return amount;
    }
    private BigDecimal getLow(List<TBotKlineModelInfo> amounts){
        BigDecimal amount = BigDecimal.ZERO;
        for (TBotKlineModelInfo am: amounts) {
            if(amount.compareTo(BigDecimal.ZERO)==0){
                amount = am.getLow();
            }else if(am.getLow().compareTo(amount)<0){
                amount = am.getLow();
            }
        }
        return amount;
    }
    private Double getPrice(Double price, Double rate) {
        BigDecimal bigDecimal = new BigDecimal(price);
        BigDecimal rateBig = new BigDecimal(rate);
        BigDecimal result = bigDecimal.add(bigDecimal.multiply(rateBig).divide(new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP));
        return result.doubleValue();
    }

    private void  getKine(Kline his, TBotKlineModelInfo bot, Double open){
        log.info("时间戳："+his.getTimestamp()+"查出参考价"+open+"控线"+ JSONObject.toJSONString(bot));
        if(Objects.nonNull(bot)) {
            if(bot.getClose()!=null){
                his.setClose(getPrice(open, bot.getClose().doubleValue()));
            }
            if(bot.getOpen()!=null){
                his.setOpen(getPrice(open, bot.getOpen().doubleValue()));
            }
            his.setLow(getPrice(open, bot.getLow().doubleValue()));
            his.setHigh(getPrice(open, bot.getHigh().doubleValue()));
        }

    }
}







