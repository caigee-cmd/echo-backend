package com.ruoyi.web.controller.bussiness;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.TMingOrder;
import com.ruoyi.bussiness.domain.TMingProduct;
import com.ruoyi.bussiness.domain.TMingProductUser;
import com.ruoyi.bussiness.domain.setting.MingSettlementSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.ITMingOrderService;
import com.ruoyi.bussiness.service.ITMingProductService;
import com.ruoyi.bussiness.service.ITMingProductUserService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.CommonEnum;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 挖矿Controller
 *
 * @author ruoyi
 * @date 2023-07-17
 */
@RestController
@RequestMapping("/api/mingProduct")
public class TMingProductController extends ApiBaseController {
    @Resource
    private ITMingProductService mingProductService;
    @Resource
    private SettingService settingService;
    @Resource
    private ITMingProductUserService mingProductUserService;
    @Resource
    private ITMingOrderService mingOrderService;

    /**
     * 查询挖矿订单列表
     */
    @PostMapping("/list")
    public TableDataInfo list(HttpServletRequest request) {
        startPage();
        TMingProduct product = new TMingProduct();
        product.setStatus(0L);
        List<TMingProduct> list = mingProductService.selectTMingProductList(product);
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(mProduct->{
                int count = mingOrderService.count(new LambdaQueryWrapper<TMingOrder>()
                        .eq(TMingOrder::getPlanId, mProduct.getId())
                        .eq(TMingOrder::getUserId, getStpUserId()));
                if (Objects.nonNull(count)) mProduct.setBuyPurchase((long)count);
                TMingProductUser one = mingProductUserService.getOne(new LambdaQueryWrapper<TMingProductUser>()
                        .eq(TMingProductUser::getProductId, mProduct.getId())
                        .eq(TMingProductUser::getAppUserId, getStpUserId()));
                if (Objects.nonNull(one)) mProduct.setTimeLimit(one.getPledgeNum());
            });
        }

        return getDataTable(list);
    }



}
