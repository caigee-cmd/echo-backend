package com.ruoyi.web.controller.bussiness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.enums.NoticeTypeEnum;
import com.ruoyi.common.utils.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.bussiness.domain.TNotice;
import com.ruoyi.bussiness.service.ITNoticeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通知公告Controller
 * 
 * @author ruoyi
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/api/notice")
public class TNoticeController extends BaseController
{
    @Autowired
    private ITNoticeService tNoticeService;

    @PostMapping("/list")
    public AjaxResult list(TNotice tNotice,HttpServletRequest httpRequest)
    {
        String lang = httpRequest.getHeader("Lang");
        if (StringUtils.isNotBlank(lang)){
            tNotice.setLanguageId(lang);
        }
        tNotice.setNoticeType(NoticeTypeEnum.valueOf(tNotice.getKey()).getCode());
        if (StringUtils.isNotBlank(tNotice.getModelKey())){
            tNotice.setModelType(NoticeTypeEnum.ChildrenEnum.valueOf(tNotice.getModelKey()).getCode());
        }
        startPage();
        List<TNotice> list = tNoticeService.selectTNoticeList(tNotice);
        NoticeTypeEnum[] typeEnumList = NoticeTypeEnum.values();
        NoticeTypeEnum.ChildrenEnum[] childrenEnumList = NoticeTypeEnum.ChildrenEnum.values();
        for (TNotice notice:list) {
            if (notice.getModelType()!=null){
                for (int i = 0; i < childrenEnumList.length; i++) {
                    if (notice.getNoticeType().equals(childrenEnumList[i].getPrent().getCode()) && childrenEnumList[i].getCode().equals(notice.getModelType())){
                        notice.setModelType(childrenEnumList[i].getValue());
                    }
                }
            }
            for (NoticeTypeEnum typeEnum:typeEnumList) {
                if (typeEnum.getCode().equals(notice.getNoticeType())){
                    notice.setNoticeType(typeEnum.getValue());
                }
            }
        }
        return AjaxResult.success(list);
    }
    @PostMapping("/getAllNoticeList")
    public AjaxResult getAllNoticeList(HttpServletRequest httpRequest)
    {
        Map<String,List<TNotice>> map  = new HashMap<>();
        String lang = httpRequest.getHeader("Lang");
        TNotice tNotice = new TNotice();
        TNotice tNotice2 = new TNotice();
        tNotice.setKey("ACTIVITY_NOTICE");
        tNotice.setModelKey("HOME_ACTIVITY");
        if (StringUtils.isNotBlank(lang)){
            tNotice.setLanguageId(lang);
            tNotice2.setLanguageId(lang);
        }
        tNotice.setNoticeType(NoticeTypeEnum.valueOf(tNotice.getKey()).getCode());
        if (StringUtils.isNotBlank(tNotice.getModelKey())){
            tNotice.setModelType(NoticeTypeEnum.ChildrenEnum.valueOf(tNotice.getModelKey()).getCode());
        }
        startPage();
        List<TNotice> tNotices = tNoticeService.selectTNoticeList(tNotice);
        NoticeTypeEnum[] typeEnumList = NoticeTypeEnum.values();
        NoticeTypeEnum.ChildrenEnum[] childrenEnumList = NoticeTypeEnum.ChildrenEnum.values();
        for (TNotice notice:tNotices) {
            if (notice.getModelType()!=null){
                for (int i = 0; i < childrenEnumList.length; i++) {
                    if (notice.getNoticeType().equals(childrenEnumList[i].getPrent().getCode()) && childrenEnumList[i].getCode().equals(notice.getModelType())){
                        notice.setModelType(childrenEnumList[i].getValue());
                    }
                }
            }
            for (NoticeTypeEnum typeEnum:typeEnumList) {
                if (typeEnum.getCode().equals(notice.getNoticeType())){
                    notice.setNoticeType(typeEnum.getValue());
                }
            }
        }
        map.put("ACTIVITY_NOTICE",tNotices);



        tNotice2.setKey("ROLL_NOTICE");
        tNotice2.setNoticeType(NoticeTypeEnum.valueOf(tNotice2.getKey()).getCode());
        if (StringUtils.isNotBlank(tNotice2.getModelKey())){
            tNotice2.setModelType(NoticeTypeEnum.ChildrenEnum.valueOf(tNotice2.getModelKey()).getCode());
        }
        startPage();
        List<TNotice> tNotices1 = tNoticeService.selectTNoticeList(tNotice2);
        NoticeTypeEnum[] typeEnumList1 = NoticeTypeEnum.values();
        NoticeTypeEnum.ChildrenEnum[] childrenEnumList1 = NoticeTypeEnum.ChildrenEnum.values();
        for (TNotice notice:tNotices1) {
            if (notice.getModelType()!=null){
                for (int i = 0; i < childrenEnumList1.length; i++) {
                    if (notice.getNoticeType().equals(childrenEnumList1[i].getPrent().getCode()) && childrenEnumList1[i].getCode().equals(notice.getModelType())){
                        notice.setModelType(childrenEnumList1[i].getValue());
                    }
                }
            }
            for (NoticeTypeEnum typeEnum:typeEnumList1) {
                if (typeEnum.getCode().equals(notice.getNoticeType())){
                    notice.setNoticeType(typeEnum.getValue());
                }
            }
        }
        map.put("ROLL_NOTICE",tNotices1);
        return AjaxResult.success(map);
    }
    /**
     * 获取通知公告详细信息
     */
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable("noticeId") Long noticeId)
    {
        TNotice tNotice = tNoticeService.selectTNoticeByNoticeId(noticeId);
        NoticeTypeEnum[] typeEnumList = NoticeTypeEnum.values();
        NoticeTypeEnum.ChildrenEnum[] childrenEnumList = NoticeTypeEnum.ChildrenEnum.values();
        if (tNotice.getModelType()!=null){
            for (int i = 0; i < childrenEnumList.length; i++) {
                if (tNotice.getNoticeType().equals(childrenEnumList[i].getPrent().getCode()) && childrenEnumList[i].getCode().equals(tNotice.getModelType())){
//                    tNotice.setModelType(childrenEnumList[i].getValue());
                    tNotice.setModelKey(childrenEnumList[i].name());
                }
            }
        }
        for (NoticeTypeEnum typeEnum:typeEnumList) {
            if (typeEnum.getCode().equals(tNotice.getNoticeType())){
//                tNotice.setNoticeType(typeEnum.getValue());
                tNotice.setKey(typeEnum.name());
            }
        }
        return success(tNotice);
    }

}
