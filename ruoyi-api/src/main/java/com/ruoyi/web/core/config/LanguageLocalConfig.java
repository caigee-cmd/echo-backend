package com.ruoyi.web.core.config;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class LanguageLocalConfig implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale = Locale.CHINA;
        String language = request.getHeader("Lang");
        if (StringUtils.isNotBlank(language)) {
            if(language.contains("_")){
                String[] splitLanguage = language.split("_");
                if (splitLanguage.length > 1) {
                    locale = new Locale(splitLanguage[0], splitLanguage[1]);
                }
            }else {
                locale = new Locale(language);
            }

        }
        return locale;
    }

    @Override
    public void setLocale(
            HttpServletRequest request,
            HttpServletResponse response,
            Locale locale) {

    }

    @Bean
    public LocaleResolver localeResolver() {
        return new LanguageLocalConfig();
    }
}
