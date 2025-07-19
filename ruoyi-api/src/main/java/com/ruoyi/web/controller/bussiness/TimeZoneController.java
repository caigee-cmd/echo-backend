package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.TimeZone;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:michael
 * @createDate: 2022/9/19 16:29
 */
@RestController
@RequestMapping("/api/timezone")
public class TimeZoneController extends ApiBaseController {

    @PostMapping("/getTimeZone")
    public AjaxResult getTimeZone() {
        TimeZone timeZone = DateUtils.getTimeZone();
        return AjaxResult.success(timeZone);
    }
}
