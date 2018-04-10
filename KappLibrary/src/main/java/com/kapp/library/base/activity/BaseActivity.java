package com.kapp.library.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kapp.library.manager.AppSkipManager;
import com.kapp.library.utils.KeyboardUtils;
import com.kapp.library.utils.Logger;
import com.umengs.library.analytics.UmStateUtils;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Logger logger = new Logger(this.getClass().getSimpleName());
    private View keyHideView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppSkipManager.addActivity(this);
        keyHideView = new View(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeyboardUtils.hideInputSoft(this,getWindow().getDecorView());
        UmStateUtils.getInstances().onStartTimes(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmStateUtils.getInstances().onEndTimes(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppSkipManager.removeActivity(this);
        //KeyboardUtils.hideInputSoft(this,getWindow().getDecorView());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            KeyboardUtils.hideInputSoft(this, keyHideView);
        }
        return super.onKeyDown(keyCode, event);
    }

    /** 获取资源色值 */
    public int getResColor(int colorId){
        return ContextCompat.getColor(this, colorId);
    }

    /** 获取资源字符串 */
    public String getResString(int stringId){
        return getResources().getString(stringId);
    }

    /** 获取控件内容 */
    public String getViewStr(TextView textView){
        if (textView == null)
            return null;
        return textView.getText().toString().trim();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
