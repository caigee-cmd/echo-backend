package com.ruoyi.bussiness.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bussiness.domain.TWithdraw;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;
import org.springframework.stereotype.Service;

@Service
public interface ThirdPayOutService {
      String getName();

      JSONObject payOut(TWithdraw withdraw, ThirdPaySetting setting);



}

