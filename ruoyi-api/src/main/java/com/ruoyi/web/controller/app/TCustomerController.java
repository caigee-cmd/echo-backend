package com.ruoyi.web.controller.app;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.TActivityRecharge;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.domain.setting.SupportStaffSetting;
import com.ruoyi.bussiness.service.SettingService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.enums.SettingEnum;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/app/customer")
public class TCustomerController {


    @Resource
    private SettingService settingService;
    /**
     * PC端查询客服列表
     */
    @PostMapping("/info")
    public AjaxResult info()
    {
//        HashMap<String, Object> map = new HashMap<>();
        //多语言
//        List<SysDictData> data = dictTypeService.selectDictDataByType("t_app_language");
//        if (StringUtils.isNull(data))
//        {
//            data = new ArrayList<SysDictData>();
//        }

        Setting setting = settingService.get(SettingEnum.SUPPORT_STAFF_SETTING.name());
        List<SupportStaffSetting> supportStaffSettings = JSONUtil.toList(JSONUtil.parseArray(setting.getSettingValue()), SupportStaffSetting.class);

        Map map = new HashMap();
        map.put("customer",supportStaffSettings.get(0).getUrl());
        map.put("customerEmail","");
        map.put("registType","");
        map.put("signType","");

//        map.put("t_app_language",data);
        return new AjaxResult(0,"success",map);
    }
}
