package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.enums.OptionRulesEnum;
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
import com.ruoyi.bussiness.domain.TOptionRules;
import com.ruoyi.bussiness.service.ITOptionRulesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 前台文本配置Controller
 * 
 * @author ruoyi
 * @date 2023-07-19
 */
@RestController
@RequestMapping("/api/option/rules")
public class TOptionRulesController extends ApiBaseController
{
    @Autowired
    private ITOptionRulesService tOptionRulesService;

    @PostMapping("/list")
    public AjaxResult list(TOptionRules tOptionRules, HttpServletRequest httpRequest)
    {
        String lang = httpRequest.getHeader("Lang");
        if (StringUtils.isNotBlank(lang)){
            tOptionRules.setLanguage(lang);
        }
        tOptionRules.setType(OptionRulesEnum.valueOf(tOptionRules.getKey()).getCode());
        List<TOptionRules> list = tOptionRulesService.selectTOptionRulesList(tOptionRules);
        return AjaxResult.success(list);
    }

}
