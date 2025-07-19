package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.domain.TAgentActivityInfo;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.vo.TAgentActivityInfoVo;
import com.ruoyi.bussiness.service.ITAgentActivityInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 返利活动明细Controller
 * 
 * @author ruoyi
 * @date 2023-07-06
 */
@RestController
@RequestMapping("/api/agentActivityInfo")
public class TAgentActivityInfoController extends ApiBaseController
{
    @Autowired
    private ITAgentActivityInfoService tAgentActivityInfoService;

    /**
     * 统计返利
     */
    @PostMapping("/getAgentInfo")
    public AjaxResult getAgentInfo(TAgentActivityInfo tAgentActivityInfo)
    {
        TAppUser appUser = getAppUser();
        tAgentActivityInfo.setUserId(appUser.getUserId());
        Map<Object, Object> map = tAgentActivityInfoService.selectUserActivityInfo(tAgentActivityInfo);
        return AjaxResult.success(map);
    }

    /**
     * 查询返利活动明细列表
     * @param tAgentActivityInfo
     * @return
     */
    @PostMapping("/getAgentList")
    public AjaxResult getAgentList(@RequestBody TAgentActivityInfo tAgentActivityInfo)
    {
        TAppUser appUser = getAppUser();
        tAgentActivityInfo.setUserId(appUser.getUserId());
        List<TAgentActivityInfoVo> list = tAgentActivityInfoService.getAgentList(tAgentActivityInfo);
        if(!CollectionUtils.isEmpty(list) && list.get(0)!=null){
            list.forEach(t->{
                Map<String, Object> params = new HashMap<>();
                params.put("createTime", Objects.nonNull(t.getCreateTime())?t.getCreateTime().getTime():0l);
                t.setParams(params);
            });
        }else{
            list = new ArrayList<>();
        }
        return AjaxResult.success(list);
    }


}
