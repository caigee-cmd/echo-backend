package com.ruoyi.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.bussiness.domain.KlineSymbol;
import com.ruoyi.socket.manager.WebSocketUserManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.*;

@Slf4j
public class WebSocketSubscriber extends WebSocketClient {
    private static final long RECONNECT_INTERVAL = 5000; // 重连间隔，单位：毫秒

    private Timer reconnectTimer;
    private WebSocketUserManager webSocketUserManager;
    private Set<String> allCoins;

    private List<KlineSymbol> coinList;

    public WebSocketSubscriber(URI serverUri, Draft draft, List<KlineSymbol> coinList, WebSocketUserManager webSocketUserManager, Set<String> allCoins) {
        super(serverUri, draft);
        reconnectTimer = new Timer();
        this.coinList = coinList;
        this.webSocketUserManager = webSocketUserManager;
        this.allCoins = allCoins;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
       log.info("WebSocket connection opened.");
    }

    @Override
    public void onMessage(String message) {
        // 处理接收到的消息
        // 如果收到服务器发送的ping帧，回复pong帧
        if (message.equals("Ping")) {
            send("Pong");
        }
        //处理k线是数据
        if(message.contains("kline")){
            JSONObject obj = JSON.parseObject(message);
            String event = obj.toString();
            JSONObject jsonObject = JSON.parseObject(message);
            webSocketUserManager.binanceKlineSendMeg(event);
            String s =  event;
            //kline 逻辑  分发 和 控币   异步
            for (KlineSymbol kSymbol : coinList) {
                if((jsonObject.getString("s").toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                    event = ownKline(s, kSymbol);
                    webSocketUserManager.binanceKlineSendMeg(event);
                }
            }
            s = createEvent(s);
            webSocketUserManager.binanceTRADESendMeg(s);
            for (KlineSymbol kSymbol : coinList) {
                if((jsonObject.getString("s").toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                    event = ownTrade(s, kSymbol);
                    webSocketUserManager.binanceTRADESendMeg(event);
                }
            }
        }
        //处理detail
        if(message.contains("24hrTicker")){
            JSONArray arr = JSONArray.parseArray(message);
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = JSON.parseObject(arr.get(i).toString());
                String event = obj.toString();
                webSocketUserManager.savePriceRdies(event);
                if(allCoins.contains(obj.getString("s").toLowerCase())){
                    webSocketUserManager.binanceDETAILSendMeg(event);
                    String s = event;
                    for (KlineSymbol kSymbol : coinList) {
                        if((obj.getString("s").toLowerCase().replace("usdt","")).equals(kSymbol.getReferCoin().toLowerCase())) {
                            event= ownDetail(s,kSymbol);
                            webSocketUserManager.binanceDETAILSendMeg(event);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed. Code: " + code + ", Reason: " + reason);
        if(-1 != code){
            reconnectWebSocket();
        }
    }

    @Override
    public void onError(Exception e) {
        System.err.println(e);
        System.err.println("WebSocket error: " + e.getMessage());
        reconnectWebSocket();
    }

    private void reconnectWebSocket() {
        reconnectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                reconnect();
                System.out.println("Attempting to reconnect to WebSocket...");
            }
        }, RECONNECT_INTERVAL);
    }

    private String ownDetail(String event,KlineSymbol kSymbol) {
        JSONObject jsonObject = JSONObject.parseObject(event);
        BigDecimal proportion = kSymbol.getProportion();
        if(proportion.compareTo(BigDecimal.ZERO)==0){
            proportion=new BigDecimal("100");
        }
        BigDecimal o = new BigDecimal(jsonObject.getString("o"));
        BigDecimal h = new BigDecimal(jsonObject.getString("h"));
        BigDecimal l = new BigDecimal(jsonObject.getString("l"));
        BigDecimal c = new BigDecimal(jsonObject.getString("c"));
        BigDecimal q = new BigDecimal(jsonObject.getString("q"));
        jsonObject.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        jsonObject.put("o",o.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("h",h.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("l",l.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("c",c.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("q",q.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        return jsonObject.toJSONString();
    }

    private String ownTrade(String event,KlineSymbol kSymbol ) {
        BigDecimal proportion = kSymbol.getProportion();
        if(proportion.compareTo(BigDecimal.ZERO)==0){
            proportion=new BigDecimal("100");
        }
        JSONObject jsonObject = JSONObject.parseObject(event);
        BigDecimal p = new BigDecimal(jsonObject.getString("p"));
        jsonObject.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        jsonObject.put("p",p.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        return jsonObject.toJSONString();
    }


    private String ownKline(String event,KlineSymbol kSymbol) {
        BigDecimal proportion = kSymbol.getProportion();
        if(proportion.compareTo(BigDecimal.ZERO)==0){
            proportion=new BigDecimal("100");
        }
        JSONObject jsonObject = JSONObject.parseObject(event);
        JSONObject k = jsonObject.getJSONObject("k");
        BigDecimal o = new BigDecimal(k.getString("o"));
        BigDecimal h = new BigDecimal(k.getString("h"));
        BigDecimal l = new BigDecimal(k.getString("l"));
        BigDecimal c = new BigDecimal(k.getString("c"));
        BigDecimal q = new BigDecimal(k.getString("q"));
        jsonObject.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        k.put("s",kSymbol.getSymbol().toUpperCase()+"USDT");
        k.put("o",o.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("h",h.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("l",l.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("c",c.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        k.put("q",q.multiply(proportion).divide(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
        jsonObject.put("k",k);
        return jsonObject.toJSONString();
    }
    private String createEvent(String event) {
        JSONObject jsonObject = JSONObject.parseObject(event);
        String k = jsonObject.getString("k");
        JSONObject K = JSONObject.parseObject(k);

        String s = jsonObject.getString("s");
        String E = jsonObject.getString("E");
        String T = K.getString("T");

        BigDecimal p =new  BigDecimal(K.getString("c"));
        BigDecimal q = null;
        // 根据价格 生成随机成交数量  0-1   50--2000    价格 1-10  200以内   价格 10-50     0-20    价格50-5000    0.00005000  以内随机数    5000+以上    0.000005000
        Integer randomBoolean=new Random().nextInt(2);  //这儿是生成的小于100的整数，nextInt方法的参数值要是大于0的整数
        Boolean m = randomBoolean>0?true:false;
        // 产生一个2~100的数
        if(p.compareTo(BigDecimal.ONE)<0){
            int min = 50; // 定义随机数的最小值
            int max = 2000; // 定义随机数的最大值
            Integer random = (int) min + (int) (Math.random() * (max - min));
            q=new BigDecimal(random.toString());
        }
        if(p.compareTo(new BigDecimal("1"))>0 &&  p.compareTo(new BigDecimal("10"))<0){
            int max = 2000; // 定义随机数的最大值
            Integer random =  (int) (Math.random() * (max));
            q=new BigDecimal(random.toString());
        }
        if(p.compareTo(new BigDecimal("10"))>0 &&  p.compareTo(new BigDecimal("50"))<0){
            int max = 20; // 定义随机数的最大值
            Integer random =  + (int) (Math.random() * (max));
            q=new BigDecimal(random.toString());
        }
        if(p.compareTo(new BigDecimal("50"))>0 &&  p.compareTo(new BigDecimal("5000"))<0){
            int min = 50; // 定义随机数的最小值
            int max = 5000; // 定义随机数的最大值
            Integer random = (int) min + (int) (Math.random() * (max - min));
            q=new BigDecimal(random.toString()).divide(new BigDecimal("1000000"));
        }
        if(p.compareTo(new BigDecimal("5000"))>0 ){
            int min = 50; // 定义随机数的最小值
            int max = 5000; // 定义随机数的最大值
            Integer random = (int) min + (int) (Math.random() * (max - min));
            q=new BigDecimal(random.toString()).divide(new BigDecimal("10000000"));
        }
        String str = "{'e':'aggTrade','E':'"+E+"','s':'"+s+"','p':'"+p+"','q':'"+q.toString()+"','T':'"+T+"','m':'"+m+"'}";
        return str;
    }
}
