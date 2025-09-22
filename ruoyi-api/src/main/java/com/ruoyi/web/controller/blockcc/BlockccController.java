package com.ruoyi.web.controller.blockcc;

import cc.block.data.api.domain.market.Kline;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.framework.web.domain.KlineParamVO;
import com.ruoyi.framework.web.domain.Ticker24hVO;
import com.ruoyi.framework.web.service.BlockccService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源接口 不做验证 直接调用api接口
 *
 * @author ruoyi
 */
@RestController
public class BlockccController {
    public static final Logger log = LoggerFactory.getLogger(BlockccController.class);
    @Value("${mifeng.api.eth}")
    private String apiKey;
    @Value("${mifeng.api.host}")
    private String host;
    @Autowired
    BlockccService blockccService;

    /**
     * 登录历史k线
     *
     * @param klineParamVO 入参
     * @return 结果
     */
    @PostMapping("/kline")
    public AjaxResult kline(@RequestBody KlineParamVO klineParamVO) {
        AjaxResult ajax = AjaxResult.success();
        HashMap<String, Object> map = new HashMap<>();
        List<Kline> historyKline = blockccService.getHistoryKline(klineParamVO);
        historyKline = blockccService.getConPriceMap(klineParamVO,historyKline);
        Ticker24hVO ticker =  blockccService.getHistoryKline24hrTicker(klineParamVO);
        map.put("historyKline",historyKline);
        if (historyKline != null && historyKline.size() > 0) {
            Kline kline = historyKline.get(historyKline.size() - 1);
            //ticker.setLowPrice(new BigDecimal(kline.getHigh()));
            ticker.setHighPrice(new BigDecimal(kline.getHigh()));
        }
        map.put("ticker",ticker);
        ajax.put("data", map);
        return ajax;
    }

    /**
     * 封装缩减k线数据
     *
     * @param list 入参数组list
     * @return 结果
     */
      @PostMapping("/newKline")
    public AjaxResult new_kline(@RequestBody List<KlineParamVO> list) {
        AjaxResult ajax = AjaxResult.success();
        List<Map> re=new ArrayList<>();
        for (KlineParamVO paramVO : list) {
            HashMap<String, Object> map = new HashMap<>();
            List<Kline> historyKline = blockccService.getHistoryKline2(paramVO);
            historyKline = blockccService.getConPriceMap(paramVO,historyKline);
            Ticker24hVO ticker =  blockccService.getHistoryKline24hrTicker(paramVO);
            map.put("historyKline",historyKline);
            map.put("ticker",ticker);
            re.add(map);
        }
        ajax.put("data", re);
        return ajax;
    }
}
