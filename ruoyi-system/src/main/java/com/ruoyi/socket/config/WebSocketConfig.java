package com.ruoyi.socket.config;

import com.ruoyi.bussiness.service.ITOwnCoinService;
import com.ruoyi.bussiness.service.ITWithdrawService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.socket.manager.WebSocketUserManager;

import com.ruoyi.socket.socketserver.WebSocketCoinOver;
import com.ruoyi.socket.socketserver.WebSocketNotice;
import com.ruoyi.socket.socketserver.WebSocketServers;
import com.ruoyi.socket.socketserver.WebSocketSubCoins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebSocketConfig {
    @Autowired
    RedisCache redisCache;
    @Autowired
    WebSocketServers webSocketServers;
    @Autowired
    WebSocketUserManager webSocketConfig;
    @Autowired
    WebSocketNotice webSocketNotice;
    @Autowired
    WebSocketCoinOver webSocketCoinOver;
    @Autowired
    WebSocketSubCoins webSocketSubCoins;
    @Autowired
    ITWithdrawService withdrawService;
    @Autowired
    ITOwnCoinService tOwnCoinService;
    /**
     * 这个bean会自动注册使用了@ServerEndpoint注解声明的对象
     * @return
     */
    @Bean
    public void webSocketHandler() {
       webSocketServers.setWebSocketServers(redisCache,webSocketConfig);
       webSocketNotice.setWebSocketServers(redisCache,withdrawService);
       webSocketCoinOver.setWebSocketServers(redisCache);
       webSocketSubCoins.setWebSocketServers(redisCache,tOwnCoinService);
    }
}