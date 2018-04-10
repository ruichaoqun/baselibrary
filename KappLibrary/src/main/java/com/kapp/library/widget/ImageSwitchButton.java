package com.kapp.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.kapp.library.R;

/**
 * Created by Administrator on 2018/3/23 0023.
 *
 * 图片切换按钮
 */

public class ImageSwitchButton extends ImageView {

    private int drawable;//默认状态
    private int drawableLight;//选中状态
    private boolean checked;//是否选中
    private OnImageSwitchListener listener;

    public ImageSwitchButton(Context context) {
        super(context);
        init(null);
    }

    public ImageSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImageSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray ta = null;
        try {
             ta = getContext().obtainStyledAttributes(attrs, R.styleable.ImageSwitchButton);

             drawable = ta.getResourceId(R.styleable.ImageSwitchButton_drawable, 0);
             drawableLight = ta.getResourceId(R.styleable.ImageSwitchButton_drawableLight, 0);
             checked = ta.getBoolean(R.styleable.ImageSwitchButton_checked, false);

        }catch (Exception e){

        }finally {
            if (ta != null)
                ta.recycle();
        }

        formateImage();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onImageSwitch(checked);
            }
        });
    }

    private void formateImage(){
        if (drawable == 0 && drawableLight == 0)
            return;

        if (drawable == 0 || drawableLight ==0)
            setImageResource(drawable == 0 ? drawableLight : drawable);

        setImageResource(checked ? drawableLight : drawable);
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
        formateImage();
    }

    public void setDrawableLight(int drawableLight) {
        this.drawableLight = drawableLight;
        formateImage();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        formateImage();
    }

    public void setOnImageSwitchListener(OnImageSwitchListener listener){
        this.listener = listener;
    }

    public interface OnImageSwitchListener{

        void onImageSwitch(boolean checked);
    }
}
