package com.kapp.library.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2016/9/5.
 */
public abstract class BasePopup {

    protected PopupWindow popupWindow;
    protected Activity context;
    private View baseView;
    private OnDismissPopupListener listener;

    public BasePopup(Activity context) {
        this(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public BasePopup(Activity context, int width, int height){
        this.context = context;

        baseView = setContextView(LayoutInflater.from(context));

        popupWindow = new PopupWindow(baseView, width, height);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置去除遮挡虚拟按键
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setPopupAttrs(popupWindow);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (listener != null)
                    listener.onDismissPopup();
            }
        });
    }

    public abstract View setContextView(LayoutInflater inflater);

    public abstract void setPopupAttrs(PopupWindow popupWindow);

    public View getView(){
        return baseView;
    }

    public Activity getContext(){
        return context;
    }

    public void showPopup(View view){
        //7.0系统showAsDropDown有BUG，7.0用showAtLocation方法，其他还用showAsDropDown方法
        try{
            if (Build.VERSION.SDK_INT < 24) {
                popupWindow.showAsDropDown(view);
            } else {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                // 7.1 版本处理
                if (Build.VERSION.SDK_INT == 25) {
                    //【note!】Gets the screen height without the virtual key
                    WindowManager wm = (WindowManager) popupWindow.getContentView().getContext().getSystemService(Context.WINDOW_SERVICE);
                    int screenHeight = wm.getDefaultDisplay().getHeight();
                /*
                /*
                 * PopupWindow height for match_parent,
                 * will occupy the entire screen, it needs to do special treatment in Android 7.1
                */
                    popupWindow.setHeight(screenHeight - location[1] - view.getHeight()/* - yoff*/);
                }
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, /*xoff*/0, location[1] + view.getHeight()/* + yoff*/);

//                // 获取控件的位置，安卓系统>7.0
//                int[] location = new int[2];
//                view.getLocationOnScreen(location);
//                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, location[1] + view.getHeight());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showPopup(int viewId){
        try{
            popupWindow.showAtLocation(context.findViewById(viewId), Gravity.CENTER, 0, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isShowing(){
        return popupWindow.isShowing();
    }

    public void showPopup(View v,int gravity){
       showPopup(v, gravity, 0, 0);
    }

    public void showPopup(View v,int gravity, int x, int y){
        try{
            popupWindow.showAtLocation(v, gravity, x, y);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dismissPopup(){
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    public void setOnDismissPopupListener(OnDismissPopupListener listener){
        this.listener = listener;
    }

    public interface OnDismissPopupListener{

        void onDismissPopup();
    }

    /** 关闭监听 */
    public void setClosePopupListener(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopup();
            }
        });
    }
}
