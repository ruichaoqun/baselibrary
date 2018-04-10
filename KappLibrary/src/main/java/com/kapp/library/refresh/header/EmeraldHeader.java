package com.kapp.library.refresh.header;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.refresh.api.RefreshHeader;
import com.kapp.library.refresh.api.RefreshKernel;
import com.kapp.library.refresh.api.RefreshLayout;
import com.kapp.library.refresh.constant.RefreshState;
import com.kapp.library.refresh.constant.SpinnerStyle;
import com.kapp.library.refresh.util.DensityUtil;

import static android.view.View.MeasureSpec.getSize;

/**
 * 翡翠货源--定制刷新Header
 * Created by SCWANG on 2017/6/2.
 */

public class EmeraldHeader extends ViewGroup implements RefreshHeader {

    private boolean mFinished;

    /**
     * 贝塞尔背景
     */
    private int mWaveHeight;
    private int mHeadHeight;
    private Path mBezierPath;
    private Paint mBezierPaint;
    private boolean mShowBezierWave = false;

    private Animation loadAnim;

    private RelativeLayout rlPage;
    private ImageView ivLoad;
    private TextView tvLoad;

    //<editor-fold desc="MaterialHeader">
    public EmeraldHeader(Context context) {
        super(context);
        initView(context, null);
    }

    public EmeraldHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EmeraldHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        DensityUtil density = new DensityUtil();
        setMinimumHeight(density.dp2px(100));

        ImageView ivBgs = new ImageView(context);
        ivBgs.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getMinimumHeight()));
        ivBgs.setImageResource(R.mipmap.refresh_header_bgs);
        ivBgs.setVisibility(View.VISIBLE);
        ivBgs.setScaleType(ImageView.ScaleType.FIT_CENTER);

        int loadSize = density.dip2px(50);
        int loadPadding = density.dip2px(6);

        ivLoad = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(loadSize, loadSize);
        ivLoad.setPadding(loadPadding, loadPadding, loadPadding, loadPadding);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        ivLoad.setImageResource(R.mipmap.refresh_header_load);
        ivLoad.setScaleType(ImageView.ScaleType.FIT_XY);
        ivLoad.setLayoutParams(layoutParams);

        tvLoad = new TextView(context);
        RelativeLayout.LayoutParams lpTvLoad = new RelativeLayout.LayoutParams(loadSize, loadSize);
        tvLoad.setPadding(loadPadding, loadPadding, loadPadding, loadPadding);
        lpTvLoad.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvLoad.setLayoutParams(lpTvLoad);
        tvLoad.setText("加载中...");
        tvLoad.setTextColor(ContextCompat.getColor(getContext(), R.color.color_666666));
        tvLoad.setGravity(Gravity.CENTER);
        tvLoad.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
        tvLoad.setVisibility(View.GONE);

        rlPage = new RelativeLayout(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlPage.setLayoutParams(lp);
        rlPage.addView(ivBgs);
        rlPage.addView(ivLoad);
        rlPage.addView(tvLoad);
        rlPage.setVisibility(View.GONE);
        addView(rlPage);

        mBezierPath = new Path();
        mBezierPaint = new Paint();
        mBezierPaint.setAntiAlias(true);
        mBezierPaint.setStyle(Paint.Style.FILL);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialHeader);
        mShowBezierWave = ta.getBoolean(R.styleable.MaterialHeader_mhShowBezierWave, mShowBezierWave);
        mBezierPaint.setColor(ta.getColor(R.styleable.MaterialHeader_mhPrimaryColor, 0xff11bbff));
        if (ta.hasValue(R.styleable.MaterialHeader_mhShadowRadius)) {
            int radius = ta.getDimensionPixelOffset(R.styleable.MaterialHeader_mhShadowRadius, 0);
            int color = ta.getColor(R.styleable.MaterialHeader_mhShadowColor, 0xff000000);
            mBezierPaint.setShadowLayer(radius, 0, 0, color);
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        ta.recycle();

        try {
            loadAnim = new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
                    0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            loadAnim.setRepeatCount(Animation.INFINITE);
            loadAnim.setRepeatMode(Animation.RESTART);
            loadAnim.setDuration(500);
            loadAnim.setInterpolator(new LinearInterpolator());
        }catch (Exception e){

        }
    }

    @Override
    public void setLayoutParams(LayoutParams params) {
        super.setLayoutParams(params);
        params.height = -3;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getSize(widthMeasureSpec), getSize(heightMeasureSpec));
        rlPage.measure(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMinimumHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() == 0) {
            return;
        }
        rlPage.layout(left, top, right, getMinimumHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mShowBezierWave) {
            //重置画笔
            mBezierPath.reset();
            mBezierPath.lineTo(0, mHeadHeight);
            //绘制贝塞尔曲线
            mBezierPath.quadTo(getMeasuredWidth() / 2, mHeadHeight + mWaveHeight * 1.9f, getMeasuredWidth(), mHeadHeight);
            mBezierPath.lineTo(getMeasuredWidth(), 0);
            canvas.drawPath(mBezierPath, mBezierPaint);
        }
        super.dispatchDraw(canvas);
    }

    /** 设置水波纹效果 */
    public EmeraldHeader setShowBezierWave(boolean show) {
        this.mShowBezierWave = show;
        return this;
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        if (isInEditMode()) {
            mWaveHeight = mHeadHeight = height / 2;
        }
    }

    @Override
    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
        if (mShowBezierWave) {
            mHeadHeight = Math.min(offset, headHeight);
            mWaveHeight = Math.max(0, offset - headHeight);
            postInvalidate();
        }

        float originalDragPercent = 1f * offset / headHeight;

        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;
        float extraOS = Math.abs(offset) - headHeight;
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, (float) headHeight * 2)
                / (float) headHeight);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                (tensionSlingshotPercent / 4), 2)) * 2f;

        float rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f;
        float startAngle = rotation * 360;
        ivLoad.setPivotX(ivLoad.getWidth()/2);
        ivLoad.setPivotY(ivLoad.getHeight()/2);
        ivLoad.setRotation(startAngle);

        float targetBgsY = offset - getMinimumHeight();
        rlPage.setTranslationY(Math.min(offset, targetBgsY));
    }

    @Override
    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
        if (!mFinished) {
            onPullingDown(percent, offset, headHeight, extendHeight);
        } else {
            if (mShowBezierWave) {
                mHeadHeight = Math.min(offset, headHeight);
                mWaveHeight = Math.max(0, offset - headHeight);
                postInvalidate();
            }
        }
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
        ivLoad.clearAnimation();
        ivLoad.startAnimation(loadAnim);
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                break;
            case PullDownToRefresh:
                mFinished = false;
                rlPage.setVisibility(View.VISIBLE);
                rlPage.setScaleY(1);
                break;
            case ReleaseToRefresh:
                break;
            case Refreshing:
                tvLoad.setVisibility(View.VISIBLE);
                break;
            case PullDownCanceled:
            case RefreshFinish:
                tvLoad.setVisibility(View.GONE);
                rlPage.setVisibility(View.GONE);
                rlPage.setScaleY(1);
                break;
        }
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        ivLoad.clearAnimation();
        mFinished = true;
        return 0;
    }

    @Override
    public void setPrimaryColors(int... colors) {
        if (colors.length > 0) {
            mBezierPaint.setColor(colors[0]);
        }
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.FixedFront;
    }

}
