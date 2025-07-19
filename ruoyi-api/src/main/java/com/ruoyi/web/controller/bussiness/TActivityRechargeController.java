package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TActivityRecharge;
import com.ruoyi.bussiness.service.ITActivityRechargeService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 充值活动Controller
 * 
 * @author ruoyi
 * @date 2023-07-05
 */
@RestController
@RequestMapping("/api/activityrecharge")
public class TActivityRechargeController extends ApiBaseController
{
    @Autowired
    private ITActivityRechargeService tActivityRechargeService;

    /**
     * 查询充值活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:recharge:list')")
    @GetMapping("/list")
    public AjaxResult list(TActivityRecharge tActivityRecharge)
    {
        List<TActivityRecharge> list = tActivityRechargeService.selectTActivityRechargeList(tActivityRecharge);
        return AjaxResult.success(list);
    }


}
