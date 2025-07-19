package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.vo.AssetTransFundsVO;
import com.ruoyi.bussiness.service.ITAppAssetService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 玩家资产Controller
 * 
 * @author shenshen
 * @date 2023-06-27
 */
@RestController
@RequestMapping("/api/asset")
public class TAppAssetController extends ApiBaseController
{
    @Autowired
    private ITAppAssetService tAppAssetService;



    /**
     * 获取玩家资产详细信息
     */
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo()
    {
        TAppAsset appAsset = new TAppAsset();
        appAsset.setUserId(getStpUserId());
        return success(tAppAssetService.selectTAppAssetList(appAsset));
    }

    /**
     * 获取玩家资产划转
     */
    @PostMapping(value = "/transferFunds")
    public AjaxResult transferFunds(@RequestBody AssetTransFundsVO assetTransFundsVo)
    {
        String msg = tAppAssetService.transferFunds(assetTransFundsVo);
        if(!StringUtils.isEmpty(msg)){
          return   error(msg);
        }
        return success();
    }

    /**
     * 资产余额
     */
    @PostMapping(value = "/assetBalance")
    public AjaxResult tAppAssetService()
    {
        List<TAppAsset> list = tAppAssetService.tAppAssetService();
        return success(list);
    }



}
