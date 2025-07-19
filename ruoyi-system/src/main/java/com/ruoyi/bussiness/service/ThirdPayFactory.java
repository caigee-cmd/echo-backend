package com.ruoyi.bussiness.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThirdPayFactory implements ApplicationContextAware {

    private static Map<String, ThirdPayService> thirdPayBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ThirdPayService> map = applicationContext.getBeansOfType(ThirdPayService.class);
        thirdPayBeanMap = new HashMap<>();
        map.forEach((key, value) -> thirdPayBeanMap.put(value.getName(), value));
    }

    public static <T extends ThirdPayService> T getThirdpay(String name) {
        return (T)thirdPayBeanMap.get(name);
    }

}
