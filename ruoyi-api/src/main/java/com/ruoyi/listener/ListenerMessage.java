package com.ruoyi.listener;

import cn.hutool.json.JSONUtil;
import com.ruoyi.bussiness.domain.setting.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.RedisUtil;
import com.ruoyi.common.utils.SpringContextUtil;
import com.ruoyi.socket.service.MarketThread;
import com.ruoyi.socket.socketserver.WebSocketCoinOver;
import com.ruoyi.socket.socketserver.WebSocketNotice;
import com.ruoyi.socket.socketserver.WebSocketSubCoins;
import com.ruoyi.websocket.WebSocketRunner;
import com.ruoyi.websocket.WebSocketSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ListenerMessage implements StreamListener<String, MapRecord<String, String, String>> {

    RedisUtil redisUtil;
    List<MarketThread> marketThread;
    WebSocketNotice webSocketNotice;
    WebSocketCoinOver webSocketCoinOver;
    WebSocketSubCoins webSocketCoin;
    WebSocketRunner webSocketRunner;


    public ListenerMessage(RedisUtil redisUtil,WebSocketRunner webSocketRunner, WebSocketNotice webSocketNotice, WebSocketCoinOver webSocketCoinOver,WebSocketSubCoins webSocketCoin){
        this.redisUtil = redisUtil;
        this.webSocketRunner = webSocketRunner;
        this.webSocketNotice = webSocketNotice;
        this.webSocketCoinOver = webSocketCoinOver;
        this.webSocketCoin = webSocketCoin;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        try{
            //check用于验证key和对应消息是否一直
            log.debug("stream name :{}, body:{}, check:{}",entries.getStream(),entries.getValue(),(entries.getStream().equals(entries.getValue().get("name"))));
            String stream = entries.getStream();
            switch (stream) {
                case "socket_key":
                    Map<String, String> map = entries.getValue();
                    for (String s : map.keySet()) {
                        if(s.equals("settlement")){
                            webSocketCoin.sendInfoAll(Integer.parseInt(map.get(s)));
                        }else  if(s.equals("position")){
                            webSocketCoin.sendInfo(Integer.parseInt(map.get(s)));
                        }else  if(s.equals("user_status")){
                            webSocketCoin.sendUserFreeze(map.get(s));
                        } else if (s.equals("add_coin")) {
                            webSocketRunner.reStart();
                            marketThread.stream().forEach(marketThread1 -> {
                               // marketThread1.marketThreadRun();
                            });
                        }
                    }
            }
            redisUtil.delField(entries.getStream(),entries.getId().getValue());
        }catch (Exception e){
            log.error("error message:{}",e.getMessage());
        }
    }

}
