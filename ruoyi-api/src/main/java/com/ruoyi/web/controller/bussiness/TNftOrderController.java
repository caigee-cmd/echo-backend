package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TNftOrder;
import com.ruoyi.bussiness.service.ITNftOrderService;
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
 * nft订单Controller
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
@RestController
@RequestMapping("/api/nftOrder")
public class TNftOrderController extends BaseController
{
    @Autowired
    private ITNftOrderService tNftOrderService;

    /**
     * 查询nft订单列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody TNftOrder tNftOrder)
    {
        startPage();
        List<TNftOrder> list = tNftOrderService.selectTNftOrderList(tNftOrder);
        return getDataTable(list);
    }

    /**
     * 获取nft订单详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tNftOrderService.selectTNftOrderById(id));
    }

    /**
     * 新增nft订单
     */
    @PostMapping
    public AjaxResult add(@RequestBody TNftOrder tNftOrder)
    {
        return toAjax(tNftOrderService.insertTNftOrder(tNftOrder));
    }

}
