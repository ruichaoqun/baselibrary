package com.kapp.library.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.utils.UIUtils;

/**
 * @author meteorshower
 *         <p>
 *         满足需求： 1、文字与图文并排 2、文字与图文的实时变化 3、Checkbox与RadioButton部分功能雷同
 *         <p>
 *         XML 中使用属性说明： 顶层布局控件中添加：
 *         xmlns:app="http://schemas.android.com/apk/res-auto"
 *         <p>
 *         1、text 文本
 *         2、textColor 字体颜色
 *         3、textSize 字体大小
 *         4、textSizeLight 字体点亮大小
 *         5、textColorLight 点亮字体颜色
 *         6、drawable 默认图片
 *         7、drawableLight 点亮图片
 *         8、drawablePadding 图片与文本的距离
 *         9、mixedModeLeft 图片在文本左边
 *         10、mixedModeRight 图片在文本右边
 *         11、mixedModeTop 图片在文本上部
 *         12、mixedModeBottom 图片在文本下部
 *         13、background 文本默认背景
 *         14、backgroundLight 文本选中背景
 *         15、paddingLeft 文本左内边距
 *         16、paddingRight 文本右内边距
 *         17、paddingTop 文本上内边距
 *         18、paddingBottom 文本下内边距
 *         19、mixedFontShadow 设置文本的阴影
 *         20、mixedTextFixy 文本充满父布局
 *         21、gravity 文字在文本内的位置
 *         22、parentBackground 父类背景填充
 *         23、parentBackgroundLight 父类背景点亮填充
 */
public class MixedTextDrawView extends LinearLayout {

    //	private Logger logger = new Logger(this.getClass().getSimpleName());
    private TextView mTextView;
    private boolean checked = false;// 是否选中
    private String text;
    private int textSize;
    private int textSizeLight;
    private int textColor;// 字体颜色
    private int textColorLight;// 字体点亮颜色
    private MixedDrawableMode mixedMode = MixedDrawableMode.MIXED_DRAWABLE_NORMAL;
    private int drawable;
    private int drawableLight;
    private int drawablePadding;
    private int background;
    private int backgroundLight;
    private int parentBackground;
    private int parentBackgroundLight;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private boolean mixedFontShadow;// 是否加载文字阴影效果
    private Drawable mixedTextDrawable;
    private int textGravity;//文字Gravity排版
    private boolean mixedTextFixy = false;

    public MixedTextDrawView(Context context) {
        super(context);
        initMixed((AttributeSet) null);
    }

    public MixedTextDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMixed(attrs);
    }

    public MixedTextDrawView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMixed(attrs);
    }

    private void initMixed(AttributeSet attrs) {

        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.MixedTextDraw);
        try {
            checked = mTypedArray.getBoolean(R.styleable.MixedTextDraw_checked,
                    false);
            text = mTypedArray.getString(R.styleable.MixedTextDraw_text);
            textColor = mTypedArray.getColor(
                    R.styleable.MixedTextDraw_textColor, Color.BLACK);
            textColorLight = mTypedArray.getColor(
                    R.styleable.MixedTextDraw_textColorLight, 0);
            textSize = mTypedArray.getDimensionPixelSize(
                    R.styleable.MixedTextDraw_textSize, 12);
            textSizeLight = mTypedArray.getDimensionPixelSize(
                    R.styleable.MixedTextDraw_textSizeLight, 0);

            drawable = mTypedArray.getResourceId(
                    R.styleable.MixedTextDraw_drawable, 0);
            drawableLight = mTypedArray.getResourceId(
                    R.styleable.MixedTextDraw_drawableLight, 0);
            drawablePadding = mTypedArray.getDimensionPixelSize(
                    R.styleable.MixedTextDraw_drawablePadding, 0);

            background = mTypedArray.getResourceId(
                    R.styleable.MixedTextDraw_background, R.color.none);
            backgroundLight = mTypedArray.getResourceId(
                    R.styleable.MixedTextDraw_backgroundLight, R.color.none);

            parentBackground = mTypedArray.getResourceId(
                    R.styleable.MixedTextDraw_parentBackground, R.color.none);
            parentBackgroundLight = mTypedArray.getResourceId(
                    R.styleable.MixedTextDraw_parentBackgroundLight, R.color.none);

            paddingLeft = mTypedArray.getDimensionPixelSize(
                    R.styleable.MixedTextDraw_paddingLeft, 0);
            paddingRight = mTypedArray.getDimensionPixelSize(
                    R.styleable.MixedTextDraw_paddingRight, 0);
            paddingTop = mTypedArray.getDimensionPixelSize(
                    R.styleable.MixedTextDraw_paddingTop, 0);
            paddingBottom = mTypedArray.getDimensionPixelSize(
                    R.styleable.MixedTextDraw_paddingBottom, 0);

            int mixedModeValue = mTypedArray.getInt(R.styleable.MixedTextDraw_mixedMode, 0);

            switch (mixedModeValue) {
                case 0x1:
                    mixedMode = MixedDrawableMode.MIXED_DRAWABLE_LEFT;
                    break;
                case 0x2:
                    mixedMode = MixedDrawableMode.MIXED_DRAWABLE_TOP;
                    break;
                case 0x3:
                    mixedMode = MixedDrawableMode.MIXED_DRAWABLE_RIGHT;
                    break;
                case 0x4:
                    mixedMode = MixedDrawableMode.MIXED_DRAWABLE_BOTTOM;
                    break;
            }

            mixedFontShadow = mTypedArray.getBoolean(
                    R.styleable.MixedTextDraw_mixedFontShadow, false);
            textGravity = mTypedArray.getInt(R.styleable.MixedTextDraw_gravity, 0x1);
            mixedTextFixy = mTypedArray.getBoolean(R.styleable.MixedTextDraw_mixedTextFixy, false);

        } finally {
            mTypedArray.recycle();
        }

        initTextView();
        removeAllViews();
        addView(mTextView);
        initMixedFontShadow();
        notifyMixedTextDraw(checked);
    }

    private void initTextView() {

        mTextView = new TextView(getContext());
        if (mixedTextFixy)
            mTextView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        else
            mTextView.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mTextView.setTextColor(textColor);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTextView.setCompoundDrawablePadding(drawablePadding);
        mTextView.setPadding(paddingLeft, paddingTop, paddingRight,
                paddingBottom);
        if (!TextUtils.isEmpty(text)) {
            mTextView.setText(text);
        }
        setTextGravity();
    }

    /**
     * 添加字体阴影
     */
    private void initMixedFontShadow() {
        if (mixedFontShadow) {
            mTextView.setShadowLayer(3.0f, 0,
                    UIUtils.dip2px(getContext(), 1),
                    getResources().getColor(R.color.color_7f000000));
        }
    }

    /**
     * 反向响应设置
     */
    public void notifyMixedTextDraw() {
        notifyMixedTextDraw(!checked);
    }

    /**
     * 刷新设置
     */
    public void notifyMixedTextDraw(boolean checked) {
        this.checked = checked;
        formatMixedResourcesId();
        mixedModeDrawable();
    }

    private void formatMixedResourcesId() {
        if (background != 0)
            mTextView.setBackgroundResource(!checked ? background
                    : defaultValue(backgroundLight, background));
        if (textColor != 0)
            mTextView.setTextColor(!checked ? textColor : defaultValue(
                    textColorLight, textColor));
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, !checked ? textSize : defaultValue(textSizeLight, textSize));
        setBackgroundResource(!checked ? parentBackground : parentBackgroundLight);
    }

    private void mixedModeDrawable() {

        if (drawable == 0)
            return;

        mixedTextDrawable = getDrawableImage(!checked ? drawable : defaultValue(
                drawableLight, drawable));

        if (mixedTextDrawable == null)
            return;

        switch (mixedMode) {
            case MIXED_DRAWABLE_LEFT:
                mTextView.setCompoundDrawablesWithIntrinsicBounds(
                        mixedTextDrawable, null, null, null);
                break;
            case MIXED_DRAWABLE_TOP:
                mTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        mixedTextDrawable, null, null);
                break;
            case MIXED_DRAWABLE_RIGHT:
                mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mixedTextDrawable, null);
                break;
            case MIXED_DRAWABLE_BOTTOM:
                mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                        mixedTextDrawable);
                break;
            default:
                mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                        null);
                break;
        }
        invalidate();
    }

    private Drawable getDrawableImage(int resources) {
        Drawable drawable = getResources().getDrawable(resources);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 资源重置
     */
    private int defaultValue(int value, int defaultValue) {
        if (value == 0)
            return defaultValue;
        return value;
    }

    // 图片绘制部位
    public enum MixedDrawableMode {
        MIXED_DRAWABLE_LEFT, MIXED_DRAWABLE_RIGHT, MIXED_DRAWABLE_TOP, MIXED_DRAWABLE_BOTTOM, MIXED_DRAWABLE_NORMAL
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setText(int textResources) {
        setText(getResources().getString(textResources));
    }

    public String getTextStr() {
        if (mTextView != null) {
            return mTextView.getText().toString();
        }
        return "";
    }

    public void setText(String text) {
        this.text = text;
        mTextView.setText(text);
    }

    public void setText(SpannableString text) {
        mTextView.setText(text);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setTextSizeLight(int textSizeLight) {
        this.textSizeLight = textSizeLight;
    }

    /**
     * color resource 需要外部转换
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        mTextView.setTextColor(textColor);
    }

    public void setTextColor(ColorStateList colors) {
        mTextView.setTextColor(colors);
    }

    public void setTextColorLight(int textColorLight) {
        this.textColorLight = textColorLight;
        formatMixedResourcesId();
    }

    public void setMixedMode(MixedDrawableMode mixedMode) {
        this.mixedMode = mixedMode;
        formatMixedResourcesId();
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
        mixedModeDrawable();
    }

    public void setDrawableLight(int drawableLight) {
        this.drawableLight = drawableLight;
        mixedModeDrawable();
    }

    public void setDrawablePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
        mTextView.setCompoundDrawablePadding(drawablePadding);
    }

    public void setBackground(int background) {
        this.background = background;
        formatMixedResourcesId();
    }

    public void setBackgroundLight(int backgroundLight) {
        this.backgroundLight = backgroundLight;
        formatMixedResourcesId();
    }

    public void setParentBackground(int parentBackground){
        this.parentBackground = parentBackground;
        formatMixedResourcesId();
    }

    public void setParentBackgroundLight(int parentBackgroundLight){
        this.parentBackgroundLight = parentBackgroundLight;
        formatMixedResourcesId();
    }

    public void setPadding(int paddingLeft, int paddingTop, int paddingRight,
                           int paddingBottom) {
        try {
            this.paddingLeft = paddingLeft;
            this.paddingTop = paddingTop;
            this.paddingRight = paddingRight;
            this.paddingBottom = paddingBottom;
            mTextView.setPadding(paddingLeft, paddingTop, paddingRight,
                    paddingBottom);
        } catch (Exception e) {

        }
    }

    public void setMixedFontShadow(boolean mixedFontShadow) {
        this.mixedFontShadow = mixedFontShadow;
        initMixedFontShadow();
    }

    public void setTextGravity(int gravity) {
        mTextView.setGravity(gravity);
    }

    private void setTextGravity() {
        int gravity = Gravity.LEFT;
        ;
        switch (textGravity) {
            case 0x1:
                gravity = Gravity.LEFT;
                break;
            case 0x2:
                gravity = Gravity.TOP;
                break;
            case 0x3:
                gravity = Gravity.RIGHT;
                break;
            case 0x4:
                gravity = Gravity.BOTTOM;
                break;
            case 0x5:
                gravity = Gravity.CENTER_VERTICAL;
                break;
            case 0x6:
                gravity = Gravity.CENTER_HORIZONTAL;
                break;
            case 0x7:
                gravity = Gravity.CENTER;
                break;
            case 0x8:
                gravity = Gravity.CLIP_VERTICAL;
                break;
            case 0x9:
                gravity = Gravity.CLIP_HORIZONTAL;
                break;
            case 0x10:
                gravity = Gravity.FILL_VERTICAL;
                break;
            case 0x11:
                gravity = Gravity.FILL_HORIZONTAL;
                break;
            case 0x12:
                gravity = Gravity.FILL;
                break;
            case 0x13:
                gravity = Gravity.START;
                break;
            case 0x14:
                gravity = Gravity.END;
                break;
        }
        mTextView.setGravity(gravity);
    }

    /**
     * 设置Check效果
     */
    public void setCheckedMode() {
        MixedTextDrawView.this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyMixedTextDraw();
            }
        });
    }

    public void setTextFixy(boolean mixedTextFixy) {
        this.mixedTextFixy = mixedTextFixy;
        if (this.mixedTextFixy)
            mTextView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        else
            mTextView.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        notifyMixedTextDraw(checked);
    }

    public void setLineSpacing(float add, float mult) {
        mTextView.setLineSpacing(add, mult);
    }

    /**
     * 设置文本行数
     */
    public void setTextMaxLine(int maxLines) {
        mTextView.setMaxLines(maxLines);
    }

    /**
     * 内容View
     */
    public TextView getContentView() {
        return mTextView;
    }

}
