package com.ruoyi.socket.socketserver;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.bussiness.domain.TOwnCoinSubscribeOrder;
import com.ruoyi.bussiness.service.ITOwnCoinService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.socket.constants.SocketTypeConstants;
import com.ruoyi.socket.dto.MessageVo;
import com.ruoyi.socket.dto.SocketDto;
import com.ruoyi.socket.dto.SocketMessageVo;
import com.ruoyi.socket.dto.WsCoinSubVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 新币订阅消息推送
 *
 * @author HH
 * @date 2023/10/10
 */
@Component
@Slf4j
@ServerEndpoint("/ws/coin/{userId}")
public class WebSocketSubCoins {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
     */
    public static ConcurrentHashMap<String, WebSocketSubCoins> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    private static RedisCache redisCache;
    private static ITOwnCoinService tOwnCoinService;

    public void setWebSocketServers(RedisCache redisCache, ITOwnCoinService tOwnCoinService) {
        //把spring注入的类放到 ws中
        this.redisCache = redisCache;
        this.tOwnCoinService = tOwnCoinService;
    }

    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
        }
        SocketDto socketDto = new SocketDto();
        socketDto.setType("0");
        socketDto.setMessage("连接成功");
        sendMessage(JSONObject.toJSONString(socketDto));
    }

    /**
     * 收到客户端消
     * 息后调用的方法
     *
     * @param message 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
    }

    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            redisCache.deleteObject(this.userId);
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //推出后要遍历redis 订阅的所有信息
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务
     * 器主动推送
     */
    public synchronized void sendMessage(String message) {
        try {
            if (session == null) {
                return;
            }
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendUserFreeze(String userId) {
        SocketMessageVo socketMessageVo = new SocketMessageVo();
        socketMessageVo.setType(SocketTypeConstants.USER_STATUS);
        MessageVo messageVo = new MessageVo();
        messageVo.setMessage(SocketTypeConstants.USER_STATUS);
        messageVo.setCode("1");
        socketMessageVo.setDate(messageVo);
        String jsonStr = JSONUtil.toJsonStr(socketMessageVo);
        if (webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(jsonStr);
        }
    }


    public  void sendInfoAll(Integer code) {
        SocketMessageVo socketMessageVo = new SocketMessageVo();
        socketMessageVo.setType(SocketTypeConstants.SETTLEMENT);
        MessageVo messageVo = new MessageVo();
        messageVo.setCode("1");
        Map<String,Integer> map = new HashMap<>();
        map.put("type",code);
        for (String s : webSocketMap.keySet()) {
            messageVo.setMessage(com.alibaba.fastjson2.JSON.toJSONString(map));
            socketMessageVo.setDate(messageVo);
            String jsonStr = JSONUtil.toJsonStr(socketMessageVo);
            webSocketMap.get(s).sendMessage(jsonStr);
        }
    }

    public  void sendInfo(Integer code) {
        Map<String,Integer> map = new HashMap<>();
        map.put("position",code);

        SocketMessageVo socketMessageVo = new SocketMessageVo();
        socketMessageVo.setType(SocketTypeConstants.POSITION);
        MessageVo messageVo = new MessageVo();
        messageVo.setCode("1");
        for (String s : webSocketMap.keySet()) {
            messageVo.setMessage(JSON.toJSONString(map));
            socketMessageVo.setDate(messageVo);
            String jsonStr = JSONUtil.toJsonStr(socketMessageVo);
            webSocketMap.get(s).sendMessage(jsonStr);
        }
    }
}

