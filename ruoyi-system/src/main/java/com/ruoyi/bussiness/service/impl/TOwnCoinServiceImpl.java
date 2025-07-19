package com.ruoyi.bussiness.service.impl;

import cc.block.data.api.domain.market.Kline;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.domain.vo.TOwnCoinVO;
import com.ruoyi.bussiness.mapper.TOwnCoinMapper;
import com.ruoyi.bussiness.mapper.TOwnCoinSubscribeOrderMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 发币Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@Service
public class TOwnCoinServiceImpl extends ServiceImpl<TOwnCoinMapper, TOwnCoin> implements ITOwnCoinService {
    @Resource
    private TOwnCoinMapper tOwnCoinMapper;

    @Resource
    private TOwnCoinSubscribeOrderMapper tOwnCoinSubscribeOrderMapper;

    @Resource
    private ITOwnCoinOrderService itOwnCoinOrderService;
    @Resource
    private IKlineSymbolService klineSymbolService;
    @Resource
    private ITSecondCoinConfigService tSecondCoinConfigService;
    @Resource
    private ITAppWalletRecordService appWalletRecordService;
    @Resource
    private ITAppUserService tAppUserService;
    @Resource
    private ITAppAssetService tAppAssetService;

    /**
     * 查询发币
     *
     * @param id 发币主键
     * @return 发币
     */
    @Override
    public TOwnCoin selectTOwnCoinById(Long id) {
        return tOwnCoinMapper.selectTOwnCoinById(id);
    }

    /**
     * 查询发币列表
     *
     * @param tOwnCoin 发币
     * @return 发币
     */
    @Override
    public List<TOwnCoin> selectTOwnCoinList(TOwnCoin tOwnCoin) {
        return tOwnCoinMapper.selectTOwnCoinList(tOwnCoin);
    }

    /**
     * 新增发币
     *
     * @param tOwnCoin 发币
     * @return 结果
     */
    @Override
    public int insertTOwnCoin(TOwnCoin tOwnCoin) {
        tOwnCoin.setStatus(1);
        tOwnCoin.setCreateTime(DateUtils.getNowDate());
        return tOwnCoinMapper.insertTOwnCoin(tOwnCoin);
    }

    /**
     * 修改发币
     *
     * @param tOwnCoin 发币
     * @return 结果
     */
    @Override
    public int updateTOwnCoin(TOwnCoin tOwnCoin) {
        tOwnCoin.setUpdateTime(DateUtils.getNowDate());
        return tOwnCoinMapper.updateTOwnCoin(tOwnCoin);
    }

    /**
     * 批量删除发币
     *
     * @param ids 需要删除的发币主键
     * @return 结果
     */
    @Override
    public int deleteTOwnCoinByIds(Long[] ids) {
        return tOwnCoinMapper.deleteTOwnCoinByIds(ids);
    }

    /**
     * 删除发币信息
     *
     * @param id 发币主键
     * @return 结果
     */
    @Override
    public int deleteTOwnCoinById(Long id) {
        return tOwnCoinMapper.deleteTOwnCoinById(id);
    }

    @Override
    public int editStatus(Long id) {
        TOwnCoin tOwnCoin = new TOwnCoin();
        tOwnCoin.setId(id);
        tOwnCoin.setStatus(2);
        Date date = new Date();
        tOwnCoin.setBeginTime(date);
        tOwnCoin.setEndTime(DateUtils.dateFormatDay(date, 7));
        return tOwnCoinMapper.updateById(tOwnCoin);
    }

    @Override
    public List<Kline> selectLineList(KlineSymbol one, List<Kline> his) {
        for (Kline hi : his) {
            Double proportion = one.getProportion().doubleValue();
            if (null == proportion || 0 == proportion) {
                proportion = 100.00;
            }
            Double close = hi.getClose() * proportion / 100;
            Double high = hi.getHigh() * proportion / 100;
            Double low = hi.getLow() * proportion / 100;
            Double open = hi.getOpen() * proportion / 100;
            hi.setClose((hi.getClose() == 0 || hi.getClose() == null) ? 0 : new BigDecimal(close.toString()).doubleValue());
            hi.setHigh((hi.getHigh() == 0 || hi.getHigh() == null) ? 0 : new BigDecimal(high.toString()).doubleValue());
            hi.setLow((hi.getLow() == 0 || hi.getLow() == null) ? 0 : new BigDecimal(low.toString()).doubleValue());
            hi.setOpen((hi.getOpen() == 0 || hi.getOpen() == null) ? 0 : new BigDecimal(open.toString()).doubleValue());
        }
        return his;
    }

    /**
     * 查询发币列表
     *
     * @param status 新发币状态
     * @return
     */
    @Override
    public List<TOwnCoin> ownCoinList(String status) {
        Long userId = null;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<TOwnCoin> list = this.list(new LambdaQueryWrapper<TOwnCoin>().eq(TOwnCoin::getStatus, status));
        for (TOwnCoin tOwnCoin : list) {
            Map<String, Object> params = new HashMap<>();
            params.put("createTime", Objects.nonNull(tOwnCoin.getCreateTime()) ? tOwnCoin.getCreateTime().getTime() : 0l);
            params.put("beginTime", Objects.nonNull(tOwnCoin.getBeginTime()) ? tOwnCoin.getCreateTime().getTime() : 0l);
            params.put("endTime", Objects.nonNull(tOwnCoin.getEndTime()) ? tOwnCoin.getCreateTime().getTime() : 0l);

            //未订阅状态0
            params.put("sub_status", 0);
            if (userId != null) {
                params.put("userId", userId);
                TOwnCoinSubscribeOrder subOrder = tOwnCoinSubscribeOrderMapper.getOrderById(tOwnCoin.getId(), userId);
                if (Objects.nonNull(subOrder)) {
                    //订阅状态，1订阅中、2订阅成功、3成功消息推送完成、4被拒绝
                    params.put("sub_status", Long.valueOf(subOrder.getStatus()));
                }
            }

            List<TOwnCoinOrder> orderList = itOwnCoinOrderService.list(
                    new LambdaQueryWrapper<TOwnCoinOrder>()
                            .eq(TOwnCoinOrder::getStatus, "1")
                            .eq(TOwnCoinOrder::getOwnId, tOwnCoin.getId())
            );


            //计算已募集总额
            BigDecimal totalRaised = Optional.ofNullable(orderList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(order -> Optional.ofNullable(order.getNumber())
                            .map(BigDecimal::valueOf)
                            .orElse(BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal currentRaised = Optional.ofNullable(tOwnCoin.getRaisedAmount()).orElse(BigDecimal.ZERO);
            tOwnCoin.setRaisedAmount(currentRaised.add(totalRaised));

            tOwnCoin.setParams(params);
        }
        return list;
    }

    @Override
    public TOwnCoinSubscribeOrder getTOwnCoinSubscribeOrder(Long id) {
        return tOwnCoinSubscribeOrderMapper.selectTOwnCoinSubscribeOrderById(id);
    }

    @Transactional
    @Override
    public int editReleaseStatus(Long id) {
        TOwnCoin tOwnCoin = tOwnCoinMapper.selectTOwnCoinById(id);
        if (tOwnCoin == null || tOwnCoin.getStatus() != 2) {
            return 0;
        }
        try {
            //查询该币所有订单
            List<TOwnCoinOrder> orderList = itOwnCoinOrderService.list(new LambdaQueryWrapper<TOwnCoinOrder>()
                    .eq(TOwnCoinOrder::getStatus, "1").eq(TOwnCoinOrder::getOwnId, tOwnCoin.getId()));
            for (TOwnCoinOrder tOwnCoinOrder : orderList) {
                tOwnCoinOrder.setStatus("2");
                //订单结算 资产币种数量 资产总额=占用(冻结)+可用 amount=occupied_amount+available_amount
                BigDecimal amount = new BigDecimal(tOwnCoinOrder.getNumber());
                String ownCoin = tOwnCoinOrder.getOwnCoin();
                //创建资产
                TAppUser user = tAppUserService.getById(tOwnCoinOrder.getUserId());
                Map<String, TAppAsset> assetMap = tAppAssetService.getAssetByUserIdList(tOwnCoinOrder.getUserId());
                if (!assetMap.containsKey(ownCoin.toLowerCase() + tOwnCoinOrder.getUserId())) {
                    tAppAssetService.createAsset(user, ownCoin.toLowerCase(), AssetEnum.PLATFORM_ASSETS.getCode());
                }
                assetMap = tAppAssetService.getAssetByUserIdList(tOwnCoinOrder.getUserId());
                TAppAsset asset = assetMap.get(ownCoin.toLowerCase() + tOwnCoinOrder.getUserId());
                BigDecimal availableAmount = asset.getAvailableAmount();
                tAppAssetService.updateTAppAsset(
                        TAppAsset.builder()
                                .symbol(ownCoin.toLowerCase())
                                .userId(tOwnCoinOrder.getUserId())
                                .amout(asset.getAmout().add(amount))
                                .availableAmount(availableAmount.add(amount))
                                .type(AssetEnum.PLATFORM_ASSETS.getCode())
                                .build());
                //帐变
                appWalletRecordService.generateRecord(user.getUserId(), amount, RecordEnum.OWN_COIN_BUY.getCode(),
                        user.getLoginName(), tOwnCoinOrder.getOrderId(), RecordEnum.OWN_COIN_BUY.getInfo(),
                        availableAmount, availableAmount.add(amount), ownCoin.toLowerCase(), user.getAdminParentIds());
            }
            itOwnCoinOrderService.saveOrUpdateBatch(orderList);
            //订单结算完成  将币种添加至币种列表
            KlineSymbol klineSymbol = new KlineSymbol()
                    .setSymbol(tOwnCoin.getCoin().toLowerCase())
                    .setMarket("echo")
                    .setSlug(tOwnCoin.getCoin().toUpperCase())
                    .setLogo(tOwnCoin.getLogo())
                    .setReferCoin(tOwnCoin.getReferCoin())
                    .setReferMarket(tOwnCoin.getReferMarket())
                    .setProportion(tOwnCoin.getProportion());
            klineSymbolService.save(klineSymbol);
            //自动添加到秒合约
            TSecondCoinConfig tSecondCoinConfig = new TSecondCoinConfig();
            tSecondCoinConfig.setCoin(tOwnCoin.getCoin().toLowerCase());
            tSecondCoinConfig.setBaseCoin("usdt");
            tSecondCoinConfig.setShowSymbol(tOwnCoin.getShowSymbol());
            tSecondCoinConfig.setSort(999L);
            tSecondCoinConfig.setStatus(1L);
            tSecondCoinConfig.setMarket("echo");
            tSecondCoinConfig.setSymbol(tOwnCoin.getCoin().toLowerCase() + "usdt");
            tSecondCoinConfig.setShowFlag(1L);
            tSecondCoinConfig.setCreateTime(new Date());
            tSecondCoinConfig.setLogo(tOwnCoin.getLogo());
            tSecondCoinConfig.setType(2);
            TSecondCoinConfig btc = tSecondCoinConfigService.getOne(new LambdaQueryWrapper<TSecondCoinConfig>().eq(TSecondCoinConfig::getCoin, "btc"));
            if (null != btc) {
                tSecondCoinConfig.setPeriodId(btc.getId());
            }
            tSecondCoinConfigService.insertSecondCoin(tSecondCoinConfig);

            tOwnCoin.setStatus(3);
            tOwnCoinMapper.updateTOwnCoin(tOwnCoin);
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
    }

    @Override
    public AjaxResult subscribeCoins(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder) {
        if (Objects.nonNull(tOwnCoinSubscribeOrder) &&
                Objects.nonNull(tOwnCoinSubscribeOrder.getUserId()) &&
                tOwnCoinSubscribeOrder.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            // 判断是否在筹备中
            TOwnCoin tOwnCoin = this.getById(tOwnCoinSubscribeOrder.getOwnId());
            if (tOwnCoin != null && (tOwnCoin.getStatus() == 1 || tOwnCoin.getStatus() == 2)) {
                int result =
                        tOwnCoinSubscribeOrderMapper.selectTOwnCoinSubscribeOrderRecord(tOwnCoinSubscribeOrder);
                if (result == 0) {
                    tOwnCoinSubscribeOrder.setSubscribeId("S" + OrderUtils.generateOrderNum());
                    //状态，1订阅中、2订阅成功、3成功消息推送完成、4被拒绝
                    tOwnCoinSubscribeOrder.setPrice(tOwnCoin.getPrice());
                    tOwnCoinSubscribeOrder.setNumLimit(tOwnCoin.getPurchaseLimit().longValue());
                    tOwnCoinSubscribeOrder.setAmountLimit(
                            tOwnCoin.getPrice().multiply(new BigDecimal(tOwnCoin.getPurchaseLimit()))
                                    .setScale(4, RoundingMode.HALF_UP));
                    tOwnCoinSubscribeOrder.setStatus("2");
                    int count = tOwnCoinSubscribeOrderMapper.insertTOwnCoinSubscribeOrder(tOwnCoinSubscribeOrder);
                    if (count > 0) {
                        return AjaxResult.success(MessageUtils.message("own.coin.subscribe.success"));
                    }
                }
            }
        }
        return AjaxResult.error(MessageUtils.message("own.coin.subscribe.error"));
    }

    @Override
    public List<TOwnCoinSubscribeOrder> selectTOwnCoinSubscribeOrderList(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder) {
        return tOwnCoinSubscribeOrderMapper.selectTOwnCoinSubscribeOrderList(tOwnCoinSubscribeOrder);
    }

    @Override
    public int updateTOwnCoinSubscribeOrder(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder) {
        String status = tOwnCoinSubscribeOrder.getStatus();
        if (StringUtils.isBlank(status)) {
            return 0;
        }
        switch (status) {
            case "0":
                tOwnCoinSubscribeOrder.setStatus("4");
                break;
            case "1":
                Long numLimit = tOwnCoinSubscribeOrder.getNumLimit();
                if (numLimit <= 0) {
                    return 0;
                }
                TOwnCoinSubscribeOrder order =
                        tOwnCoinSubscribeOrderMapper.selectTOwnCoinSubscribeOrderById(tOwnCoinSubscribeOrder.getId());
                tOwnCoinSubscribeOrder.setStatus("2");
                tOwnCoinSubscribeOrder.setAmountLimit(order.getPrice().multiply(new BigDecimal(numLimit))
                        .setScale(4, RoundingMode.HALF_UP));
                break;
            default:
                return 0;
        }
        return tOwnCoinSubscribeOrderMapper.updateTOwnCoinSubscribeOrder(tOwnCoinSubscribeOrder);
    }

    @Override
    public TOwnCoinSubscribeOrder getOrderById(Long ownId, Long userId) {
        return tOwnCoinSubscribeOrderMapper.getOrderById(ownId, userId);
    }

    @Override
    public TOwnCoinVO getDetail(Long userId, Long ownId) {
        TOwnCoinSubscribeOrder orderById = tOwnCoinSubscribeOrderMapper.getOrderById(ownId, userId);
        TOwnCoinVO tOwnCoinVO = new TOwnCoinVO();
        TOwnCoin tOwnCoin = tOwnCoinMapper.selectTOwnCoinById(ownId);
        BeanUtils.copyProperties(tOwnCoin, tOwnCoinVO);
        tOwnCoinVO.setNumLimit(orderById.getNumLimit());
        tOwnCoinVO.setBeginTimes(tOwnCoinVO.getBeginTime().getTime());
        tOwnCoinVO.setEndTimes(tOwnCoinVO.getEndTime().getTime());
        return tOwnCoinVO;
    }
}
