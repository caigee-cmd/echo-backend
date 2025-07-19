package com.ruoyi.web.controller.bussiness;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TUserCoin;
import com.ruoyi.bussiness.service.ITUserCoinService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userCoin")
public class TUserCoinController extends ApiBaseController {

    @Resource
    private ITUserCoinService userCoinService;


    /**
     * 单个收藏
     * @param userCoin
     * @return
     */
    @PostMapping("/save")
    public AjaxResult save(@RequestBody TUserCoin userCoin) {
        TAppUser user = getAppUser();
        userCoin.setUserId(user.getUserId());
        userCoin.setCoin(userCoin.getCoin().toLowerCase());
        TUserCoin one = userCoinService.getOne(new LambdaQueryWrapper<TUserCoin>().eq(TUserCoin::getCoin, userCoin.getCoin()).eq(TUserCoin::getUserId,user.getUserId()));
        if(null != one){
           return AjaxResult.success();
        }
        return AjaxResult.success(userCoinService.save(userCoin));
    }
    @PostMapping("/removeByCoin")
    public AjaxResult remove(@RequestBody TUserCoin userCoin)
    {
        TAppUser user = getAppUser();
        userCoin.setUserId(user.getUserId());
        return toAjax(userCoinService.remove(new LambdaQueryWrapper<TUserCoin>().eq(TUserCoin::getCoin,userCoin.getCoin().toLowerCase()).eq(TUserCoin::getUserId,userCoin.getUserId())));
    }
    /**
     * 收藏列表
     * @return
     */
    @PostMapping("/getUserCoin")
    public AjaxResult getUserCoin() {
        TAppUser user = getAppUser();
        List<TUserCoin> list = userCoinService.list(new LambdaQueryWrapper<TUserCoin>().eq(TUserCoin::getUserId, user.getUserId()));
        return AjaxResult.success(list);
    }


    @PostMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        List<Long> collect = Arrays.stream(ids).collect(Collectors.toList());
        return toAjax(userCoinService.removeByIds(collect));
    }


    @PostMapping("/addBath")
    public AjaxResult add(@RequestBody List<TUserCoin> userCoins)
    {
        TAppUser user = getAppUser();
        for (TUserCoin userCoin : userCoins) {
            userCoin.setUserId(user.getUserId());
        }
        userCoinService.removeByUserId(user.getUserId());
        return toAjax(userCoinService.saveBatch(userCoins));
    }

}
