package com.ruoyi.bussiness.domain.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 首页数据统计
 *
 * @author ruoyi
 */
@Data
public class SysDataVO {

    //标题 1平台总收入，2玩家数量，3总充值金额，4提现金额
    private Integer title;
    //总值
    private BigDecimal totalNum;

    private String redLineName;
    private String blueLineName;
    private Map redLine;
    private Map blueLine;

}
