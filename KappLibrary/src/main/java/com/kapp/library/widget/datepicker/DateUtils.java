package com.kapp.library.widget.datepicker;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	/**
     * 通过年份和月份 得到当月的日子
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
		month++;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: 
		    return 31;
		case 4:
		case 6:
		case 9:
		case 11: 
		    return 30;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
				return 29;
			}else{
				return 28;
			}
		default:
			return  -1;
		}
    }
    /**
     * 返回当前月份1号位于周几
     * @param year
     * 		年份
     * @param month
     * 		月份，传入系统获取的，不需要正常的
     * @return
     * 	日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month){
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, 1);
    	Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
    	return calendar.get(Calendar.DAY_OF_WEEK);
    }

	/**
	 * 获取某个日子相差多少天后的日期
	 * @param date 基础日期
	 * @param days 相差天数。正数，后面；负数，前面
	 * @return
	 */
    public static Date getGapDaysData(Date date, int days) {
		// 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
		calendar.add(Calendar.DATE, days);
		Date newDate = calendar.getTime();
		return newDate;
	}

	/**
	 * 返回两个日期是否按照小到大的顺序。一样也不行，必须后面的大。
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isTimeOrder(Date date1, Date date2) {
    	return date2.getTime() - date1.getTime() > 0;
	}
}
