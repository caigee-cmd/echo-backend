package com.ruoyi.framework.web.domain;

import lombok.Data;

@Data
public class KlineParamVO {
    //["ONE_MIN","FIVE_MIN","FIFTEEN_MIN","THIRTY_MIN","ONE_HOUR","TWO_HOUR","SIX_HOUR","ONE_DAY","TWO_DAY","SEVEN_DAY"]
    private String Interval;
    //BTC  or  ETH  ....等等
    private String symbol;

    private String[] symbols;
    //结束
    private Long end ;
    //交易所
    private String market;
    private String[] markets;
}
