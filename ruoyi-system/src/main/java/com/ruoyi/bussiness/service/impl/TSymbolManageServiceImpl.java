package com.ruoyi.bussiness.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.bussiness.domain.KlineSymbol;
import com.ruoyi.bussiness.domain.TSymbolManage;
import com.ruoyi.bussiness.mapper.KlineSymbolMapper;
import com.ruoyi.bussiness.mapper.TSymbolManageMapper;
import com.ruoyi.bussiness.service.ITSymbolManageService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.socket.service.MarketThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 币种管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-12
 */
@Service
public class TSymbolManageServiceImpl extends ServiceImpl<TSymbolManageMapper, TSymbolManage> implements ITSymbolManageService
{
    @Resource
    private TSymbolManageMapper tSymbolManageMapper;
    @Resource
    private KlineSymbolMapper klineSymbolMapper;
    @Resource
    private RedisUtil redisUtil;
    @Value("${api-redis-stream.names}")
    private String redisStreamNames;

    /**
     * 查询币种管理
     * 
     * @param id 币种管理主键
     * @return 币种管理
     */
    @Override
    public TSymbolManage selectTSymbolManageById(Long id)
    {
        return tSymbolManageMapper.selectTSymbolManageById(id);
    }

    /**
     * 查询币种管理列表
     * 
     * @param tSymbolManage 币种管理
     * @return 币种管理
     */
    @Override
    public List<TSymbolManage> selectTSymbolManageList(TSymbolManage tSymbolManage)
    {
        return tSymbolManageMapper.selectTSymbolManageList(tSymbolManage);
    }

    /**
     * 新增币种管理
     * 
     * @param tSymbolManage 币种管理
     * @return 结果
     */
    @Override
    public int insertTSymbolManage(TSymbolManage tSymbolManage)
    {
        tSymbolManage.setCreateTime(DateUtils.getNowDate());
        tSymbolManage.setDelFlag("0");
        tSymbolManage.setSymbol(tSymbolManage.getSymbol().toLowerCase());
        List<KlineSymbol> kList = klineSymbolMapper.selectList(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, tSymbolManage.getSymbol().toUpperCase()));
        if (!CollectionUtils.isEmpty(kList)){
            tSymbolManage.setLogo(kList.get(0).getLogo());
        }
        int i = tSymbolManageMapper.insertTSymbolManage(tSymbolManage);
        HashMap<String, Object> object = new HashMap<>();
        object.put("add_coin",tSymbolManage.getSymbol());
        redisUtil.addStream(redisStreamNames,object);
        return i;
    }

    /**
     * 修改币种管理
     * 
     * @param tSymbolManage 币种管理
     * @return 结果
     */
    @Override
    public int updateTSymbolManage(TSymbolManage tSymbolManage)
    {
        tSymbolManage.setUpdateTime(DateUtils.getNowDate());
        tSymbolManage.setSymbol(tSymbolManage.getSymbol().toLowerCase());
        List<KlineSymbol> kList = klineSymbolMapper.selectList(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, tSymbolManage.getSymbol().toUpperCase()));
        if (!CollectionUtils.isEmpty(kList)){
            tSymbolManage.setLogo(kList.get(0).getLogo());
        }

        return tSymbolManageMapper.updateTSymbolManage(tSymbolManage);
    }

    /**
     * 批量删除币种管理
     * 
     * @param ids 需要删除的币种管理主键
     * @return 结果
     */
    @Override
    public int deleteTSymbolManageByIds(Long[] ids)
    {
        return tSymbolManageMapper.deleteTSymbolManageByIds(ids);
    }

    /**
     * 删除币种管理信息
     * 
     * @param id 币种管理主键
     * @return 结果
     */
    @Override
    public int deleteTSymbolManageById(Long id)
    {
        return tSymbolManageMapper.deleteTSymbolManageById(id);
    }

    @Override
    public List<String> selectSymbolList(TSymbolManage tSymbolManage) {
        return tSymbolManageMapper.selectSymbolList(tSymbolManage);
    }

    @Override
    public boolean addBatch(String[] symbols) {
        List<TSymbolManage> list = new ArrayList<TSymbolManage>();
        for (int i = 0; i < symbols.length; i++) {
            List<KlineSymbol> kList = klineSymbolMapper.selectList(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, symbols[i].toUpperCase()));
            TSymbolManage tSymbolManage = new TSymbolManage();
            tSymbolManage.setSymbol(symbols[i]);
            tSymbolManage.setEnable("1");
            tSymbolManage.setMinChargeNum(BigDecimal.ONE);
            tSymbolManage.setMaxChargeNum(new BigDecimal("100000"));
            tSymbolManage.setCommission(new BigDecimal("0.1"));
            tSymbolManage.setSort(0);
            tSymbolManage.setDelFlag("0");
            if (!CollectionUtils.isEmpty(kList)){
                tSymbolManage.setLogo(kList.get(0).getLogo());
            }
            tSymbolManage.setCreateBy(SecurityUtils.getUsername());
            tSymbolManage.setUpdateBy(SecurityUtils.getUsername());
            tSymbolManage.setCreateTime(DateUtils.getNowDate());
            tSymbolManage.setUpdateTime(DateUtils.getNowDate());
            list.add(tSymbolManage);
        }

        return this.saveBatch(list);
    }
}
