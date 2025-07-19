package com.ruoyi.common.utils.ucontract;

import com.google.zxing.client.result.BizcardResultParser;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 合约交易计算类
 *
 * @author:michael
 * @createDate: 2022/10/19 16:21
 */
public class ContractComputerUtil {


    //计算强平价

    /**
     * @param num   交易币数量
     * @param level 杠杆
     * @param
     * @return
     */

    public static BigDecimal getStrongPrice(BigDecimal level, Integer type, BigDecimal openPrice, BigDecimal num, BigDecimal amount, BigDecimal fee) {
        BigDecimal result = BigDecimal.ZERO;
        if (level.compareTo(new BigDecimal(1)) >= 0) {
            if (type == 0) {
                result = (openPrice.subtract(amount.divide(num, 6, RoundingMode.HALF_UP)).add(fee.divide(num, 6, RoundingMode.HALF_UP)).setScale(6, RoundingMode.HALF_UP));
            } else if (type == 1) {
                result = ((amount.divide(num, 6, RoundingMode.HALF_UP).add(openPrice)).subtract(fee.divide(num, 6, RoundingMode.HALF_UP)).setScale(6, RoundingMode.HALF_UP));
            }
        }

        return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
    }

    /**
     * 计算合约收益
     *
     * @param openPrice
     * @param num
     * @param closePrice
     * @param type
     * @return
     */
    public static BigDecimal getPositionEarn(BigDecimal openPrice, BigDecimal num, BigDecimal closePrice, int type) {
        if (0 == type) {
            return (closePrice.subtract(openPrice)).multiply(num);
        } else if (1 == type) {
            return (openPrice.subtract(closePrice)).multiply(num);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 计算买入金额
     *
     * @param price
     * @return
     */
    public static BigDecimal getAmount(BigDecimal price, BigDecimal num, BigDecimal level) {
        return price.multiply(num).divide(level, 6, RoundingMode.UP);

    }

    /**
     * 计算成交交
     */
    public static BigDecimal getEarnDealPrice(BigDecimal openPrice, int type, BigDecimal earnRate) {
        BigDecimal result = BigDecimal.ZERO;
        if (0 == type) {
            result = openPrice.add((openPrice.multiply(earnRate)));
        } else {
            result = openPrice.subtract((openPrice.multiply(earnRate)));
        }

        return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result.setScale(6, RoundingMode.HALF_UP);

    }

    /**
     * 计算成交交
     */
    public static BigDecimal getLossDealPrice(BigDecimal openPrice, int type, BigDecimal lossRate) {
        BigDecimal result = BigDecimal.ZERO;
        if (0 == type) {
            result = openPrice.subtract((openPrice.multiply(lossRate)));
        } else {
            result = openPrice.add((openPrice.multiply(lossRate)));
        }
        return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result.setScale(6, RoundingMode.HALF_UP);
    }

    public static BigDecimal getPositionRate(BigDecimal openPrice, BigDecimal closePrice, int type) {
        if (0 == type) {
            return (closePrice.subtract(openPrice)).divide(openPrice, 4, RoundingMode.DOWN);
        } else if (1 == type) {
            return (openPrice.subtract(closePrice)).divide(openPrice, 4, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal getRate(BigDecimal openPrice, BigDecimal closePrice, int type) {
        if (0 == type) {
            return closePrice.subtract(openPrice);
        } else if (1 == type) {
            return openPrice.subtract(closePrice);
        }
        return BigDecimal.ZERO;
    }
}
