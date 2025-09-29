package com.ruoyi.bussiness.service.impl;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.bussiness.domain.KlineSymbol;
import com.ruoyi.bussiness.domain.TOwnCoin;
import com.ruoyi.bussiness.domain.TUserCoin;
import com.ruoyi.bussiness.mapper.KlineSymbolMapper;
import com.ruoyi.bussiness.mapper.TUserCoinMapper;
import com.ruoyi.bussiness.service.ITOwnCoinService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TCurrencySymbolMapper;
import com.ruoyi.bussiness.domain.TCurrencySymbol;
import com.ruoyi.bussiness.service.ITCurrencySymbolService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 币币交易币种配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-25
 */
@Service
public class TCurrencySymbolServiceImpl extends ServiceImpl<TCurrencySymbolMapper,TCurrencySymbol> implements ITCurrencySymbolService
{
    @Resource
    private TCurrencySymbolMapper tCurrencySymbolMapper;
    @Resource
    private KlineSymbolMapper klineSymbolMapper;
    @Resource
    private RedisUtil redisUtil;
    @Value("${api-redis-stream.names}")
    private String redisStreamNames;
    @Resource
    private TUserCoinMapper userCoinMapper;
    @Resource
    private ITOwnCoinService itOwnCoinService;
    /**
     * 查询币币交易币种配置
     * 
     * @param id 币币交易币种配置主键
     * @return 币币交易币种配置
     */
    @Override
    public TCurrencySymbol selectTCurrencySymbolById(Long id)
    {
        return tCurrencySymbolMapper.selectTCurrencySymbolById(id);
    }

    /**
     * 查询币币交易币种配置列表
     * 
     * @param tCurrencySymbol 币币交易币种配置
     * @return 币币交易币种配置
     */
    @Override
    public List<TCurrencySymbol> selectTCurrencySymbolList(TCurrencySymbol tCurrencySymbol)
    {
        return tCurrencySymbolMapper.selectTCurrencySymbolList(tCurrencySymbol);
    }

    /**
     * 新增币币交易币种配置
     * 
     * @param tCurrencySymbol 币币交易币种配置
     * @return 结果
     */
    @Override
    public int insertTCurrencySymbol(TCurrencySymbol tCurrencySymbol)
    {
        tCurrencySymbol.setSymbol(tCurrencySymbol.getCoin().toLowerCase()+"usdt");
        tCurrencySymbol.setShowSymbol(StringUtils.isNotBlank(tCurrencySymbol.getShowSymbol())?tCurrencySymbol.getShowSymbol().toUpperCase():(tCurrencySymbol.getCoin()+"/usdt").toUpperCase());
        tCurrencySymbol.setCoin(tCurrencySymbol.getCoin().toLowerCase());
        tCurrencySymbol.setBaseCoin("usdt");
        tCurrencySymbol.setUpdateBy(SecurityUtils.getUsername());
        tCurrencySymbol.setUpdateTime(DateUtils.getNowDate());
        tCurrencySymbol.setCreateBy(SecurityUtils.getUsername());
        tCurrencySymbol.setUpdateTime(DateUtils.getNowDate());
        List<KlineSymbol> klist = klineSymbolMapper.selectList(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, tCurrencySymbol.getCoin().toUpperCase()));
        if (!CollectionUtils.isEmpty(klist) && StringUtils.isEmpty(tCurrencySymbol.getLogo())) {
            tCurrencySymbol.setLogo(klist.get(0).getLogo());
        }
        if (tCurrencySymbol.getMarket().equals("echo") && StringUtils.isEmpty(tCurrencySymbol.getLogo())) {
            KlineSymbol klineSymbol = klineSymbolMapper.selectOne(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getMarket, tCurrencySymbol.getMarket()).eq(KlineSymbol::getSymbol, tCurrencySymbol.getCoin().toLowerCase()));
            tCurrencySymbol.setLogo(klineSymbol.getLogo());
        }
        int i = tCurrencySymbolMapper.insertTCurrencySymbol(tCurrencySymbol);
        HashMap<String, Object> object = new HashMap<>();
        object.put("add_coin",tCurrencySymbol.getCoin());
        redisUtil.addStream(redisStreamNames,object);
        return i;
    }

    /**
     * 修改币币交易币种配置
     * 
     * @param tCurrencySymbol 币币交易币种配置
     * @return 结果
     */
    @Override
    public int updateTCurrencySymbol(TCurrencySymbol tCurrencySymbol)
    {
        tCurrencySymbol.setSymbol(tCurrencySymbol.getCoin().toLowerCase()+"usdt");
        tCurrencySymbol.setShowSymbol(tCurrencySymbol.getShowSymbol().toUpperCase());
        tCurrencySymbol.setCoin(tCurrencySymbol.getCoin().toLowerCase());
        tCurrencySymbol.setUpdateBy(SecurityUtils.getUsername());
        tCurrencySymbol.setUpdateTime(DateUtils.getNowDate());
        List<KlineSymbol> klist = klineSymbolMapper.selectList(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, tCurrencySymbol.getCoin().toUpperCase()));
        if (!CollectionUtils.isEmpty(klist)){
            tCurrencySymbol.setLogo(klist.get(0).getLogo());
        }
        return tCurrencySymbolMapper.updateTCurrencySymbol(tCurrencySymbol);
    }

    /**
     * 批量删除币币交易币种配置
     * 
     * @param ids 需要删除的币币交易币种配置主键
     * @return 结果
     */
    @Override
    public int deleteTCurrencySymbolByIds(Long[] ids)
    {
        return tCurrencySymbolMapper.deleteTCurrencySymbolByIds(ids);
    }

    /**
     * 删除币币交易币种配置信息
     * 
     * @param id 币币交易币种配置主键
     * @return 结果
     */
    @Override
    public int deleteTCurrencySymbolById(Long id)
    {
        return tCurrencySymbolMapper.deleteTCurrencySymbolById(id);
    }

    @Override
    public boolean batchSave(String[] symbols) {
        List<TCurrencySymbol> list = new ArrayList<>();
        for (String symbol : symbols) {
            TCurrencySymbol tCurrencySymbol = tCurrencySymbolMapper.selectOne(new LambdaQueryWrapper<TCurrencySymbol>().eq(TCurrencySymbol::getCoin, symbol.toLowerCase()));
            if(null != tCurrencySymbol){
                continue;
            }
            TCurrencySymbol currencySymbol = new TCurrencySymbol();
            List<KlineSymbol> klist = klineSymbolMapper.selectList(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, symbol.toUpperCase()));
            if (!CollectionUtils.isEmpty(klist)){
                currencySymbol.setLogo(klist.get(0).getLogo());
            }
            currencySymbol.setSymbol(symbol.toLowerCase()+"usdt");
            currencySymbol.setShowSymbol(symbol.toUpperCase()+"/USDT");
            currencySymbol.setCoin(symbol.toLowerCase());
            currencySymbol.setBaseCoin("usdt");

            list.add(currencySymbol);
        }
        return this.saveBatch(list);
    }

    @Override
    public List<TCurrencySymbol> getSymbolList() {
        TCurrencySymbol tCurrencySymbol = new TCurrencySymbol();
        tCurrencySymbol.setEnable("1");
        tCurrencySymbol.setIsShow("1");
        List<TCurrencySymbol> tCurrencySymbols = tCurrencySymbolMapper.selectTCurrencySymbolList(tCurrencySymbol);
        for (TCurrencySymbol tCurrencySymbol1: tCurrencySymbols) {
            LambdaQueryWrapper<TUserCoin> queryWrapper = new LambdaQueryWrapper<TUserCoin>();
            queryWrapper.eq(TUserCoin::getCoin, tCurrencySymbol1.getCoin().toLowerCase());
            if(StpUtil.isLogin()){
                queryWrapper.eq(TUserCoin::getUserId, StpUtil.getLoginIdAsLong());
                TUserCoin userCoin = userCoinMapper.selectOne(queryWrapper);
                if(ObjectUtils.isNotEmpty(userCoin)){
                    tCurrencySymbol1.setIsCollect(1);
                }else {
                    tCurrencySymbol1.setIsCollect(2);
                }
            }
        }
        return tCurrencySymbols;
    }

    @Override
    public TCurrencySymbol selectByCoin(String symbol) {
        return tCurrencySymbolMapper.selectByCoin(symbol);
    }
}
