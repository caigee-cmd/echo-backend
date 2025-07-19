package com.ruoyi.common.eth;

import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class EtherscanApi {
    public static final String ethUrl = "https://api.etherscan.io/api";
    public static final String apiToken = "B49VGKYKJKUMZ97TI636Y52RN46V6CY1F4";
    public static final String usdtAddr = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
    private String token;

    //查询eth
    public void getEthBalance(String address){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("apikey", apiToken);
        params.put("action", "balance");
        params.put("module", "account");
        params.put("tag", "latest");
        params.put("address", address);

        String ret = HttpUtil.get(ethUrl, params);
        log.debug("sendRawTransaction result = {}",ret);
    }
    //查询usdt
    public void getErc20Balance(String address){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("apikey", apiToken);
        params.put("action", "tokenbalance");
        params.put("module", "account");
        params.put("tag", "latest");
        params.put("address", address);
        params.put("contractaddress", usdtAddr);

        String ret = HttpUtil.get(ethUrl, params);
        log.debug("sendRawTransaction result = {}",ret);
    }

    public void sendRawTransaction(String hexValue){
        try {
            HttpResponse<String> response =  Unirest.post("https://api.etherscan.io/api")
                    .field("module","proxy")
                    .field("action","eth_sendRawTransaction")
                    .field("hex",hexValue)
                    .field("apikey",token)
                    .asString();
            log.debug("sendRawTransaction result = {}",response.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


    public boolean checkEventLog(final Long blockHeight,String address,String topic0,String txid){
        try {
            HttpResponse<String> response = Unirest.post("https://api.etherscan.io/api")
                    .field("module", "logs")
                    .field("action", "getLogs")
                    .field("fromBlock", blockHeight)
                    .field("toBlock",blockHeight)
                    .field("address",address)
                    .field("topic0",topic0)
                    .field("apikey", token)
                    .asString();
            log.debug("getLogs result = {}",response.getBody());
            JSONObject result = JSON.parseObject(response.getBody());
            if(result.getInteger("status")==0){
                return false;
            }
            else{
                JSONArray txs = result.getJSONArray("result");
                for(int i=0;i<txs.size();i++){
                    JSONObject item = txs.getJSONObject(i);
                    if(item.getString("transactionHash").equalsIgnoreCase(txid))return true;
                }
                return false;
            }

        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



}
