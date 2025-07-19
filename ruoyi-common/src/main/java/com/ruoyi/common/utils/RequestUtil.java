package com.ruoyi.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * IP工具类
 *
 * @author zebra
 *
 */
@Slf4j
public class RequestUtil {


    /**
     * 获取拦截json
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static String readJSONString(HttpServletRequest request) throws Exception {
        StringBuffer json = new StringBuffer();
        String line ;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            log.error("[信息]解析json异常", e);
            throw new Exception();
        }
        return json.toString();
    }

    public static String getUrlLast(String url) {
        int index = url.lastIndexOf("/");
        if (index < 0) {
            return "";
        }
        url = url.substring(index + 1);

        int index_2 = url.lastIndexOf("?");
        if (index_2 > 0) {
            url = url.substring(index_2 + 1);
        }
        int index_3 = url.lastIndexOf("#");
        if (index_3 > 0) {
            url = url.substring(index_3 + 1);
        }
        return url;
    }

    public static String getUrlLastTwo(String url) {
        int index = url.lastIndexOf("/");
        if (index < 0) {
            return "";
        }
        url = url.substring(0, index);
        index = url.lastIndexOf("/");
        if (index < 0) {
            return "";
        }
        return url.substring(index + 1);
    }

    public static Map<String,Object> getDataFromRequest(HttpServletRequest request){
        Gson gson = new Gson();
        String type = request.getContentType();
        Map<String,Object> receiveMap = new HashMap<String,Object>();
        if("application/x-www-form-urlencoded".equals(type)){
            Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String key = String.valueOf(enu.nextElement());
                String value = request.getParameter(key);
                receiveMap.put(key, value);
            }
        }else{	//else是text/plain、application/json这两种情况
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try{
                reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
                String line ;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                try{
                    if (null != reader){
                        reader.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            receiveMap = gson.fromJson(sb.toString(), new TypeToken<Map<String, String>>(){}.getType());//把JSON字符串转为对象
        }
        return receiveMap;
    }

    public static String getParams(HttpServletRequest req){
        Enumeration em = req.getParameterNames();
        JSONObject retJson = new JSONObject();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = req.getParameter(name);
            retJson.put(name, value);
        }
        return retJson.toJSONString();
    }
}

