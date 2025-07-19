package com.ruoyi.web.controller.bussiness;

import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TLoadOrder;
import com.ruoyi.bussiness.domain.setting.LoadSetting;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.StringUtils;
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
import com.ruoyi.bussiness.domain.TLoadProduct;
import com.ruoyi.bussiness.service.ITLoadProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 借贷产品Controller
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@RestController
@RequestMapping("/api/load/product")
public class TLoadProductController extends ApiBaseController
{
    @Autowired
    private ITLoadProductService tLoadProductService;

    /**
     * 查询借贷产品列表
     */
    @PostMapping("/list")
    public TableDataInfo list(TLoadProduct tLoadProduct)
    {
        startPage();
        tLoadProduct.setStatus(1l);
        List<TLoadProduct> list = tLoadProductService.selectTLoadProductList(tLoadProduct);
        return getDataTable(list);
    }

    /**
     * 获取借贷产品详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tLoadProductService.selectTLoadProductById(id));
    }
}
