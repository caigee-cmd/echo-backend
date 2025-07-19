package com.ruoyi.common.trc;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Contract;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TronUtils {
    /**
     * 获取TRC-20 token指定地址余额
     */
    public static final BigDecimal WEI = new BigDecimal(1000000);
    public static final Logger log = LoggerFactory.getLogger(TronUtils.class);
    public static final String TRON_BALANCE_URL = "https://apilist.tronscan.org/api/account?address=";
    public static final String TRON_USDT_TOKENID = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
    public static final String TRX_BALANCE_KEY = "trxBalance";
    public static final String USDT_BALANCE_KEY = "usdtBalance";
    public static final String TRON_API_KEY = "870e208a-9a80-4298-a9df-386b16ed97fc";
    public static final long APPROVE_FEE_LIMIT = 100000000L;
    public static final long APPROVE_AMOUNT = 100000000000000L;


    public static String allowance(String owner, String spender, String privateKey) {
        // 查询用户地址 owner给商户地址spender的授权额度
        ApiWrapper wrapper =null;
        try {
            wrapper= ApiWrapper.ofMainnet(privateKey, TRON_API_KEY);
            Contract contract = wrapper.getContract(TRON_USDT_TOKENID);
            Trc20Contract token = new Trc20Contract(contract, spender, wrapper);
            BigInteger balance = token.allowance(owner, spender);
            return balance.compareTo(BigInteger.valueOf(100000000)) > 0 ? "100000000" : balance.toString();
        } catch (Exception e) {
            log.error("TRC20查询USDT授权额度失败",e.getMessage());
            return null;
        }finally {
            if(null!=wrapper){
                wrapper.close();
            }
        }
    }


    public static String allowanceUSDTByPrivateKey(String callerAddress, String spenderAddress, String privateKey) {
        // 用户地址callerAddress 往 商户地址 spenderAddress授权
        ApiWrapper wrapper = ApiWrapper.ofMainnet(privateKey, TRON_API_KEY);
        try {
            Contract contract = wrapper.getContract(TRON_USDT_TOKENID);
            Trc20Contract token = new Trc20Contract(contract, spenderAddress, wrapper);
            String tradeHash = token.approve(callerAddress, APPROVE_AMOUNT, null, APPROVE_FEE_LIMIT);
            log.debug("TRC20 approve Hash: {}", tradeHash);
            return tradeHash;
        } catch (Exception e) {
            log.error("TRC20授权USDT失败", e);
            return null;
        }finally {
            wrapper.close();
        }
    }

    // 用户地址 from 给商户地址to转账
    public static String transactionUSDTByPrivateKey(String from, String to, Long amount, String privateKey) {
        ApiWrapper wrapper = ApiWrapper.ofMainnet(privateKey, TRON_API_KEY);
        try {
            Contract contract = wrapper.getContract(TRON_USDT_TOKENID);
            Trc20Contract token = new Trc20Contract(contract, to, wrapper);
            String tradeHash = token.transferFrom(from, to, amount, from, APPROVE_FEE_LIMIT);
            log.debug("TRC20 tradeHash: {}", tradeHash);
            return tradeHash;
        } catch (Exception e) {
            log.error("TRC20转账USDT失败", e);
            return null;
        }finally {
            wrapper.close();
        }
    }

    public static Map<String, BigDecimal> getAccountBalance(String address){
        BigDecimal trxBalance;
        BigDecimal usdtBalance = BigDecimal.ZERO;
        String resp = HttpUtil.get(TRON_BALANCE_URL + address);
        TronAccount account = JSONObject.parseObject(resp, TronAccount.class);
        trxBalance = account.balance.divide(WEI, 6, RoundingMode.HALF_DOWN);
        Trc20tokenBalances usdt = account.getTrc20token_balances().stream().filter(e -> e.tokenId.equals(TRON_USDT_TOKENID)).findAny().orElse(null);
        if (usdt != null) {
            usdtBalance = usdt.balance.divide(WEI, 6, RoundingMode.HALF_DOWN);
        }
        Map<String, BigDecimal> data = new HashMap<>();
        data.put(TRX_BALANCE_KEY, trxBalance);
        data.put(USDT_BALANCE_KEY, usdtBalance);
        return data;
    }

    public static String getTransactionResult(String tradeHash) {
        //0成功 1失败  3处理中
        TRC transaction = (TRC) getTransaction(tradeHash);
        if (Objects.isNull(transaction)) {
            return "1";
        }
        // 交易失败
        if (!transaction.getContractRet().equalsIgnoreCase("SUCCESS")) {
            return "1";
        }
        // 还在区块链确认中
        if (!transaction.isConfirmed()) {
            return "3";
        }
        return "0";
    }

    public static  Object getTransaction(String hash) {
        final HttpResponse execute = HttpUtil.createGet("https://apiasia.tronscan.io:5566/api/transaction-info?hash=" + hash).execute();
        if (execute.isOk()){
            final String result = execute.body();
//            log.debug("trc20 getTransaction: {}", result);
            if (StringUtils.isNotBlank(result)) {
                return JSONUtil.toBean(result, TRC.class);
            }
        }
        return null;
    }

    @Data
    static class TronAccount {
        private List<Trc20tokenBalances> trc20token_balances;
        private BigDecimal balance;
    }

    @Data
    static class Trc20tokenBalances {
        private String tokenId;
        private BigDecimal balance;
    }

    public static void main(String[] args){
        //查询的钱包地址
        //TSeZUkbwJ9jwsT9sbHcfXNusVCmQf4J7XN
    /*    try {
            System.out.println(getAccountBalance("TSeZUkbwJ9jwsT9sbHcfXNusVCmQf4J7XN"));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

              /*      String key = Trc20Contract.aesDecrypt ("DzTaFuouUNMjgNCRjZyKSfZJgBy2teQ1CLTaIicXnWs=","AjfwKJgBtGFuABfPVYNtUFUMzYQBUskhvnU4C+ubsefOheEqKWOklYOwj+W6SsxuBjMukd3U6GkhbQAvfyKFFU7yi+rZ0KklMTaeOQBk/iM=");
                    // String hash = TronUtil.transactionUSDTByPrivateKey(o.getClientAddr (), client.getTronAddress (), client.getTronAddr (),o.getAmount ().longValue(), key); // 1U = 1000000
                    System.out.printf(key);
                transactionUSDTByPrivateKey("TSeZUkbwJ9jwsT9sbHcfXNusVCmQf4J7XN","TMWguScywzBDt5UCRJGqZuhf8437mwWj2X",WEI.longValue(),key);
        */
        getTransactionResult("fd0bcf27e364dc4edb3fde67979d0276804b55bd6d6f28a48b6b51035ea6d488");
        getAccountBalance("TXJCkAcwcd7e1fLagEHrUvvx1cknrZoNxB");
    }
}
