package com.ruoyi.socket.socketserver;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.socket.dto.SocketDto;
import com.ruoyi.socket.dto.WsSubBO;
import com.ruoyi.socket.manager.WebSocketUserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * websocket的处理类。
 * 作用相当于HTTP请求
 * 中的controller
 */
@Component
@Slf4j
@ServerEndpoint("/ws/{userId}")
public class WebSocketServers {
    //websocket 存储订阅对象   key 为 订阅类型。DETAIL   TRADE   KLINE List为订阅对象Id  取消时候delete
    public static ConcurrentHashMap<String , List<String>> detailMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String , List<String>> tradeMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String , List<String>> klineMap = new ConcurrentHashMap<>();
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    public static ConcurrentHashMap<String,WebSocketServers> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId = "";
    private static WebSocketUserManager webSocketUserManager;
    private static RedisCache redisCache;
    public void setWebSocketServers(RedisCache redisCache,WebSocketUserManager webSocketUserManager){
        //把spring注入的类放到 ws中
        this.redisCache = redisCache;
        this.webSocketUserManager = webSocketUserManager;
    }
    
    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //加入set中
            webSocketMap.put(userId,this);
        }else{
            //加入set中
            webSocketMap.put(userId,this);
            //在线数加1
            addOnlineCount();
        }
        log.debug("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());
        SocketDto socketDto = new SocketDto();
        socketDto.setType("0");
        socketDto.setMessage("连接成功");
        sendMessage(JSONObject.toJSONString(socketDto));
    }

    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
            redisCache.deleteObject(this.userId);
            //DETAIL   TRADE   KLINE
            List<String> detail = detailMap.get("DETAIL");
            detail.remove(userId);
            detailMap.put("DETAIL",detail);
            for (Map.Entry<String,  List<String>> entry : klineMap.entrySet()) {
                List<String> value = entry.getValue();
                value.remove(userId);
            }
            for (Map.Entry<String,  List<String>> entry : tradeMap.entrySet()) {

                List<String> value = entry.getValue();
                value.remove(userId);
            }
        }
        log.debug("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消
     * 息后调用的方法
     * @param message
     * 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
      /*  message= message.replace("\"","");
       */

        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)&&!message.equals("heartbeat")){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //消息订阅
                WsSubBO wsSubBo = jsonObject.toJavaObject(WsSubBO.class);
                wsSubBo.setUserId(userId);
                webSocketUserManager.subscribeMsg(wsSubBo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //推出后要遍历redis 订阅的所有信息
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务
     * 器主动推送
     */
    public synchronized  void sendMessage(String message) {
        try {
            if(session==null){
                return;
            }
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *发送自定
     *义消息
     **/
    public static void sendInfo(String message, String userId) {
        if(StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }
    }

    /**
     * 获得此时的
     * 在线人数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人
     * 数加1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServers.onlineCount++;
    }

    /**
     * 在线人
     * 数减1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServers.onlineCount--;
    }

}

