package com.ruoyi.common.trc;

import cn.hutool.crypto.SecureUtil;
import com.ruoyi.common.utils.StringUtils;
import org.tron.trident.abi.FunctionReturnDecoder;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.abi.datatypes.Bool;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.Utf8String;
import org.tron.trident.abi.datatypes.generated.Uint256;
import org.tron.trident.abi.datatypes.generated.Uint8;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Contract;
import org.tron.trident.core.transaction.TransactionBuilder;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;
import org.tron.trident.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class Trc20Contract extends Contract {
    protected int decimals;

    public Trc20Contract(Contract cntr, String ownerAddr, ApiWrapper wrapper) {
        super(cntr, ownerAddr, wrapper);
        decimals = decimals().intValue();
    }

    /**
     * Call function name() public view returns (string).
     *
     * Returns the name of the token - e.g. "MyToken".
     *
     * @return the name of the token
     */
    //调用TRC20合约的name()函数获取通证的名称。
    public String name() {
        Function name = new Function("name",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        Response.TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), name);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (String) FunctionReturnDecoder.decode(result, name.getOutputParameters()).get(0).getValue();
    }

    /**
     * Call function symbol() public view returns (string).
     *
     * Returns the symbol of the token. E.g. "HIX".
     *
     * @return the symbol of the token
     */
    //调用TRC20合约的symbol函数获取代币的符号
    public String symbol() {
        Function symbol = new Function("symbol",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        Response.TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), symbol);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (String)FunctionReturnDecoder.decode(result, symbol.getOutputParameters()).get(0).getValue();
    }

    /**
     * Call function decimals() public view returns (uint8).
     *
     * Returns the number of decimals the token uses - e.g. 8,
     * means to divide the token amount by 100000000 to get its user representation
     *
     * @return the number of decimals the token uses
     */
    //调用TRC20合约的decimals函数获取代币的精度
    public BigInteger decimals() {
        Function decimals = new Function("decimals",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint8>() {}));

        Response.TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), decimals);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, decimals.getOutputParameters()).get(0).getValue();
    }

    /**
     * Call function totalSupply() public view returns (uint256).
     *
     * Returns the total token supply.
     *
     * @return the total token supply
     */
    //调用TRC20合约的totalSupply函数获取代币的总供应量。
    public BigInteger totalSupply() {
        Function totalSupply = new Function("totalSupply",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint256>() {}));

        Response.TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), totalSupply);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, totalSupply.getOutputParameters()).get(0).getValue();
    }

    /**
     * Call function balanceOf(address _owner) public view returns (uint256 balance).
     *
     * Returns the account balance of another account with address _owner.
     *
     * @param accountAddr The token owner's address
    //       * @param callerAddr The caller's address
    //       * @param cntrAddr The contract's address
     * @return the account balance of another account with address _owner
     */
    //调用TRC20合约的balanceOf函数获取指定账户的代币余额
    public BigInteger balanceOf(String accountAddr) {
        Function balanceOf = new Function("balanceOf",
                Arrays.asList(new Address(accountAddr)), Arrays.asList(new TypeReference<Uint256>() {}));

        Response.TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), balanceOf);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, balanceOf.getOutputParameters()).get(0).getValue();
    }

    /**
     * Call function transfer(address _to, uint256 _value) public returns (bool success).
     *
     * Transfers _value amount of tokens to address _to.
     *
     * @param destAddr The address to receive the token
     * @param amount The transfer amount
     * @param memo The transaction memo
     * @param feeLimit The energy fee limit
     * @return Transaction hash
     */
    //调用TRC20合约的transfer函数进行代币转账。
    public String transfer(String destAddr, long amount,
                           String memo, long feeLimit) {
        Function transfer = new Function("transfer",
                Arrays.asList(new Address(destAddr),
                        new Uint256(BigInteger.valueOf(amount))),
                Arrays.asList(new TypeReference<Bool>() {}));

        TransactionBuilder builder = wrapper.triggerCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), transfer);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Chain.Transaction signedTxn = wrapper.signTransaction(builder.build());
        return wrapper.broadcastTransaction(signedTxn);
    }

    /**
     * call function transferFrom(address _from, address _to, uint256 _value) public returns (bool success)
     *
     * The transferFrom method is used for a withdraw workflow,
     * allowing contracts to transfer tokens on your behalf. This can only be called
     * when someone has allowed you some amount.
     *
     * @param fromAddr The address who sends tokens (or the address to withdraw from)
     * @param destAddr The address to receive the token
     * @param amount The transfer amount
     * @param memo The transaction memo
     * @param feeLimit The energy fee limit
     * @return Transaction hash
     */
    //调用TRC20合约的transferFrom函数从他们账户中转账代币，需要配合approve方法使用。
    public String transferFrom(String fromAddr, String destAddr, long amount,
                               String memo, long feeLimit) {
        Function transferFrom = new Function("transferFrom",
                Arrays.asList(new Address(fromAddr) ,new Address(destAddr),
                        new Uint256(BigInteger.valueOf(amount))),
                Arrays.asList(new TypeReference<Bool>() {}));
        com.google.protobuf.ByteString ownerAddr1 = ownerAddr;

        TransactionBuilder builder = wrapper.triggerCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), transferFrom);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Chain.Transaction signedTxn = wrapper.signTransaction(builder.build());
        return wrapper.broadcastTransaction(signedTxn);
    }
    public static String aesDecrypt(String key,String content){
        if(StringUtils.isBlank(key) || StringUtils.isBlank(content)){
            return "";
        }
        byte[] decode = Base64.getDecoder().decode(key);
        return SecureUtil.aes(decode).decryptStr(content);
    }

    /**
     * Call function approve(address _spender, uint256 _value) public returns (bool success)
     *
     * Allows _spender to withdraw from your account multiple times, up to the _value amount.
     * If this function is called again it overwrites the current allowance with _value.
     *
     * @param spender The address who is allowed to withdraw.
     * @param amount The amount allowed to withdraw.
     * @param memo The transaction memo
     * @param feeLimit The energy fee limit
     * @return Transaction hash
     */
    //调用TRC20合约的approve函数授权代币使用权给其他地址
    public String approve(String spender ,long amount,
                          String memo, long feeLimit) {
        Function approve = new Function("approve",
                Arrays.asList(new Address(spender) ,
                        new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(decimals)))),
                Arrays.asList(new TypeReference<Bool>() {}));

        TransactionBuilder builder = wrapper.triggerCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), approve);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Chain.Transaction signedTxn = wrapper.signTransaction(builder.build());
        return wrapper.broadcastTransaction(signedTxn);
    }

    /**
     * Call function allowance(address _owner, address _spender) public view returns (uint256 remaining).
     *
     * Returns the amount which _spender is still allowed to withdraw from _owner.
     *
     * @param owner The address to be withdrew from.
     * @param spender The address of the withdrawer.
    //       * @param callerAddr The caller's address
    //       * @param cntrAddr The contract's address
     * @return the amount which _spender is still allowed to withdraw from _owner
     */
    //调用TRC20合约的allowance函数查询可供第三方转账的查询账户的通证余额。
    public BigInteger allowance(String owner, String spender) {
        Function allowance = new Function("allowance",
                Arrays.asList(new Address(owner), new Address(spender)),
                Arrays.asList(new TypeReference<Uint256>() {}));

        Response.TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()),
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), allowance);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, allowance.getOutputParameters()).get(0).getValue();
    }
}
