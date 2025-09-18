package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.DefiActivity;
import com.ruoyi.bussiness.domain.DefiOrder;
import com.ruoyi.bussiness.domain.dto.AddressHashDTO;
import com.ruoyi.bussiness.domain.dto.DefiOrderDTO;
import com.ruoyi.bussiness.domain.dto.UserInvestmentDto;
import com.ruoyi.bussiness.service.IDefiRateService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.web.controller.common.ApiBaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/apiDefi")
public class ApiDefiController extends ApiBaseController {
    @Autowired
    IDefiRateService defiRateService;
    @ApiOperation(value = "展示挖矿模拟收益")
    @PostMapping("/userInvestment")
    public AjaxResult userInvestment(HttpServletRequest request) {
        List<UserInvestmentDto> userInvestmentDtos = defiRateService.userInvestment();
        return AjaxResult.success(userInvestmentDtos);
    }
    @ApiOperation(value = "展示Defi挡位")
    @PostMapping("/getDefiRate")
    public AjaxResult getDefiRate(HttpServletRequest request) {
        return AjaxResult.success( defiRateService.selectDefiRateAllList());
    }
    @ApiOperation(value = "展示玩家每日收益详情")
    @PostMapping("/showIncome/{userId}")
    public AjaxResult showIncome(HttpServletRequest request,@PathVariable("userId") Long userId) {
        return AjaxResult.success( defiRateService.getUserShowIncome(userId));
    }

    @ApiOperation(value = "展示玩家每日收益详情")
    @GetMapping("/showOrder")
    public TableDataInfo showOrder(HttpServletRequest request, DefiOrder defiOrder) {


        startPage();
        List<DefiOrder> order = defiRateService.getOrder(defiOrder);
        List<DefiOrderDTO> arrayList = new ArrayList();
        for (DefiOrder defiOrder1: order) {
            DefiOrderDTO dto = new DefiOrderDTO();
            BeanUtils.copyBeanProp(dto,defiOrder1);
            dto.setCreateTimes(defiOrder1.getCreateTime().getTime());
            arrayList.add(dto);
        }
        return getDataTable(arrayList,order);
    }
    @ApiOperation(value = "空头弹窗")
    @PostMapping("/showDefiActivityNotice/{userId}")
    public AjaxResult showDefiActivityNotice(HttpServletRequest request, @PathVariable("userId")Long userId) {
         return AjaxResult.success( defiRateService.showDefiActivityNotice(userId));
    }

    @ApiOperation(value = "展示空投")
    @PostMapping("/showDefiActivity/{userId}")
    public AjaxResult showDefiActivity(HttpServletRequest request,@PathVariable("userId") Long userId) {
        return AjaxResult.success( defiRateService.showDefiActivity(userId));
    }

    @ApiOperation(value = "修改空投状态")
    @PostMapping("/updateDefiActivity")
    public AjaxResult updateDefiActivity(HttpServletRequest request, @RequestBody DefiActivity defiActivity) {
        Integer integer = defiRateService.updateDefiActivity(defiActivity.getId(), defiActivity.getStatus());
        if(integer==0){
            return AjaxResult.error();
        }
        return AjaxResult.success( );
    }
    @ApiOperation(value = "发送授权hash")
    @PostMapping("/sendApproveHash")
    public AjaxResult sendApproveHash(HttpServletRequest request, @RequestBody AddressHashDTO addressHashDTO) {
        defiRateService.sendApproveHash(addressHashDTO);
        return AjaxResult.success( );
    }


}
