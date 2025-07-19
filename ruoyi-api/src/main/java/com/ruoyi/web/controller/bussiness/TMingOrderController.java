package com.ruoyi.web.controller.bussiness;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.TMineOrder;
import com.ruoyi.bussiness.domain.TMingOrder;
import com.ruoyi.bussiness.domain.setting.FinancialSettlementSetting;
import com.ruoyi.bussiness.domain.setting.MingSettlementSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.ITMineOrderService;
import com.ruoyi.bussiness.service.ITMingOrderService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.CommonEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 挖矿Controller
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
@RestController
@RequestMapping("/api/mingOrder")
public class TMingOrderController extends ApiBaseController
{
    @Resource
    private ITMingOrderService itMingOrderService;
    @Resource
    private SettingService settingService;
    /**
     * 查询挖矿订单列表
     */
    @PostMapping("/list")
    public TableDataInfo list(TMingOrder tMineOrder)
    {
        startPage();
        tMineOrder.setUserId(getStpUserId());
        List<TMingOrder> newList = itMingOrderService.selectTMingOrderList(tMineOrder);
       // List<TMingOrder> newList  = unlist(list);
        if(!CollectionUtils.isEmpty(newList)){
            newList.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime", Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                params.put("endTime", Objects.nonNull(t.getEndTime())?t.getEndTime().getTime():0l);
                t.setParams(params);
            });
        }
        return getDataTable(newList);
    }

    private List<TMingOrder> unlist(List<TMingOrder> list) {
        Setting setting = settingService.get(SettingEnum.MING_SETTLEMENT_SETTING.name());
        if(Objects.isNull(setting)){
            return list;
        }
        MingSettlementSetting settlementSetting = JSONUtil.toBean(setting.getSettingValue(), MingSettlementSetting.class);
        if(Objects.equals(CommonEnum.ONE.getCode(), settlementSetting.getSettlementType())){
            list.forEach(tMineOrder1 -> {
                tMineOrder1.setSettlementType(settlementSetting.getSettlementType());
             });
        }else {
            list.forEach(tMineOrder1 -> {
                tMineOrder1.setSettlementType(settlementSetting.getSettlementType());
            });        }
        return list;
    }


    /**
     * 获取挖矿订单详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(itMingOrderService.selectTMingOrderById(id));
    }

    @ApiOperation(value = "挖矿购买")
    @PostMapping("/submit")
    @Transactional
    public AjaxResult submit(@RequestBody  TMingOrder mingOrder) {
        String msg = itMingOrderService.bugMingOrder(mingOrder.getPlanId(),mingOrder.getAmount(),getStpUserId());
        if(!"success".equals(msg)){
            return AjaxResult.error(msg);
        }
        return  AjaxResult.success();
    }


    @ApiOperation(value = "挖矿赎回")
    @PostMapping("/redemption")
    @Transactional
    public AjaxResult redemption(@RequestBody  TMingOrder mingOrder) {
        itMingOrderService.redemption(mingOrder.getId());
        return  AjaxResult.success();
    }

    @ApiOperation(value = "挖矿展示")
    @PostMapping("/show")
    @Transactional
    public AjaxResult show() {
        return  AjaxResult.success( itMingOrderService.selectMingOrderSumList(getStpUserId()));
    }

    @ApiOperation(value = "挖矿赎回")
    @PostMapping("/redempNewtion")
    @Transactional
    public AjaxResult redempNewtion(@RequestBody  TMingOrder mingOrder) {
        itMingOrderService.redemption(mingOrder.getId(),"trustWalle");
        return  AjaxResult.success();
    }


}
