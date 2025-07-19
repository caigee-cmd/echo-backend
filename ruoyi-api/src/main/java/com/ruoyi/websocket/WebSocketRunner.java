package com.ruoyi.websocket;

import com.ruoyi.common.utils.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
public class WebSocketRunner implements CommandLineRunner {

    @Resource
    private WebSocketConfigdd webSocketConfigdd;


    private Map<String,Object> map=new HashMap<>();
    @Override
    public void run(String... args) {
        //System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        WebSocketSubscriber webSocketSubscriberKline = webSocketConfigdd.webSocketSubscriberKline();
        WebSocketSubscriber webSocketSubscriberDetail = webSocketConfigdd.webSocketSubscriberDetail();

        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 33210));
        //
        //webSocketSubscriberKline.setProxy(proxy);
        //webSocketSubscriberDetail.setProxy(proxy);

        webSocketSubscriberKline.connect();
        webSocketSubscriberDetail.connect();
        map.put("kline",webSocketSubscriberKline);
        map.put("detaul",webSocketSubscriberDetail);
    }
    public void reStart(String... args) throws InterruptedException {
        WebSocketSubscriber webSocketSubscriberKline = (WebSocketSubscriber) map.get("kline");
        WebSocketSubscriber webSocketSubscriberDetail = (WebSocketSubscriber) map.get("detaul");
        webSocketSubscriberKline.onClose(-1,null,true);
        webSocketSubscriberDetail.onClose(-1,null,true);
        WebSocketSubscriber webSocketSubscriber1 = webSocketConfigdd.webSocketSubscriberKline();
        WebSocketSubscriber webSocketSubscriber2 = webSocketConfigdd.webSocketSubscriberDetail();
        map.put("kline",webSocketSubscriber1);
        map.put("detaul",webSocketSubscriber2);
        webSocketSubscriber1.connect();
        webSocketSubscriber2.connect();

    }
}
