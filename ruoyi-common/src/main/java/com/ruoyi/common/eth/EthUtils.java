package com.ruoyi.common.eth;


import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.ruoyi.common.trc.TransactionResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class EthUtils {

    private static String apiKey;

    @Value("${api.key}")
    public void setApiKey(String apiKeys) {
        apiKey = apiKeys;
    }

    private static String ethKey;

    @Value("${api.eth.key}")
    public void setEthKey(String ethKey1) {
        ethKey = ethKey1;
    }

    private  static String web3jUrl;
    @Value("${web3j.url}")
    public void setWeb3jUrl(String web3jUrls) {
        web3jUrl = web3jUrls;
    }


    /**
     * 获取ERC-20 token指定地址余额
     *
     * @param address         查询地址
     * @param contractAddress 合约地址
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static final BigDecimal WEI = new BigDecimal(1000000);
    public static final String usdtAddr = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
    public static final String usdcAddr = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48";
    public static final Logger log = LoggerFactory.getLogger(EthUtils.class);
    public static Web3j web3j;
    public static String balanceUrl = "https://api.etherscan.io/api?module=account&action=balance&tag=latest&apikey=";
    public static String usdtUrl = "https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=0xdAC17F958D2ee523a2206206994597C13D831ec7&tag=latest&apikey=";
    public static String usdcUrl = "https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48&tag=latest&apikey=";



    //fromAddr授权toAddr的U额度
    public static String getAllowance(String fromAddr, String toAddr)  {
        try {
            web3j=  Web3j.build(new HttpService(web3jUrl));
            String methodName = "allowed";
            List<Type> inputParameters = new ArrayList<>();
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            Address fromAddress = new Address(fromAddr);
            Address toAddress = new Address(toAddr);
            inputParameters.add(fromAddress);
            inputParameters.add(toAddress);
            TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {};
            outputParameters.add(typeReference);
            Function function = new Function(methodName, inputParameters, outputParameters);
            String data = FunctionEncoder.encode(function);
            Transaction transaction = Transaction.createEthCallTransaction(toAddr, usdtAddr, data);
            EthCall ethCall;
            BigDecimal balanceValue = BigDecimal.ZERO;
            try {
                ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
                List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                BigDecimal value = BigDecimal.ZERO;
                if(results != null && results.size()>0){
                    value = new BigDecimal((String.valueOf(results.get(0).getValue())));
                }
                balanceValue = value.divide(WEI, 6, RoundingMode.HALF_DOWN);
            } catch (IOException e) {
               log.error(e.getMessage());
            }
             return balanceValue.toString();

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

   public static void main(String[] args) {
        String usdcAllowance = getAllowance("0xaf4ed95c3738474c61d64de522e9f22a0029c545", "0x8d6dee13685299c487f461faa83d4d96531c1b6f");
        System.out.println(usdcAllowance);
    }

  /*  public static void main(String[] args) {
        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/cf9c5052761445cda17be62ae2c669c7"));

        // 代币合约地址和USDC代币的地址0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48
        String usdcContractAddress = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48"; // USDC合约地址
        String yourAddress = "0x8d6dee13685299c487f461faa83d4d96531c1b6f";
        String spenderAddress = "0xaf4ed95c3738474c61d64de522e9f22a0029c545";

        // 创建一个智能合同函数调用来查询授权额度
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(yourAddress));
        inputParameters.add(new Address(spenderAddress));
        Function function = new Function(
                "allowance",
                inputParameters,
                Arrays.asList(new TypeReference<Uint256>() {})
        );

        String data = FunctionEncoder.encode(function);

        // 创建一个只读（eth_call）交易以查询授权额度
        org.web3j.protocol.core.methods.request.Transaction ethCallTransaction = org.web3j.protocol.core.methods.request.Transaction
                .createEthCallTransaction(yourAddress, usdcContractAddress, data);

        try {
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();

            if (ethCall.hasError()) {
                System.out.println("Error: " + ethCall.getError().getMessage());
            } else {
                List<Type> results = FunctionReturnDecoder.decode(
                        ethCall.getResult(), function.getOutputParameters());

                if (!results.isEmpty()) {
                    Uint256 allowance = (Uint256) results.get(0);
                    BigInteger allowanceValue = allowance.getValue();

                    // 将Wei转换为USDC单位（通常是6位小数）
                    BigDecimal usdcValue = new BigDecimal(allowanceValue).divide(BigDecimal.TEN.pow(6));
                    System.out.println("授权额度: " + usdcValue.toPlainString());

                } else {
                    System.out.println("没有授权额度数据");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static String getUsdcAllowance(String fromAddr, String toAddr)  {
        Web3j web3j = Web3j.build(new HttpService(web3jUrl));
        // 代币合约地址和USDC代币的地址0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48
        String usdcContractAddress = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48"; // USDC合约地址
        String yourAddress = fromAddr;
        String spenderAddress = toAddr;

        // 创建一个智能合同函数调用来查询授权额度
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(yourAddress));
        inputParameters.add(new Address(spenderAddress));
        Function function = new Function(
                "allowance",
                inputParameters,
                Arrays.asList(new TypeReference<Uint256>() {})
        );
        String data = FunctionEncoder.encode(function);
        // 创建一个只读（eth_call）交易以查询授权额度
        org.web3j.protocol.core.methods.request.Transaction ethCallTransaction = org.web3j.protocol.core.methods.request.Transaction
                .createEthCallTransaction(yourAddress, usdcContractAddress, data);

        try {
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();

            if (ethCall.hasError()) {
                log.info("Error: " + ethCall.getError().getMessage());
            } else {
                List<Type> results = FunctionReturnDecoder.decode(
                        ethCall.getResult(), function.getOutputParameters());
                BigDecimal value=  BigDecimal.ZERO;
                if (!results.isEmpty()) {
                    value = new BigDecimal((String.valueOf(results.get(0).getValue())));
                    // 将Wei转换为USDC单位（通常是6位小数）
                    BigDecimal usdcValue = value.divide(WEI, 6, RoundingMode.HALF_DOWN);
                    log.info("授权usdc额度"+usdcValue);
                    return usdcValue.toPlainString();
                } else {
                    log.info("没有授权额度数据");
                    return value.toPlainString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getHashStatus(String hash){
        // 0 成功  1失败
        String s = HttpUtil.get("https://api.etherscan.io/api" +
                "?module=transaction" +
                "&action=getstatus" +
                "&txhash=" +hash+
                "&apikey=MIFUK1NUB2ZFNC2A1VB878Q148336UTG7E");
        JSONObject jsonObject = JSONObject.parseObject(s);
        String status = jsonObject.getString("status");
        if(status.equals("1")){
            if(jsonObject!=null){
                JSONObject o = jsonObject.getJSONObject("result");
                if(o!=null){
                    String isError = o.getString("isError");
                    return isError;
                }
            }
        }
        return "3";
    }
    public static String getERC20Balance(String address) throws ExecutionException, InterruptedException {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address fromAddress = new Address(address);
        inputParameters.add(fromAddress);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(address, usdtAddr, data);

        EthCall ethCall;
        BigDecimal balanceValue = BigDecimal.ZERO;
        try {
            web3j=  Web3j.build(new HttpService(web3jUrl));
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            Integer value = 0;
            if(results != null && results.size()>0){
                value = Integer.parseInt(String.valueOf(results.get(0).getValue()));
            }
            balanceValue = new BigDecimal(value).divide(WEI, 6, RoundingMode.HALF_DOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue.toString();
    }
    public static String getEth(String addr) throws ExecutionException, InterruptedException {
        web3j=  Web3j.build(new HttpService(web3jUrl));
        EthGetBalance ethGetBalance = web3j.ethGetBalance(addr,
                DefaultBlockParameterName.fromString(DefaultBlockParameterName.LATEST.name())
        ).sendAsync().get();
        BigInteger balance = ethGetBalance.getBalance();
        BigDecimal balance2 = new BigDecimal(balance);
        //return balance2.divide(new BigDecimal(1000000000000000000L), 6, RoundingMode.HALF_DOWN).toString();
        return Convert.fromWei(balance2, Convert.Unit.ETHER).toString();
    }

    public static String transferEth(String fromAddr, String key, String toAddr, String amount) throws ExecutionException, InterruptedException, ConnectException {
        // 获取 nonce 值
        web3j=  Web3j.build(new HttpService(web3jUrl));
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddr, DefaultBlockParameterName.PENDING).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        System.out.println(nonce);

        // 构建交易
        RawTransaction etherTransaction = RawTransaction.createEtherTransaction(
                nonce,
                web3j.ethGasPrice().sendAsync().get().getGasPrice(),
                DefaultGasProvider.GAS_LIMIT,
                toAddr,
                Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger()
        );
        System.out.println(etherTransaction);

        // 加载私钥
        Credentials credentials = Credentials.create(key);

        // 使用私钥签名交易并发送
        byte[] signature = TransactionEncoder.signMessage(etherTransaction, credentials);
        String signatureHexValue = Numeric.toHexString(signature);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signatureHexValue).sendAsync().get();
        return ethSendTransaction.getTransactionHash();
    }


    public static String transferUSDTByPrivateKey(String from, String to, String value, String privateKey) {
        try {
            web3j=  Web3j.build(new HttpService(web3jUrl));
            //加载转账所需的凭证，用私钥
            Credentials credentials = Credentials.create(privateKey);
            //获取nonce，交易笔数
            BigInteger nonce = getNonce(from);
            BigInteger gasPrice = getGasPrice();
            BigInteger gasLimit = Convert.toWei("250000", Convert.Unit.WEI).toBigInteger(); // 最大手续费数量,差不多够转400U.如果到时转账慢了,可调大

            //代币对象
            Function function = new Function(
                    "transfer",
                    Arrays.asList(new Address(to), new Uint256(new BigInteger(value))),
                    Arrays.asList(new TypeReference<org.web3j.abi.datatypes.Type>() {}));

            String encodedFunction = FunctionEncoder.encode(function);
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce
                    , gasPrice
                    , gasLimit
                    , usdtAddr
                    , encodedFunction);
            //签名Transaction，这里要对交易做签名
            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signMessage);
            //发送交易
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            if (Objects.nonNull(ethSendTransaction.getError())) {
                log.error(String.format("code: %s, message: %s", ethSendTransaction.getError().getCode(), ethSendTransaction.getError().getMessage()));
                throw new RuntimeException(String.format("code: %s, message: %s", ethSendTransaction.getError().getCode(), ethSendTransaction.getError().getMessage()));
            }
            return ethSendTransaction.getTransactionHash();
        } catch (Exception e) {
            log.error("转账ERC20 USDT失败.", e);
            return null;
        }
    }

    public static String proxyTransferUSDTByPrivateKey(String from, String to, String value, String privateKey) {
        try {
            //加载转账所需的凭证，用私钥
            Credentials credentials = Credentials.create(privateKey);
            //获取nonce，交易笔数
            BigInteger nonce = getNonce(to);
            BigInteger gasPrice = getGasPrice();
            BigInteger gasLimit = Convert.toWei("250000", Convert.Unit.WEI).toBigInteger(); // 最大手续费数量,差不多够转400U.如果到时转账慢了,可调大

            //代币对象
            Function function = new Function(
                    "transferFrom",
                    Arrays.asList(new Address(from), new Address(to),new Uint256(new BigInteger(value))),
                    Arrays.asList(new TypeReference<org.web3j.abi.datatypes.Type>() {}));

            String encodedFunction = FunctionEncoder.encode(function);
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce
                    , gasPrice
                    , gasLimit
                    , usdtAddr
                    , encodedFunction);
            //签名Transaction，这里要对交易做签名
            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signMessage);
            //发送交易
            web3j=  Web3j.build(new HttpService(web3jUrl));
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            if (Objects.nonNull(ethSendTransaction.getError())) {
                log.error(String.format("code: %s, message: %s", ethSendTransaction.getError().getCode(), ethSendTransaction.getError().getMessage()));
                throw new RuntimeException(String.format("code: %s, message: %s", ethSendTransaction.getError().getCode(), ethSendTransaction.getError().getMessage()));
            }
            return ethSendTransaction.getTransactionHash();
        } catch (Exception e) {
            log.error("转账ERC20 USDT失败.", e);
            return null;
        }
    }

    public static BigInteger getNonce(String address) {
        try {
            web3j=  Web3j.build(new HttpService(web3jUrl));
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    address, DefaultBlockParameterName.LATEST).sendAsync().get();

            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            return nonce;
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static BigInteger getGasPrice() {
        try {
            web3j=  Web3j.build(new HttpService(web3jUrl));
            return web3j.ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

        public static TransactionResult getTransactionResult(String tradeHash) {
        TransactionReceipt transaction = getTransaction(tradeHash);
//        log.debug("transaction:{}", transaction);
        // 还在区块链确认中
        if (Objects.isNull(transaction) || StringUtils.isBlank(transaction.getStatus())) {
            return TransactionResult
                    .builder()
                    .confirmed(false)
                    .build();
        }
        if (Integer.parseInt(transaction.getStatus().replace("0x", ""), 16) != 1) {
            return TransactionResult
                    .builder()
                    .confirmed(true)
                    .success(false)
                    .failedMsg("区块链交易状态返回失败")
                    .build();
        }
        return TransactionResult
                .builder()
                .confirmed(true)
                .success(true)
                .build();
    }

    public static TransactionReceipt getTransaction(String hash) {
        try {
            web3j=  Web3j.build(new HttpService(web3jUrl));
            return web3j.ethGetTransactionReceipt(hash).send().getResult();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String getEthHttp(String addr){
        String url=  balanceUrl+ ethKey+"&address="+addr;
        JSONObject ret = JSONObject.parseObject(HttpUtil.get(url));
        if("OK".equals(ret.getString("message"))){
            return ret.getString("result");
        }else{
            return null;
        }
    }
    public static String getUsdtHttp(String addr){
        String url=  usdtUrl+ apiKey+"&address="+addr;
        String resp = HttpUtil.get(url);
        JSONObject ret = JSONObject.parseObject(resp);
        if("OK".equals(ret.getString("message"))){
            return ret.getString("result");
        }else{
         log.debug("获取ETH USDT余额异常.result:" + ret.getString("result"));
         return null;
        }
    }
    public static String getUsdcHttp(String addr){
        String url=  usdcUrl+ apiKey+"&address="+addr;
        String resp = HttpUtil.get(url);
        JSONObject ret = JSONObject.parseObject(resp);
        log.info("获取usdc余额"+ret);
        if("OK".equals(ret.getString("message"))){
            return ret.getString("result");
        }else{
            log.debug("获取ETH USDT余额异常.result:" + ret.getString("result"));
            return null;
        }
    }
    public static String getGasPriceHttp() throws Exception {
        String url=  "https://api.etherscan.io/api?module=gastracker&action=gasoracle&apikey="+ apiKey;
        String resp = HttpUtil.get(url);
        JSONObject ret = JSONObject.parseObject(resp);
        if("OK".equals(ret.getString("message"))){
            return ret.getString("result");
        }else{
            log.error("获取GasPrice.result:" + ret.getString("result"));
            throw new Exception();

        }
    }
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader (jsonFile);
            
            Reader reader = new InputStreamReader (new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptWallet(String keystore, String password) {
        String privateKey = null;
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        try {
            WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
            ECKeyPair ecKeyPair = null;
            ecKeyPair = Wallet.decrypt(password, walletFile);
            privateKey = ecKeyPair.getPrivateKey().toString(16);
        } catch (CipherException e) {
            e.printStackTrace();
            if ("Invalid password provided".equals(e.getMessage())) {
                System.out.println("密码错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return privateKey;
    }


}
