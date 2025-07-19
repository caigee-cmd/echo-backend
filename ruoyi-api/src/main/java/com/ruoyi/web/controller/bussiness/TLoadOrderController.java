package com.ruoyi.web.controller.bussiness;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TLoadProduct;
import com.ruoyi.bussiness.domain.setting.LoadSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.ITLoadProductService;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.bussiness.domain.TLoadOrder;
import com.ruoyi.bussiness.service.ITLoadOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 贷款订单Controller
 * 
 * @author ruoyi
 * @date 2023-07-14
 */
@RestController
@RequestMapping("/api/load/order")
public class TLoadOrderController extends ApiBaseController
{
    @Autowired
    private ITLoadOrderService tLoadOrderService;
    @Autowired
    private SettingService settingService;
    @Resource
    private ITLoadProductService tLoadProductService;

    /**
     * 查询贷款订单列表
     */
    @PostMapping("/list")
    public TableDataInfo list(TLoadOrder tLoadOrder)
    {
        startPage();
        List<TLoadOrder> list = tLoadOrderService.selectTLoadOrderList(tLoadOrder);
        return getDataTable(list);
    }

    /**
     * 获取贷款订单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tLoadOrderService.selectTLoadOrderById(id));
    }

    /**
     * 新增贷款订单
     */
    @PostMapping
    public AjaxResult add(@RequestBody TLoadOrder tLoadOrder)
    {
        return toAjax(tLoadOrderService.insertTLoadOrder(tLoadOrder));
    }

    /**
     * 获取用户贷款订单列表
     * @param tLoadOrder
     * @return
     */
    @PostMapping("/orderList")
    public TableDataInfo orderList(TLoadOrder tLoadOrder)
    {
        TAppUser user = getAppUser();
        tLoadOrder.setUserId(user.getUserId());
        Setting setting = settingService.get(SettingEnum.LOAD_SETTING.name());
        LoadSetting loadSetting = JSONUtil.toBean(setting.getSettingValue(), LoadSetting.class);
        BigDecimal overRwate = loadSetting.getOverdueRate();
        if(StringUtils.isNull(loadSetting.getOverdueRate())){
            overRwate= new BigDecimal("0.025");
        }
        startPage();
        List<TLoadOrder> list = tLoadOrderService.selectTLoadOrderList(tLoadOrder);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(t->{
                Map<String,Object> params=new HashMap<>();
                params.put("finalRepayTime", Objects.nonNull(t.getFinalRepayTime())?t.getFinalRepayTime().getTime():0l);
                t.setParams(params);
            });
        }
        for (TLoadOrder loadOrder1:list) {
            Map<String, Object> params = loadOrder1.getParams();
            if(Objects.isNull(loadOrder1.getFinalRepayTime())){
                continue;
            }
            int enddays = DateUtils.daysBetween(loadOrder1.getFinalRepayTime(), new Date());
            //逾期
            if(enddays>0){
                if (loadOrder1.getStatus() == 1) {
                    loadOrder1.setStatus(4);
                    tLoadOrderService.updateTLoadOrder(loadOrder1);
                    loadOrder1.setLastInstets(loadOrder1.getDisburseAmount().multiply(new BigDecimal(enddays)).multiply(overRwate));
                    loadOrder1.setDays(enddays);
                }
            }
            if(loadOrder1.getStatus()==4){
                loadOrder1.setLastInstets(loadOrder1.getDisburseAmount().multiply(new BigDecimal(enddays)).multiply(overRwate));
                loadOrder1.setDays(enddays);
            }
            params.put("date",Objects.nonNull(loadOrder1.getFinalRepayTime())?loadOrder1.getFinalRepayTime().getTime():null);
            loadOrder1.setParams(params);
        }
        return getDataTable(list);
    }

    /**
     * 保存贷款订单
     * @param loadOrder
     * @return
     */
    @PostMapping("submit")
    //type=3是USDT
    public AjaxResult submit(@RequestBody TLoadOrder loadOrder) {
        TAppUser user=getAppUser();
        return tLoadOrderService.saveTLoadOrder(loadOrder,user);
    }
}
