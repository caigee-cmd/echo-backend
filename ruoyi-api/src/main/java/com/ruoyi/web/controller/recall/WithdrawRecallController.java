package com.ruoyi.web.controller.recall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.bussiness.domain.TAppAsset;
import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.TWithdraw;
import com.ruoyi.bussiness.service.ITAppAssetService;
import com.ruoyi.bussiness.service.ITAppUserService;
import com.ruoyi.bussiness.service.ITAppWalletRecordService;
import com.ruoyi.bussiness.service.ITWithdrawService;
import com.ruoyi.common.utils.RequestUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.common.ApiBaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 */
@RestController
@RequestMapping("/recall/withdraw")
@Slf4j
public class WithdrawRecallController extends ApiBaseController {

    @Resource
    private ITWithdrawService withdrawService;

    @Resource
    private ITAppAssetService assetService;
    @Autowired
    private ITAppWalletRecordService recordService;

    @Resource
    private ITAppUserService appUserService;

    @PostMapping(value = "/unc")
    public void onePayOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeOnePayOut(request, response, "unc");
    }


    private void executeOnePayOut(HttpServletRequest request, HttpServletResponse response, String name) throws IOException {
        String postDataStr;
        try {
            postDataStr = RequestUtil.readJSONString(request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("unc回调json data解析失败");
            return;
        }
        log.info("unc recall post body: {}", postDataStr);

        JSONObject postData = JSON.parseObject(postDataStr);

        // 三方订单编号
        String fee = postData.getString("fee");
        String address = postData.getString("address");

        String amount = postData.getString("amount");

        String decimals = postData.getString("decimals");

        String businessId = postData.getString("businessId");
        //0 	待審核1 	審核成功2 	審核駁回3 	交易成功4 	交易失敗
        Integer status = postData.getInteger("status");
        Integer tradeType = postData.getInteger("tradeType");
        String tradeId = postData.getString("tradeId");

        BigDecimal decimal = new BigDecimal(amount);
        Double real = Math.pow(10, Double.valueOf(decimals));
        BigDecimal realAmount = decimal.divide(new BigDecimal(real), 8, RoundingMode.HALF_UP);
        BigDecimal fee1 = new BigDecimal(fee).divide(new BigDecimal(real), 8, RoundingMode.HALF_UP);

        log.info("{}代付回调信息:{},{},{},{},{}", address, amount, businessId, status, tradeId);
        if (tradeType == 2) {
            TWithdraw appWithdraw = new TWithdraw();
            appWithdraw.setSerialId(businessId);
            List<TWithdraw> list = withdrawService.selectTWithdrawList(appWithdraw);
            if (CollectionUtils.isEmpty(list)) {
                log.error("代收提现回调时，订单不存在, third_serial_id:{}", businessId);
                response.getWriter().write("ok");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            } else {
                TWithdraw tWithdraw = list.get(0);
                if (tWithdraw.getStatus() == 1 || tWithdraw.getStatus() == 2) {
                    log.error("代收提现回调已经处理, third_serial_id:{}  ,{}", businessId, status);
                    response.getWriter().write("success");
                    response.getWriter().flush();
                    response.getWriter().close();
                    return;
                }
                if (3 == status) {
                    tWithdraw.setFee(fee1);
                    tWithdraw.setRealAmount(realAmount);
                    tWithdraw.setStatus(1);
                    withdrawService.updateTWithdraw(tWithdraw);
                    log.info("{}代付回调成功: s erialId:{}", name, tWithdraw.getSerialId());
                    response.getWriter().write("success");
                    response.getWriter().flush();
                    response.getWriter().close();
                    return;
                } else {
                    log.error("回调未成功，返回status:{}", status);
                    if (2 == status || 4 == status) {
                        BigDecimal amount1 = tWithdraw.getAmount();
                        String coin = tWithdraw.getCoin();
                        TAppUser user = appUserService.selectTAppUserByUserId(tWithdraw.getUserId());
                        //资产
                        Map<String, TAppAsset> map = assetService.getAssetByUserIdList(tWithdraw.getUserId());
                        TAppAsset asset = map.get(tWithdraw.getCoin() + user.getUserId());
                        BigDecimal beforeMount = asset.getAvailableAmount();
                        asset.setAmout(asset.getAmout().add(amount1));
                        asset.setAvailableAmount(beforeMount.add(amount1));
                        assetService.updateByUserId(asset);
                        tWithdraw.setStatus(2);
                        tWithdraw.setUpdateBy(SecurityUtils.getUsername());
                        tWithdraw.setWithDrawRemark("unc提现失败");
                        tWithdraw.setRemark("unc提现失败");
                        withdrawService.updateTWithdraw(tWithdraw);
                        recordService.generateRecord(tWithdraw.getUserId(), tWithdraw.getAmount(), 8,
                                SecurityUtils.getUsername(), tWithdraw.getSerialId(), "unc提现失败", beforeMount,
                                beforeMount.add(tWithdraw.getAmount()), coin, user.getAdminParentIds());
                        log.debug("账变前amout ,账变后 ，userId  {}-{} {}", beforeMount, beforeMount.add(tWithdraw.getAmount()), user.getUserId());
                    }
                }
            }
        }
        response.getWriter().write("success");
        response.getWriter().flush();
        response.getWriter().close();
        return;
    }
}
