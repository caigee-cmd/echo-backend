package com.ruoyi.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台文本类型
 */
public enum OptionRulesEnum {

    PERIOD_EXPLAIN(1,"秒合约说明"),
    COIN_EXPLAIN(2,"币币交易说明"),
    U_STANDARD_EXPLAIN(4,"U本位合约说明"),
    DEFI_EXPLAIN(9,"DEFI挖矿说明"),
    PLEDGE_EXPLAIN(10,"质押挖矿说明"),
    FINANCIAL_AGREEMENT(8,"理财协议"),
    LOANS_RULE(7,"贷款规则"),
    AGENCY_ACTIVITY(3,"代理活动"),
    PROMOTION_CENTER_EXPLAIN(11,"推广中心"),
    TERMS_CLAUSE(0,"服务条款"),
    REGISTRY_PRIVACY(5,"注册隐私政策"),
    REGISTRY_CLAUSE(6,"注册使用条款"),
    ABOUT_US(12,"关于我们");


    OptionRulesEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    private Integer code;
    private String value;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static List<Map<String, Object>> getEnum(){
        OptionRulesEnum[] values = OptionRulesEnum.values();
        List<Map<String, Object>> list = new ArrayList<>();
        for (OptionRulesEnum optionRules :values) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key",optionRules.name());
//            resultMap.put("code",optionRules.getCode());
            resultMap.put("value",optionRules.getValue());
            list.add(resultMap);
        }
        return list;
    }
}
