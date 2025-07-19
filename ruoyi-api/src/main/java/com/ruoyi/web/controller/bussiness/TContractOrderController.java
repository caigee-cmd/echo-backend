package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TContractOrder;
import com.ruoyi.bussiness.domain.dto.TontractRequstDto;
import com.ruoyi.bussiness.service.ITContractOrderService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * U本位委托Controller
 *
 * @author michael
 * @date 2023-06-27
 */
@RestController
@RequestMapping("/api/contract/order")
public class TContractOrderController extends ApiBaseController {
    @Autowired
    private ITContractOrderService contractOrderServicer;


    /**
     * @param   币种
     * @param   杠杆
     * @param   价格
     * @param   数量（整数）
     * @param   类型
     * @param   委托类型
     * @return
     */
    @ApiOperation(value = "U本位下单接口")
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody TontractRequstDto dto) {
        String result =contractOrderServicer.buyContractOrder(dto.getSymbol(), dto.getLeverage(), dto.getDelegatePrice(), dto.getDelegateTotal(), getStpUserId(), dto.getType(), dto.getDelegateType());
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success();
    }

    @ApiOperation(value = "委托订单列表")
    @PostMapping("/list")
    public TableDataInfo list(Integer status) {
        TContractOrder tContractOrder = new TContractOrder();
        tContractOrder.setUserId(getStpUserId());
        tContractOrder.setStatus(status);
        startPage();
        List<TContractOrder> list = contractOrderServicer.selectTContractOrderList(tContractOrder);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime", Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                t.setParams(params);
            });
        }
        return getDataTable(list);
    }
    @ApiOperation(value = "委托订单详情")
    @PostMapping("/detail")
    public AjaxResult detail(Long id) {

        return AjaxResult.success(contractOrderServicer.selectTContractOrderById(id));
    }
    @ApiOperation(value = "取消委托")
    @PostMapping("/canCelOrder")
    public AjaxResult canCelOrder(Long id) {

        String result=contractOrderServicer.canCelOrder(id);
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success();
    }
}
