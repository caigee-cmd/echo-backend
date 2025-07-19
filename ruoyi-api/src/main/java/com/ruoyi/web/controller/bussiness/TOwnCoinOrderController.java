package com.ruoyi.web.controller.bussiness;

import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.bussiness.domain.TOwnCoinOrder;
import com.ruoyi.bussiness.service.ITOwnCoinOrderService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 申购订单Controller
 * 
 * @author ruoyi
 * @date 2023-09-20
 */
@RestController
@RequestMapping("/api/ownCoinOrder")
public class TOwnCoinOrderController extends BaseController
{
    @Resource
    private ITOwnCoinOrderService tOwnCoinOrderService;

    /**
     * 查询申购订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TOwnCoinOrder tOwnCoinOrder)
    {
        startPage();
        List<TOwnCoinOrder> list = tOwnCoinOrderService.selectTOwnCoinOrderList(tOwnCoinOrder);
        return getDataTable(list);
    }
    /**
     * 查询申购订单列表
     */
    @PostMapping("/createOrder")
    public AjaxResult createOrder(@RequestBody TOwnCoinOrder tOwnCoinOrder)
    {
        tOwnCoinOrder.setUserId(StpUtil.getLoginIdAsLong());

        if(tOwnCoinOrder.getNumber() == null || tOwnCoinOrder.getNumber() <= 0 || tOwnCoinOrder.getOwnId() == null){
            return AjaxResult.error(MessageUtils.message("own.coin.error"));
        }
        return AjaxResult.success(tOwnCoinOrderService.createOrder(tOwnCoinOrder));
    }

    /**
     * 申购
     *
     * @param tOwnCoinOrder
     * @return
     */
    @PostMapping("/placing")
    public AjaxResult placing(@RequestBody TOwnCoinOrder tOwnCoinOrder){
        long userId = StpUtil.getLoginIdAsLong();
        if(Objects.isNull(tOwnCoinOrder) ||
                Objects.isNull(tOwnCoinOrder.getUserId()) ||
                !tOwnCoinOrder.getUserId().equals(userId)){
            return AjaxResult.error(MessageUtils.message("own.coin.error"));
        }
        return tOwnCoinOrderService.placingCoins(tOwnCoinOrder);
    }

}
