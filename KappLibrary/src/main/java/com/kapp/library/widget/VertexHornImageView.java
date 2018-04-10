package com.kapp.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kapp.library.R;

/**
 * Created by Administrator on 2016/10/24 0024.
 * xmlns:app="http://schemas.android.com/apk/res-auto"
 * 支持四个角单独实现圆角效果
 * 支持任意一边两个角实现圆角效果
 * 暂时未支持三个角圆角（没有使用场景）
 */
public class VertexHornImageView extends NetImageView {

    private Paint paint, paintType;
    private int round = 20;//圆角度数
    private RoundType type = RoundType.ROUND_NORMAL;

    public VertexHornImageView(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public VertexHornImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VertexHornImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init( AttributeSet attrs){

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.VertexHorn);
        try{
            round = ta.getDimensionPixelSize(R.styleable.VertexHorn_round, round);
            int typeValue = ta.getInt(R.styleable.VertexHorn_roundType, 0);
            switch (typeValue){
                case 0x01:
                    type = RoundType.ROUND_LEFT;
                    break;
                case 0x02:
                    type = RoundType.ROUND_TOP;
                    break;
                case 0x03:
                    type = RoundType.ROUND_RIGHT;
                    break;
                case 0x04:
                    type = RoundType.ROUND_BOTTOM;
                    break;
                case 0x05:
                    type = RoundType.ROUND_LEFT_TOP;
                    break;
                case 0x06:
                    type = RoundType.ROUND_LEFT_BOTTOM;
                    break;
                case 0x07:
                    type = RoundType.ROUND_RIGHT_TOP;
                    break;
                case 0x08:
                    type = RoundType.ROUND_RIGHT_BOTTOM;
                    break;
            }
        }catch (Exception e){

        }finally {
            if (ta != null)
                ta.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paintType = new Paint();
        paintType.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        Drawable drawable = getDrawable();
        if (drawable != null && getWidth() > 0 && getHeight() > 0){
            Bitmap bitmap = drawableToBitamp(drawable);

            //缩放操作
            Matrix matrix = new Matrix();
            float scaleX = (float) getWidth()/bitmap.getWidth();
            float scaleY = (float) getHeight()/bitmap.getHeight();
            matrix.postScale(scaleX, scaleY);
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            Rect rect = new Rect(0, 0 , getWidth(), getHeight());
            RectF rectF = new RectF(rect);
            BitmapShader shader = new BitmapShader(resizeBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            canvas.drawRoundRect(rectF, round, round, paint);

            switch (type){
                case ROUND_LEFT:
                    drawRightTop(canvas, resizeBmp);
                    drawRightBottom(canvas, resizeBmp);
                    break;
                case ROUND_TOP:
                    drawLetBottom(canvas, resizeBmp);
                    drawRightBottom(canvas, resizeBmp);
                    break;
                case ROUND_RIGHT:
                    drawLetTop(canvas, resizeBmp);
                    drawLetBottom(canvas, resizeBmp);
                    break;
                case ROUND_BOTTOM:
                    drawLetTop(canvas, resizeBmp);
                    drawRightTop(canvas, resizeBmp);
                    break;
                case ROUND_LEFT_TOP:
                    drawLetBottom(canvas, resizeBmp);
                    drawRightTop(canvas, resizeBmp);
                    drawRightBottom(canvas, resizeBmp);
                    break;
                case ROUND_LEFT_BOTTOM:
                    drawLetTop(canvas, resizeBmp);
                    drawRightTop(canvas, resizeBmp);
                    drawRightBottom(canvas, resizeBmp);
                    break;
                case ROUND_RIGHT_TOP:
                    drawLetTop(canvas, resizeBmp);
                    drawLetBottom(canvas, resizeBmp);
                    drawRightBottom(canvas, resizeBmp);
                    break;
                case ROUND_RIGHT_BOTTOM:
                    drawLetTop(canvas, resizeBmp);
                    drawLetBottom(canvas, resizeBmp);
                    drawRightTop(canvas, resizeBmp);
                    break;
            }

            try {
                shader = null;
                resizeBmp.recycle();
                resizeBmp = null;
            }catch (Exception e){

            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    //填充左上角
    private void drawLetTop(Canvas canvas, Bitmap bitmap){
        Rect rect = new Rect(0, 0 , round, round);
        RectF rectF = new RectF(rect);
        canvas.drawBitmap(bitmap, rect, rectF, paintType);
    }

    //填充左下角
    private void drawLetBottom(Canvas canvas, Bitmap bitmap){
        Rect rect = new Rect(0, getHeight()-round , round, getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawBitmap(bitmap, rect, rectF, paintType);
    }

    //填充右上角
    private void drawRightTop(Canvas canvas, Bitmap bitmap){
        Rect rect = new Rect(getWidth() - round, 0 , getWidth(), round);
        RectF rectF = new RectF(rect);
        canvas.drawBitmap(bitmap, rect, rectF, paintType);
    }

    //填充右xia角
    private void drawRightBottom(Canvas canvas, Bitmap bitmap){
        Rect rect = new Rect(getWidth()-round, getHeight()-round , getWidth(), getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawBitmap(bitmap, rect, rectF, paintType);
    }

    /** drawable转bitmap */
    private Bitmap drawableToBitamp(Drawable drawable){
        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /** 设置圆角位置 */
    public void setRoundType(RoundType type){
        this.type = type;
        invalidate();
    }

    //
    public enum RoundType{
        ROUND_LEFT,//左边两个圆角
        ROUND_TOP,//顶部两个圆角
        ROUND_RIGHT,//右边两个圆角
        ROUND_BOTTOM,//底部两个圆角
        ROUND_LEFT_TOP,//左上圆角
        ROUND_LEFT_BOTTOM,//左下圆角
        ROUND_RIGHT_TOP,//右上圆角
        ROUND_RIGHT_BOTTOM,//右下圆角
        ROUND_NORMAL;//无效果
    }

}
