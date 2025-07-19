package com.ruoyi.task;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.bussiness.domain.TBotKlineModel;
import com.ruoyi.bussiness.domain.TOwnCoinSubscribeOrder;
import com.ruoyi.bussiness.service.ITBotKlineModelService;
import com.ruoyi.bussiness.service.ITOwnCoinService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CachePrefix;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.socket.config.KLoader;
import com.ruoyi.socket.constants.SocketTypeConstants;
import com.ruoyi.socket.dto.MessageVo;
import com.ruoyi.socket.dto.SocketMessageVo;
import com.ruoyi.socket.dto.WsCoinSubVO;
import com.ruoyi.socket.socketserver.WebSocketSubCoins;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BotKlineTask {
    @Resource
    ITBotKlineModelService itBotKlineModelService;
    @Resource
    RedisCache redisCache;

    @Scheduled(cron = "*/15 * * * * ?")
    public void botKline() {
        Date date = new Date();
        TBotKlineModel tBotKlineModel = new TBotKlineModel();
        tBotKlineModel.setBeginTime(date);
        List<TBotKlineModel> botModelListByTime = itBotKlineModelService.getBotModelListByTime(tBotKlineModel);
        for (TBotKlineModel botModel : botModelListByTime) {
            log.info("存储控线缓存");
            HashMap botMap = new HashMap();
            BigDecimal currentlyPrice = redisCache.getCacheObject(CachePrefix.CURRENCY_PRICE.getPrefix() + botModel.getSymbol().replace("usdt", ""));
            botMap.put("id", botModel.getId());
            botMap.put("currentlyPrice", currentlyPrice);
            botModel.setConPrice(currentlyPrice);
            itBotKlineModelService.updateByid(botModel);
            KLoader.BOT_MAP.put("bot-" + botModel.getSymbol().replace("usdt", ""), botMap);
            KLoader.BOT_TIME_MAP.put(botModel.getSymbol().replace("usdt", ""), "0");
            KLoader.BOT_PRICE.put(botModel.getSymbol().replace("usdt", ""),currentlyPrice);
        }
        List<TBotKlineModel> list = itBotKlineModelService.list();
        if (list.size() < 1) {
            KLoader.BOT_MAP.clear();
            KLoader.BOT_PRICE.clear();
            KLoader.BOT_TIME_MAP.clear();
        }
    }

    /**
     * 保持心跳
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void keepHeartBeat() {
        Map<String, WebSocketSubCoins> webSocketMap = WebSocketSubCoins.webSocketMap;
        webSocketMap.forEach((userId, webSocketSubCoins) -> {
            SocketMessageVo socketMessageVo = new SocketMessageVo();
            socketMessageVo.setType(SocketTypeConstants.HEARTBEAT);

            MessageVo messageVo = new MessageVo();
            messageVo.setCode("1");
            messageVo.setMessage(SocketTypeConstants.HEARTBEAT);
            socketMessageVo.setDate(messageVo);

            String jsonStr = JSONUtil.toJsonStr(socketMessageVo);
            webSocketSubCoins.sendMessage(jsonStr);
        });
    }

    @Resource
    ITOwnCoinService tOwnCoinService;

    /**
     * 成功订阅推送消息
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void subCoinSendMsgTask() {
        try {
            Map<String, WebSocketSubCoins> webSocketMap = WebSocketSubCoins.webSocketMap;
            SocketMessageVo socketMessageVo = new SocketMessageVo();
            socketMessageVo.setType(SocketTypeConstants.OWN_COIN);
            MessageVo messageVo = new MessageVo();

            webSocketMap.forEach((userId, webSocketSubCoins) -> {
                TOwnCoinSubscribeOrder selectOrder = new TOwnCoinSubscribeOrder();
                selectOrder.setStatus("2");
                selectOrder.setUserId(Long.parseLong(userId));
                List<TOwnCoinSubscribeOrder> orders =
                        tOwnCoinService.selectTOwnCoinSubscribeOrderList(selectOrder);
                if (!CollectionUtils.isEmpty(orders)) {
                    TOwnCoinSubscribeOrder subscribeOrder = orders.get(0);
                    WsCoinSubVO vo = new WsCoinSubVO();
                    BeanUtils.copyProperties(subscribeOrder, vo);
                    vo.setMsg(MessageUtils.message("own.coin.sub.success", vo.getOwnCoin().toUpperCase().concat("/USDT")));
                    //vo.setMsg("您已获得 " + vo.getOwnCoin() + "/USDT 申购资格");
                    String message = JSONObject.toJSONString(vo);
                    if (WebSocketSubCoins.webSocketMap.containsKey(userId)) {
                        subscribeOrder.setStatus("3");
                        int count = tOwnCoinService.updateTOwnCoinSubscribeOrder(subscribeOrder);
                        if(count>0){
                            messageVo.setMessage(message);
                            socketMessageVo.setDate(messageVo);
                            String jsonStr = JSONUtil.toJsonStr(socketMessageVo);
                            WebSocketSubCoins.webSocketMap.get(userId).sendMessage(jsonStr);
                        }
                    }
                }
            });
        }catch (Exception e){
            log.info(e.toString());
        }
    }

}
