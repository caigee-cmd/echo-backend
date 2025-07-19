package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TCurrencySymbol;
import com.ruoyi.bussiness.service.ITCurrencySymbolService;
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
 * 币币交易币种配置Controller
 * 
 * @author ruoyi
 * @date 2023-07-25
 */
@RestController
@RequestMapping("/api/currency/symbol")
public class TCurrencySymbolController extends ApiBaseController
{

    @Autowired
    private ITCurrencySymbolService tCurrencySymbolService;

    /**
     * 查询币币交易币种配置列表
     */
    @PostMapping("/list")
    public AjaxResult list(TCurrencySymbol tCurrencySymbol)
    {
        List<TCurrencySymbol> list = tCurrencySymbolService.getSymbolList();
        return AjaxResult.success(list);
    }

    /**
     * 导出币币交易币种配置列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCurrencySymbol tCurrencySymbol)
    {
        List<TCurrencySymbol> list = tCurrencySymbolService.selectTCurrencySymbolList(tCurrencySymbol);
        ExcelUtil<TCurrencySymbol> util = new ExcelUtil<TCurrencySymbol>(TCurrencySymbol.class);
        util.exportExcel(response, list, "币币交易币种配置数据");
    }

    /**
     * 获取币币交易币种配置详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCurrencySymbolService.selectTCurrencySymbolById(id));
    }

    /**
     * 新增币币交易币种配置
     */
    @PostMapping
    public AjaxResult add(@RequestBody TCurrencySymbol tCurrencySymbol)
    {
        return toAjax(tCurrencySymbolService.insertTCurrencySymbol(tCurrencySymbol));
    }

    /**
     * 修改币币交易币种配置
     */
    @PutMapping
    public AjaxResult edit(@RequestBody TCurrencySymbol tCurrencySymbol)
    {
        return toAjax(tCurrencySymbolService.updateTCurrencySymbol(tCurrencySymbol));
    }

    /**
     * 删除币币交易币种配置
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCurrencySymbolService.deleteTCurrencySymbolByIds(ids));
    }
}
