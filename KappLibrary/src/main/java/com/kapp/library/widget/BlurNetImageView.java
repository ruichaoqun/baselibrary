package com.kapp.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import net.qiujuer.genius.blur.StackBlur;

/**
 * Created by Administrator on 2017/10/24 0024.
 *
 *  高斯模糊效果
 */

public class BlurNetImageView extends NetImageView {

    private int radius = 70;//模糊值（可以通过改变radius的值来改变模糊度，值越大，模糊度越大，radius<=0时则图片不显示）

    public BlurNetImageView(Context context) {
        super(context);
    }

    public BlurNetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurNetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** 设置模糊值，优先于setImage...使用 */
    public void setRadius(int radius){
        this.radius = radius;
    }

//    @Override
//    public void setImageResource(@DrawableRes int resId) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
//        Bitmap overlay = StackBlur.blurNatively(bitmap, radius, false);
//        super.setImageBitmap(overlay);
//    }
//
//    @Override
//    public void setImageBitmap(Bitmap bm) {
//        Bitmap overlay = StackBlur.blurNatively(bm, radius, false);
//        super.setImageBitmap(overlay);
//    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable != null) {
            Bitmap overlay = StackBlur.blur(drawableToBitmap(drawable), radius, false);
            super.setImageDrawable(new BitmapDrawable(getResources(), overlay));
        }else
            super.setImageDrawable(drawable);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }
}
