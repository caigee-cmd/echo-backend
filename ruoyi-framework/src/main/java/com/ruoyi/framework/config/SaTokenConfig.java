package com.ruoyi.framework.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zouhuu
 * @description [Sa-Token 权限认证] 配置类
 * @date 2022/06/15 15:32:59
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
        registry.addInterceptor(new SaRouteInterceptor())
                // 拦截 APP或小程序的 所有请求
                .addPathPatterns("/api/**")
                // 排除 APP或小程序的 认证授权请求，如登录等

                .excludePathPatterns(
                        "/api/user/register",
                        "/api/user/easyGenerateCode",
                        "/api/common/**",
                        "/api/app/**",
                        "/api/user/sendEmailCode",
                        "/api/user/sendMobileCode",
                        "/api/user/getCountryCode",
                        "/api/user/login",
                        "/api/user/backPwd",
                        "/api/user/bindPhoneEmail",
                        "/api/coin/list",
                        "/api/user/bindPhoneEmail",
                        "/api/notice/**",
                        "/api/timezone/getTimeZone",
                        "/api/option/rules/**",
                        "/ws/**",
                        "/api/recall/withdraw/unc",
                        "/api/recall/pay/unc"
                );
    }
}
