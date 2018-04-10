package com.kapp.library.widget.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbAccessory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kapp.library.utils.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 自定义日历控件
 *
 * */
public class MonthDateView extends View {

	private Logger logger = new Logger(this.getClass().getSimpleName());
	private static final int NUM_COLUMNS = 7;
	private static final int NUM_ROWS = 6;
	private Paint mPaint;
	private DisplayMetrics mDisplayMetrics;
	private TextView tv_date,tv_week;

	private int mDayColor = Color.parseColor("#000000");//正常色（黑色）
	private int mSelectDayColor = Color.parseColor("#ffffff");//白色
	private int mSelectBGColor = Color.parseColor("#1FC2F3");//选中日期背景色(绿色)
	private int mCurrentColor = Color.parseColor("#ff0000");//红色
	private int mCircleColor = Color.parseColor("#ff0000");//红色
	private int mOverdueColor = Color.parseColor("#9F9F9F");//过期色（灰色）

	private DatePoint currPoint;
	private DatePoint selPoint;
	private DatePoint clickPoint;
	private int mColumnSize,mRowSize;
	private int mDaySize = 18;
	private int weekRow;
	private int mCircleRadius = 6;
	private int downX = 0,downY = 0;

	private int [][] daysString;
	private List<Integer> daysHasThingList;
	private boolean circleSelectBgFlag = false;//选中是否使用圆背景

	private DateClick dateClick;
	private boolean overdueEnable = false;
	private List<DatePointInfo> overdueDateList = new ArrayList<>();//过期时间列表

	public MonthDateView(Context context){
		super(context);
		init();
	}

	public MonthDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MonthDateView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		mDisplayMetrics = getResources().getDisplayMetrics();
		mPaint = new Paint();
		currPoint = new DatePoint(Calendar.getInstance());
		setSelectYearMonth(currPoint.year, currPoint.month, currPoint.day);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		initSize();
		daysString = new int[6][7];
		mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
		int dayChoose;
		int mMonthDays = DateUtils.getMonthDays(selPoint.year, selPoint.month);
		int weekNumber = DateUtils.getFirstDayWeek(selPoint.year, selPoint.month);
//		Log.d("DateView", "DateView:" + selPoint.month+"月1号周" + weekNumber);
		for(int day = 0;day < mMonthDays;day++){
			dayChoose = day + 1;
			int column = (day+weekNumber - 1) % 7;
			int row = (day+weekNumber - 1) / 7;
			daysString[row][column]=day + 1;
			int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(String.valueOf(dayChoose)))/2);
			int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);

			//选择日期
			if(isChooseYearMonth(selPoint.year, selPoint.month, dayChoose)){
				//绘制背景色矩形
				int startRecX = mColumnSize * column;
				int startRecY = mRowSize * row;
				int endRecX = startRecX + mColumnSize;
				int endRecY = startRecY + mRowSize;
				mPaint.setColor(mSelectBGColor);
				if(circleSelectBgFlag){
					int radius = (endRecY - startRecY)/2;
					int circleX = (endRecX + startRecX)/2;
					int circleY = (endRecY + startRecY) /2;
					canvas.drawCircle(circleX, circleY,radius,mPaint);
				}else{
					canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
				}
				//记录第几行，即第几周
				weekRow = row + 1;
			}
			//绘制事务圆形标志
			drawCircle(row,column,day + 1,canvas);
			if(isChooseYearMonth(selPoint.year, selPoint.month, dayChoose)){
				mPaint.setColor(mSelectDayColor);
			}else if(dayChoose == currPoint.day && currPoint.day != selPoint.day && (currPoint.month-1) == selPoint.month){
				//正常月，选中其他日期，则今日为红色
				mPaint.setColor(mCurrentColor);
			}else if (isOverdueData(selPoint.year, selPoint.month, dayChoose)){
				mPaint.setColor(mOverdueColor);
			}else {
				mPaint.setColor(mDayColor);
			}
			canvas.drawText(String.valueOf(dayChoose), startX, startY, mPaint);
			if(tv_date != null){
				int monthNum = selPoint.month+1;
				StringBuffer sb = new StringBuffer();
				sb.append(selPoint.year).append("-");
				sb.append(monthNum > 9 ? monthNum : "0"+monthNum);
				tv_date.setText(sb.toString());
			}
			
			if(tv_week != null){
				tv_week.setText("第" + weekRow  +"周");
			}
		}
	}

	//是否执行过期
	private boolean isOverdueData(int year, int month, int day){
//		logger.test_i("year : ", year+" ** month : "+month+" ** day : "+day+" -- "+currPoint.year+" -- "+currPoint.month+" -- "+currPoint.day);

		if (!overdueEnable)
			return false;

		for (DatePointInfo info : overdueDateList){
			if (info.getStartPoint() != null && info.getEndPoint() != null){
				int startPoint = info.getStartPoint().checkDate(year, month, day);
				int endPoint = info.getEndPoint().checkDate(year, month, day);
				if (startPoint < 0 || endPoint > 0)
					return true;
			}
		}
		return false;
	}

	private boolean isChooseYearMonth(int year, int month, int day){
		if (year == clickPoint.year && month == clickPoint.month && day == clickPoint.day)
			return true;
		return false;
	}
	
	private void drawCircle(int row,int column,int day,Canvas canvas){
		if(daysHasThingList != null && daysHasThingList.size() >0){
			if(!daysHasThingList.contains(day))return;
			mPaint.setColor(mCircleColor);
			float circleX = (float) (mColumnSize * column +	mColumnSize*0.8);
			float circley = (float) (mRowSize * row + mRowSize*0.2);
			canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventCode=  event.getAction();
		switch(eventCode){
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			int upX = (int) event.getX();
			int upY = (int) event.getY();
			if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
				performClick();
				doClickAction((upX + downX)/2,(upY + downY)/2);
			}
			break;
		}
		return true;
	}

	/** 初始化列宽行高 */
	private void initSize(){
		mColumnSize = getWidth() / NUM_COLUMNS;
		mRowSize = getHeight() / NUM_ROWS;
	}
	
	/** 设置年月 */
	private void setSelectYearMonth(int year,int month,int day){
		selPoint = new DatePoint(year, month, day);
	}

	/** 执行点击事件 */
	private void doClickAction(int x,int y){
		int row = y / mRowSize;
		int column = x / mColumnSize;

		if (isOverdueData(selPoint.year,selPoint.month,daysString[row][column]))
			return;

		setSelectYearMonth(selPoint.year,selPoint.month,daysString[row][column]);

		invalidate();
		//执行activity发送过来的点击处理事件
		if(dateClick != null){
			if (clickPoint == null)
				clickPoint = new DatePoint();
			clickPoint.year = getmSelYear();
			clickPoint.month = selPoint.month;
			clickPoint.day = getmSelDay();
			dateClick.onClickOnDate(clickPoint.year, getmSelMonth(), clickPoint.day);
		}
	}

	/** 左点击，日历向后翻页 */
	public void onLeftClick(){
		int year = selPoint.year;
		int month = selPoint.month;
		int day = selPoint.day;
		if(month == 0){//若果是1月份，则变成12月份
			year = selPoint.year-1;
			month = 11;
		}else if(DateUtils.getMonthDays(year, month) == day){
			//如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
			month = month-1;
			day = DateUtils.getMonthDays(year, month);
		}else{
			month = month-1;
		}
		setSelectYearMonth(year,month,day);
		invalidate();
	}
	
	/** 右点击，日历向前翻页 */
	public void onRightClick(){
		int year = selPoint.year;
		int month = selPoint.month;
		int day = selPoint.day;
		if(month == 11){//若果是12月份，则变成1月份
			year = selPoint.year+1;
			month = 0;
		}else if(DateUtils.getMonthDays(year, month) == day){
			//如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
			month = month + 1;
			day = DateUtils.getMonthDays(year, month);
		}else{
			month = month + 1;
		}
		setSelectYearMonth(year,month,day);
		invalidate();
	}

	/************************************** Set Date Widget Attrs **********************************/

	/** 是否设置过期不可点击 */
	public void setOverdueEnable(boolean enable){
		this.overdueEnable = enable;
		invalidate();
	}

	/** 设置过期日期 */
	public void setOverdueDate(List<DatePointInfo> overdueDateList){
		if (overdueDateList != null)
			this.overdueDateList.clear();
		this.overdueDateList.addAll(overdueDateList);
		invalidate();
	}

	/** 设置过期日期 */
	public void setOverdueDate(Date startDate, Date endData){
		DatePoint startOverduePoint = new DatePoint(startDate);
		DatePoint endOverduePoint = new DatePoint(endData);
		DatePointInfo info = new DatePointInfo();
		info.setStartPoint(startOverduePoint);
		info.setEndPoint(endOverduePoint);
		overdueDateList.add(info);
		invalidate();
	}

	/** 设置过期日期 */
	public void setOverdueDate(DatePoint startDatePoint, DatePoint endDatePoint){
		DatePointInfo info = new DatePointInfo();
		info.setStartPoint(startDatePoint);
		info.setEndPoint(endDatePoint);
		overdueDateList.add(info);
		invalidate();
	}

	/** 设置默认年月日 */
	public void setDefaultDate(int year, int month, int day){
		if (clickPoint == null)
			clickPoint = new DatePoint();
		clickPoint.year = year;
		clickPoint.month = month-1;
		clickPoint.day = day;
		setSelectYearMonth(this.clickPoint.year, this.clickPoint.month, this.clickPoint.day);
		createValidityDateLists(this.clickPoint.year, this.clickPoint.month, this.clickPoint.day);
	}
	
	/** 获取选择的年份 */
	public int getmSelYear() {
		return selPoint.year;
	}

	/** 获取选择的月份 */
	public int getmSelMonth() {
		return selPoint.month+1;
	}

	/** 获取选择的日期 */
	public int getmSelDay() {
		return this.selPoint.day;
	}

	/** 普通日期的字体颜色，默认黑色 */
	public void setmDayColor(int mDayColor) {
		this.mDayColor = mDayColor;
	}
	
	/** 选择日期的颜色，默认为白色  */
	public void setmSelectDayColor(int mSelectDayColor) {
		this.mSelectDayColor = mSelectDayColor;
	}

	/** 选中日期的背景颜色，默认蓝色 */
	public void setmSelectBGColor(int mSelectBGColor) {
		this.mSelectBGColor = mSelectBGColor;
	}

	/** 当前日期不是选中的颜色，默认红色 */
	public void setmCurrentColor(int mCurrentColor) {
		this.mCurrentColor = mCurrentColor;
	}

	/** 日期的大小，默认18sp  */
	public void setmDaySize(int mDaySize) {
		this.mDaySize = mDaySize;
	}

	/** 设置显示当前日期的控件 (tv_date:显示日期, tv_week:显示周) */
	public void setTextView(TextView tv_date, TextView tv_week){
		this.tv_date = tv_date;
		this.tv_week = tv_week;
		invalidate();
	}

	/** 设置事务天数 */
	public void setDaysHasThingList(List<Integer> daysHasThingList) {
		this.daysHasThingList = daysHasThingList;
	}

	/*** 设置圆圈的半径，默认为6 */
	public void setmCircleRadius(int mCircleRadius) {
		this.mCircleRadius = mCircleRadius;
	}
	
	/** 设置圆圈的半径 */
	public void setmCircleColor(int mCircleColor) {
		this.mCircleColor = mCircleColor;
	}

	/** 跳转至今天 */
	public void setTodayToView(){
		setSelectYearMonth(currPoint.year,currPoint.month,currPoint.day);
		invalidate();
	}

	/** 设置圆形选中背景模式 */
	public void setIsCircleSelectBgFlag(boolean circleSelectBgFlag){
		this.circleSelectBgFlag = circleSelectBgFlag;
		invalidate();
	}

	/** 设置日期点击事件 */
	public void setDateClick(DateClick dateClick) {
		this.dateClick = dateClick;
	}

	public interface DateClick{
		void onClickOnDate(int year, int monty, int day);
	}

	//创建有效时间数据
	private void createValidityDateLists(int year, int month, int day){
		DatePointInfo validityInfo = new DatePointInfo();

		Calendar calendar = Calendar.getInstance();
		try{
			StringBuffer dateStr = new StringBuffer();
			dateStr.append(year).append("-");
			dateStr.append(month).append("-");
			dateStr.append(day);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
			Date date = sdf.parse(dateStr.toString());
			calendar.setTime(date);
		} catch (ParseException e) {
			//日期错误，默认使用当天作为标准
		}

		validityInfo.setEndPoint(new DatePoint(calendar));

		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 30);
		validityInfo.setStartPoint(new DatePoint(calendar));

		overdueDateList.add(validityInfo);
		overdueEnable = true;
	}

}
