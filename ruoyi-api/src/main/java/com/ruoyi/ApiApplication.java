package com.ruoyi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableScheduling
@EnableAsync
@EnableCaching

public class ApiApplication{
    public static final String ProxyHost = "g943.kdlfps.com";
    public static final int ProxyPort = 18866; // 替换为你的代理端口号
    public static void main(String[] args){

//        System.setProperty("https.protocols", "TLSv1.3,TLSv1.2,TLSv1.1,SSLv3,TLSv1");
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("f2514518491", "tmr90mc6".toCharArray());
                    }
                }
        );

        SpringApplication.run(ApiApplication.class, args);
        System.out.println("api 启动成功  \n" );
    }

}
