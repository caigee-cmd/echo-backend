package com.ruoyi.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 *
 * @author zebra
 *
 */
@Slf4j
public class DateUtil {
	/**
	 * 将Date类型转换为字符串
	 *
	 * @param date
	 *            日期类型
	 * @return 日期字符串
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将Date类型转换为字符串
	 *
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 将字符串转换为Date类型
	 *
	 * @param date
	 *            字符串类型
	 * @return 日期类型
	 */
	public static Date format(String date) {
		return format(date, "null");
	}

	/**
	 * 将字符串转换为Date类型
	 *
	 * @param date
	 *            字符串类型
	 * @param pattern
	 *            格式
	 * @return 日期类型
	 */
	public static Date format(String date, String pattern) {
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (date == null || date.equals("") || date.equals("null")) {
			return new Date();
		}
		Date d = null;
		try {
			d = new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
			log.debug("转换日期格式错误");
		}
		return d;
	}

	/**
	 * 将Date类型转换为字符串
	 *
	 * @param date
	 *            日期类型
	 * @return 日期字符串
	 */
	public static Date formatStartDay(Date date) {
		String day = format(date, "yyyy-MM-dd");
		return format(day + " 00:00:00");
	}

	public static Date formatEndDay(Date date) {
		String day = format(date, "yyyy-MM-dd");
		return format(day + " 23:59:59");
	}

	public static Date formatStartDay(String date) {
		return format(date + " 00:00:00");
	}

	public static Date formatEndDay(String date) {
		return format(date + " 23:59:59");
	}

	/**
	 *
	 * @Description:比较两个日期的天数差
	 * @Check parameters by the 【caller】 or 【itself】(参数由谁校验)
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2)

	{

		Calendar cal = Calendar.getInstance();

		cal.setTime(date1);

		long time1 = cal.getTimeInMillis();

		cal.setTime(date2);

		long time2 = cal.getTimeInMillis();

		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));

	}

	/**
	 * 相加月份
	 *
	 * @param datetime
	 * @return
	 */
	public static Date dateFormat(Date datetime, Integer month) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(datetime);
		cl.add(Calendar.MONTH, +month);
		datetime = cl.getTime();
		return datetime;
	}

	/**
	 * 相加天
	 *
	 * @param datetime
	 * @return
	 */
	public static Date dateFormatDay(Date datetime, Integer day) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(datetime);
		cl.add(Calendar.DATE, +day);
		datetime = cl.getTime();
		return datetime;
	}

	/**
	 * 相加分钟
	 *
	 * @param datetime
	 * @return
	 */
	public static Date dateFormatMINUTE(Date datetime, Integer day) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(datetime);
		cl.add(Calendar.MINUTE, +day);
		datetime = cl.getTime();
		return datetime;
	}

	/**
	 * 根据小写数字格式的日期转换成大写格式的日期
	 *
	 * @param date
	 * @return
	 */
	public final static char[] upper = "零一二三四五六七八九十".toCharArray();

	public static String getUpperDate(String date) {
		// 支持yyyy-MM-dd、yyyy/MM/dd、yyyyMMdd等格式
		if (date == null)
			return null;
		// 非数字的都去掉
		date = date.replaceAll("\\D", "");
		if (date.length() != 8)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) { // 年
			sb.append(upper[Integer.parseInt(date.substring(i, i + 1))]);
		}
		sb.append("年"); // 拼接年
		int month = Integer.parseInt(date.substring(4, 6));
		if (month <= 10) {
			sb.append(upper[month]);
		} else {
			sb.append("十").append(upper[month % 10]);
		}
		sb.append("月");// 拼接月

		int day = Integer.parseInt(date.substring(6));
		if (day <= 10) {
			sb.append(upper[day]);
		} else if (day < 20) {
			sb.append("十").append(upper[day % 10]);
		} else {
			sb.append(upper[day / 10]).append("十");
			int tmp = day % 10;
			if (tmp != 0)
				sb.append(upper[tmp]);
		}
		sb.append("日"); // 拼接日
		return sb.toString();
	}

	/**
	 * 相差天数
	 *
	 * @param startTime
	 * @param endTime
	 * @param format
	 * @return
	 */
	public static Long dateDiff(String startTime, String endTime, String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		// 获得两个时间的毫秒时间差异
		Long diff;
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			long day = diff / nd;// 计算差多少天
			return day;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}
	public static int daydiff(Date fDate, Date oDate) {

		Calendar aCalendar = Calendar.getInstance();

		aCalendar.setTime(fDate);

		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

		aCalendar.setTime(oDate);

		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		return day2 - day1;

	}



	public static  Map<String, Object> getDays(Date date) {

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

	/**
	 * 获取今天是几号
	 * @return
	 */
	public static  Integer getDays() {
		Integer dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		return dayOfMonth;
	}
	/**
	 * 获取某月的最后一天
	 *
	 */
	public static String getLastDayOfMonth(int year,int month)
	{
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month);
		//获取当月最小值
		int lastDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		//设置日历中的月份，当月+1月-1天=当月最后一天
		cal.set(Calendar.DAY_OF_MONTH, lastDay-1);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		//获取当月月份
		int month = calendar.get(Calendar.MONTH) + 2;
		System.out.println(month);
		System.out.println(getDays(new Date()));
        System.out.println(getLastDayOfMonth(2023,3));
	}

}
