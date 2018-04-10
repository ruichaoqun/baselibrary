package com.kapp.library.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.utils.KeyboardUtils;

/**
 * Created by Administrator on 2016/10/13 0013.
 * 通用标题
 */
public class NavigationView extends RelativeLayout {

    private ImageButton iBtnRight, iBtnRightTwo, iBtnBack;
    private TextView btnLeft, btnRight;
    private TextView tvTitle;
    private ImageView ivTitle;
    private View line, parentView;

    public NavigationView(Context context) {
        super(context);
        init();
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.title_bar_navigation_view, this);

        iBtnBack = (ImageButton) findViewById(R.id.back_ibtn);
        iBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideInputSoft(getContext(), v);
                ((Activity) getContext()).onBackPressed();
            }
        });

        parentView = findViewById(R.id.parent);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        btnLeft = (TextView) findViewById(R.id.left_btn);
        btnRight = (TextView) findViewById(R.id.right_btn);
        iBtnRight = (ImageButton) findViewById(R.id.right_ibtn);
        iBtnRightTwo = (ImageButton) findViewById(R.id.right2_ibtn);
        line = findViewById(R.id.horiz_line);
    }

    /**
     * 设置图片标题
     */
    public void setTitleImage(int resourcesId) {
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        ivTitle.setImageResource(resourcesId);
    }

    /**
     * 定义左边返回按钮的点击事件
     */
    public void setImageLeftBtn(OnClickListener listener) {
        setImageLeftBtn(0, listener);
        iBtnBack.setOnClickListener(listener);
    }

    /**
     * 定义左边图片按钮
     */
    public void setImageLeftBtn(int imageId) {
        ImageButton iBtnLeft = (ImageButton) findViewById(R.id.back_ibtn);
        if (imageId > 0)
            iBtnLeft.setImageResource(imageId);
    }

    /**
     * 定义左边图片按钮
     */
    public void setImageLeftBtn(int imageId, OnClickListener listener) {
        ImageButton iBtnLeft = (ImageButton) findViewById(R.id.back_ibtn);
        if (imageId > 0)
            iBtnLeft.setImageResource(imageId);
        iBtnLeft.setOnClickListener(listener);
    }

    public void setImgTitle(int imageId) {
        tvTitle.setVisibility(View.GONE);
        ivTitle.setVisibility(View.VISIBLE);
        ivTitle.setBackgroundResource(imageId);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(title);
    }

    /**
     * 设置标题
     */
    public void setTitle(int titleId) {
        tvTitle.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(titleId);
    }

    public void setTitle(String title, int colorId) {
        tvTitle.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(title);
        tvTitle.setTextColor(ContextCompat.getColor(getContext(), colorId));
    }

    /**
     * 设置左侧文本按钮
     */
    public void setTxtLeftBtn(int valueId, OnClickListener listener) {
        setTxtLeftBtn(getResources().getString(valueId), listener);
    }

    /**
     * 设置左侧文本颜色
     */
    public void setBtnLeftColor(int id) {
        btnLeft.setTextColor(ContextCompat.getColor(getContext(), id));
    }

    /**
     * 设置左侧文本按钮
     */
    public void setTxtLeftBtn(String value, OnClickListener listener) {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setText(value);
        btnLeft.setOnClickListener(listener);
    }

    /**
     * 设置右侧文本按钮
     */
    public void setTxtBtn(int valueId, OnClickListener listener) {
        setTxtBtn(getResources().getString(valueId), listener);
    }

    /**
     * 设置右侧文本颜色
     */
    public void setBtnRightColor(int id) {
        btnRight.setTextColor(ContextCompat.getColor(getContext(), id));
    }

    /**
     * 设置右侧文本按钮
     */
    public void setTxtBtn(String value, OnClickListener listener) {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText(value);
        btnRight.setOnClickListener(listener);
    }

    /**
     * 设置图片按钮
     */
    public void setImageBtn(int imageId, OnClickListener listener) {
        if (imageId == 0 && listener == null) {
            iBtnRight.setVisibility(View.GONE);
            iBtnRight.setOnClickListener(null);
            return;
        }
        iBtnRight.setVisibility(View.VISIBLE);
        iBtnRight.setImageResource(imageId);
        iBtnRight.setOnClickListener(listener);
    }

    /**
     * 设置第二个图片按钮
     */
    public void setImageTwoBtn(int imageId, OnClickListener listener) {
        iBtnRightTwo.setVisibility(View.VISIBLE);
        iBtnRightTwo.setImageResource(imageId);
        iBtnRightTwo.setOnClickListener(listener);
    }

    /**
     * 设置标题背景色值
     */
    public void setParentBgsColor(int colorId) {
        parentView.setBackgroundColor(ContextCompat.getColor(getContext(), colorId));
    }

    /**
     * 设置标题背景色值
     */
    public void setParentBgsResources(int resourcesId) {
        parentView.setBackgroundResource(resourcesId);
    }

    /**
     * 设置back键隐藏
     */
    public void setBackGone() {
        iBtnBack.setVisibility(View.GONE);
    }

    /**
     * 设置横线显隐藏
     */
    public void setLineVisbility(int visibility) {
        line.setVisibility(visibility);
    }

    /**
     * 设置横线背景色
     */
    public void setLineBgsColor(int colorBgs) {
        line.setBackgroundResource(colorBgs);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageButton getiBtnRight() {
        return iBtnRight;
    }

}
