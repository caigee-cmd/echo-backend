package com.ruoyi.web.controller.bussiness;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.TMineOrder;
import com.ruoyi.bussiness.domain.setting.FinancialSettlementSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.ITMineOrderService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.CommonEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 理财订单Controller
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
@RestController
@RequestMapping("/api/order")
public class TMineOrderController extends ApiBaseController
{
    @Resource
    private ITMineOrderService tMineOrderService;
    @Resource
    private SettingService settingService;
    /**
     * 查询理财订单列表
     */
    @PostMapping("/list")
    public TableDataInfo list(TMineOrder tMineOrder)
    {
        startPage();
        tMineOrder.setUserId(getStpUserId());
        List<TMineOrder> list = tMineOrderService.selectTMineOrderList(tMineOrder);
        List<TMineOrder> newList  = unlist(list);
        if(!CollectionUtils.isEmpty(newList)){
            newList.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime", Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                t.setParams(params);
            });
        }
        return getDataTableFrom2Lists(newList,list);
    }

    private List<TMineOrder> unlist(List<TMineOrder> list) {
        Setting setting = settingService.get(SettingEnum.FINANCIAL_SETTLEMENT_SETTING.name());
        FinancialSettlementSetting settlementSetting = JSONUtil.toBean(setting.getSettingValue(), FinancialSettlementSetting.class);
        if(Objects.equals(CommonEnum.ONE.getCode(), settlementSetting.getSettlementType())){
            list.forEach(tMineOrder1 -> {
                tMineOrder1.setSettlementType(settlementSetting.getSettlementType());
                tMineOrder1.setSettlementDay(settlementSetting.getSettlementDay());
            });
        }else {
            list.forEach(tMineOrder1 -> {
                tMineOrder1.setSettlementType(settlementSetting.getSettlementType());
            });        }
        return list;
    }


    /**
     * 获取理财订单详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tMineOrderService.selectTMineOrderById(id));
    }
}
