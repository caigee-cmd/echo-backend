package com.ruoyi.web.controller.bussiness;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.TNftOrder;
import com.ruoyi.bussiness.domain.TNftProduct;
import com.ruoyi.bussiness.domain.TNftSeries;
import com.ruoyi.bussiness.service.ITNftOrderService;
import com.ruoyi.bussiness.service.ITNftProductService;
import com.ruoyi.bussiness.service.ITNftSeriesService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * nft详情Controller
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
@RestController
@RequestMapping("/api/nftProduct")
public class TNftProductController extends BaseController
{
    @Autowired
    private ITNftProductService tNftProductService;


    /**
     * 查询nft详情列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody TNftProduct tNftProduct)
    {
        startPage();
        List<TNftProduct> list = tNftProductService.selectTNftProductList(tNftProduct);
        return getDataTable(list);
    }

    /**
     * 获取nft详情详细信息
     */
    @PostMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tNftProductService.selectTNftProductById(id));
    }

    @PostMapping("/upOrDownPro")
    public AjaxResult upOrDownPro(@RequestBody TNftProduct tNftProduct)
    {
        return toAjax(tNftProductService.updateTNftProduct(tNftProduct));
    }

}
