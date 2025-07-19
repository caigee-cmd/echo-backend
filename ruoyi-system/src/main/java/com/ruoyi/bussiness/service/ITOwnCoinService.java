package com.ruoyi.bussiness.service;

import cc.block.data.api.domain.market.Kline;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.ruoyi.bussiness.domain.KlineSymbol;
import com.ruoyi.bussiness.domain.TOwnCoin;
import com.ruoyi.bussiness.domain.TOwnCoinSubscribeOrder;
import com.ruoyi.bussiness.domain.vo.TOwnCoinVO;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 发币Service接口
 * 
 * @author ruoyi
 * @date 2023-09-18
 */
public interface ITOwnCoinService extends IService<TOwnCoin>
{
    /**
     * 查询发币
     * 
     * @param id 发币主键
     * @return 发币
     */
    public TOwnCoin selectTOwnCoinById(Long id);

    /**
     * 查询发币列表
     * 
     * @param tOwnCoin 发币
     * @return 发币集合
     */
    public List<TOwnCoin> selectTOwnCoinList(TOwnCoin tOwnCoin);

    /**
     * 新增发币
     * 
     * @param tOwnCoin 发币
     * @return 结果
     */
    public int insertTOwnCoin(TOwnCoin tOwnCoin);

    /**
     * 修改发币
     * 
     * @param tOwnCoin 发币
     * @return 结果
     */
    public int updateTOwnCoin(TOwnCoin tOwnCoin);

    /**
     * 批量删除发币
     * 
     * @param ids 需要删除的发币主键集合
     * @return 结果
     */
    public int deleteTOwnCoinByIds(Long[] ids);

    /**
     * 删除发币信息
     * 
     * @param id 发币主键
     * @return 结果
     */
    public int deleteTOwnCoinById(Long id);

    int editStatus(Long id);

    List<Kline> selectLineList(KlineSymbol one, List<Kline> his);

    /**
     * 新发币订阅
     *
     * @param tOwnCoinSubscribeOrder
     * @return
     */
    AjaxResult subscribeCoins(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder);

    /**
     * 查询用户订阅新发币列表
     *
     * @param tOwnCoinSubscribeOrder
     * @return
     */
    List<TOwnCoinSubscribeOrder> selectTOwnCoinSubscribeOrderList(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder);

    /**
     * 修改(审批)发币订阅
     *
     * @param tOwnCoinSubscribeOrder
     * @return
     */
    int updateTOwnCoinSubscribeOrder(TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder);

    /**
     * 查询用户订阅审批订单
     *
     * @param ownId
     * @param userId
     * @return
     */
    TOwnCoinSubscribeOrder getOrderById(Long ownId, Long userId);
    /**
     * 查询用户订阅审批订单
     *
     * @param ownId
     * @param userId
     * @return
     */
    TOwnCoinVO getDetail(Long ownId, Long userId);

    /**
     *  查询发币列表
     *
     * @param status 新发币状态
     * @return
     */
    List<TOwnCoin> ownCoinList(String status);

    /**
     * 获取新币详情
     *
     * @param id
     * @return
     */
    TOwnCoinSubscribeOrder getTOwnCoinSubscribeOrder(Long id);

    /**
     * 提前结束上线新币,资产发送
     *
     * @param id
     * @return
     */
    int editReleaseStatus(Long id);
}
