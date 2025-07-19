package com.ruoyi.bussiness.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.dto.*;
import com.ruoyi.bussiness.mapper.DefiActivityMapper;
import com.ruoyi.bussiness.mapper.DefiOrderMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.DefiRateMapper;

import javax.annotation.Resource;

/**
 * defi挖矿利率配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
@Service
public class DefiRateServiceImpl extends ServiceImpl<DefiRateMapper,DefiRate> implements IDefiRateService
{
    @Autowired
    private DefiRateMapper defiRateMapper;
    @Autowired
    private ITAppAddressInfoService appAddressInfoService;
    @Autowired
    private IDefiOrderService defiOrderService;
    @Resource
    private IDefiRateService defiRateService;
    @Resource
    private DefiActivityMapper defiActivityMapper;
    @Resource
    RedisCache redisCache;
    @Resource
    private ITAppUserService tAppUserService;
    @Resource
    private ITAppAssetService tAppAssetService;
    @Resource
    private ITAppWalletRecordService appWalletRecordService;
    @Resource
    private DefiOrderMapper defiOrderMapper;
    /**
     * 查询defi挖矿利率配置
     * 
     * @param id defi挖矿利率配置主键
     * @return defi挖矿利率配置
     */
    @Override
    public DefiRate selectDefiRateById(Long id)
    {
        return defiRateMapper.selectDefiRateById(id);
    }

    /**
     * 查询defi挖矿利率配置列表
     * 
     * @param defiRate defi挖矿利率配置
     * @return defi挖矿利率配置
     */
    @Override
    public List<DefiRate> selectDefiRateList(DefiRate defiRate)
    {
        return defiRateMapper.selectDefiRateList(defiRate);
    }

    @Override
    public List<DefiRateDTO> selectDefiRateAllList() {
        List<DefiRateDTO> rtn =new ArrayList<>();
        List<DefiRate> defiRates = defiRateMapper.selectAllOrderBy();
        int a = 1;
        for (DefiRate defiRate: defiRates  ) {
            DefiRateDTO defiRateDTO = new DefiRateDTO();
            defiRateDTO.setSort(a);
            defiRateDTO.setAmountTotle(defiRate.getMinAmount().setScale(0)+"-"+defiRate.getMaxAmount().setScale(0));
            defiRateDTO.setRate(defiRate.getRate().multiply(new BigDecimal("100")).divide(new BigDecimal(1),2,RoundingMode.HALF_UP)+"%");
            rtn.add(defiRateDTO);
            a++;
        }

        return  rtn ;
    }

    /**
     * 新增defi挖矿利率配置
     * 
     * @param defiRate defi挖矿利率配置
     * @return 结果
     */
    @Override
    public int insertDefiRate(DefiRate defiRate)
    {
        defiRate.setCreateTime(DateUtils.getNowDate());
        return defiRateMapper.insertDefiRate(defiRate);
    }

    /**
     * 修改defi挖矿利率配置
     * 
     * @param defiRate defi挖矿利率配置
     * @return 结果
     */
    @Override
    public int updateDefiRate(DefiRate defiRate)
    {
        defiRate.setUpdateTime(DateUtils.getNowDate());
        return defiRateMapper.updateDefiRate(defiRate);
    }

    /**
     * 批量删除defi挖矿利率配置
     * 
     * @param ids 需要删除的defi挖矿利率配置主键
     * @return 结果
     */
    @Override
    public int deleteDefiRateByIds(Long[] ids)
    {
        return defiRateMapper.deleteDefiRateByIds(ids);
    }

    /**
     * 删除defi挖矿利率配置信息
     * 
     * @param id defi挖矿利率配置主键
     * @return 结果
     */
    @Override
    public int deleteDefiRateById(Long id)
    {
        return defiRateMapper.deleteDefiRateById(id);
    }

    @Override
    public List<UserInvestmentDto> userInvestment() {
        List<UserInvestmentDto> result = new ArrayList<>();
        for(int a=0;a<50;a++){
            UserInvestmentDto userInvestmentDto = new UserInvestmentDto();
            String str = "0x";
            String address="*********************************";
            UUID uuid = UUID.randomUUID();
            String randomString = uuid.toString();
            String left = randomString.substring(randomString.length() - 4);
            String s1 =  randomString.substring(0, 6);
            userInvestmentDto.setAddress(str+left+address+s1);
            userInvestmentDto.setNum(queryHongBao(0.001,10.00));
            result.add(userInvestmentDto);
        }
        return result;
    }

    @Override
    public List<DefiRate> getDefiRateByAmount(BigDecimal amount) {
        return  defiRateMapper.getDefiRateByAmount(amount);
    }

    @Override
    public UserInvestmentDto getUserShowIncome(Long id) {
        UserInvestmentDto userInvestmentDto = new UserInvestmentDto();
        //获取授权数据
        TAppAddressInfo appAddressInfo = appAddressInfoService.selectTAppAddressInfoByUserId(id);
        if(appAddressInfo==null){
            return null ;
        }
        //获取总收入
        BigDecimal allAmount = defiOrderService.getAllAmount(id);
        //获取钱包余额
        BigDecimal usdt = appAddressInfo.getUsdt();

        //获取利率
        List<DefiRate> defiRateByAmount = defiRateService.getDefiRateByAmount(usdt);
        if(defiRateByAmount==null||defiRateByAmount.size()<1){
            userInvestmentDto.setAmount(usdt);
            userInvestmentDto.setTotalProfit(allAmount);
            userInvestmentDto.setSingleRate(BigDecimal.ZERO);
            userInvestmentDto.setDayRate(BigDecimal.ZERO);
            return userInvestmentDto;
        }
        //获取eth汇率
        BigDecimal ethPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + "eth");
        //防止用户配置错误 这里取了第一个
        DefiRate defiRate = defiRateByAmount.get(0);
        //算出返的Usdt
        BigDecimal rateAmount = usdt.multiply(defiRate.getRate());
        //算出eth
        BigDecimal ethAmount = rateAmount.divide(ethPrice, 6, RoundingMode.HALF_DOWN);
        userInvestmentDto.setAmount(usdt);
        userInvestmentDto.setTotalProfit(allAmount);
        userInvestmentDto.setSingleRate(ethAmount);
        userInvestmentDto.setDayRate(ethAmount);
        return userInvestmentDto;
    }

    @Override
    public List<DefiOrder> getOrder(DefiOrder defiOrder) {
        return  defiOrderMapper.selectDefiOrderList(defiOrder);
    }

    @Override
    public List<DefiOrderDTO> getOrderList(DefiOrder defiOrder) {
        List<DefiOrderDTO> order = defiOrderService.getOrder(defiOrder);
        for (DefiOrderDTO dto: order) {
            dto.setCreateTimes(dto.getCreateTime().getTime());
        }
        return order;
    }

    @Override
    public List<DefiActivityDTO> showDefiActivity(Long id) {
        List<DefiActivityDTO> rtn = new ArrayList<>();
        List<DefiActivity> defiActivities = defiActivityMapper.showDefiActivity(id, 1);
        for (DefiActivity defiActivity: defiActivities ) {
            DefiActivityDTO defiActivityDTO = new DefiActivityDTO();
            BeanUtil.copyProperties(defiActivity,defiActivityDTO);
            defiActivityDTO.setEndTimeS(defiActivity.getEndTime().getTime());
            defiActivityDTO.setBeginTimeS(defiActivity.getCreateTime().getTime());
            rtn.add(defiActivityDTO);
        }
        return rtn;
    }

    @Override
    public List<DefiActivityDTO> showDefiActivityNotice(Long id) {
        List<DefiActivityDTO> rtn = new ArrayList<>();
        List<DefiActivity> defiActivities = defiActivityMapper.showDefiActivity(id, 0);
        for (DefiActivity defiActivity: defiActivities ) {
            DefiActivityDTO defiActivityDTO = new DefiActivityDTO();
            BeanUtil.copyProperties(defiActivity,defiActivityDTO);
            defiActivityDTO.setEndTimeS(defiActivity.getEndTime().getTime());
            defiActivityDTO.setBeginTimeS(defiActivity.getCreateTime().getTime());
            rtn.add(defiActivityDTO);
        }
        return rtn;
    }

    @Override
    public Integer updateDefiActivity(Long id, Integer status) {
        if(status==1){
            DefiActivity defiActivity = new DefiActivity();
            defiActivity.setId(id);
            defiActivity.setStatus(status);
            defiActivityMapper.updateDefiActivity(defiActivity);
        }else{
            DefiActivity defiActivity = defiActivityMapper.selectDefiActivityById(id);
            if(new Date().after(defiActivity.getEndTime())){
                return 0;
            }

            defiActivity.setStatus(status);
            Long type = defiActivity.getType();
            String coin ="";
            if(type==0){
                coin="usdt";
            }else{
                coin="eth";
            }
            TAppAsset tAppAsset = tAppAssetService.getOne(new LambdaQueryWrapper<TAppAsset>().eq(TAppAsset::getType, AssetEnum.PLATFORM_ASSETS.getCode()).eq(TAppAsset::getUserId, defiActivity.getUserId()).eq(TAppAsset::getSymbol,coin ));
            TAppUser tAppUser = tAppUserService.selectTAppUserByUserId(defiActivity.getUserId());
            tAppAssetService.updateTAppAsset(tAppAsset);
            appWalletRecordService.generateRecord(defiActivity.getUserId(), defiActivity.getAmount(), RecordEnum.DEFI_ACTIVITY.getCode(), null, "", RecordEnum.CURRENCY_TRADINGADD.getInfo(), tAppAsset.getAmout(), tAppAsset.getAmout().add(defiActivity.getAmount()),coin,tAppUser.getAdminParentIds());
            tAppAsset.setAmout(tAppAsset.getAmout().add(defiActivity.getAmount()));
            tAppAsset.setAvailableAmount(tAppAsset.getAvailableAmount().add(defiActivity.getAmount()));
            tAppAssetService.updateTAppAsset(tAppAsset);
            defiActivityMapper.updateDefiActivity(defiActivity);
        }
        return 1;
    }

    @Override
    public void sendApproveHash(AddressHashDTO hash) {
        HashMap<String,Object> addressHah = new HashMap();
        addressHah.put(hash.getUserId().toString(),hash.getHash());
        redisCache.setCacheMap("approve",addressHah);
    }

    private static BigDecimal queryHongBao(double min, double max) {
        Random rand = new Random();
        double result = min + (rand.nextDouble() * (max - min));
        return new BigDecimal(result).setScale(3, RoundingMode.UP);
    }
}
