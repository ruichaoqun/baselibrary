package com.umengs.library.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.SocializeUtils;
import com.umengs.library.UMManagerAPI;

import java.io.File;

/**
 * Created by Administrator on 2016/10/9 0009.
 * 分享
 * Log.LOG ＝ false; 关闭Log
 * Config.IsToastTip = false; 关闭Toast
 * Config.dialogSwitch = false; 不使用默认的Dialog
 */
public class ShareUtils {

    private UMShareAPI umShareAPI;
    private UMShareListener listener;
    private final int SCALE_SIZE = 24 * 1024;//设置链接和图片同时分享的时候,一般缩略图最好在24k左右

    public ShareUtils(UMShareAPI umShareAPI, UMShareListener listener) {
        this.umShareAPI = umShareAPI;
        this.listener = listener;
    }

    public void setListener(UMShareListener listener) {
        this.listener = listener;
    }

    /**
     * 带标题的图片分享
     */
    public void openImage(Activity context, String title, Object image) {
        openImage(context, title, null, image);
    }

    /**
     * 带标题、内容的图片分享
     */
    public void openImage(Activity context, String title, String content, Object image) {

        ShareAction shareAction = new ShareAction(context);
        shareAction.withTitle(title);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
        else
            shareAction.withText(title);
        shareAction.withMedia(getUMImage(context, image));
        shareAction.setCallback(listener);
        shareAction.open();
    }

    /**
     * 带标题分享链接
     */
    public void openUrl(Activity context, String title, String shareUrl) {
        openUrl(context, title, null, shareUrl);
    }

    /**
     * 带标题、内容的分享链接
     */
    public void openUrl(Activity context, String title, String content, String shareUrl) {

        ShareAction shareAction = new ShareAction(context);
        shareAction.withTitle(title);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
        else
            shareAction.withText(title);
        shareAction.withTargetUrl(shareUrl);
        shareAction.setCallback(listener);
        shareAction.open();
    }

    /**
     * 带标题的分享链接带图
     */
    public void openUrlAsImage(Activity context, String title, String shareUrl, Object image) {
        openUrlAsImage(context, title, null, shareUrl, image);
    }

    /**
     * 带标题、内容的分享链接带图
     */
    public void openUrlAsImage(Activity context, String title, String content, String shareUrl, Object image) {

        ShareAction shareAction = new ShareAction(context);
        shareAction.withTitle(title);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
        else
            shareAction.withText(title);
        shareAction.withTargetUrl(shareUrl);
        shareAction.withMedia(getUMImage(context, image, true));
        shareAction.setCallback(listener);
        shareAction.open();
    }

    /**
     * 带标题的图片分享
     */
    public void shareImage(Activity context, String title, Object image, int shareType) {
        shareImage(context, title, null, image, shareType);
    }

    /**
     * 带标题、内容的图片分享
     */
    public void shareImage(Activity context, String title, String content, Object image, int shareType) {
        CheckStateInfo info = checkThirdState(context, shareType);
        if (!info.isState) {
            Toast.makeText(context, info.error, Toast.LENGTH_SHORT).show();
            return;
        }

        SHARE_MEDIA share_media = getPlatform(shareType);
        if (share_media == null) {
            Toast.makeText(context, "请设置正确分享渠道！！", Toast.LENGTH_SHORT).show();
            return;
        }

        ShareAction shareAction = new ShareAction(context);
        shareAction.withTitle(title);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
        else
            shareAction.withText(title);
        shareAction.withMedia(getUMImage(context, image));
        shareAction.setPlatform(share_media);
        shareAction.setCallback(listener);
        shareAction.share();
    }

    /**
     * 带标题分享链接
     */
    public void shareUrl(Activity context, String title, String shareUrl, int shareType) {
        shareUrl(context, title, null, shareUrl, shareType);
    }

    /**
     * 带标题、内容的分享链接
     */
    public void shareUrl(Activity context, String title, String content, String shareUrl, int shareType) {
        CheckStateInfo info = checkThirdState(context, shareType);
        if (!info.isState) {
            Toast.makeText(context, info.error, Toast.LENGTH_SHORT).show();
            return;
        }

        SHARE_MEDIA share_media = getPlatform(shareType);
        if (share_media == null) {
            Toast.makeText(context, "请设置正确分享渠道！！", Toast.LENGTH_SHORT).show();
            return;
        }

        ShareAction shareAction = new ShareAction(context);
        shareAction.withTitle(title);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
        else
            shareAction.withText(title);
        shareAction.withTargetUrl(shareUrl);
        shareAction.setPlatform(share_media);
        shareAction.setCallback(listener);
        shareAction.share();
    }

    /**
     * 带标题的分享链接带图
     */
    public void shareUrlAsImage(Activity context, String title, String shareUrl, Object image, int shareType) {
        shareUrlAsImage(context, title, null, shareUrl, image, shareType);
    }

    /**
     * 带标题、内容的分享链接带图
     */
    public void shareUrlAsImage(Activity context, String title, String content, String shareUrl, Object image, int shareType) {
        CheckStateInfo info = checkThirdState(context, shareType);
        if (!info.isState) {
            Toast.makeText(context, info.error, Toast.LENGTH_SHORT).show();
            if (listener != null)
                listener.onError(null, null);
            return;
        }

        SHARE_MEDIA share_media = getPlatform(shareType);
        if (share_media == null) {
            Toast.makeText(context, "请设置正确分享渠道！！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image instanceof String && TextUtils.isEmpty(String.valueOf(image))) {
            // 图片路径为空， 会分享失败。使用logo的url图片地址
            image = "https://mmbiz.qlogo.cn/mmbiz_png/zc2Z2YG7q4VTlezTShZ9JtOyfhWMQSpU0mdsicvhYw1ib20aBaxdQ4lnShT4J1rGsMySYgwnrBmNbh9fcQwPCInw/0?wx_fmt=png";
        }

        ShareAction shareAction = new ShareAction(context);
        shareAction.withTitle(title);
        if (!TextUtils.isEmpty(content))
            shareAction.withText(content);
        else
            shareAction.withText(title);
        shareAction.withTargetUrl(shareUrl);
        shareAction.withMedia(getUMImage(context, image, true));
        shareAction.setPlatform(share_media);
        shareAction.setCallback(listener);
        shareAction.share();
    }

    /**
     * 检查第三方分享平台信息
     */
    private CheckStateInfo checkThirdState(Activity context, int shareType) {
        if (umShareAPI == null) {
            CheckStateInfo info = new CheckStateInfo();
            info.error = CheckStateInfo.ERROR_INIT_FAIL;
            return info;
        }

        switch (shareType) {
            case UMManagerAPI.SHARE_TYPE_QQ:
            case UMManagerAPI.SHARE_TYPE_QQ_ZONE:
                return getShareTypeState(context, SHARE_MEDIA.QQ);
            case UMManagerAPI.SHARE_TYPE_WX:
            case UMManagerAPI.SHARE_TYPE_WX_CIRCLE:
                return getShareTypeState(context, SHARE_MEDIA.WEIXIN);
            case UMManagerAPI.SHARE_TYPE_WB:
                return getShareTypeState(context, SHARE_MEDIA.SINA);
        }

        CheckStateInfo info = new CheckStateInfo();
        info.error = CheckStateInfo.ERROR_NORMAL;
        return info;
    }

    private CheckStateInfo getShareTypeState(Activity context, SHARE_MEDIA share_media) {
        CheckStateInfo info = new CheckStateInfo();
        boolean isInstall = umShareAPI.isInstall(context, share_media);
        if (isInstall) {
            boolean isSupport = umShareAPI.isSupport(context, share_media);
            if (isSupport)
                info.isState = true;
            else
                info.error = CheckStateInfo.ERROR_NO_SUPPERT;
        } else {
            info.error = CheckStateInfo.ERROR_NO_INSTALL;
        }
        return info;
    }

    private SHARE_MEDIA getPlatform(int shareType) {
        switch (shareType) {
            case UMManagerAPI.SHARE_TYPE_QQ:
                return SHARE_MEDIA.QQ;
            case UMManagerAPI.SHARE_TYPE_QQ_ZONE:
                return SHARE_MEDIA.QZONE;
            case UMManagerAPI.SHARE_TYPE_WX:
                return SHARE_MEDIA.WEIXIN;
            case UMManagerAPI.SHARE_TYPE_WX_CIRCLE:
                return SHARE_MEDIA.WEIXIN_CIRCLE;
            case UMManagerAPI.SHARE_TYPE_WB:
                return SHARE_MEDIA.SINA;
        }
        return null;
    }

    /**
     * 生成UMImage
     */
    private UMImage getUMImage(Context context, Object image) {
        if (image instanceof String) {
            return new UMImage(context, String.valueOf(image));
        } else if (image instanceof File) {//SocializeUtils.File2byte()
            return new UMImage(context, (File) image);
        } else if (image instanceof Integer) {
            return new UMImage(context, Integer.parseInt(String.valueOf(image)));
        } else if (image instanceof Bitmap) {//BitmapUtils.bitmap2Bytes(bitmap)
            return new UMImage(context, (Bitmap) image);
        } else if (image instanceof byte[]) {
            return new UMImage(context, (byte[]) image);
        }
        return null;
    }

    /**
     * 生成UMImage：isScale是否压缩
     */
    public UMImage getUMImage(Context context, Object image, boolean isScale) {
        if (!isScale || image instanceof String)
            return getUMImage(context, image);

        if (image instanceof File) {//SocializeUtils.File2byte()
            byte[] bytes = SocializeUtils.File2byte((File) image);
            return new UMImage(context, getScaleBitmap(bytes, SCALE_SIZE));
        } else if (image instanceof Integer) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(String.valueOf(image)));
            if (bitmap == null)
                return null;
            byte[] bytes = BitmapUtils.bitmap2Bytes(bitmap);
            return new UMImage(context, getScaleBitmap(bytes, SCALE_SIZE));
        } else if (image instanceof Bitmap) {//BitmapUtils.bitmap2Bytes(bitmap)
            byte[] bytes = BitmapUtils.bitmap2Bytes((Bitmap) image);
            return new UMImage(context, getScaleBitmap(bytes, SCALE_SIZE));
        } else if (image instanceof byte[]) {
            return new UMImage(context, getScaleBitmap((byte[]) image, SCALE_SIZE));
        }

        return null;
    }

    /**
     * 压缩图片
     */
    private byte[] getScaleBitmap(byte[] bytes, int count) {
        return BitmapUtils.compressBitmap(bytes, count);
    }
}
