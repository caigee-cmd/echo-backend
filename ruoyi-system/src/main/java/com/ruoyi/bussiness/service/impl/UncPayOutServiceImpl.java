package com.ruoyi.bussiness.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bussiness.domain.TWithdraw;
import com.ruoyi.bussiness.domain.setting.ThirdPaySetting;
import com.ruoyi.bussiness.service.ThirdPayOutService;
import com.ruoyi.common.enums.WithdrawTypeUncEmun;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.udun.client.UdunClient;
import com.ruoyi.udun.domain.Coin;
import com.ruoyi.udun.domain.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UncPayOutServiceImpl implements ThirdPayOutService {

    @Override
    public String getName() {
        return "unc";
    }

    @Override
    public JSONObject payOut(TWithdraw withdraw, ThirdPaySetting setting) {
        JSONObject jsonObject = new JSONObject();
        try {
            UdunClient udunClient = new UdunClient(setting.getUrl(),
                    setting.getMechId(),
                    setting.getKey(),
                    setting.getReturnUrl());
            //查询支持的币种
            String type = withdraw.getType();

            String orderNo = withdraw.getSerialId();

            BigDecimal amount = withdraw.getRealAmount();

            String toAdress = withdraw.getToAdress();
            String name = WithdrawTypeUncEmun.getValue(type);
            if (StringUtils.isEmpty(name)) {
                log.info("unc 类型" + name);

                return null;
            }
            Coin coin1 = checkWithByName(udunClient, name);
            if (Objects.isNull(coin1)) {
                log.info("unc 支持币种" + type);
                 return null;
            }
            ResultMsg resultMsg = udunClient.withdraw(toAdress, amount, coin1.getMainCoinType(), coin1.getCoinType(), orderNo, null);
            if (resultMsg.getCode() == 200) {
                jsonObject.put("code",200);
                jsonObject.put("message", resultMsg.getMessage());
            }else {
                log.error("undu, resultMsg:{}", resultMsg);
                jsonObject.put("code", resultMsg.getCode());
                jsonObject.put("message", resultMsg.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 10002);
            jsonObject.put("message", "U盾提现接口异常");
        }
        return jsonObject;
    }


    private Coin checkWithByName(UdunClient udunClient, String name) {
        Coin f = null;
        name = name.toUpperCase();
        try {
            List<Coin> coinList = udunClient.listSupportCoin(false);
            if (!CollectionUtils.isEmpty(coinList)) {
                for (Coin coin1 : coinList) {
                    String name1 = coin1.getName();
                    if (name.equals(name1)) {
                        f = coin1;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
