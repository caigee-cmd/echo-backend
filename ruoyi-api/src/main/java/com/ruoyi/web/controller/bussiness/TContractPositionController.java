package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TContractPosition;
import com.ruoyi.bussiness.service.ITContractPositionService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * U本位仓位Controller
 *
 * @author michael
 * @date 2023-06-27
 */
@RestController
@RequestMapping("/api/contract/position")
public class TContractPositionController extends ApiBaseController {
    @Autowired
    private ITContractPositionService contractPositionService;

    @ApiOperation(value = "持仓列表")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(String symbol, Integer status) {
        Long userId = getStpUserId();
        TContractPosition contractPosition = new TContractPosition();
        contractPosition.setUserId(userId);
        contractPosition.setSymbol(symbol);
        contractPosition.setStatus(status);
        startPage();
        List<TContractPosition> list = contractPositionService.selectTContractPositionList(contractPosition);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime", Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                Long days= Objects.nonNull(t.getDeliveryDays())?t.getDeliveryDays()*3600*24*1000:0L;
                params.put("subTime", Objects.nonNull(t.getSubTime())?t.getSubTime().getTime()+days:0L);
                Long sub=System.currentTimeMillis()-t.getCreateTime().getTime();
                Long result=t.getDeliveryDays()*3600*24*1000-sub;
                if(result<0){
                    result=0L;
                }
                params.put("deliveryDays", Objects.nonNull(t.getDeliveryDays())?result:0L);
                t.setParams(params);
            });
        }
        return getDataTable(list);
    }

    @ApiOperation(value = "平仓")
    @PostMapping("/stopOrder")
    @ResponseBody
    public AjaxResult stopOrder(Long id) {

        contractPositionService.allClosePosition(id);
        return AjaxResult.success();
    }
    @ApiOperation(value = "调整保证金")
    @PostMapping("/adjustAmount")
    @ResponseBody
    public AjaxResult adjustAmout(Long id, BigDecimal money, String flag) {
        String result = contractPositionService.adjustAmout(id, money, flag);
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success();
    }
    @ApiOperation(value = "rxce平仓")
    @PostMapping("/stopPosition")
    @ResponseBody
    public AjaxResult stopPosition(Long id) {
        String result = contractPositionService.verifyStopPostion(id);
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        result= contractPositionService.closePosition(id);
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success();
    }
    @ApiOperation(value = "追加保证金")
    @PostMapping("/adjustPositionMargn")
    @ResponseBody
    public AjaxResult adjustPositionMargn(Long id, BigDecimal money) {
        String result =   contractPositionService.adjustPositionMargn(id,money);
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success();
    }
    @ApiOperation(value = "追加本金")
    @PostMapping("/adjustPositionAmout")
    @ResponseBody
    public AjaxResult adjustPositionAmout(Long id, BigDecimal money) {
        String result =   contractPositionService.adjustPositionAmout(id,money);
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success();
    }
}
