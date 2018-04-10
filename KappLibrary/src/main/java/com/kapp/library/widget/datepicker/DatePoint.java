package com.kapp.library.widget.datepicker;

import com.kapp.library.utils.Logger;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class DatePoint {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    public int year;
    public int month;
    public int day;

    public DatePoint(){

    }

    public DatePoint(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DatePoint(Calendar calendar){
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH)+1;
        this.day = calendar.get(Calendar.DATE);
    }

    public DatePoint(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DATE);
    }

    /** 对比日期 */
    public int checkDate(int mYear, int mMonth, int mDay){
        return checkDate(mYear, mMonth, mDay, false);
    }

    /** 对比日期 */
    public int checkDate(int mYear, int mMonth, int mDay, boolean isLogger){
        if (isLogger) {
            logger.i("year1 : " + mYear + " ** month : " + mMonth + " && day : " + mDay);
            logger.i("year2 : " + year + " ** month : " + month + " && day : " + day);
        }
        if (mYear > year) {
            if (isLogger)
                logger.i(" Year Size Win !!");
            return 1;
        }else if (mYear == year && mMonth > (month-1)) {
            if (isLogger)
                logger.i(" Month Size Win !!");
            return 1;
        }else if (mYear == year && mMonth == (month-1) && mDay > day) {
            if (isLogger)
                logger.i(" Day Size Win !!");
            return 1;
        }else if(mYear == year && mMonth == (month-1) && mDay == day) {
            if (isLogger)
                logger.i(" No Win !!");
            return 0;
        }else {
            if (isLogger)
                logger.i(" Small !!");
            return -1;
        }
    }

}
