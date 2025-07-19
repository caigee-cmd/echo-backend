package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TMineFinancial;
import com.ruoyi.bussiness.service.ITMineFinancialService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TranslatorUtil;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 理财产品Controller
 * 
 * @author ruoyi
 * @date 2023-07-17
 */
@RestController
@RequestMapping("/api/financial")
public class TMineFinancialController extends ApiBaseController
{
    @Autowired
    private ITMineFinancialService tMineFinancialService;

    /**
     * 查询理财产品列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody  TMineFinancial tMineFinancial,HttpServletRequest request)
    {
        String lang = request.getHeader("Lang");
        startPage();
        List<TMineFinancial> list = tMineFinancialService.selectTMineFinancialList(tMineFinancial);
        list.forEach(f ->{
            if (!lang.equals("zh")){
                try {
                    f.setTitle(TranslatorUtil.translate("zh-CN",lang,f.getTitle()));
                    f.setProblem(TranslatorUtil.translate("zh-CN",lang,f.getProblem()));
                    f.setProdectIntroduction(TranslatorUtil.translate("zh-CN",lang,f.getProdectIntroduction()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } );
        return getDataTable(list);
    }



    /**
     * 获取理财产品详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id,HttpServletRequest request)
    {
        String lang = request.getHeader("Lang");
        TMineFinancial tMineFinancial = tMineFinancialService.selectTMineFinancialById(id);
        try {
            tMineFinancial.setTitle(TranslatorUtil.translate("zh-CN",lang,tMineFinancial.getTitle()));
            tMineFinancial.setProblem(TranslatorUtil.translate("zh-CN",lang,tMineFinancial.getProblem()));
            tMineFinancial.setProdectIntroduction(TranslatorUtil.translate("zh-CN",lang,tMineFinancial.getProdectIntroduction()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success(tMineFinancial);
    }

    @ApiOperation(value = "理财产品购买")
    @PostMapping("/submit")
    @Transactional
    public AjaxResult submit(Long planId, BigDecimal money, Long days) {
        String msg = tMineFinancialService.submit(planId,money,days);
        if(StringUtils.isNotBlank(msg)){
            return AjaxResult.error(msg);
        }
        return  AjaxResult.success();
    }

//    @ApiOperation(value = "理财产品赎回")
//    @PostMapping("/reCall")
//    public AjaxResult reCall(String id) {
//        String msg = tMineFinancialService.reCall(id);
//        if(StringUtils.isNotBlank(msg)){
//            return AjaxResult.error(msg);
//        }
//        return  AjaxResult.success();
//    }

    @ApiOperation(value = "个人收益类加")
    @PostMapping("/personalIncome")
    public AjaxResult personalIncome() {
        return  AjaxResult.success(tMineFinancialService.personalIncome());
    }
}
