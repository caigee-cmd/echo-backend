package com.ruoyi.web.controller.app;

import com.ruoyi.bussiness.domain.TActivityRecharge;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName TdictController
 * @Description TODO
 * @Author fuck
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/app/dict")
public class TDictController extends ApiBaseController {


    @Resource
    private ISysDictTypeService dictTypeService;
    /**
     * PC端查询语言列表
     */
    @PostMapping("/dictByLanguage")
    public AjaxResult dictByLanguage(TActivityRecharge tActivityRecharge)
    {
//        HashMap<String, Object> map = new HashMap<>();
        //多语言
        List<SysDictData> data = dictTypeService.selectDictDataByType("t_app_language");
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
//        map.put("t_app_language",data);
        return new AjaxResult(0,"success",data);
    }
}
