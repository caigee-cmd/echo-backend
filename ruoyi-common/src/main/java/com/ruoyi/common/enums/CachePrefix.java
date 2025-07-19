package com.ruoyi.common.enums;

public enum CachePrefix {

    /**
     * token 信息
     */
    ACCESS_TOKEN,
    /**
     * token 信息
     */
    REFRESH_TOKEN,
    /**
     * 用户错误登录限制
     */
    LOGIN_TIME_LIMIT,
    /**
     * 普通验证码
     */
    CODE,
    /**
     * 短信验证码
     */
    SMS_CODE,
    /**
     * 邮箱验证码
     */
    EMAIL_CODE,
    /**
     * 币种汇率
     */
    CURRENCY_PRICE,
    /**
     * 24小时开盘价
     */
    CURRENCY_OPEN_PRICE,
    /**
     * 收盘价
     **/
    CURRENCY_CLOSE_PRICE,
    /**
     * 用户多币种充值地址
     */
    USER_ADDRESS,
    /**
     * 秒合约订单
     */
    ORDER_SECOND_CONTRACT,
    /**
     * 钱包KEY
     */
    USER_WALLET,
    /**
     * 用户借贷KEY
     */
    APP_LOADORDER,
    /**
     * 理财购买限制
     */
    MINE_FINANCIAL,
    /**
     *理财购买成交频繁限制
     */
    MINE_FINANCIAL_ORDER,
    /**
     * 玩家的站内信
     */
    USER_MAIL,
    /**
     * 玩家公共的站内信
     */
    COMMONALITY_MAIL,
    /**
     * 币币交易订单key
     */
    REDIS_KEY_CURRENCY_ORDER,
    /**
     * 用户登录 标识
     */
    USER_LOGIN_ADDRESS_FLAG,

    /**
     * 自有币种缓存
     */
    OWN_COIN_CACHE,
    /**
     * 币种k线缓存
     */
    COIN_KLINE,
    /**
     * 币种交易
     */
    COIN_TRADE,
    /**
     * 币种详情
     */
    COIN_DETAIL,
    /**
     * 止盈
     */
    POSITION_PRICE,
    /**
     * 用户上次提现地址
     */
    USER_ADDRESS_WITHDRAW
    ;


    public static String removePrefix(String str) {
        return str.substring(str.lastIndexOf("}_") + 2);
    }

    /**
     * 通用获取缓存key值
     *
     * @return 缓存key值
     */
    public String getPrefix() {
        return "{" + this.name() + "}_:";
    }

}
