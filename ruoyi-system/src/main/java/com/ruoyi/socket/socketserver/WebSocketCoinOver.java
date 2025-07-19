package com.ruoyi.socket.socketserver;


        import com.alibaba.fastjson2.JSON;
        import com.alibaba.fastjson2.JSONObject;
        import com.ruoyi.bussiness.service.ITWithdrawService;
        import com.ruoyi.common.core.redis.RedisCache;
        import com.ruoyi.socket.dto.SocketDto;
        import jnr.ffi.annotations.In;
        import lombok.extern.slf4j.Slf4j;
        import org.apache.commons.lang3.StringUtils;
        import org.springframework.stereotype.Component;

        import javax.annotation.Resource;
        import javax.websocket.*;
        import javax.websocket.server.PathParam;
        import javax.websocket.server.ServerEndpoint;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket的处理类。
 * 作用相当于HTTP请求
 * 中的controller
 */
@Component
@Slf4j
@ServerEndpoint("/webSocket/coinOver/{userId}")
public class WebSocketCoinOver {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    public static ConcurrentHashMap<String,WebSocketCoinOver> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId = "";

    private static RedisCache redisCache;
    private static ITWithdrawService withdrawService;
    public void setWebSocketServers(RedisCache redisCache){
        WebSocketCoinOver.redisCache = redisCache;
    }

    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        redisCache.setCacheObject(userId,"1");

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
    public void onClose(CloseReason closeReason) {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
            redisCache.deleteObject(this.userId);
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
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {

                //解析发送的报文
                String toUserId=this.userId;
                //传送给对应toUserId用户的websocket
                if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(message);
                }
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
    public void sendMessage(String message) {
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
     *发送自定
     *义消息
     **/
    public  void sendInfoAll(Integer code) {
        Map<String,Integer> map = new HashMap<>();
        map.put("type",code);
        for (String s : webSocketMap.keySet()) {
            webSocketMap.get(s).sendMessage(JSON.toJSONString(map));
        }
    }

    public  void sendInfo(Integer code) {
        Map<String,Integer> map = new HashMap<>();
        map.put("position",code);
        for (String s : webSocketMap.keySet()) {
            webSocketMap.get(s).sendMessage(JSON.toJSONString(map));
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
        WebSocketCoinOver.onlineCount++;
    }

    /**
     * 在线人
     * 数减1
     */
    public static synchronized void subOnlineCount() {
        WebSocketCoinOver.onlineCount--;
    }

}

