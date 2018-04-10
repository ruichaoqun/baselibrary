package com.kapp.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.kapp.library.refresh.SmartRefreshLayout;
import com.kapp.library.refresh.api.DefaultRefreshHeaderCreater;
import com.kapp.library.refresh.api.RefreshHeader;
import com.kapp.library.refresh.api.RefreshLayout;
import com.kapp.library.refresh.constant.SpinnerStyle;
import com.kapp.library.refresh.header.ClassicsHeader;
import com.kapp.library.refresh.header.MaterialHeader;
import com.umengs.library.UMManagerAPI;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public abstract class KAPPApplication extends MultiDexApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        KAPPApplication.this.context = getApplicationContext();
        UMManagerAPI.getInstances().initShareToken(getContext());

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    public static Context getContext(){
        return context;
    }
}
