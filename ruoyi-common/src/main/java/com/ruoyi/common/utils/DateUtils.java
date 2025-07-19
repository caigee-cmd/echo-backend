package com.ruoyi.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import com.ruoyi.common.core.domain.entity.TimeZone;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 * 
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }


    public static int daysBetween(Date date1, Date date2)

    {

        int i = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));

        return i;

    }

    /**
     * 计算时间差
     *
     * @param endDate 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 相加天数
     * @param datetime 时间
     * @param day   天数
     * @return
     */
    public static Date dateFormatDay(Date datetime, Integer day) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(datetime);
        cl.add(Calendar.DATE, +day);
        datetime = cl.getTime();
        return datetime;
    }

    public static Map<String, Object> getWeek(Date date) {

        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        //创建日历类
        Calendar calendar = Calendar.getInstance();
        //获取当月月份
        int month = calendar.get(Calendar.MONTH) + 1;
        //设置当前月份
        calendar.set(0, month, 0);
        //获取当月每个月有多少天
        int monthday = calendar.get(Calendar.DAY_OF_MONTH);
        //设置当前时间
        calendar.setTime(date);
        //今天是几号
        int today = calendar.get(Calendar.DATE);
        //今天是周几
        String weekday = weekDays[calendar.get(Calendar.DAY_OF_WEEK)-1];
        Map<String,Object> map=new HashMap<>();
        map.put("week",weekday);
        map.put("day",today);
        return map;
    }


    public static List<TimeZone> getZoneTimeList() {
        List<TimeZone>  list=new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        for (String zoneId : ZoneId.getAvailableZoneIds()) {
            TimeZone  timeZone=new TimeZone();
            ZoneId id = ZoneId.of(zoneId);

            // LocalDateTime -> ZonedDateTime
            ZonedDateTime zonedDateTime = localDateTime.atZone(id);

            // ZonedDateTime -> ZoneOffset
            ZoneOffset zoneOffset = zonedDateTime.getOffset();

            //replace Z to +00:00
            String offset = zoneOffset.getId().replaceAll("Z", "");

            if(StringUtils.isNotEmpty(offset)){
                timeZone.setZoneId(id.toString());
                timeZone.setOffSet(offset);
                list.add(timeZone);
            }
        }
        List<TimeZone> newList = new ArrayList<>();// 用于存放没有重复的元素的list
        for (TimeZone timeZone : list) {
            boolean b = newList.stream().anyMatch(u -> u.getOffSet().equals(timeZone.getOffSet()));
            if (!b) {
                newList.add(timeZone);
            }
        }
        newList = newList.stream().sorted(Comparator.comparing(TimeZone::getOffSet)).collect(Collectors.toList());
        return newList;
    }

    public static TimeZone getTimeZone() {
        java.util.TimeZone timeZone = java.util.TimeZone.getDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId id = ZoneId.of(timeZone.getID());
        ZonedDateTime zonedDateTime = localDateTime.atZone(id);
        ZoneOffset zoneOffset = zonedDateTime.getOffset();

        TimeZone resultTimeZone = new TimeZone();
        resultTimeZone.setOffSet(zoneOffset.getId().replaceAll("Z", ""));
        resultTimeZone.setOffSetValue(resultTimeZone.getOffSet().replaceAll(":",".").replaceAll("\\+0","")
                .replaceAll("\\+","").replaceAll("\\-0","").replaceAll(".00",""));
        resultTimeZone.setZoneId(id.toString());
        return resultTimeZone;
    }
    public static TimeZone getTimeZone(String gmt) {
      java.util.TimeZone timeZone = java.util.TimeZone.getTimeZone(gmt);
        ZoneId id = ZoneId.of(timeZone.getID());
        LocalDateTime localDateTime = LocalDateTime.now(id);
        ZonedDateTime zonedDateTime = localDateTime.atZone(id);
        ZoneOffset zoneOffset = zonedDateTime.getOffset();
        com.ruoyi.common.core.domain.entity.TimeZone resultTimeZone = new com.ruoyi.common.core.domain.entity.TimeZone();
        resultTimeZone.setOffSet(zoneOffset.getId().replaceAll("Z", ""));
        String  offSetValue="";
        String offSet = resultTimeZone.getOffSet();
        String[] split = offSet.split(":");
        int i = Integer.parseInt(split[0]);
        int i1 = Integer.parseInt(split[1]);
        if(i1>0){
            offSetValue=i+"."+i1;
        }else {
            offSetValue=i+"";
        }
        resultTimeZone.setOffSetValue(offSetValue);
        resultTimeZone.setZoneId(id.toString());
        System.out.println(resultTimeZone);
        return resultTimeZone;
    }

    public static void main(String[] args) {

        TimeZone timeZone = getTimeZone();
        System.out.println(timeZone);

    }
}
