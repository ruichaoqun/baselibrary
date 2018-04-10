package com.kapp.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kapp.library.R;
import com.kapp.library.utils.Logger;

/**
 * 自定义评分控件
 * @starPadding：星星间距
 * @starSize：星星大小（height设置小于星星的大小，默认星星大小为高度）
 * @numStars：星星数量
 * @src：星星资源图片
 * @srcLight：星星点亮资源图片
 * @rating：点亮星星值
 * @clickable：是否相应手势
 *
 */
public class RatingStarBar extends View{

    private Logger logger = new Logger(this.getClass().getSimpleName());
    private Bitmap src,srcLight;
    private Paint paint,srcPaint;//绘制星星画笔

    private int starPadding = 0;
    private int numStars = 0;
    private int starSize = 0;
    private float rating = 0.0F;
    private boolean integerMark = false;
    private boolean clickable = true;//是否可点击

    private OnStarChangeListener onStarChangeListener;//监听星星变化接口

    public RatingStarBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatingStarBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        setClickable(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingStarBar);
        try{
            this.starPadding = typedArray.getDimensionPixelOffset(R.styleable.RatingStarBar_starPadding, 0);
            this.starSize = typedArray.getDimensionPixelOffset(R.styleable.RatingStarBar_starSize, 10);
            this.numStars = typedArray.getInteger(R.styleable.RatingStarBar_numStars, 5);

            int srcResId = typedArray.getResourceId(R.styleable.RatingStarBar_src, 0);
            if (srcResId != 0)
                this.src = createScaleBitmap(srcResId);
            int srcLightResId = typedArray.getResourceId(R.styleable.RatingStarBar_srcLight, 0);
            if (srcLightResId != 0)
                this.srcLight = createScaleBitmap(srcLightResId);
            this.rating = typedArray.getFloat(R.styleable.RatingStarBar_rating, 0.0f);
            this.clickable = typedArray.getBoolean(R.styleable.RatingStarBar_clickable, true);
        }catch (Exception e){

        }finally {
            if (typedArray != null)
                typedArray.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(srcLight, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        srcPaint = new Paint();
        srcPaint.setAntiAlias(true);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = starSize * numStars + starPadding * (numStars - 1);
        int height = starSize;
//        logger.test_i("onMeasure : ", width +" ** "+height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (srcLight == null || src == null) {
            return;
        }

        for (int i = 0;i < numStars;i++) {
            canvas.drawBitmap(src, (starPadding + starSize) * i, 0, srcPaint);
        }

        canvas.translate(0,0);
        if (rating > 1) {
            canvas.drawRect(0, 0, starSize, starSize, paint);
            if(rating-(int)(rating) == 0) {
                for (int i = 1; i < rating; i++) {
                    canvas.translate(starPadding + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
            }else {
                for (int i = 1; i < rating - 1; i++) {
                    canvas.translate(starPadding + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
                canvas.translate(starPadding + starSize, 0);
                canvas.drawRect(0, 0, starSize * (Math.round((rating - (int) (rating))*10)*1.0f/10), starSize, paint);
            }
        }else {
            canvas.drawRect(0, 0, starSize * rating, starSize, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clickable){
            int x = (int) event.getX();
            if (x < 0) x = 0;
            if (x > getMeasuredWidth()) x = getMeasuredWidth();
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN: {
                    setRating(x*1.0f / (getMeasuredWidth()*1.0f/numStars));
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    setRating(x*1.0f / (getMeasuredWidth()*1.0f/numStars));
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    break;
                }
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    /** 设置是否需要整数评分 */
    public void setIntegerMark(boolean integerMark){
        this.integerMark = integerMark;
    }

    /** 设置显示的星星的分数  */
    public void setRating(float mark){
        if (integerMark) {
            rating = (int)Math.ceil(mark);
        }else {
            rating = Math.round(mark * 10) * 1.0f / 10;
        }
        if (this.onStarChangeListener != null) {
            this.onStarChangeListener.onStarChange(rating);  //调用监听接口
        }
        invalidate();
    }

    /** 获取显示星星的数目 */
    public float getRating(){
        return rating;
    }

    /** 缩放目标图片适应设置大小 */
    private Bitmap createScaleBitmap(int resourseId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourseId);
        Matrix matrix = new Matrix();
        float scaleX = ((float)starSize)/bitmap.getWidth();
        float scaleY = ((float)starSize)/bitmap.getHeight();
        matrix.setScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /** 设置监听 */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener){
        this.onStarChangeListener = onStarChangeListener;
    }

    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    public void setNumStars(int numStars){
        this.numStars = numStars;
        invalidate();
    }

}
