package com.ruoyi.web.controller.app;


import com.ruoyi.bussiness.domain.TActivityRecharge;
import com.ruoyi.bussiness.domain.TNotice;
import com.ruoyi.bussiness.service.ITNoticeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.enums.NoticeTypeEnum;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/app/noticeTop")
public class TNoticeTopController extends BaseController {


    @Autowired
    private ITNoticeService tNoticeService;

    @PostMapping("/list")
    public AjaxResult list(TNotice tNotice, HttpServletRequest httpRequest)
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
        return new AjaxResult(0,"success",list);
    }
}
