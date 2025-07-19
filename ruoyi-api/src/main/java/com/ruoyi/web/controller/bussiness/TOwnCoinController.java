package com.ruoyi.web.controller.bussiness;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.TOwnCoin;
import com.ruoyi.bussiness.domain.TOwnCoinSubscribeOrder;
import com.ruoyi.bussiness.service.ITOwnCoinService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 发币Controller
 *
 * @author ruoyi
 * @date 2023-09-18
 */
@RestController
@RequestMapping("/api/ownCoin")
public class TOwnCoinController extends BaseController {
    @Resource
    private ITOwnCoinService tOwnCoinService;

    /**
     * 查询发币列表
     */
    @PostMapping("/list")
    public AjaxResult list(String status) {

        return AjaxResult.success(tOwnCoinService.ownCoinList(status));
    }

    /**
     * 订阅新币
     */
    @PostMapping("/subscribeCoins")
    public AjaxResult subscribeCoins(@RequestBody TOwnCoinSubscribeOrder tOwnCoinSubscribeOrder) {

        return tOwnCoinService.subscribeCoins(tOwnCoinSubscribeOrder);
    }

    /**
     * 查询发币列表
     */
    @GetMapping("/getDetail/{ownId}")
    public AjaxResult getDetail(@PathVariable Long ownId) {
        long userId = StpUtil.getLoginIdAsLong();
        return AjaxResult.success(tOwnCoinService.getDetail(userId,ownId));
    }

}
