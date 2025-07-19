package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TSecondPeriodConfig;
import com.ruoyi.bussiness.service.ITSecondPeriodConfigService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 秒合约币种周期配置Controller
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/api/period")
public class TSecondPeriodConfigController extends BaseController
{
    @Resource
    private ITSecondPeriodConfigService tSecondPeriodConfigService;

    /**
     * 查询秒合约币种周期配置列表
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestBody  TSecondPeriodConfig tSecondPeriodConfig)
    {
        List<TSecondPeriodConfig> list = tSecondPeriodConfigService.selectTSecondPeriodConfigList(tSecondPeriodConfig);
        return AjaxResult.success(list);
    }
}
