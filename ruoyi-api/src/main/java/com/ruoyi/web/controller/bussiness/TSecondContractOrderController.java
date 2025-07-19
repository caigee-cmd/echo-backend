package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TCurrencyOrder;
import com.ruoyi.bussiness.domain.TSecondContractOrder;
import com.ruoyi.bussiness.service.ITSecondContractOrderService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 秒合约订单Controller
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@RestController
@RequestMapping("/api/secondContractOrder")
public class TSecondContractOrderController extends ApiBaseController
{
    @Resource
    private ITSecondContractOrderService tSecondContractOrderService;

    /**
     * 新增秒合约订单
     */
    @PostMapping("/createSecondContractOrder")
    public AjaxResult createSecondContractOrder(@RequestBody TSecondContractOrder tSecondContractOrder)
    {
        String secondContractOrder = tSecondContractOrderService.createSecondContractOrder(tSecondContractOrder);
        if(!StringUtils.isNumeric(secondContractOrder)){
            return AjaxResult.error(secondContractOrder);
        }else {
            return AjaxResult.success(tSecondContractOrderService.selectTSecondContractOrderById(Long.valueOf(secondContractOrder)));
        }

    }

    /**
     * 查询秒合约订单
     */
    @PostMapping("/selectOrderList")
    public AjaxResult selectOrderList(@RequestBody TSecondContractOrder tSecondContractOrder)
    {
        tSecondContractOrder.setUserId(getStpUserId());
        List<TSecondContractOrder> list = tSecondContractOrderService.selectTSecondContractOrderList(tSecondContractOrder);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime",Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                t.setParams(params);
            });
        }
        return AjaxResult.success(list);
    }
}
