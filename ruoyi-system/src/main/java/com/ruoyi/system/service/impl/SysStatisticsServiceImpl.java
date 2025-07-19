package com.ruoyi.system.service.impl;

import com.ruoyi.bussiness.domain.TAppUser;
import com.ruoyi.bussiness.domain.vo.SysDataVO;
import com.ruoyi.bussiness.mapper.TAppRechargeMapper;
import com.ruoyi.bussiness.mapper.TAppUserMapper;
import com.ruoyi.bussiness.mapper.TWithdrawMapper;
import com.ruoyi.common.enums.RecoenOrderStatusEmun;
import com.ruoyi.common.enums.RecordEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 系统首页数据统计 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysStatisticsServiceImpl implements ISysStatisticsService {
    @Autowired
    private TAppRechargeMapper rechargeMapper;

    @Autowired
    private TWithdrawMapper withdrawMapper;

    @Autowired
    private TAppUserMapper userMapper;

    @Override
    public List<SysDataVO> getDataList(String parentId) {
        List<SysDataVO> resultList = new ArrayList<>();
        List<String> dateList = this.recentDays(7);
        Supplier<SysDataVO> factory = SysDataVO::new;
        SysDataVO rechargeVO;
        BigDecimal allRecharge = rechargeMapper.getAllRecharge(parentId, RecordEnum.RECHARGE.getCode());
        //allRecharge = allRecharge == null ? BigDecimal.ZERO : allRecharge;
        BigDecimal allWithdraw = withdrawMapper.getAllWithdraw(parentId, RecordEnum.WITHDRAW.getCode());
        //allWithdraw = allWithdraw == null ? BigDecimal.ZERO : allWithdraw;
        //一周充值数据
        Map<String, Object> redLine = this.weekRecharge(parentId, RecoenOrderStatusEmun.pass.getCode());
        //一周提现数据
        Map<String, Object> blueLine = this.weekWithdraw(parentId, RecoenOrderStatusEmun.pass.getCode());
        //查询平台总收入
        rechargeVO = factory.get();
        rechargeVO.setTotalNum(allRecharge.subtract(allWithdraw).setScale(2, RoundingMode.HALF_UP));
        this.getTotalRevenueOfPlatform(resultList, dateList, rechargeVO, redLine, blueLine);
        //查询平台玩家数量
        rechargeVO = factory.get();
        this.getUserNum(parentId, resultList, dateList, rechargeVO);
        //查询平台总充值金额
        rechargeVO = factory.get();
        rechargeVO.setTotalNum(allRecharge.setScale(2, RoundingMode.HALF_UP));
        rechargeVO.setRedLine(redLine);
        this.getRechargeOfPlatform(parentId, resultList, dateList, rechargeVO);
        //查询平台总提现金额
        rechargeVO = factory.get();
        rechargeVO.setTotalNum(allWithdraw.setScale(2, RoundingMode.HALF_UP));
        rechargeVO.setRedLine(blueLine);
        this.getWithdrawOfPlatform(parentId, resultList, dateList, rechargeVO);

        return resultList;
    }

    /**
     * 查询平台总收入
     */
    private void getTotalRevenueOfPlatform(List<SysDataVO> resultList, List<String> dateList,
                                           SysDataVO rechargeVO, Map<String, Object> redLine, Map<String, Object> blueLine) {
        rechargeVO.setTitle(1);
        rechargeVO.setRedLineName("充值");
        rechargeVO.setBlueLineName("提现");
        dateList.forEach(str -> {
            if (!redLine.containsKey(str)) {
                redLine.put(str, 0L);
            }
            if (!blueLine.containsKey(str)) {
                blueLine.put(str, 0L);
            }
        });
        rechargeVO.setRedLine(redLine);
        rechargeVO.setBlueLine(blueLine);
        resultList.add(rechargeVO);
    }

    /**
     * 查询平台玩家数量
     */
    private void getUserNum(String parentId, List<SysDataVO> resultList, List<String> dateList, SysDataVO rechargeVO) {
        Date now = DateUtils.dateFormatDay(new Date(), -6);
        rechargeVO.setTitle(2);
        TAppUser tAppUser = new TAppUser();
        tAppUser.setIsTest(0);
        tAppUser.setAdminParentIds(parentId);
        List<TAppUser> tAppUsers = userMapper.selectTAppUserList(tAppUser);
        rechargeVO.setTotalNum(new BigDecimal(tAppUsers.size()).stripTrailingZeros());
        rechargeVO.setRedLineName("注册");
        rechargeVO.setBlueLineName("冻结");
        tAppUsers = tAppUsers.stream().filter(user -> user.getCreateTime().getTime() > now.getTime())
                .collect(Collectors.toList());
        Map<String, Long> redLine = tAppUsers.stream().filter(user -> user.getStatus() == 0)
                .collect(Collectors.groupingBy(user -> DateUtils.dateTime(user.getCreateTime()), Collectors.counting()));
        Map<String, Long> blueLine = tAppUsers.stream().filter(user -> user.getStatus() != 0)
                .collect(Collectors.groupingBy(user -> DateUtils.dateTime(user.getCreateTime()), Collectors.counting()));
        dateList.forEach(str -> {
            if (!redLine.containsKey(str)) {
                redLine.put(str, 0L);
            }
            if (!blueLine.containsKey(str)) {
                blueLine.put(str, 0L);
            }
        });
        rechargeVO.setRedLine(redLine);
        rechargeVO.setBlueLine(blueLine);
        resultList.add(rechargeVO);
    }

    /**
     * 查询平台总充值金额
     */
    private void getRechargeOfPlatform(String parentId, List<SysDataVO> resultList, List<String> dateList, SysDataVO rechargeVO) {
        rechargeVO.setTitle(3);
        rechargeVO.setRedLineName("充值成功");
        rechargeVO.setBlueLineName("充值失败");
        //充值失败
        Map<String, Object> blueLine = this.weekRecharge(parentId, RecoenOrderStatusEmun.failed.getCode());
        dateList.forEach(str -> {
            if (!blueLine.containsKey(str)) {
                blueLine.put(str, 0L);
            }
        });
        rechargeVO.setBlueLine(blueLine);
        resultList.add(rechargeVO);
    }

    /**
     * 查询平台总提现金额
     */
    private void getWithdrawOfPlatform(String parentId, List<SysDataVO> resultList, List<String> dateList, SysDataVO rechargeVO) {
        rechargeVO.setTitle(4);
        rechargeVO.setRedLineName("提现成功");
        rechargeVO.setBlueLineName("提现失败");
        //提现失败
        Map<String, Object> blueLine = this.weekWithdraw(parentId, RecoenOrderStatusEmun.failed.getCode());
        dateList.forEach(str -> {
            if (!blueLine.containsKey(str)) {
                blueLine.put(str, 0L);
            }
        });
        rechargeVO.setBlueLine(blueLine);
        resultList.add(rechargeVO);
    }

    /**
     * 一周充值数据
     */
    private Map<String, Object> weekRecharge(String parentId, String type) {
        Map<String, Object> returnMap;
        List<Map<String, Object>> recharge;
        if ("1".equals(type)) {
            recharge = rechargeMapper.getWeekRecharge(parentId);
        } else {
            recharge = rechargeMapper.getWeekFailedRecharge(parentId);
        }
        if (!CollectionUtils.isEmpty(recharge)) {
            returnMap = recharge.stream()
                    .collect(Collectors.toMap(map -> (String) map.get("createTime"), map -> map.get("recharge")));
        } else {
            returnMap = new HashMap<>(12);
        }
        return returnMap;
    }

    /**
     * 一周提现数据
     */
    private Map<String, Object> weekWithdraw(String parentId, String type) {
        Map<String, Object> returnMap;
        List<Map<String, Object>> withdraw;
        if ("1".equals(type)) {
            withdraw = withdrawMapper.getWeekWithdraw(parentId);
        } else {
            withdraw = withdrawMapper.getWeekFailedWithdraw(parentId);
        }
        if (!CollectionUtils.isEmpty(withdraw)) {
            returnMap = withdraw.stream()
                    .collect(Collectors.toMap(map -> map.get("createTime").toString(), map -> map.get("withdraw")));
        } else {
            returnMap = new HashMap<>(12);
        }
        return returnMap;
    }

    /**
     * 查询近几天的日期
     *
     * @param days 天数
     * @return
     */
    private List<String> recentDays(int days) {
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            dateList.add(DateUtils.parseDateToStr("yyyy-MM-dd", DateUtils.dateFormatDay(new Date(), -i)));
        }
        return dateList;
    }

}
