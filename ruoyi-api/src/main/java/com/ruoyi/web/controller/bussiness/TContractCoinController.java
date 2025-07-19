package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TContractCoin;
import com.ruoyi.bussiness.service.ITContractCoinService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * U本位合约币种Controller
 * 
 * @author michael
 * @date 2023-06-27
 */
@RestController
@RequestMapping("/api/contract/coin")
public class TContractCoinController extends ApiBaseController
{
    @Autowired
    private ITContractCoinService contractCoinService;


    /**
     * 获取玩家资产详细信息
     */
    @ApiOperation(value = "u本位币种列表")
    @PostMapping(value = "/list")
    public TableDataInfo list()
    {
        TContractCoin tContractCoin=new TContractCoin();
        tContractCoin.setEnable(0L);
        tContractCoin.setVisible(0L);
        startPage();
        List<TContractCoin> list=contractCoinService.selectTContractCoinList(tContractCoin);
        return getDataTable(list);
    }
    @ApiOperation(value = "u本位币种列表")
    @PostMapping(value = "/getCoinList")
    public AjaxResult getCoinList()
    {
        List<TContractCoin> list=contractCoinService.getCoinList();
        return AjaxResult.success(list);
    }

}
