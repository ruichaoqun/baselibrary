package com.kapp.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kapp.library.R;

/**
 * Created by Administrator on 2016/12/6 0006.
 * 提醒弹框
 */
public class RemindDialog extends Dialog {

    private TextView tvTitle,tvContent,tvCancel,tvEnter;
    private View line;
    private OnRemindDialogClickListener listener;

    private String titleValue, msgValue,cancelValue,enterValue;
    private boolean isShowAllButton = true;//全显示按钮
    private boolean isOnlyEnterButton = false;//是否只显示Enter按钮
    private boolean isNoTitle = false;//无标题

    public RemindDialog(Context context) {
        super(context, R.style.dialog_custom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_dialog);
        setCanceledOnTouchOutside(false);//按对话框以外的地方不起作用。按返回键还起作用
//        setCancelable(false);//按对话框以外的地方不起作用。按返回键也不起作用

        tvTitle = (TextView) findViewById(R.id.title_tv);
        tvContent = (TextView) findViewById(R.id.message_tv);
        tvCancel = (TextView) findViewById(R.id.cancel_tv);
        tvEnter = (TextView) findViewById(R.id.enter_tv);
        line = findViewById(R.id.line_view);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.remindDialogClick(false);
                dismiss();
            }
        });



        tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.remindDialogClick(true);
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();

        if (!TextUtils.isEmpty(titleValue))
            tvTitle.setText(titleValue);

        if (!TextUtils.isEmpty(msgValue))
            tvContent.setText(msgValue);

        if (!TextUtils.isEmpty(cancelValue))
            tvCancel.setText(cancelValue);

        if (!TextUtils.isEmpty(enterValue))
            tvEnter.setText(enterValue);

        if (isShowAllButton){
            tvCancel.setVisibility(View.VISIBLE);
            tvEnter.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }else{
            if (isOnlyEnterButton)
                tvCancel.setVisibility(View.GONE);
            else
                tvEnter.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        if (isNoTitle){
            tvTitle.setVisibility(View.GONE);
            findViewById(R.id.controls).setVisibility(View.GONE);
        }
    }

    /** 显示双按钮 */
    public void showAllButton(){
        this.isShowAllButton = true;
    }

    /** 显示单个按钮 */
    public void showOnlyButton(boolean isEnterButton){
        this.isShowAllButton = false;
        this.isOnlyEnterButton = isEnterButton;
    }

    /** 设置无标题 */
    public void setNoTitle(){
        this.isNoTitle = true;
    }

    /** 设置标题 */
    public void setTitle(String title){
        this.titleValue = title;
    }

    /** 设置标题 */
    public void setTvTitle(int resourcesId){
        this.titleValue = getContext().getResources().getString(resourcesId);
    }

    /** 设置内容 */
    public void setMessage(String message){
        this.msgValue = message;
    }

    /** 设置内容 */
    public void setMessage(int resourcesId){
        this.msgValue = getContext().getResources().getString(resourcesId);
    }

    /** 设置取消按钮内容 */
    public void setCancelText(String cancelText){
        this.cancelValue = cancelText;
    }

    /** 设置取消按钮内容 */
    public void setCancelText(int resourcesId){
        this.cancelValue = getContext().getResources().getString(resourcesId);
    }

    /** 设置确定按钮内容 */
    public void setEnterText(String enterText){
        this.enterValue = enterText;
    }

    /** 设置确定按钮内容 */
    public void setEnterText(int resourcesId){
        this.enterValue = getContext().getResources().getString(resourcesId);
    }

    public void setOnRemindDialogClickListener(OnRemindDialogClickListener listener){
        this.listener = listener;
    }

    public interface OnRemindDialogClickListener{

        void remindDialogClick(boolean isEnterButton);
    }
}
