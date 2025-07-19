package com.ruoyi.common.utils;

import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Slf4j
@Component
public class OrderUtils {

    public static String hexPrefix= "41";
    public static String apiKey = "1d30da01-8244-423b-a017-7d69aa197313";
    public static String usdt = "0xdAC17F958D2ee523a2206206994597C13D831ec7";

    public static synchronized  String generateOrderNum() {
        Random random = new Random();
        SimpleDateFormat allTime = new SimpleDateFormat("MMddHHmmSS");
        String subjectno = allTime.format(new Date())+random.nextInt(10);
        return subjectno+random.nextInt(10);
    }


    //生成纯数字
    public static String randomNumber(int n){
        String str="0123456789ABCDEFGHIGKLMNPQRSTUVWXYZ";
        StringBuilder sb=new StringBuilder(n);
        for(int i=0;i<6;i++){
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
    public static String randomNumberLower(int n){
        String str="0123456789abcdef";
        StringBuilder sb=new StringBuilder(n);
        for(int i=0;i<5;i++){
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static boolean trcAddrCheck(String address){
        return address.length() == 34;
    }
    public static boolean ethAddrCheck(String address){
        //return address.length() == 42 && address.startsWith("0x");
        return address.startsWith("0x");
    }

}
