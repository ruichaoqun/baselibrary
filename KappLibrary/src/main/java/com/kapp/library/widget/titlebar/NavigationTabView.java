package com.kapp.library.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.utils.KeyboardUtils;
import com.kapp.library.widget.MixedTextDrawView;

/**
 * Created by Administrator on 2017/1/10 0010.
 * TabLayout -- TitleView
 */
public class NavigationTabView extends RelativeLayout {

    private ImageButton iBtnRight,iBtnRightTwo;
    private TextView btnRight;
    private MixedTextDrawView mixedLeft,mixedRight;
    private OnTitleItemSelectedListener listener;

    public NavigationTabView(Context context) {
        super(context);
        init();
    }

    public NavigationTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.title_bar_navigation_tab_view, this);

        findViewById(R.id.back_ibtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideInputSoft(getContext(), v);
                ((Activity)getContext()).finish();
            }
        });

        mixedLeft = (MixedTextDrawView) findViewById(R.id.mixed_left);
        mixedRight = (MixedTextDrawView) findViewById(R.id.mixed_right);
        btnRight = (TextView) findViewById(R.id.right_btn);
        iBtnRight = (ImageButton) findViewById(R.id.right_ibtn);
        iBtnRightTwo = (ImageButton) findViewById(R.id.right2_ibtn);

        mixedLeft.setOnClickListener(new MixedClickListener());
        mixedRight.setOnClickListener(new MixedClickListener());
    }

    private class MixedClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            mixedLeft.notifyMixedTextDraw(false);
            mixedRight.notifyMixedTextDraw(false);
            if(v.getId() == R.id.mixed_left){
                mixedLeft.notifyMixedTextDraw(true);
                if (listener != null)
                    listener.titleItemSelected(0);
            }

            if(v.getId() == R.id.mixed_right){
                mixedRight.notifyMixedTextDraw(true);
                if (listener != null)
                    listener.titleItemSelected(1);
            }
        }
    }

    /** 定义左边返回按钮的点击事件 */
    public void setBackClickListener(OnClickListener listener){
        findViewById(R.id.back_ibtn).setOnClickListener(listener);
    }

    /** 设置标题 */
    public void setTitle(String titleLeft, String titleRight){
        mixedLeft.setText(titleLeft);
        mixedRight.setText(titleRight);
    }

    /** 设置标题 */
    public void setTitle(int titleLeftId, int titleRightId){
        mixedLeft.setText(titleLeftId);
        mixedRight.setText(titleRightId);
    }

    /** 设置标题Tab高亮Item */
    public void setTitleCurrentItem(int position){
        boolean flag = (position == 0);
        mixedLeft.notifyMixedTextDraw(flag ? true : false);
        mixedRight.notifyMixedTextDraw(flag ? false : true);
    }

    /** 设置文本按钮 */
    public void setTxtBtn(int valueId, OnClickListener listener){
        setTxtBtn(getResources().getString(valueId), listener);
    }

    /** 设置文本按钮 */
    public void setTxtBtn(String value, OnClickListener listener){
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText(value);
        btnRight.setOnClickListener(listener);
    }

    /** 设置图片按钮 */
    public void setImageBtn(int imageId, OnClickListener listener){
        iBtnRight.setVisibility(View.VISIBLE);
        iBtnRight.setImageResource(imageId);
        iBtnRight.setOnClickListener(listener);
    }

    /** 设置第二个图片按钮 */
    public void setImageTwoBtn(int imageId, OnClickListener listener){
        iBtnRightTwo.setVisibility(View.VISIBLE);
        iBtnRightTwo.setImageResource(imageId);
        iBtnRightTwo.setOnClickListener(listener);
    }

    public void setOnTitleItemSelectedListener(OnTitleItemSelectedListener listener){
        this.listener = listener;
    }

    public interface OnTitleItemSelectedListener{

        void titleItemSelected(int position);
    }

}
