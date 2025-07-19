package com.ruoyi.framework.manager.blockcc;

import cc.block.data.api.BlockccApiClientFactory;
import cc.block.data.api.BlockccApiRestClient;
import cc.block.data.api.domain.BlockccResponse;
import cc.block.data.api.domain.enumeration.Interval;
import cc.block.data.api.domain.market.Kline;
import cc.block.data.api.domain.market.OrderBook;
import cc.block.data.api.domain.market.Price;
import cc.block.data.api.domain.market.request.KlineParam;
import cc.block.data.api.domain.market.request.OrderBookParam;
import cc.block.data.api.domain.market.request.PriceParam;
import cc.block.data.api.domain.news.Announcement;
import cc.block.data.api.domain.news.request.AnnouncementParam;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * 数据源处理 历史k线  汇率 交易深度 币种价格 等等。。
 * @author ruoyi
 */
@Component
public class BlockccManager {

    public static final Logger log = LoggerFactory.getLogger(BlockccManager.class);
    @Value("${mifeng.api.eth}")
    private String apiKey;
    @Value("${mifeng.api.host}")
    private String host;
    /**
     * 获取交易所公告数据
     *
     * @param  locale  语言
     * @return 结果
     */
    public List<Announcement> getAnnouncement(Locale locale){
        BlockccApiClientFactory factory = BlockccApiClientFactory.newInstance(apiKey,host);

        BlockccApiRestClient client = factory.newRestClient();
        AnnouncementParam announcementParams = AnnouncementParam.builder().locale(locale).build();
        List<Announcement> content = client.getAnnouncements(announcementParams).getContent();
        return content;
    }
    /**
     * 获取历史k线
     *
     * @param  klineParam  kline请求参数
     * @return 结果
     */
    public List<Kline> getHistoryKline(KlineParam klineParam){
        try {
            BlockccApiClientFactory factory = BlockccApiClientFactory.newInstance(apiKey,host);
            BlockccApiRestClient client = factory.newRestClient();
            // 请求方式如下
             KlineParam klineParams = KlineParam.builder().interval(klineParam.getInterval()).desc("gate-io_"+"ETH"+"_USDT").build();
            List<Kline> content = client.getKline(klineParam).getContent();
            return content;
        }catch (Exception e){
            log.error("获取历史k线异常{}"+e);
        }
            return null;
    }
    /**
     * 获取币种 汇率 可做定时任务 每隔4小时  存入外汇数据
     *
     * @return 结果
     */
    public String getExchangeRate(){
        String ret = HttpUtil.createGet ("https://data.mifengcha.com/api/v3/exchange_rate?"+apiKey)
                .contentType ("application/json")
                .execute ().body ();
        return ret;
    }
    /**
     * 获取币种币种价格 更新时间：5秒-60秒，按照交易量大小分级，交易量最大的币种5秒更新一次价格。
     * @param  symbol  币种名称
     * @return 结果
     */
    public  List<Price> getPrices(String symbol){
        //示例参数"ethereum"
        BlockccApiClientFactory factory = BlockccApiClientFactory.newInstance(apiKey,host);
        BlockccApiRestClient client = factory.newRestClient();
        PriceParam priceParams = PriceParam.builder().slug(symbol).build();
        List<Price> content = client.getPrices(priceParams).getContent();
        return content;
    }
    /**
     * 获取交易对深度
     * @param  symbol  币种名称
     * @return 结果
     */
    public BlockccResponse<OrderBook>  getOrderBook(String symbol){
        BlockccApiClientFactory factory = BlockccApiClientFactory.newInstance(apiKey,host);
        BlockccApiRestClient client = factory.newRestClient();
        //参数 BTC
        OrderBookParam orderBookParams = OrderBookParam.builder().desc("gate-io_"+symbol+"_USDT").build();
        BlockccResponse<OrderBook> orderBook = client.getOrderBook(orderBookParams);
        System.out.println();
        return orderBook;
    }
}
