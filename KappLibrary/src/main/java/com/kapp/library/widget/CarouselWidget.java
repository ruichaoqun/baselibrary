package com.kapp.library.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kapp.library.R;
import com.kapp.library.utils.UIUtils;


/**
 * Created by Administrator on 2016/9/6.
 */
public class CarouselWidget extends RelativeLayout {

    private ViewPager vp;
    private LinearLayout llPage;
    private PagerAdapter adapter;
    private boolean isStopCarousel = true;
    private int delayedTime = 5000;//切换时间
    private int totalCount;
    private int imageNormal, imagePressed;
    private boolean isImageDot = false;//是否图标为亮点
    private int dotSize = 8;//小圆点大小

    public CarouselWidget(Context context) {
        super(context);
        init();
    }

    public CarouselWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CarouselWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_carousel_view, this);

        vp = (ViewPager) findViewById(R.id.carousel_vp);
        llPage = (LinearLayout) findViewById(R.id.sign_layout);
    }

    public void setAdapter(PagerAdapter adapter, final int totalCount) {
        this.adapter = adapter;
        this.totalCount = totalCount;
        vp.setAdapter(this.adapter);
        if(totalCount > 2){
            vp.setCurrentItem((adapter.getCount()/(totalCount*2))*totalCount);
        }

        notifySignPage(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                notifySignPage(position % totalCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置亮点显隐藏
     */
    public void setDotPageVisibility(int visibility) {
        llPage.setVisibility(visibility);
    }

    /**
     * 设置亮点图片
     */
    public void setDotImage(int normal, int pressed) {
        if (normal > 0 && pressed > 0) {
            isImageDot = true;
            this.imageNormal = normal;
            this.imagePressed = pressed;
            notifySignPage(0);
        }
    }

    /**
     * 设置亮点大小
     */
    public void setDotSize(int dotSize) {
        if (dotSize > 0) {
            this.dotSize = dotSize;
            notifySignPage(0);
        }
    }

    /**
     * 设置亮点Margin 单位：dp
     */
    public void setDotPageMargin(int left, int top, int right, int bottom) {
        LayoutParams lp = (LayoutParams) llPage.getLayoutParams();
        lp.setMargins(UIUtils.dip2px(getContext(), left), UIUtils.dip2px(getContext(), top),
                UIUtils.dip2px(getContext(), right), UIUtils.dip2px(getContext(), bottom));
        llPage.setLayoutParams(lp);
    }

    /**
     * 设置亮点View 位置
     */
    public void setDotPageLocation(boolean left, boolean right) {
        LayoutParams lp = (LayoutParams) llPage.getLayoutParams();
        if (left)
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        if (right)
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        llPage.setLayoutParams(lp);
    }

    /**
     * 启动轮播
     */
    public void startCarousel() {
        if (isStopCarousel) { // 如果本来就是停止状态，则直接开启，否则不用重复开启，重复开启会造成实际轮播间隔时间比设置的短
            this.isStopCarousel = false;
            handler.sendEmptyMessageDelayed(1, delayedTime);
        }
    }

    /**
     * 关闭轮播
     */
    public void stopCarousel() {
        this.isStopCarousel = true;
    }

    /**
     * 轮播切换时长
     */
    public void setDelayedTime(int delayedTime) {
        this.delayedTime = delayedTime;
    }

    private void notifySignPage(int size) {
        if (llPage.getVisibility() != View.VISIBLE)
            return;
        int count = totalCount;
        llPage.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.dip2px(getContext(), dotSize),
                    UIUtils.dip2px(getContext(), dotSize)));
//            iv.setBackgroundColor(i == size ? 0xFFFFFFFF : 0x7FFFFFFF);
            if (imageNormal > 0 && imagePressed > 0) {
                iv.setImageResource(i == size ? imagePressed : imageNormal);
            } else {
                iv.setImageResource(i == size ? R.mipmap.icon_dot_checked_deepred : R.mipmap.icon_dot_uncheck);
            }

            llPage.addView(iv);
            if (i != (count - 1)) {
                View view = new View(getContext());
                view.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.dip2px(getContext(), dotSize * 1.5f), dotSize));
                llPage.addView(view);
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!isStopCarousel) {
                int currentNum = vp.getCurrentItem();
                currentNum++;
//                if (currentNum == (adapter.getCount() - 1)) {
//                    currentNum = 0;
//                } else {
//                    currentNum++;
//                }
                vp.setCurrentItem(currentNum);
                handler.sendEmptyMessageDelayed(1, delayedTime);
            }
        }
    };

    public ViewPager getVp() {
        return vp;
    }
}
