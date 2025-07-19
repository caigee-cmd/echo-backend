package com.ruoyi.web.controller.bussiness;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.KlineSymbol;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TUserBank;
import com.ruoyi.bussiness.service.ITUserBankService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 银行卡Controller
 * 
 * @author ruoyi
 * @date 2023-08-21
 */
@RestController
@RequestMapping("/api/userBank")
public class TUserBankController extends ApiBaseController
{
    @Autowired
    private ITUserBankService tUserBankService;

    /**
     * 保存银行卡信息
     * @param userBank
     * @return
     */
    @PostMapping("/save")
    public AjaxResult save(@RequestBody TUserBank userBank) {
        TAppUser user = getAppUser();
        TUserBank oldBack = tUserBankService.getOne(new LambdaQueryWrapper<TUserBank>().eq(TUserBank::getUserId, user.getUserId()).eq(TUserBank::getCardNumber, userBank.getCardNumber()));
        if (Objects.nonNull(oldBack)){
            return AjaxResult.error(MessageUtils.message("back.code.exist.error"),userBank.getCardNumber());
        }
        userBank.setUserId(user.getUserId());
        userBank.setCreateTime(new Date());
        userBank.setUpdateTime(new Date());
        userBank.setAdminParentIds(user.getAdminParentIds());
        return AjaxResult.success(tUserBankService.save(userBank));
    }

    /**
     * 银行卡列表
     * @return
     */
    @PostMapping("/getbank")
    public AjaxResult activityList() {
        TAppUser user = getAppUser();
        List<TUserBank> list = tUserBankService.list(new LambdaQueryWrapper<TUserBank>().eq(TUserBank::getUserId, user.getUserId()));
        return AjaxResult.success(list);
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody TUserBank tUserBank)
    {
        TUserBank oldBack = tUserBankService.getOne(new LambdaQueryWrapper<TUserBank>().eq(TUserBank::getCardNumber, tUserBank.getCardNumber()));
        if (Objects.nonNull(oldBack) && oldBack.getId()!=tUserBank.getId()){
            return AjaxResult.error(tUserBank.getCardNumber()+"该银行卡已经存在");
        }
        tUserBank.setUpdateTime(new Date());
        return toAjax(tUserBankService.updateTUserBank(tUserBank));
    }

    @PostMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tUserBankService.deleteTUserBankByIds(ids));
    }
}
