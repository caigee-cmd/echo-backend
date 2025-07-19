package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TContractLoss;
import com.ruoyi.bussiness.service.ITContractLossService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/contract/loss")
public class TContractLossController extends ApiBaseController {
    @Autowired
    private ITContractLossService contractLossService;


    @ApiOperation(value = "止盈止损列表")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        TContractLoss contractLoss = new TContractLoss();
        contractLoss.setUserId(getStpUserId());
        contractLoss.setStatus(0);
        startPage();
        List<TContractLoss> list = contractLossService.selectTContractLossList(contractLoss);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime", Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                t.setParams(params);
            });
        }
        return getDataTable(list);
    }

    @ApiOperation(value = "撤销止盈止损")
    @PostMapping("/cancel")
    @ResponseBody
    public AjaxResult cancel(Long id) {
        TContractLoss contractLoss = contractLossService.selectTContractLossById(id);
        contractLoss.setStatus(2);
        contractLossService.updateTContractLoss(contractLoss);
        return AjaxResult.success();
    }

    @ApiOperation(value = "止盈止损")
    @PostMapping("/sett")
    @ResponseBody
    public AjaxResult sett(@RequestBody TContractLoss contractLoss) {
        String result = contractLossService.cntractLossSett( contractLoss);
        if (!"success".equals(result)) {
            return AjaxResult.error(result);
        }
        return AjaxResult.success();
    }
}

