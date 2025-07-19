package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TSymbolManage;
import com.ruoyi.bussiness.service.ITSymbolManageService;
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
 * 币种管理Controller
 * 
 * @author ruoyi
 * @date 2023-07-12
 */
@RestController
@RequestMapping("/api/symbolmanage")
public class TSymbolManageController extends ApiBaseController
{
    @Autowired
    private ITSymbolManageService tSymbolManageService;

    /**
     * 查询币种管理列表
     */
    @PostMapping("/list")
    public TableDataInfo list(TSymbolManage tSymbolManage)
    {
//        startPage();
        tSymbolManage.setEnable("1");
        List<TSymbolManage> list = tSymbolManageService.selectTSymbolManageList(tSymbolManage);
        return getDataTable(list);
    }

    /**
     * 获取币种管理详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tSymbolManageService.selectTSymbolManageById(id));
    }
}
