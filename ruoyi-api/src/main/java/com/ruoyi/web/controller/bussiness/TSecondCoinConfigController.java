package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TSecondCoinConfig;
import com.ruoyi.bussiness.domain.vo.SymbolCoinConfigVO;
import com.ruoyi.bussiness.service.ITSecondCoinConfigService;
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
 * 秒合约币种配置Controller
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/api/coin")
public class TSecondCoinConfigController extends ApiBaseController
{
    @Autowired
    private ITSecondCoinConfigService tSecondCoinConfigService;

    /**
     * 查询秒合约币种配置列表
     */
    @PostMapping("/list")
    public AjaxResult list()
    {
        List<SymbolCoinConfigVO> symbolList = tSecondCoinConfigService.getSymbolList();
        return AjaxResult.success(symbolList);
    }
    /**
     * 获取秒合约币种配置详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tSecondCoinConfigService.selectTSecondCoinConfigById(id));
    }

}
