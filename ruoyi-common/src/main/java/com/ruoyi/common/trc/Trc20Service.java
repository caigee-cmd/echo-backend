package com.ruoyi.common.trc;


import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Trc20Contract;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;



@Service
@Slf4j
public class Trc20Service {

    //Hex
    //@Value("${tron.contract-address}")
    private String contractAddress = "0x282abe3f315cc1e3f7fddba6bfecce833a378300";

    //Hex
    //@Value("${tron.address}")
    private String address = "0xd3109fbd400f73dc7d96c7bf8de169cb917438fc";

    //@Value("${tron.private-key}")
    private String privateKey = "fb61d82ab7dd6763357afd4381ba37da180936c27692116e129b488e2b375e35";

    //token的精度  就是小数点后面有多少位小数 然后1后面加多少个0就可以
    private static final BigDecimal decimal = new BigDecimal("1000000");


    public BigInteger balanceOf(String address) {
        ApiWrapper client = ApiWrapper.ofShasta(privateKey);

        org.tron.trident.core.contract.Contract contract = client.getContract(contractAddress);

        org.tron.trident.core.contract.Trc20Contract token = new org.tron.trident.core.contract.Trc20Contract(contract, address, client);
        BigInteger balance = token.balanceOf(address);
        return balance;

    }

    public String transferFee(String toAddress, long feeAmt) {
        ApiWrapper client = ApiWrapper.ofNile(privateKey);
        try {
            log.debug("矿工费地址" + toAddress + "费用:" + feeAmt);
            Response.TransactionExtention result = client.transfer(address, toAddress, feeAmt * 1000000);
            Chain.Transaction signedTransaction = client.signTransaction(result);
            String txId = client.broadcastTransaction(signedTransaction);
            log.debug("转账矿工费成功" + toAddress + ":::" + txId);
            return txId;
        } catch (Exception e) {
            log.error("矿工费转账失败：" + e);
        }
        return null;
    }

    //用户合约中代币向归集中转账
    public String transferContract(String fromAddress, String privateKey, long amount) {
        ApiWrapper client = ApiWrapper.ofNile(privateKey);
        try {

            org.tron.trident.core.contract.Contract contract = client.getContract(contractAddress);

            org.tron.trident.core.contract.Trc20Contract token = new org.tron.trident.core.contract.Trc20Contract(contract, fromAddress, client);

            String txId = token.transfer(address, amount,0, "代币转账", 1000000000L);

            return txId;
        } catch (Exception e) {
            log.error("地址" + fromAddress + new Date() + "转账失败：" + e);
        }
        return null;
    }

    //用户提现
    public boolean withdraw(String toAddress, long amount) {
        //ApiWrapper client = ApiWrapper.ofNile(privateKey);
        ApiWrapper client = ApiWrapper.ofShasta(privateKey);
        try {

            org.tron.trident.core.contract.Contract contract = client.getContract(contractAddress);

            org.tron.trident.core.contract.Trc20Contract token = new Trc20Contract(contract, address, client);

            String txId = token.transfer(toAddress, amount,0, "代币转账", 1000000000L);
            return true;
        } catch (Exception e) {
            log.error("地址" + toAddress + new Date() + "转账失败：" + e);
            return false;
        }
    }
    public static String base582Hex(String base58){
        byte[] bytes = Base58Check.base58ToBytes(base58);
        return HexUtil.encodeHexStr(bytes);
    }
    public static void main(String[] args){
        String usdtString = "TCWQcSjbi2Hn9v4D38xaLeT2USndDhVpc5";
        //String zeroString = "T9yD14Nj9j7xAB4dbGeiX9h8unkKHxuWwb";
        System.out.println(base582Hex(usdtString));
    }
}

