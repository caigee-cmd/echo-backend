package com.ruoyi.bussiness.service;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bussiness.domain.TAppRecharge;
import com.ruoyi.bussiness.domain.setting.Setting;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;
import org.springframework.stereotype.Service;

@Service
public interface ThirdPayService {
      String getName();
     //代收
     JSONObject pay(TAppRecharge recharge, ThirdPaySetting setting);

     JSONObject createAdress(String coin ,String  symbol,Long userId,ThirdPaySetting setting);

     boolean  existAdress(String  symbol,String adress,ThirdPaySetting setting);
}

