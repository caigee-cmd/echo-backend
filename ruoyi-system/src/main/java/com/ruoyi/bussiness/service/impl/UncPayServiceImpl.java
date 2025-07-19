package com.ruoyi.bussiness.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bussiness.domain.TAppRecharge;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;
import com.ruoyi.bussiness.service.ThirdPayService;
import com.ruoyi.common.enums.RechargeTypeUncEmun;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.udun.client.UdunClient;
import com.ruoyi.udun.domain.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Slf4j
public class UncPayServiceImpl implements ThirdPayService {


//    private static final String MECHID="314177";
//
//    public static final String CALLURL = "https://api.rxcecoin.com/api/recall/withdraw/unc";

    @Override
    public String getName() {
        return "unc";
    }

    @Override
    public JSONObject pay(TAppRecharge recharge, ThirdPaySetting setting) {



        return null;
    }

    @Override
    public JSONObject createAdress(String coin, String symbol, Long userId,ThirdPaySetting setting) {
        JSONObject jsonObject=new JSONObject();
        UdunClient udunClient = new UdunClient(setting.getUrl(),
                setting.getMechId(),
                setting.getKey(),
                setting.getReturnUrl());
        String value = RechargeTypeUncEmun.getValue(symbol);
        if (StringUtils.isEmpty(value)) {
            return  jsonObject;
        }
        Address address = udunClient.createAddress(value);
        if(!Objects.isNull(address)){
            jsonObject.put("adress",address.getAddress());
        }
        return jsonObject;
    }

    @Override
    public boolean existAdress(String symbol, String adress, ThirdPaySetting setting) {
        UdunClient udunClient = new UdunClient(setting.getUrl(),
                setting.getMechId(),
                setting.getKey(),
                setting.getReturnUrl());
        String value = RechargeTypeUncEmun.getValue(symbol);
            if (StringUtils.isEmpty(value)) {
                return  false;
        }
        return  udunClient.existAdress(value,adress);
    }
}
