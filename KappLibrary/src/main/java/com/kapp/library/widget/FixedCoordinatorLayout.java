package com.kapp.library.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/3/16.
 */

public class FixedCoordinatorLayout extends CoordinatorLayout {
    private boolean canNestedScroll = true;

    public FixedCoordinatorLayout(Context context) {
        super(context);
    }

    public FixedCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if(canNestedScroll)
            return super.onStartNestedScroll(child, target, nestedScrollAxes);
        else
            return false;
    }

    public boolean isCanNestedScroll() {
        return canNestedScroll;
    }

    public void setCanNestedScroll(boolean canNestedScroll) {
        this.canNestedScroll = canNestedScroll;
    }
}
