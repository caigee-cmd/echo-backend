package com.ruoyi.web.controller.bussiness;

import com.ruoyi.bussiness.service.ITAppWalletRecordService;
import com.ruoyi.web.controller.common.ApiBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;


/**
 * 用户信息Controller
 * 
 * @author ruoyi
 * @date 2023-07-04
 */
@RestController
@RequestMapping("/api/record")
public class TAppWalletRecordController extends ApiBaseController
{
    @Autowired
    private ITAppWalletRecordService tAppWalletRecordService;

}
