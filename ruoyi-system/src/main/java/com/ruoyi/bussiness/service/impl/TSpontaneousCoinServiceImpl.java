package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.ruoyi.bussiness.domain.KlineSymbol;
import com.ruoyi.bussiness.domain.TOwnCoin;
import com.ruoyi.bussiness.mapper.KlineSymbolMapper;
import com.ruoyi.bussiness.mapper.TOwnCoinMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TSpontaneousCoinMapper;
import com.ruoyi.bussiness.domain.TSpontaneousCoin;
import com.ruoyi.bussiness.service.ITSpontaneousCoinService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 自发币种配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-08
 */
@Service
public class TSpontaneousCoinServiceImpl extends ServiceImpl<TSpontaneousCoinMapper,TSpontaneousCoin> implements ITSpontaneousCoinService
{
    @Autowired
    private TSpontaneousCoinMapper tSpontaneousCoinMapper;
    @Resource
    private TOwnCoinMapper tOwnCoinMapper;
    @Resource
    private KlineSymbolMapper klineSymbolMapper;
    @Resource
    private RedisUtil redisUtil;
    @Value("${api-redis-stream.names}")
    private String redisStreamNames;
    /**
     * 查询自发币种配置
     * 
     * @param id 自发币种配置主键
     * @return 自发币种配置
     */
    @Override
    public TSpontaneousCoin selectTSpontaneousCoinById(Long id)
    {
        return tSpontaneousCoinMapper.selectTSpontaneousCoinById(id);
    }

    /**
     * 查询自发币种配置列表
     * 
     * @param tSpontaneousCoin 自发币种配置
     * @return 自发币种配置
     */
    @Override
    public List<TSpontaneousCoin> selectTSpontaneousCoinList(TSpontaneousCoin tSpontaneousCoin)
    {
        return tSpontaneousCoinMapper.selectTSpontaneousCoinList(tSpontaneousCoin);
    }

    /**
     * 新增自发币种配置
     * 
     * @param tSpontaneousCoin 自发币种配置
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTSpontaneousCoin(TSpontaneousCoin tSpontaneousCoin)
    {
            tSpontaneousCoin.setCreateTime(DateUtils.getNowDate());
            tSpontaneousCoin.setCreateBy(SecurityUtils.getUsername());
            tSpontaneousCoin.setUpdateTime(DateUtils.getNowDate());
            tSpontaneousCoin.setUpdateBy(SecurityUtils.getUsername());
            int i = tSpontaneousCoinMapper.insertTSpontaneousCoin(tSpontaneousCoin);
            KlineSymbol klineSymbol = new KlineSymbol()
                    .setSymbol(tSpontaneousCoin.getCoin().toLowerCase())
                    .setSlug(tSpontaneousCoin.getCoin().toUpperCase())
                    .setLogo(tSpontaneousCoin.getLogo())
                    .setMarket("echo")
                    .setReferMarket(tSpontaneousCoin.getReferMarket())
                    .setReferCoin(tSpontaneousCoin.getReferCoin())
                    .setProportion(tSpontaneousCoin.getProportion());
            klineSymbolMapper.insert(klineSymbol);

            //监听获取新的kline
//            HashMap<String, Object> object = new HashMap<>();
//            object.put(tSpontaneousCoin.getCoin(),tSpontaneousCoin.getReferCoin()+"usdt");
//            redisUtil.addStream(redisStreamNames,object);
            return i;
    }

    /**
     * 修改自发币种配置
     * 
     * @param tSpontaneousCoin 自发币种配置
     * @return 结果
     */
    @Override
    public int updateTSpontaneousCoin(TSpontaneousCoin tSpontaneousCoin)
    {
        tSpontaneousCoin.setUpdateTime(DateUtils.getNowDate());
        return tSpontaneousCoinMapper.updateTSpontaneousCoin(tSpontaneousCoin);
    }

    /**
     * 批量删除自发币种配置
     * 
     * @param ids 需要删除的自发币种配置主键
     * @return 结果
     */
    @Override
    public int deleteTSpontaneousCoinByIds(Long[] ids)
    {
        return tSpontaneousCoinMapper.deleteTSpontaneousCoinByIds(ids);
    }

    /**
     * 删除自发币种配置信息
     * 
     * @param id 自发币种配置主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTSpontaneousCoinById(Long id)
    {
        TSpontaneousCoin tSpontaneousCoin = tSpontaneousCoinMapper.selectById(id);
        if (Objects.nonNull(tSpontaneousCoin)){
            klineSymbolMapper.delete(new LambdaQueryWrapper<KlineSymbol>()
                    .eq(KlineSymbol::getSymbol, tSpontaneousCoin.getCoin())
                    .eq(KlineSymbol::getMarket, "echo"));
        }
        return tSpontaneousCoinMapper.deleteTSpontaneousCoinById(id);
    }
}
