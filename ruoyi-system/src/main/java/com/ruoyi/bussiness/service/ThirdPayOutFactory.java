package com.ruoyi.bussiness.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThirdPayOutFactory implements ApplicationContextAware {

    private static Map<String, ThirdPayOutService> thirdPayOutBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ThirdPayOutService> map = applicationContext.getBeansOfType(ThirdPayOutService.class);
        thirdPayOutBeanMap = new HashMap<>();
        map.forEach((key, value) -> thirdPayOutBeanMap.put(value.getName(), value));
    }

    public static <T extends ThirdPayOutService> T getThirdPayOut(String name) {
        return (T)thirdPayOutBeanMap.get(name);
    }

}
