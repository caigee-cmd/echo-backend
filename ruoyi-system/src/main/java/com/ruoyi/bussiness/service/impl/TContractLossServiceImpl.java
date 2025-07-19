package com.ruoyi.bussiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.domain.TContractOrder;
import com.ruoyi.bussiness.domain.TContractPosition;
import com.ruoyi.bussiness.service.ITContractCoinService;
import com.ruoyi.bussiness.service.ITContractPositionService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TContractLossMapper;
import com.ruoyi.bussiness.domain.TContractLoss;
import com.ruoyi.bussiness.service.ITContractLossService;

import javax.annotation.Resource;

/**
 * 止盈止损表Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-25
 */
@Service
public class TContractLossServiceImpl extends ServiceImpl<TContractLossMapper, TContractLoss> implements ITContractLossService {
    @Autowired
    private TContractLossMapper tContractLossMapper;

    @Autowired
    private ITContractPositionService contractPositionService;

    @Resource
    private RedisCache redisCache;
    @Resource
    private ITContractCoinService contractCoinService;

    /**
     * 查询止盈止损表
     *
     * @param id 止盈止损表主键
     * @return 止盈止损表
     */
    @Override
    public TContractLoss selectTContractLossById(Long id) {
        return tContractLossMapper.selectTContractLossById(id);
    }

    /**
     * 查询止盈止损表列表
     *
     * @param tContractLoss 止盈止损表
     * @return 止盈止损表
     */
    @Override
    public List<TContractLoss> selectTContractLossList(TContractLoss tContractLoss) {
        return tContractLossMapper.selectTContractLossList(tContractLoss);
    }

    /**
     * 新增止盈止损表
     *
     * @param tContractLoss 止盈止损表
     * @return 结果
     */
    @Override
    public int insertTContractLoss(TContractLoss tContractLoss) {
        tContractLoss.setCreateTime(DateUtils.getNowDate());
        return tContractLossMapper.insertTContractLoss(tContractLoss);
    }

    /**
     * 修改止盈止损表
     *
     * @param tContractLoss 止盈止损表
     * @return 结果
     */
    @Override
    public int updateTContractLoss(TContractLoss tContractLoss) {
        tContractLoss.setUpdateTime(DateUtils.getNowDate());
        return tContractLossMapper.updateTContractLoss(tContractLoss);
    }

    /**
     * 批量删除止盈止损表
     *
     * @param ids 需要删除的止盈止损表主键
     * @return 结果
     */
    @Override
    public int deleteTContractLossByIds(Long[] ids) {
        return tContractLossMapper.deleteTContractLossByIds(ids);
    }

    /**
     * 删除止盈止损表信息
     *
     * @param id 止盈止损表主键
     * @return 结果
     */
    @Override
    public int deleteTContractLossById(Long id) {
        return tContractLossMapper.deleteTContractLossById(id);
    }

    /**
     * 设置止盈止损
     *
     * @param id
     * @param contractLoss
     * @return
     */
    @Override
    public String cntractLossSett(  TContractLoss contractLoss) {

        //仓位
        TContractPosition contractPosition = contractPositionService.selectTContractPositionById(contractLoss.getPositionId());
        //购买类型 0 买多 1卖空
        Integer type = contractPosition.getType();

        BigDecimal earnPrice = contractLoss.getEarnPrice();

        BigDecimal lossPrice = contractLoss.getLosePrice();

        BigDecimal earnNum = contractLoss.getEarnNumber();

        BigDecimal lossNum = contractLoss.getLoseNumber();

        BigDecimal num = contractPosition.getOpenNum();

        BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + contractPosition.getSymbol());

        BigDecimal openPrice = contractPosition.getOpenPrice();
        //市价1 0限价
        Integer deleGateType = contractLoss.getDelegateType();
        if (1 == deleGateType) {
            if (Objects.nonNull(earnPrice)) {
                contractLoss.setEarnDelegatePrice(currentlyPrice);
            }
            if (Objects.nonNull(lossPrice)) {
                contractLoss.setLoseDelegatePrice(currentlyPrice);
            }
        }
        if (type == 0) {
            if (Objects.nonNull(earnPrice) && earnPrice.compareTo(openPrice) < 0) {
                return MessageUtils.message("contract.buy.earn.error");
            }
            if (Objects.nonNull(lossPrice) && lossPrice.compareTo(openPrice) > 0) {
                return MessageUtils.message("contract.buy.loss.error");
            }
        } else if (type == 1) {
            if (Objects.nonNull(earnPrice) && earnPrice.compareTo(openPrice) > 0) {
                return MessageUtils.message("contract.sell.earn.error");
            }
            if ((Objects.nonNull(lossPrice) && lossPrice.compareTo(openPrice) < 0)) {
                return MessageUtils.message("contract.sell.loss.error");
            }
        }
        if ((Objects.nonNull(earnNum) && earnNum.compareTo(num) > 0)) {
            return MessageUtils.message("contract.num.limit");
        }
        if ((Objects.nonNull(lossNum) && lossNum.compareTo(num) > 0)) {
            return MessageUtils.message("contract.num.limit");
        }
        contractLoss.setCreateTime(new Date());
        contractLoss.setStatus(0);
        contractLoss.setPositionId(contractPosition.getId());
        contractLoss.setUserId(contractPosition.getUserId());
        contractLoss.setType(contractPosition.getType());
        contractLoss.setSymbol(contractPosition.getSymbol());
        contractLoss.setLeverage(contractPosition.getLeverage());
        if (Objects.nonNull(earnNum)) {
            contractLoss.setEarnNumber(earnNum);
        }
        if (Objects.nonNull(lossNum)) {
            contractLoss.setLoseNumber(lossNum);
        }
        tContractLossMapper.insertTContractLoss(contractLoss);
        return "success";
    }

    @Override
    public void updateContractLoss(Long positionId) {
        tContractLossMapper.updateContractLoss(positionId);
    }
}
