package com.ruoyi.web.controller.bussiness;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TAppUserDetail;
import com.ruoyi.bussiness.domain.TCurrencyOrder;
import com.ruoyi.bussiness.service.ITAppUserDetailService;
import com.ruoyi.bussiness.service.ITCurrencyOrderService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 币币交易订单Controller
 * 
 * @author ruoyi
 * @date 2023-07-25
 */
@RestController
@RequestMapping("/api/currency/order")
public class TCurrencyOrderController extends ApiBaseController
{
    @Autowired
    private ITCurrencyOrderService tCurrencyOrderService;
    @Resource
    private ITAppUserDetailService tAppUserDetailService;

    /**
     * 查询币币交易订单列表
     * @param tCurrencyOrder
     * @return
     */
    @PostMapping("/orderList")
    public TableDataInfo orderList(TCurrencyOrder tCurrencyOrder)
    {
        tCurrencyOrder.setUserId(getAppUser().getUserId());
        startPage();
        List<TCurrencyOrder> list = tCurrencyOrderService.selectOrderList(tCurrencyOrder);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("dealTime", Objects.nonNull(t.getDealTime())?t.getDealTime().getTime():0l);
                params.put("delegateTime",Objects.nonNull(t.getDelegateTime())?t.getDelegateTime().getTime():0l);
                t.setParams(params);
            });
        }
        return getDataTable(list);
    }

    /**
     * 撤单
     * @param id
     * @return
     */
    @PostMapping("/cancelOrder")
    public AjaxResult cancelOrder(Long id) {

        TCurrencyOrder currencyOrder = tCurrencyOrderService.getOne((new LambdaQueryWrapper<TCurrencyOrder>()
                .eq(TCurrencyOrder::getUserId,getStpUserId()).eq(TCurrencyOrder::getId,id)
                .eq(TCurrencyOrder::getStatus,0)
        ));
        if(currencyOrder !=null) {
            tCurrencyOrderService.canCelOrder(currencyOrder);
        }else {
            AjaxResult.error("status is error");
        }
        return AjaxResult.success();
    }


    /**
     * 导出币币交易订单列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCurrencyOrder tCurrencyOrder)
    {
        List<TCurrencyOrder> list = tCurrencyOrderService.selectTCurrencyOrderList(tCurrencyOrder);
        ExcelUtil<TCurrencyOrder> util = new ExcelUtil<TCurrencyOrder>(TCurrencyOrder.class);
        util.exportExcel(response, list, "币币交易订单数据");
    }

    /**
     * 获取币币交易订单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCurrencyOrderService.selectTCurrencyOrderById(id));
    }

    //币币交易保存
    @RepeatSubmit(interval = 5000, message = "请求过于频繁")
    @PostMapping("submit")
    public AjaxResult submit(@RequestBody TCurrencyOrder tCurrencyOrder) {
        TAppUser user = getAppUser();
        if(user!=null){
            if (Objects.nonNull(user.getIsLimitation())){
                if(user.getIsLimitation() == 2)
                    return AjaxResult.error(MessageUtils.message("user_is_limitation"));
            }
            TAppUserDetail userDetail = tAppUserDetailService.getOne(new LambdaQueryWrapper<TAppUserDetail>().eq(TAppUserDetail::getUserId, user.getUserId()));
            if (userDetail!=null){
                Integer tradeFlag = userDetail.getTradeFlag();
                if(null!=tradeFlag){
                    if(1==tradeFlag){
                        if(userDetail.getTradeMessage()==null||userDetail.getTradeMessage().equals("")){
                            return AjaxResult.error(MessageUtils.message("user.push.message"));
                        }else{
                            return AjaxResult.error(userDetail.getTradeMessage());
                        }
                    }
                }
            }
        }else{
            return AjaxResult.error(MessageUtils.message("user.notfound"));
        }
        String result = tCurrencyOrderService.submitCurrencyOrder(user, tCurrencyOrder);
        if("success".equals(result)){
            return AjaxResult.success();
        }else{
            return AjaxResult.error(result);
        }
    }

    @PostMapping("/orderHisList")
    public TableDataInfo orderHisList(TCurrencyOrder tCurrencyOrder)
    {
        tCurrencyOrder.setUserId(getAppUser().getUserId());
        startPage();
        tCurrencyOrder.setType(1);
        tCurrencyOrder.setStatus(1);
        List<TCurrencyOrder> list = tCurrencyOrderService.selectOrderList(tCurrencyOrder);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("dealTime", Objects.nonNull(t.getDealTime())?t.getDealTime().getTime():0l);
                params.put("delegateTime",Objects.nonNull(t.getDelegateTime())?t.getDelegateTime().getTime():0l);
                t.setParams(params);
            });
        }
        return getDataTable(list);
    }
}
