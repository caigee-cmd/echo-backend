package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.THelpCenter;
import com.ruoyi.bussiness.service.ITHelpCenterService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 帮助中心Controller
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
@RestController

@RequestMapping("/api/helpcenter")
public class THelpCenterController extends BaseController
{
    @Autowired
    private ITHelpCenterService tHelpCenterService;

    /**
     * 查询帮助中心列表
     */
    @PostMapping("/list")
    public AjaxResult list(THelpCenter tHelpCenter, HttpServletRequest request)
    {
        String lang = request.getHeader("Lang");
        tHelpCenter.setLanguage(lang);
        List<THelpCenter> list = tHelpCenterService.getCenterList(tHelpCenter);
        return AjaxResult.success(list);
    }

}
