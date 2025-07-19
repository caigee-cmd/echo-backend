package com.ruoyi.web.controller.bussiness;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import javax.annotation.Resource;
import javax.security.auth.callback.LanguageCallback;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TExchangeCoinRecord;
import com.ruoyi.bussiness.domain.TSymbolManage;
import com.ruoyi.bussiness.service.ITAppAssetService;
import com.ruoyi.bussiness.service.ITExchangeCoinRecordService;
import com.ruoyi.bussiness.service.ITSymbolManageService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 币种兑换记录Controller
 * 
 * @author ruoyi
 * @date 2023-07-07
 */
@RestController
@RequestMapping("/api/texchange")
public class TExchangeCoinRecordController extends ApiBaseController
{
    @Autowired
    private ITExchangeCoinRecordService tExchangeCoinRecordService;
    @Autowired
    private ITAppAssetService assetService;
    @Resource
    private ITSymbolManageService tSymbolManageService;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询币种兑换记录列表
     */
    @PostMapping("/list")
    public TableDataInfo list(TExchangeCoinRecord tExchangeCoinRecord)
    {
        startPage();
        List<TExchangeCoinRecord> list = tExchangeCoinRecordService.selectTExchangeCoinRecordList(tExchangeCoinRecord);
        return getDataTable(list);
    }


    @PostMapping("/export")
    public void export(HttpServletResponse response, TExchangeCoinRecord tExchangeCoinRecord)
    {
        List<TExchangeCoinRecord> list = tExchangeCoinRecordService.selectTExchangeCoinRecordList(tExchangeCoinRecord);
        ExcelUtil<TExchangeCoinRecord> util = new ExcelUtil<TExchangeCoinRecord>(TExchangeCoinRecord.class);
        util.exportExcel(response, list, "币种兑换记录数据");
    }

    /**
     * 获取币种兑换记录详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tExchangeCoinRecordService.selectTExchangeCoinRecordById(id));
    }


    /**
     * 币种兑换
     * @return
     */
    @PostMapping("/currencyExchange")
    public AjaxResult currencyExchange( @RequestBody Map<String, Object> params){
        int i = 0;
        if (StringUtils.isNotNull(params)){
            String fromSymbol = String.valueOf(params.get("fromSymbol"));
            String toSymbol =String.valueOf(params.get("toSymbol"));
            BigDecimal total = new BigDecimal(String.valueOf(params.get("total")));

            BigDecimal from =fromSymbol.toLowerCase().equals("usdt")?new BigDecimal(1): redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+fromSymbol.toLowerCase());
            BigDecimal to = toSymbol.toLowerCase().equals("usdt")?new BigDecimal(1):redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix()+toSymbol.toLowerCase());
            if(from==null&&to==null){
                return AjaxResult.error("未匹配到三方提供的汇率! origin:{}", fromSymbol+toSymbol);
            }
            BigDecimal thirdRate=  from.divide(to, 8, RoundingMode.DOWN);
            BigDecimal multiply = total.multiply(thirdRate);

            if (StringUtils.isNotBlank(fromSymbol) && StringUtils.isNotBlank(toSymbol) && Objects.nonNull(total) && total.compareTo(BigDecimal.ZERO) > 0){
                TSymbolManage symbolManage = tSymbolManageService.getOne(new LambdaQueryWrapper<TSymbolManage>().eq(TSymbolManage::getSymbol, toSymbol));
                if (Objects.nonNull(symbolManage)){
                    if (multiply.compareTo(symbolManage.getMinChargeNum())<0){
                        return AjaxResult.error(symbolManage.getSymbol()+MessageUtils.message("currency.exchange_min",symbolManage.getMinChargeNum()));
                    }
                    if (multiply.compareTo(symbolManage.getMaxChargeNum())>0){
                        return AjaxResult.error(symbolManage.getSymbol()+MessageUtils.message("currency.exchange_max",symbolManage.getMaxChargeNum()));
                    }
                 }else{
                    return AjaxResult.error(MessageUtils.message("exchange_symbol_error_exist"));
                }
                TAppUser user = getAppUser();
                Map<String, TAppAsset> assetMap=assetService.getAssetByUserIdList(user.getUserId());
                boolean checkBalance = judgeAmount(assetMap,fromSymbol,total,user.getUserId());
                if (!checkBalance) {
                    return AjaxResult.error(MessageUtils.message("exchange_error"));
                }
                Integer submittedRecord = tExchangeCoinRecordService.countBySubmittedRecord(user.getUserId(), fromSymbol, toSymbol);
                if (submittedRecord > 0) {
                    return AjaxResult.error(MessageUtils.message("exchange.record.exist.error"));
                }
                i = tExchangeCoinRecordService.insertRecord(user, params);
            }else{
                return AjaxResult.error(MessageUtils.message("exchange.symbol.exist"));
            }
        }else{
            return AjaxResult.error(MessageUtils.message("exchange.symbol.exist"));
        }
        return toAjax(i);
    }

    private boolean judgeAmount(Map<String, TAppAsset> map, String fromSymbol, BigDecimal total, Long userId) {
        BigDecimal usdt = map.get(fromSymbol.toLowerCase()+userId)==null?BigDecimal.ZERO:map.get(fromSymbol.toLowerCase()+userId).getAvailableAmount();
        if(usdt.compareTo(total)>=0){
            return true;
        }
        return false;
    }

    /**
     * 获取币种的价格
     */
    @PostMapping("/getCurrencyPrice")
    public AjaxResult getCurrencyPrice(String[] currency){
        return AjaxResult.success(tExchangeCoinRecordService.getCurrencyPrice(currency));
    }
}
