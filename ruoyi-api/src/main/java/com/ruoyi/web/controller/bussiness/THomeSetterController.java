package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.bussiness.domain.THomeSetter;
import com.ruoyi.bussiness.service.ITHomeSetterService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 规则说明Controller
 * 
 * @author ruoyi
 * @date 2023-07-19
 */
@RestController
@RequestMapping("/api/home/setter")
public class THomeSetterController extends ApiBaseController
{
    @Autowired
    private ITHomeSetterService tHomeSetterService;

    @PostMapping("/list")
    public AjaxResult list(THomeSetter tHomeSetter)
    {
        List<THomeSetter> list = tHomeSetterService.selectTHomeSetterList(tHomeSetter);
        return AjaxResult.success(list);
    }



}
