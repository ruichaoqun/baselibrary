package com.kapp.library.widget.timepicker;

import android.content.Context;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/22 0022.
 * 时间选择器
 */
public class TimePickerUtils {

    public static final int ALL = 0;//年月日时分
    public static final int YEAR_MONTH_DAY = 1;//年月日
    public static final int HOURS_MINS = 2;//时分
    public static final int MONTH_DAY_HOUR_MIN = 3;//月日时分
    public static final int YEAR_MONTH = 4;//年月

    private static TimePickerUtils timePickerUtils = null;
    private TimePickerView pickerView;

    public static TimePickerUtils getInstances() {
        if (timePickerUtils == null)
            timePickerUtils = new TimePickerUtils();
        return timePickerUtils;
    }

    /**
     * 时间选择器
     */
    public void showPickerTime(Context context, int typeVlaue, OnTimePickerListener listener) {
        showPickerTime(context, new Date(), typeVlaue, listener);
    }

    /**
     * 时间选择器
     */
    public void showPickerTime(Context context, Date date, int typeValue, final OnTimePickerListener listener) {
        showPickerTime(context, null, date, typeValue, listener);
    }

    /**
     * 时间选择器
     */
    public void showPickerTime(Context context,String title, Date date, int typeValue, final OnTimePickerListener listener) {
        pickerView = new TimePickerView(context, getType(typeValue));
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pickerView.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦
        pickerView.setTime(date);
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);
        if (!TextUtils.isEmpty(title))
            pickerView.setTitle(title);
        //时间选择后回调
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (listener != null)
                    listener.timePicker(date);
            }
        });
        pickerView.show();
    }

    /**
     * 时间选择器
     */
    public void showPickerTimeBefore(Context context, Date date, int typeValue, int beforeYear, final OnTimePickerListener listener) {
        pickerView = new TimePickerView(context, getType(typeValue));
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pickerView.setRange(calendar.get(Calendar.YEAR) - beforeYear, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pickerView.setTime(date);
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);
        //时间选择后回调
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (listener != null)
                    listener.timePicker(date);
            }
        });
        pickerView.show();
    }

    /**
     * 时间选择器
     */
    public void showPickerTimeBeforeAndAfter(Context context, Date date, int typeValue, int beforeYear, int afterYear, String title, final OnTimePickerListener listener) {
        pickerView = new TimePickerView(context, getType(typeValue));
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pickerView.setRange(calendar.get(Calendar.YEAR) - beforeYear, calendar.get(Calendar.YEAR) + afterYear);//要在setTime 之前才有效果哦
        pickerView.setTime(date);
        pickerView.setTitle(title);
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);
        //时间选择后回调
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (listener != null)
                    listener.timePicker(date);
            }
        });
        pickerView.show();
    }

    private TimePickerView.Type getType(int value) {
        switch (value) {
            case ALL:
                return TimePickerView.Type.ALL;
            case YEAR_MONTH_DAY:
                return TimePickerView.Type.YEAR_MONTH_DAY;
            case HOURS_MINS:
                return TimePickerView.Type.HOURS_MINS;
            case MONTH_DAY_HOUR_MIN:
                return TimePickerView.Type.MONTH_DAY_HOUR_MIN;
            case YEAR_MONTH:
                return TimePickerView.Type.YEAR_MONTH;
        }
        return TimePickerView.Type.ALL;
    }

    /**
     * 是否显示
     */
    public boolean isShowing() {
        if (pickerView != null && pickerView.isShowing())
            return true;
        return false;
    }

    /**
     * 关闭窗口
     */
    public void dismiss() {
        if (isShowing())
            pickerView.dismiss();
    }

    public interface OnTimePickerListener {

        void timePicker(Date date);
    }
}
