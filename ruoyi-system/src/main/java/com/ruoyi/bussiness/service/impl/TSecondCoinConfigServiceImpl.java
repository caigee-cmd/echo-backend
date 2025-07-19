package com.ruoyi.bussiness.service.impl;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.setting.HomeCoinSetting;
import com.ruoyi.bussiness.domain.vo.SecondCoinCopyVO;
import com.ruoyi.bussiness.domain.vo.SymbolCoinConfigVO;
import com.ruoyi.bussiness.mapper.KlineSymbolMapper;
import com.ruoyi.bussiness.mapper.TSecondCoinConfigMapper;
import com.ruoyi.bussiness.mapper.TUserCoinMapper;
import com.ruoyi.bussiness.service.ITOwnCoinService;
import com.ruoyi.bussiness.service.ITSecondCoinConfigService;
import com.ruoyi.bussiness.service.ITSecondPeriodConfigService;
import com.ruoyi.bussiness.service.ITUserCoinService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.socket.service.MarketThread;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


/**
 * 秒合约币种配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@Service
public class TSecondCoinConfigServiceImpl extends ServiceImpl<TSecondCoinConfigMapper, TSecondCoinConfig> implements ITSecondCoinConfigService
{
    @Resource
    private TSecondCoinConfigMapper tSecondCoinConfigMapper;

    @Resource
    private TUserCoinMapper userCoinMapper;

    @Resource
    private ITOwnCoinService itOwnCoinService;

    @Resource
    private ITSecondPeriodConfigService tSecondPeriodConfigService;

    @Resource
    private KlineSymbolMapper klineSymbolMapper;
    @Resource
    RedisCache redisCache;
    @Resource
    private RedisUtil redisUtil;
    @Value("${api-redis-stream.names}")
    private String redisStreamNames;
    /**
     * 查询秒合约币种配置
     * 
     * @param id 秒合约币种配置主键
     * @return 秒合约币种配置
     */
    @Override
    public TSecondCoinConfig selectTSecondCoinConfigById(Long id)
    {
        return tSecondCoinConfigMapper.selectTSecondCoinConfigById(id);
    }

    /**
     * 查询秒合约币种配置列表
     * 
     * @param tSecondCoinConfig 秒合约币种配置
     * @return 秒合约币种配置
     */
    @Override
    public List<TSecondCoinConfig> selectTSecondCoinConfigList(TSecondCoinConfig tSecondCoinConfig)
    {
        return tSecondCoinConfigMapper.selectTSecondCoinConfigList(tSecondCoinConfig);
    }

    /**
     * 新增秒合约币种配置
     * 
     * @param tSecondCoinConfig 秒合约币种配置
     * @return 结果
     */
    @Override
    public int insertTSecondCoinConfig(TSecondCoinConfig tSecondCoinConfig)
    {
        tSecondCoinConfig.setCreateTime(DateUtils.getNowDate());
        return tSecondCoinConfigMapper.insertTSecondCoinConfig(tSecondCoinConfig);
    }

    @Override
    @Transactional
    public int insertSecondCoin(TSecondCoinConfig tSecondCoinConfig) {
        tSecondCoinConfig.setCreateTime(DateUtils.getNowDate());
        if(tSecondCoinConfig.getMarket().equals("binance")||tSecondCoinConfig.getMarket().equals("huobi")){
            tSecondCoinConfig.setCoin(tSecondCoinConfig.getCoin().toLowerCase());
            tSecondCoinConfig.setSymbol(tSecondCoinConfig.getCoin().toLowerCase()+"usdt");
            tSecondCoinConfig.setBaseCoin("usdt");
            KlineSymbol klineSymbol = new KlineSymbol();
            klineSymbol.setSlug(tSecondCoinConfig.getCoin().toLowerCase());
            List<KlineSymbol> klineSymbols = klineSymbolMapper.selectKlineSymbolList(klineSymbol);
            if(!CollectionUtils.isEmpty(klineSymbols)){
                tSecondCoinConfig.setLogo(klineSymbols.get(0).getLogo());
            }
        }else  if(tSecondCoinConfig.getMarket().equals("mt5") || tSecondCoinConfig.getMarket().equals("metal")){
            tSecondCoinConfig.setSymbol(tSecondCoinConfig.getCoin());
            tSecondCoinConfig.setCoin(tSecondCoinConfig.getCoin());
            tSecondCoinConfig.setBaseCoin(tSecondCoinConfig.getCoin());
            KlineSymbol klineSymbol = new KlineSymbol();
            klineSymbol.setSlug(tSecondCoinConfig.getSymbol().toUpperCase());
            List<KlineSymbol> klineSymbols = klineSymbolMapper.selectKlineSymbolList(klineSymbol);
            if(!CollectionUtils.isEmpty(klineSymbols)){
                tSecondCoinConfig.setLogo(klineSymbols.get(0).getLogo());
            }
        }else  if(tSecondCoinConfig.getMarket().equals("echo")){
            KlineSymbol klineSymbol = klineSymbolMapper.selectOne(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getMarket, tSecondCoinConfig.getMarket()).eq(KlineSymbol::getSymbol, tSecondCoinConfig.getCoin().toLowerCase()));
            tSecondCoinConfig.setCoin(tSecondCoinConfig.getCoin().toLowerCase());
            tSecondCoinConfig.setSymbol(tSecondCoinConfig.getCoin().toLowerCase()+"usdt");
            tSecondCoinConfig.setBaseCoin("usdt");
            tSecondCoinConfig.setLogo(klineSymbol.getLogo());
        }
        if(StringUtils.isEmpty(tSecondCoinConfig.getShowSymbol())){
            tSecondCoinConfig.setShowSymbol(tSecondCoinConfig.getCoin().toUpperCase()+"/USDT");
        }
        tSecondCoinConfigMapper.insertTSecondCoinConfig(tSecondCoinConfig);
        //周期复制
        if(null != tSecondCoinConfig.getPeriodId()){
            //复制 币种ID periodid 的周期 配置到 新增的币种中
            tSecondPeriodConfigService.copyPeriodMethod(tSecondCoinConfig.getId(),tSecondCoinConfig.getPeriodId());
        }
        HashMap<String, Object> object = new HashMap<>();
        object.put("add_coin",tSecondCoinConfig.getCoin());
        redisUtil.addStream(redisStreamNames,object);
        return 1;
    }

    /**
     * 修改秒合约币种配置
     * 
     * @param tSecondCoinConfig 秒合约币种配置
     * @return 结果
     */
    @Override
    public int updateTSecondCoinConfig(TSecondCoinConfig tSecondCoinConfig)
    {
        tSecondCoinConfig.setUpdateTime(DateUtils.getNowDate());
        return tSecondCoinConfigMapper.updateTSecondCoinConfig(tSecondCoinConfig);
    }

    /**
     * 批量删除秒合约币种配置
     * 
     * @param ids 需要删除的秒合约币种配置主键
     * @return 结果
     */
    @Override
    public int deleteTSecondCoinConfigByIds(Long[] ids)
    {
        return tSecondCoinConfigMapper.deleteTSecondCoinConfigByIds(ids);
    }

    /**
     * 删除秒合约币种配置信息
     * 
     * @param id 秒合约币种配置主键
     * @return 结果
     */
    @Override
    public int deleteTSecondCoinConfigById(Long id)
    {
        return tSecondCoinConfigMapper.deleteTSecondCoinConfigById(id);
    }

    /**
     * 批量一键添加
     * @param coins
     * @return
     */
    @Override
    public boolean batchSave(String[] coins) {
        List<TSecondCoinConfig> list = new ArrayList<>();
            for (String coin : coins) {
                TSecondCoinConfig tSecondCoinConfig1 = tSecondCoinConfigMapper.selectOne(new LambdaQueryWrapper<TSecondCoinConfig>().eq(TSecondCoinConfig::getCoin, coin.toLowerCase()));
                if(null != tSecondCoinConfig1){
                    continue;
                }
                KlineSymbol klineSymbol = klineSymbolMapper.selectOne(new LambdaQueryWrapper<KlineSymbol>().eq(KlineSymbol::getSymbol, coin.toUpperCase()));
                TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
                tSecondCoinConfig.setSymbol(coin.toLowerCase()+"usdt");
                tSecondCoinConfig.setCoin(coin.toLowerCase());
                tSecondCoinConfig.setShowSymbol(coin.toUpperCase()+"/"+"USDT");
                tSecondCoinConfig.setShowFlag(1L);
                tSecondCoinConfig.setStatus(1L);
                tSecondCoinConfig.setSort(0L);
                tSecondCoinConfig.setLogo(klineSymbol==null?null:klineSymbol.getLogo());
                tSecondCoinConfig.setCreateBy(SecurityUtils.getUsername());
                tSecondCoinConfig.setCreateTime(new Date());
                tSecondCoinConfig.setMarket(klineSymbol.getMarket());
                list.add(tSecondCoinConfig);
            }
        return this.saveBatch(list);
    }

    @Override
    public List<SymbolCoinConfigVO> getSymbolList() {
        List<SymbolCoinConfigVO> rtn = new ArrayList<SymbolCoinConfigVO>();
        TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
        tSecondCoinConfig.setStatus(1L);
        tSecondCoinConfig.setShowFlag(1L);
        List<TSecondCoinConfig> tSecondCoinConfigs = selectTSecondCoinConfigList(tSecondCoinConfig);
        tSecondCoinConfigs.stream().sorted(Comparator.comparing(TSecondCoinConfig::getSort)).collect(Collectors.toList());
        for (TSecondCoinConfig tSecondCoinConfig1: tSecondCoinConfigs ) {
            SymbolCoinConfigVO symbolCoinConfigVO = new SymbolCoinConfigVO();
            BeanUtils.copyProperties(tSecondCoinConfig1 ,symbolCoinConfigVO);
            symbolCoinConfigVO.setType(2);
            symbolCoinConfigVO.setCoinType(tSecondCoinConfig1.getType());
            BigDecimal cacheObject = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + tSecondCoinConfig1.getCoin());
            symbolCoinConfigVO.setAmount(cacheObject);
            String logo = tSecondCoinConfig1.getLogo();
            if(logo.contains("echo-res")){
                symbolCoinConfigVO.setLogo(logo);
            }else {
                symbolCoinConfigVO.setLogo("https://echo-res.oss-cn-hongkong.aliyuncs.com/waihui"+logo.substring(logo.lastIndexOf("/")));
            }
            LambdaQueryWrapper<TUserCoin> queryWrapper = new LambdaQueryWrapper<TUserCoin>();
            queryWrapper.eq(TUserCoin::getCoin, symbolCoinConfigVO.getCoin().toLowerCase());
            if(StpUtil.isLogin()){
                queryWrapper.eq(TUserCoin::getUserId, StpUtil.getLoginIdAsLong());
                TUserCoin userCoin = userCoinMapper.selectOne(queryWrapper);
                if(ObjectUtils.isNotEmpty(userCoin)){
                    symbolCoinConfigVO.setIsCollect(1);
                }else {
                    symbolCoinConfigVO.setIsCollect(2);
                }
            }
            rtn.add(symbolCoinConfigVO);

        }
        return rtn;
    }

    @Override
    public List<TSecondCoinConfig> selectBathCopySecondCoinConfigList() {
        return tSecondCoinConfigMapper.selectBathCopySecondCoinConfigList();
    }

    @Override
    public int bathCopyIng(SecondCoinCopyVO secondCoinCopyVO) {
        List<TSecondPeriodConfig> list = tSecondPeriodConfigService.list(new LambdaQueryWrapper<TSecondPeriodConfig>().eq(TSecondPeriodConfig::getSecondId, secondCoinCopyVO.getCopyId()));
        Long[] copyIds = secondCoinCopyVO.getCopyIds();
        for (Long copyId : copyIds) {
            //删除原有的
            List<TSecondPeriodConfig> copyList =new ArrayList<>();
            tSecondPeriodConfigService.remove(new LambdaQueryWrapper<TSecondPeriodConfig>().eq(TSecondPeriodConfig::getSecondId,copyId));
            for (TSecondPeriodConfig secondPeriodConfig : list) {
                secondPeriodConfig.setId(null);
                secondPeriodConfig.setSecondId(copyId);
                copyList.add(secondPeriodConfig);
            }
            tSecondPeriodConfigService.saveOrUpdateBatch(copyList);
        }
        return 1;
    }
}
