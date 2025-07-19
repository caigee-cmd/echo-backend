package com.ruoyi.websocket;

import com.ruoyi.socket.service.MarketThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.List;


@Component
@Slf4j
public class SocketThreadRun {

    @Resource
    private List<MarketThread> marketThread;

    @PostConstruct
    public void starSubMark() {
        for (MarketThread thread : marketThread) {
 /*           thread.marketThreadRun();
            thread.initMarketThreadRun();*/
        }

    }

}
