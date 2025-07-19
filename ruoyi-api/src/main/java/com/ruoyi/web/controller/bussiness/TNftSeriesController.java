package com.ruoyi.web.controller.bussiness;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.bussiness.domain.TNftProduct;
import com.ruoyi.bussiness.domain.TNftSeries;
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
 * nft合计Controller
 * 
 * @author ruoyi
 * @date 2023-09-01
 */
@RestController
@RequestMapping("/api/series")
public class TNftSeriesController extends BaseController
{
    @Resource
    private ITNftSeriesService tNftSeriesService;

    /**
     * 查询nft合计列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody TNftSeries tNftSeries)
    {
        startPage();
        List<TNftSeries> list = tNftSeriesService.selectTNftSeriesList(tNftSeries);
        return getDataTable(list);
    }

}
