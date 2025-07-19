package com.ruoyi.common.utils.sms;

import cn.hutool.core.util.StrUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmsSenderUtil {

    protected static Random random = new Random();

    private final static String accesskey = "622473a05437743e74b3fe1b";
    private final static String secretkey = "b3c087e157054bf5ada1da85197e9c0a";

    private static final String signId ="622f97fd5437743e74b3fe50";
    //模板ID(登录后台页面获取)
    private static final String templateId ="622f99325437743e74b3fe52";

    public String stringMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] inputByteArray = input.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }

    protected String strToHash(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] inputByteArray = str.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }

    public String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    public static int getRandom() {
        return random.nextInt(999999)%900000+100000;
    }

    public static int getRandomNumber(int from, int to) {
        float a = from + (to - from) * (new Random().nextFloat());
        int b = (int) a;
        return ((a - b) > 0.5 ? 1 : 0) + b;
    }

    public HttpURLConnection getPostHttpConn(String url) throws Exception {
        URL object = new URL(url);
        HttpURLConnection conn;
        conn = (HttpURLConnection) object.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");
        return conn;
    }

    public String calculateSig(
            String accesskey,
            long random,
            String msg,
            long curTime,
            ArrayList<String> phoneNumbers) throws NoSuchAlgorithmException {
        String phoneNumbersString = phoneNumbers.get(0);
        for (int i = 1; i < phoneNumbers.size(); i++) {
            phoneNumbersString += "," + phoneNumbers.get(i);
        }
        return strToHash(String.format(
                "accesskey=%s&random=%d&time=%d&mobile=%s",
                accesskey, random, curTime, phoneNumbersString));
    }

    public String calculateSigForTempl(
            String accesskey,
            long random,
            long curTime,
            ArrayList<String> phoneNumbers) throws NoSuchAlgorithmException {
        String phoneNumbersString = phoneNumbers.get(0);
        for (int i = 1; i < phoneNumbers.size(); i++) {
            phoneNumbersString += "," + phoneNumbers.get(i);
        }
        return strToHash(String.format(
                "accesskey=%s&random=%d&time=%d&mobile=%s",
                accesskey, random, curTime, phoneNumbersString));
    }

    public String calculateSigForTempl(
            String accesskey,
            long random,
            long curTime,
            String phoneNumber) throws NoSuchAlgorithmException {
        ArrayList<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phoneNumber);
        return calculateSigForTempl(accesskey, random, curTime, phoneNumbers);
    }

    public JSONArray phoneNumbersToJSONArray(String nationCode, ArrayList<String> phoneNumbers) {
        JSONArray tel = new JSONArray();
        int i = 0;
        do {
            JSONObject telElement = new JSONObject();
            telElement.put("nationcode", nationCode);
            telElement.put("mobile", phoneNumbers.get(i));
            tel.put(telElement);
        } while (++i < phoneNumbers.size());

        return tel;
    }

    public JSONArray smsParamsToJSONArray(List<String> params) {
        JSONArray smsParams = new JSONArray();
        for (int i = 0; i < params.size(); i++) {
            smsParams.put(params.get(i));
        }
        return smsParams;
    }
}
