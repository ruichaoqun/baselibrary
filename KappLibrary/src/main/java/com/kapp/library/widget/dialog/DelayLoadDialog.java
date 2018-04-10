package com.kapp.library.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.widget.NetImageView;

/**
 * Created by Administrator on 2016/11/26 0026.
 * 延迟加载体验框
 */
public class DelayLoadDialog extends ProgressDialog{

    private NetImageView niv;
    private TextView tv;
    private String message = "正在加载...";
    private int imageId = R.mipmap.loading;

    public DelayLoadDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deley_load_dialog);
        setCanceledOnTouchOutside(false);//按对话框以外的地方不起作用。按返回键还起作用
//        setCancelable(false);//按对话框以外的地方不起作用。按返回键也不起作用

        niv = (NetImageView) findViewById(R.id.niv);
        tv = (TextView) findViewById(R.id.tv);

        niv.loadImage(imageId, imageId, imageId,0 ,0 ,true);
        tv.setText(message);
    }

    /** 设置加载内容 */
    public void setMessage(String message){
        this.message = message;
        if(tv != null)
            tv.setText(message);
    }

    /** 设置加载内容 */
    public void setMessage(int resourcesId){
        this.message = getContext().getResources().getString(resourcesId);
    }

    /** 设置图片资源 */
    public void setImageLoadRources(int imageId){
        this.imageId = imageId;
    }

}
