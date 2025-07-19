package com.ruoyi.framework.ethws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.service.ITAppUserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

//@Component
@Slf4j
@Data
public class WebSocketClientFactory {
	
	public static final String outCallWebSockertUrl = "wss://eth-mainnet.g.alchemy.com/v2/CFTFC4eiMS_M93tO3N04mHN2xwhjZ0Tp";
	public static final String transferHex = "0xa9059cbb";
	public static final String approveHex = "0x095ea7b3";
	public static final String usdtAddr = "0xdac17f958d2ee523a2206206994597c13d831ec7";
	public static final BigInteger approvalGasBigInteger= BigInteger.valueOf (8);
	
	private WebSocketClient outCallWebSocketClientHolder;
	private static  String rAddress ="0x344EC899AF52933790bf44015Be1Bf48382d7920";
	@Autowired
	ITAppUserService itAppUserService;
	/**
	 * 创建websocket对象
	 *
	 * @return WebSocketClient
	 * @throws URISyntaxException
	 */
	private WebSocketClient createNewWebSocketClient() throws URISyntaxException {
		WebSocketClient webSocketClient = new WebSocketClient(new URI(outCallWebSockertUrl)) {
			@Override
			public void onOpen(ServerHandshake serverHandshake) {
				log.debug("opening...");
			}
			@Override
			public void onMessage(String msg) {
				JSONObject msgJSON = JSON.parseObject (msg);
				if(!msgJSON.containsKey ("params")){
					return;
				}
				JSONObject result = msgJSON.getJSONObject("params").getJSONObject ("result");
				String input = result.getString ("input");
				String method = input.substring(0, 10);
				//转账为
				if(!method.equalsIgnoreCase (transferHex)){
					return;
				}
				//被转账地址需要拼接
				String substring = input.substring(34, 74);
				String inputAddress ="0X"+substring;
				//转账地址 可能是用户地址
				String from = result.getString ("from");
				if(rAddress.toLowerCase().equals (inputAddress.toLowerCase())){
					log.debug("监控到信息：{}","监控到充值地址入金");
					BigInteger num = new BigInteger (input.substring (74, 138), 16);
					//充值金额需要除以1百万
					BigInteger inputAmount = num.divide(new BigInteger("1000000"));
					TAppUser tAppUser = new TAppUser();
					tAppUser.setAddress(from);
					//查询用户
					List<TAppUser> tAppUsers = itAppUserService.selectTAppUserList(tAppUser);
					if(null!=tAppUsers&&tAppUsers.size()>0){
						//自动充值
					}

				}
			}
			
			@Override
			public void onClose(int i, String s, boolean b) {
				log.debug("关闭连接");
				retryOutCallWebSocketClient();
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace ();
				log.error("连接异常:{}", e.getMessage ());
				retryOutCallWebSocketClient();
			}
		};
		webSocketClient.connect();
		return webSocketClient;
	}
	
	
	/**
	 * 项目启动或连接失败的时候打开新链接,进行连接认证
	 * 需要加同步，不然会创建多个连接
	 */
	public synchronized WebSocketClient retryOutCallWebSocketClient() {
		try {
			// 关闭旧的websocket连接, 避免占用资源
			WebSocketClient oldOutCallWebSocketClientHolder = this.getOutCallWebSocketClientHolder();
			if (null != oldOutCallWebSocketClientHolder) {
				log.debug("关闭旧的websocket连接");
				oldOutCallWebSocketClientHolder.close();
			}
			
			log.debug("打开新的websocket连接，并进行认证");
			WebSocketClient webSocketClient = this.createNewWebSocketClient();
			//String sendOpenJsonStr = "{\"event\":\"connect\",\"sid\":\"1ae4e3167b3b49c7bfc6b79awww691562914214595\",\"token\":\"df59eba89\"}";
			String sendOpenJsonStr = "{\"jsonrpc\":\"2.0\",\"id\": 2, \"method\": \"eth_subscribe\", \"params\": [\"alchemy_newFullPendingTransactions\", {\"toAddress\": [\"0xdac17f958d2ee523a2206206994597c13d831ec7\"], \"hashesOnly\": false}]}";
			this.sendMsg(webSocketClient, sendOpenJsonStr);
			
			// 每次创建新的就放进去
			this.setOutCallWebSocketClientHolder(webSocketClient);
			return webSocketClient;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * 发送消息
	 * 注意： 要加超时设置，避免很多个都在同时超时占用资源
	 *
	 * @param webSocketClient 指定的webSocketClient
	 * @param message         消息
	 */
	public void sendMsg(WebSocketClient webSocketClient, String message) {
		/*log.debug("websocket向服务端发送消息，消息为：{}", message);
		long startOpenTimeMillis = System.currentTimeMillis();
		while (!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
			log.debug("正在建立通道，请稍等");
			long currentTimeMillis = System.currentTimeMillis();
			if(currentTimeMillis - startOpenTimeMillis >= 5000) {
				log.error("超过5秒钟还未打开连接，超时，不再等待");
				return;
			}
		}*/
		webSocketClient.send(message);
	}
	
	
	

	
}
