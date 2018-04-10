package com.kapp.library.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kapp.library.R;
import com.kapp.library.utils.UIUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/11/26.
 * 加载中View
 */

public class LoadingImage extends View{
    private int arcWidth,interval;
    private Paint circlePaint,arcPaint,textPaint;
    Rect bounds = new Rect();
    private SweepGradient shade;
    private int percent = 0;


    public LoadingImage(Context context) {
        this(context,null);
    }

    public LoadingImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        arcWidth = UIUtils.dip2px(context,6);
        interval = UIUtils.dip2px(context,2);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStrokeWidth(interval);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStrokeWidth(arcWidth);
        arcPaint.setColor(Color.WHITE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setStyle(Paint.Style.STROKE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(UIUtils.dip2px(context,14));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.LEFT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int radius = (width - (arcWidth*2 + interval)*2)/2;
        int degree = (int) (percent*3.6f);
        String s = percent+"%";
        if(shade == null){
            shade = new SweepGradient(width/2,width/2,new int[]{Color.TRANSPARENT,Color.WHITE,Color.WHITE,Color.WHITE},null);
            arcPaint.setShader(shade);
//            Matrix matrix = new Matrix();
//            matrix.setRotate(-90, width/2, width/2);
//            shade.setLocalMatrix(matrix);
        }
        canvas.save();
        canvas.rotate(-90,width/2,width/2);
        if(degree > 2)
            canvas.drawArc(new RectF(10,10,width-10,width-10),5,degree,false,arcPaint);
        canvas.restore();
        canvas.drawCircle(canvas.getWidth()/2,canvas.getWidth()/2,radius,circlePaint);
        textPaint.getTextBounds(s, 0, s.length(), bounds);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(s,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, textPaint);
    }

    public void setPercent(int percent) {
        if(percent > 5){
            this.percent = percent;
            invalidate();
        }
    }
}
