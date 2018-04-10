package com.umengs.library.share;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/10 0010.
 * 授权
 */
public class AuthUtils {

    private UMShareAPI umShareAPI;
    private OnAuthStateListener listener;

    public AuthUtils(UMShareAPI umShareAPI) {
        this.umShareAPI = umShareAPI;
    }

    /**
     * 授权回调结果处理
     */
    public void onAuthResult(int requestCode, int resultCode, Intent data) {
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * QQ授权
     */
    public boolean authQQ(Activity context, OnAuthStateListener listener) {
        if (umShareAPI == null)
            return false;

        this.listener = listener;
        umShareAPI.doOauthVerify(context, SHARE_MEDIA.QQ, new AuthPlatformListener(context));
        return true;
    }

    /**
     * 微信授权
     */
    public boolean authWX(Activity context, OnAuthStateListener listener) {
        if (umShareAPI == null)
            return false;

        this.listener = listener;
        umShareAPI.doOauthVerify(context, SHARE_MEDIA.WEIXIN, new AuthPlatformListener(context));
        return true;
    }

    /**
     * 微博授权
     */
    public boolean authWB(Activity context, OnAuthStateListener listener) {
        if (umShareAPI == null)
            return false;

        this.listener = listener;
        umShareAPI.doOauthVerify(context, SHARE_MEDIA.SINA, new AuthPlatformListener(context));
        return true;
    }

    private class AuthPlatformListener implements UMAuthListener {

        private Activity context;

        public AuthPlatformListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
            if (listener == null)
                return;

            String userId = null;
            switch (share_media) {
                case QQ:
                    userId = map.get("openid");
                    break;
                case WEIXIN:
                    userId = map.get("openid");
                    break;
                case SINA:
                    userId = map.get("id");
                    break;
            }
            if (!TextUtils.isEmpty(userId)) {
                SHARE_MEDIA sm = null;
                UMShareAPI umShareAPI = UMShareAPI.get(context);
                switch (share_media) {
                    case QQ:
                        sm = SHARE_MEDIA.QQ;
                        break;
                    case WEIXIN:
                        sm = SHARE_MEDIA.WEIXIN;
                        break;
                }
                umShareAPI.getPlatformInfo(context, sm, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        ThirdUserInfo info = new ThirdUserInfo();
                        info.setId(map.get("openid"));
                        info.setNickName(map.get("screen_name"));
                        info.setHeadUrl(map.get("profile_image_url"));
                        info.setUnionid(map.get("unionid"));
                        listener.authState(true, info);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                    }
                });
            } else
                Toast.makeText(context, "获取用户ID为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
            if (listener != null)
                listener.authState(false, null);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(context, "取消授权", Toast.LENGTH_SHORT).show();
            if (listener != null)
                listener.authState(false, null);
        }
    }
}
