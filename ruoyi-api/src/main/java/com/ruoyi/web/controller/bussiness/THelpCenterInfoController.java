package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.THelpCenterInfo;
import com.ruoyi.bussiness.service.ITHelpCenterInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 帮助中心问题详情Controller
 * 
 * @author ruoyi
 * @date 2023-08-17
 */
@RestController
@RequestMapping("/api/helpCenterInfo")
public class THelpCenterInfoController extends BaseController
{
    @Autowired
    private ITHelpCenterInfoService tHelpCenterInfoService;

    /**
     * 查询帮助中心问题详情列表
     */
    @PreAuthorize("@ss.hasPermi('bussiness:helpCenterInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(THelpCenterInfo tHelpCenterInfo)
    {
        startPage();
        List<THelpCenterInfo> list = tHelpCenterInfoService.selectTHelpCenterInfoList(tHelpCenterInfo);
        return getDataTable(list);
    }

    /**
     * 获取帮助中心问题详情详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tHelpCenterInfoService.selectTHelpCenterInfoById(id));
    }
}
