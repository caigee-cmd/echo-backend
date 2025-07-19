package com.ruoyi.web.controller.bussiness;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.TWithdraw;
import com.ruoyi.bussiness.domain.vo.WithdrawFreezeVO;
import com.ruoyi.bussiness.service.ITWithdrawService;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/withdraw")
public class TAppWithdrawController extends ApiBaseController {


    @Resource
    private ITWithdrawService withdrawService;
    @Resource
    private RedisUtil redisUtil;
    @Value("${admin-redis-stream.names}")
    private String redisStreamNames;
    @Resource
    private RedisCache redisCache;
    /**
     * 用户提现
     *
     * @param amount
     * @param coinType
     * @param pwd
     * @param adress
     * @param coin
     * @param request
     * @return
     */
    @PostMapping("submit")
    @Transactional
    public AjaxResult submit(BigDecimal amount, String coinType, String pwd, String adress, String coin, HttpServletRequest request) {
       String msg = withdrawService.submit(amount,coinType,pwd,adress,coin);
       if(StringUtils.isNotEmpty(msg)){
           return AjaxResult.error(msg);
       }
        //socket 通知后台
        HashMap<String, Object> object = new HashMap<>();
        object.put(CacheConstants.WITHDRAW_KEY,CacheConstants.WITHDRAW_KEY);
        redisUtil.addStream(redisStreamNames,object);
       return AjaxResult.success();
    }

    @ApiOperation(value = "我的提现列表")
    @PostMapping("/list")
    public TableDataInfo list( Integer status) {
        TWithdraw appWithdraw = new TWithdraw();
        appWithdraw.setUserId(getStpUserId());
        appWithdraw.setStatus(status);
        startPage();
        List<TWithdraw> list = withdrawService.selectTWithdrawList(appWithdraw);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime", Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                t.setParams(params);
            });
        }
        return getDataTable(list);
    }
    @ApiOperation(value = "详情")
    @PostMapping("/detail")
    public AjaxResult detail(Long id) {
        TWithdraw result = withdrawService.getOne(new LambdaQueryWrapper<TWithdraw>().eq(TWithdraw::getId, id));
        return AjaxResult.success(result);
    }

    @ApiOperation(value = "体现锁定资产list")
    @PostMapping("/freezeList")
    public AjaxResult freezeList(Integer status) {
        TWithdraw appWithdraw = new TWithdraw();
        appWithdraw.setUserId(getStpUserId());
        appWithdraw.setStatus(3);
        List<WithdrawFreezeVO> withdrawFreezeVO = withdrawService.selectFreezeList(appWithdraw);
        return AjaxResult.success(withdrawFreezeVO);
    }

    @ApiOperation(value = "获取当前用户的体现状态")
    @PostMapping("/getWithdrawStatus")
    public AjaxResult getWithdrawStatus() {
        Boolean flag =  withdrawService.getWithdrawStatus(StpUtil.getLoginIdAsLong());
        return AjaxResult.success(flag);
    }


    @ApiOperation(value = "保存地址")
    @PostMapping("/saveCacheAddress")
    public AjaxResult saveCacheAddress(@RequestBody HashMap<String,String> map) {
        Long userId = StpUtil.getLoginIdAsLong();
        String address = map.get("address");
        String coin = map.get("coin");
        if(StringUtils.isNotEmpty(address)){
            redisCache.setCacheObject(CachePrefix.USER_ADDRESS_WITHDRAW.getPrefix()+userId+coin,address);
        }else {
            address= redisCache.getCacheObject(CachePrefix.USER_ADDRESS_WITHDRAW.getPrefix()+userId+coin);
        }
        return AjaxResult.success(address);
    }

    @ApiOperation(value = "查看用户")
    @PostMapping("/haveCacheAddress")
    public AjaxResult haveCacheAddress(@RequestBody HashMap<String,String> map) {
        String coin = map.get("coin");
        Long userId = StpUtil.getLoginIdAsLong();
        return AjaxResult.success(redisCache.getCacheObject(CachePrefix.USER_ADDRESS_WITHDRAW.getPrefix() + userId+coin));
    }
}
