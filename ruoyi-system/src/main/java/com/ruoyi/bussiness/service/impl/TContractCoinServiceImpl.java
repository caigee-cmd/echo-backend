package com.ruoyi.bussiness.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.mapper.KlineSymbolMapper;
import com.ruoyi.bussiness.mapper.TContractCoinMapper;
import com.ruoyi.bussiness.mapper.TUserCoinMapper;
import com.ruoyi.bussiness.service.ITContractCoinService;
import com.ruoyi.bussiness.service.ITOwnCoinService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RedisUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * U本位合约币种Service业务层处理
 *
 * @author michael
 * @date 2023-07-20
 */
@Service
public class TContractCoinServiceImpl extends ServiceImpl<TContractCoinMapper, TContractCoin> implements ITContractCoinService {
    @Resource
    private TContractCoinMapper tContractCoinMapper;
    @Resource
    private TUserCoinMapper userCoinMapper;

    @Resource
    private KlineSymbolMapper klineSymbolMapper;

    @Resource
    private RedisUtil redisUtil;
    @Value("${api-redis-stream.names}")
    private String redisStreamNames;

    /**
     * 查询U本位合约币种
     *
     * @param id U本位合约币种主键
     * @return U本位合约币种
     */
    @Override
    public TContractCoin selectTContractCoinById(Long id) {
        return tContractCoinMapper.selectTContractCoinById(id);
    }

    /**
     * 查询U本位合约币种列表
     *
     * @param tContractCoin U本位合约币种
     * @return U本位合约币种
     */
    @Override
    public List<TContractCoin> selectTContractCoinList(TContractCoin tContractCoin) {
        return tContractCoinMapper.selectTContractCoinList(tContractCoin);
    }

    /**
     * 新增U本位合约币种
     *
     * @param tContractCoin U本位合约币种
     * @return 结果
     */
    @Override
    public int insertTContractCoin(TContractCoin tContractCoin) {
        tContractCoin.setSymbol(tContractCoin.getSymbol().toLowerCase());
        tContractCoin.setCreateTime(DateUtils.getNowDate());
        int count = tContractCoinMapper.selectCount(new LambdaQueryWrapper<TContractCoin>().eq(TContractCoin::getCoin, tContractCoin.getCoin().toLowerCase()));
        if (count > 0) {
            return 10001;
        }else {
            KlineSymbol klineSymbol = new KlineSymbol();
            klineSymbol.setSlug(tContractCoin.getCoin().toLowerCase());
            List<KlineSymbol> klineSymbols = klineSymbolMapper.selectKlineSymbolList(klineSymbol);
            if (klineSymbols.size() > 0) {
                tContractCoin.setLogo(klineSymbols.get(0).getLogo());
            }
            if (!"mt5".equals(tContractCoin.getMarket())) {
                tContractCoin.setCoin(tContractCoin.getCoin().toLowerCase());
            }
            if(tContractCoin.getMarket().equals("echo")){
                KlineSymbol kSymbol = klineSymbolMapper.selectOne(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getMarket, tContractCoin.getMarket()).eq(KlineSymbol::getSymbol, tContractCoin.getCoin().toLowerCase()));
                tContractCoin.setLogo(kSymbol.getLogo());
            }
            HashMap<String, Object> object = new HashMap<>();
            object.put("add_coin", tContractCoin.getCoin());
            redisUtil.addStream(redisStreamNames, object);
            return tContractCoinMapper.insertTContractCoin(tContractCoin);
        }
    }

    /**
     * 修改U本位合约币种
     *
     * @param tContractCoin U本位合约币种
     * @return 结果
     */
    @Override
    public int updateTContractCoin(TContractCoin tContractCoin) {
        tContractCoin.setUpdateTime(DateUtils.getNowDate());
        return tContractCoinMapper.updateTContractCoin(tContractCoin);
    }

    /**
     * 批量删除U本位合约币种
     *
     * @param ids 需要删除的U本位合约币种主键
     * @return 结果
     */
    @Override
    public int deleteTContractCoinByIds(Long[] ids) {
        return tContractCoinMapper.deleteTContractCoinByIds(ids);
    }

    /**
     * 删除U本位合约币种信息
     *
     * @param id U本位合约币种主键
     * @return 结果
     */
    @Override
    public int deleteTContractCoinById(Long id) {
        return tContractCoinMapper.deleteTContractCoinById(id);
    }

    @Override
    public TContractCoin selectContractCoinBySymbol(String symbol) {
        return tContractCoinMapper.selectOne(new LambdaQueryWrapper<TContractCoin>().eq(TContractCoin::getCoin, symbol));
    }

    @Override
    public List<TContractCoin> getCoinList() {
        TContractCoin tContractCoin = new TContractCoin();
        tContractCoin.setEnable(0L);
        tContractCoin.setVisible(0L);
        List<TContractCoin> list = tContractCoinMapper.selectTContractCoinList(tContractCoin);
        list =list.stream().sorted(Comparator.comparing(TContractCoin::getSort)).collect(Collectors.toList());
        for (TContractCoin tContractCoin1: list) {
            LambdaQueryWrapper<TUserCoin> queryWrapper = new LambdaQueryWrapper<TUserCoin>();
            queryWrapper.eq(TUserCoin::getCoin, tContractCoin1.getCoin().toLowerCase());
            if(StpUtil.isLogin()){
                queryWrapper.eq(TUserCoin::getUserId, StpUtil.getLoginIdAsLong());
                TUserCoin userCoin = userCoinMapper.selectOne(queryWrapper);
                if(ObjectUtils.isNotEmpty(userCoin)){
                    tContractCoin1.setIsCollect(1);
                }else {
                    tContractCoin1.setIsCollect(2);
                }
            }
        }
        return list;
    }

}
