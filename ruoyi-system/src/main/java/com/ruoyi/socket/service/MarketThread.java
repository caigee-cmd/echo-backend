package com.ruoyi.socket.service;

import java.net.URISyntaxException;
import java.util.Map;

public interface MarketThread {


    void marketThreadRun() ;

/*    void addCoinTheadRun(Map<String,String> map );*/

    void getAllPrice();


    void initMarketThreadRun() ;
}
