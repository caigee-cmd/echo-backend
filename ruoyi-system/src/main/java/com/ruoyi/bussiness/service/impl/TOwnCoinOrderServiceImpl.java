package com.ruoyi.bussiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.*;
import com.ruoyi.bussiness.mapper.TOwnCoinOrderMapper;
import com.ruoyi.bussiness.mapper.TOwnCoinSubscribeOrderMapper;
import com.ruoyi.bussiness.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.AssetEnum;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * 申购订单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-20
 */
@Service
@Slf4j
public class TOwnCoinOrderServiceImpl extends ServiceImpl<TOwnCoinOrderMapper, TOwnCoinOrder> implements ITOwnCoinOrderService {
    @Resource
    private TOwnCoinOrderMapper tOwnCoinOrderMapper;
    @Resource
    private ITAppUserService tAppUserService;
    @Resource
    private ITOwnCoinService tOwnCoinService;
    @Resource
    private TOwnCoinSubscribeOrderMapper tOwnCoinSubscribeOrderMapper;
    @Resource
    private ITAppAssetService tAppAssetService;
    @Resource
    private ITAppWalletRecordService appWalletRecordService;

    /**
     * 查询申购订单
     *
     * @param id 申购订单主键
     * @return 申购订单
     */
    @Override
    public TOwnCoinOrder selectTOwnCoinOrderById(Long id) {
        return tOwnCoinOrderMapper.selectTOwnCoinOrderById(id);
    }

    /**
     * 查询申购订单列表
     *
     * @param tOwnCoinOrder 申购订单
     * @return 申购订单
     */
    @Override
    public List<TOwnCoinOrder> selectTOwnCoinOrderList(TOwnCoinOrder tOwnCoinOrder) {
        return tOwnCoinOrderMapper.selectTOwnCoinOrderList(tOwnCoinOrder);
    }

    /**
     * 新增申购订单
     *
     * @param tOwnCoinOrder 申购订单
     * @return 结果
     */
    @Override
    public int insertTOwnCoinOrder(TOwnCoinOrder tOwnCoinOrder) {
        tOwnCoinOrder.setCreateTime(DateUtils.getNowDate());
        return tOwnCoinOrderMapper.insertTOwnCoinOrder(tOwnCoinOrder);
    }

    /**
     * 修改申购订单
     *
     * @param tOwnCoinOrder 申购订单
     * @return 结果
     */
    @Override
    public int updateTOwnCoinOrder(TOwnCoinOrder tOwnCoinOrder) {
        tOwnCoinOrder.setUpdateTime(DateUtils.getNowDate());
        return tOwnCoinOrderMapper.updateTOwnCoinOrder(tOwnCoinOrder);
    }

    /**
     * 批量删除申购订单
     *
     * @param ids 需要删除的申购订单主键
     * @return 结果
     */
    @Override
    public int deleteTOwnCoinOrderByIds(Long[] ids) {
        return tOwnCoinOrderMapper.deleteTOwnCoinOrderByIds(ids);
    }

    /**
     * 删除申购订单信息
     *
     * @param id 申购订单主键
     * @return 结果
     */
    @Override
    public int deleteTOwnCoinOrderById(Long id) {
        return tOwnCoinOrderMapper.deleteTOwnCoinOrderById(id);
    }

    @Override
    @Transactional
    public String createOrder(TOwnCoinOrder tOwnCoinOrder) {
        try {
            TOwnCoin tOwnCoin = tOwnCoinService.getById(tOwnCoinOrder.getOwnId());
            // 如果是错误的OwnCoin id
            if (tOwnCoin == null) {
                return MessageUtils.message("own.coin.error");
            }
            long now = System.currentTimeMillis();
            // 判断是否在申购时间段 和发行中
            if (now < tOwnCoin.getBeginTime().getTime() || now > tOwnCoin.getEndTime().getTime() || tOwnCoin.getStatus() != 2) {
                return MessageUtils.message("own.coin.error");
            }
            //完善订单
            //效验购买上限
            Integer allAmount = tOwnCoinOrderMapper.getAllAmountByUserIdAndCoinId(tOwnCoinOrder.getOwnId(), tOwnCoinOrder.getUserId());
            log.info((allAmount + tOwnCoinOrder.getNumber().intValue()) + "================");
            if ((allAmount + tOwnCoinOrder.getNumber().intValue()) > tOwnCoin.getPurchaseLimit()) {
                //计算还能购买的上限
                Integer amount = tOwnCoin.getPurchaseLimit() - allAmount;
                //超出上限提示不能购买
                return MessageUtils.message("own.coin.limit.num", amount + "");
            }
            tOwnCoinOrder.setOrderId("M" + OrderUtils.generateOrderNum());
            TAppUser user = tAppUserService.getById(tOwnCoinOrder.getUserId());
            tOwnCoinOrder.setStatus("1");
            tOwnCoinOrder.setAdminParentIds(user.getAdminParentIds());
            tOwnCoinOrder.setAdminUserIds(user.getAppParentIds());
            //设置价格未数据价格
            tOwnCoinOrder.setPrice(tOwnCoin.getPrice());
            tOwnCoinOrder.setOwnCoin(tOwnCoin.getCoin());
            //设置额度为数据库申购额度为数据额度
            tOwnCoinOrder.setAmount(tOwnCoin.getPrice().multiply(BigDecimal.valueOf(tOwnCoinOrder.getNumber())));

            //需要先判断余额是否足够
            Map<String, TAppAsset> assetMap = tAppAssetService.getAssetByUserIdList(user.getUserId());
            TAppAsset asset = assetMap.get("usdt" + user.getUserId());
            BigDecimal price = tOwnCoinOrder.getAmount();
            BigDecimal availableAmount = asset.getAvailableAmount();
            if (availableAmount.compareTo(price) < 0) {
                return MessageUtils.message("order_amount_error");
            }
            tOwnCoinOrderMapper.insert(tOwnCoinOrder);
            //扣减余额
            tAppAssetService.updateTAppAsset(
                    TAppAsset.builder()
                            .symbol("usdt")
                            .userId(user.getUserId())
                            .amout(asset.getAmout().subtract(price))
                            .availableAmount(asset.getAvailableAmount().subtract(price))
                            .type(AssetEnum.PLATFORM_ASSETS.getCode())
                            .build());
            //发起扣钱
            appWalletRecordService.generateRecord(user.getUserId(), price, RecordEnum.OWN_COIN_BUY.getCode(), user.getLoginName(), tOwnCoinOrder.getOrderId(), RecordEnum.OWN_COIN_BUY.getInfo(), availableAmount, availableAmount.subtract(price), "usdt", user.getAdminParentIds());
            return MessageUtils.message("own.coin.success");
        } catch (Exception e) {
            e.printStackTrace();
            //手动捕获异常事务管理器会认为任务commit
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return MessageUtils.message("own.coin.error");
        }
    }

    /**
     * 订阅申购新发币
     *
     * @param tOwnCoinOrder
     * @return
     */
    @Override
    @Transactional
    public AjaxResult placingCoins(TOwnCoinOrder tOwnCoinOrder) {
        Long userId = tOwnCoinOrder.getUserId();
        Long ownId = tOwnCoinOrder.getOwnId();
        TOwnCoinOrder order = new TOwnCoinOrder();
        order.setUserId(userId);
        order.setOwnId(ownId);
        //校验购买数量
        Long buyNum = tOwnCoinOrder.getNumber();
        if (buyNum <= 0) {
            return AjaxResult.error(MessageUtils.message("own.coin.sub.num.error"));
        }
        //判断OwnCoin 是否在申购时间段 和发行中
        TOwnCoin tOwnCoin = tOwnCoinService.getById(ownId);
        long now = System.currentTimeMillis();
        if (tOwnCoin == null ||
                now < tOwnCoin.getBeginTime().getTime() ||
                now > tOwnCoin.getEndTime().getTime() ||
                tOwnCoin.getStatus() != 2) {
            return AjaxResult.error(MessageUtils.message("own.coin.error"));
        }
        //效验订阅审批上限
        TOwnCoinSubscribeOrder subscribeOrder =
                tOwnCoinService.getOrderById(ownId, userId);
        if (subscribeOrder == null || !"2|3".contains(subscribeOrder.getStatus())) {
            return AjaxResult.error(MessageUtils.message("own.coin.sub.error"));
        }
        if (buyNum > subscribeOrder.getNumLimit()) {
            return AjaxResult.error(MessageUtils.message("own.coin.limit.num", subscribeOrder.getNumLimit().toString()));
        }
        //校验是否重复申购
        List<TOwnCoinOrder> orders = tOwnCoinOrderMapper.selectTOwnCoinOrderList(order);
        if (!CollectionUtils.isEmpty(orders)) {
            return AjaxResult.error(MessageUtils.message("own.coin.sub.play"));
        }
        //额度校验 单价*申购数量
        Map<String, TAppAsset> assetMap = tAppAssetService.getAssetByUserIdList(userId);
        TAppAsset asset = assetMap.get("usdt" + userId);
        BigDecimal amount =
                subscribeOrder.getPrice().multiply(new BigDecimal(tOwnCoinOrder.getNumber()))
                        .setScale(4, RoundingMode.HALF_UP);
        BigDecimal availableAmount = asset.getAvailableAmount();
        if (availableAmount.compareTo(amount) < 0) {
            return AjaxResult.error(MessageUtils.message("own.coin.limit", availableAmount));
        }
        //生成订单，扣减余额
        try {
            String orderId = "";
            tOwnCoinOrder.setOrderId(orderId = "M" + OrderUtils.generateOrderNum());
            TAppUser user = tAppUserService.getById(tOwnCoinOrder.getUserId());
            tOwnCoinOrder.setStatus("1");
            tOwnCoinOrder.setPrice(subscribeOrder.getPrice());
            tOwnCoinOrder.setAmount(amount);
            tOwnCoinOrder.setAdminParentIds(user.getAdminParentIds());
            tOwnCoinOrder.setAdminUserIds(user.getAppParentIds());
            tOwnCoinOrderMapper.insert(tOwnCoinOrder);
            tOwnCoinSubscribeOrderMapper.updateTOwnCoinSubscribeOrderById(userId, ownId, orderId);
            //平台资产USDT账户扣钱
            tAppAssetService.updateTAppAsset(
                    TAppAsset.builder()
                            .symbol("usdt")
                            .userId(userId)
                            .amout(asset.getAmout().subtract(amount))
                            .availableAmount(asset.getAvailableAmount().subtract(amount))
                            .type(AssetEnum.PLATFORM_ASSETS.getCode())
                            .build());
            //新增交易记录
            appWalletRecordService.generateRecord(
                    user.getUserId(), amount, RecordEnum.OWN_COIN_BUY.getCode(),
                    user.getLoginName(), orderId, RecordEnum.OWN_COIN_BUY.getInfo(),
                    availableAmount, availableAmount.subtract(amount), "usdt", user.getAdminParentIds());
            return AjaxResult.success(MessageUtils.message("own.coin.success"));
        } catch (Exception e) {
            e.printStackTrace();
            //手动捕获异常事务管理器会认为任务commit
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error(MessageUtils.message("own.coin.error"));
        }
    }

    /**
     * 审批新申购订单
     *
     * @param tOwnCoinOrder
     * @return
     */
    @Override
    @Transactional
    public AjaxResult editPlacing(TOwnCoinOrder tOwnCoinOrder) {
        //查询现有申购订单
        TOwnCoinOrder order = tOwnCoinOrderMapper.selectTOwnCoinOrderById(tOwnCoinOrder.getId());
        Long beforeNum = order.getNumber();
        Long currentNum = tOwnCoinOrder.getNumber();
        if (beforeNum.compareTo(currentNum) <= 0) {
            return AjaxResult.error("审批额度已达或已超当前用户额度上限");
        }
        //差值
        try {
            long diffValue = beforeNum - currentNum;
            tOwnCoinOrderMapper.updateTOwnCoinOrder(tOwnCoinOrder);
            //数量差*发行单价
            Long userId = tOwnCoinOrder.getUserId();
            Map<String, TAppAsset> assetMap = tAppAssetService.getAssetByUserIdList(userId);
            TAppAsset asset = assetMap.get("usdt" + userId);
            TAppUser user = tAppUserService.getById(userId);
            TOwnCoinSubscribeOrder subscribeOrder =
                    tOwnCoinService.getOrderById(tOwnCoinOrder.getOwnId(), userId);
            BigDecimal amount =
                    subscribeOrder.getPrice().multiply(new BigDecimal(diffValue))
                            .setScale(4, RoundingMode.HALF_UP);
            //资产表还原多扣的钱
            tAppAssetService.updateTAppAsset(
                    TAppAsset.builder()
                            .symbol("usdt")
                            .userId(userId)
                            .amout(asset.getAmout().add(amount))
                            .availableAmount(asset.getAvailableAmount().add(amount))
                            .type(AssetEnum.PLATFORM_ASSETS.getCode())
                            .build());
            //记录表
            appWalletRecordService.generateRecord(
                    userId, amount, RecordEnum.OWN_COIN_BUY.getCode(),
                    user.getLoginName(), order.getOrderId(), RecordEnum.OWN_COIN_BUY.getInfo(),
                    asset.getAvailableAmount(), asset.getAvailableAmount().add(amount), "usdt", user.getAdminParentIds());
            return AjaxResult.success(MessageUtils.message("own.coin.success"));
        } catch (Exception e) {
            e.printStackTrace();
            //手动捕获异常事务管理器会认为任务commit
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error(MessageUtils.message("own.coin.error"));
        }

    }
}
