package com.umengs.library;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umengs.library.share.AuthUtils;
import com.umengs.library.share.ShareUtils;

/**
 * Created by Administrator on 2016/10/9 0009.
 */

public class UMManagerAPI implements UMShareListener{

    private static UMManagerAPI shareManager = null;
    private UMShareAPI umShareAPI;
    private Context context;
    public static final int SHARE_TYPE_QQ = 1;
    public static final int SHARE_TYPE_QQ_ZONE = 2;
    public static final int SHARE_TYPE_WX = 3;
    public static final int SHARE_TYPE_WX_CIRCLE = 4;
    public static final int SHARE_TYPE_WB = 5;
    private ShareUtils shareUtils;
    private AuthUtils authUtils;

    public static UMManagerAPI getInstances(){
        if (shareManager == null)
            shareManager = new UMManagerAPI();
        return shareManager;
    }

    /** Application OnCreate 初始化 */
    public void initShareToken(Context context){
        this.context = context;
        PlatformConfig.setQQZone("1106573559", "dwgxl9IjcJIitJap");
        PlatformConfig.setWeixin("wxfa5dae30ee6e17f1", "9ae11e6c36844089f7fb42103bb654b2");
        PlatformConfig.setSinaWeibo("4258288482", "49185f25da7962fefada07dc6f09d0bf");
        Config.REDIRECT_URL = "https://www.baidu.com";
        umShareAPI = UMShareAPI.get(context);
    }

    /** 分享实体 */
    public ShareUtils getShareUtils(Context context){
        return getShareUtils(context, this);
    }

    /** 分享实体 */
    public ShareUtils getShareUtils(Context context, UMShareListener listener){

        if (umShareAPI == null)
            umShareAPI = UMShareAPI.get(context);

        if (shareUtils == null)
            shareUtils = new ShareUtils(umShareAPI, listener);
        return shareUtils;
    }

    /** 分享界面回调 */
    public void setResult(int requestCode, int resultCode, Intent data){
        if (umShareAPI != null)
            umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /** 授权实体 */
    public AuthUtils getAuthUtils(){
        if (umShareAPI == null)
            umShareAPI = UMShareAPI.get(context);

        if (authUtils == null)
            authUtils = new AuthUtils(umShareAPI);
        return authUtils;
    }

    /******************************************* 分享回调 ******************************************/
    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toast.makeText(context, "分享成功！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        Toast.makeText(context, "分享失败！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Toast.makeText(context, "分享取消！！", Toast.LENGTH_SHORT).show();
    }

}
