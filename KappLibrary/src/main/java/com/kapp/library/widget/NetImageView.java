package com.kapp.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.kapp.library.utils.Logger;

import java.io.File;

/**
 * Created by Administrator on 2016/11/3 0003.
 * 图片异步加载图片控件
 * --暂时扩展的为String路径的请求，
 * 可根据自己需要自动扩展
 */
public class NetImageView extends ImageView {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    private RequestManager requestManager;
    private ScaleType scaleType = ScaleType.FIT_XY;

    public NetImageView(Context context) {
        super(context);
        init();
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        requestManager = Glide.with(getContext());

        //低内存处理
//        requestManager.onTrimMemory(3);
//        requestManager.onLowMemory();

        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        this.scaleType = scaleType;
    }

    public void loadImage(Object object){
        loadImage(object, 0);
    }

    public void loadImage(Object object, int loadImageId){
        loadImage(object, loadImageId, 0, 0);
    }

    public void loadImage(Object object,int loadImageId, int width, int height){
        loadImage(object, loadImageId, loadImageId, width, height, false);
    }

    /**
     * 图片异步加载方法实体：
     * @param object :加载地址类型（byte[], String，resourceId, File，Uri）
     * @param loadImageId :图片加载出来显示前
     *param errorImageId 失败的展示图
     * @param width : 指定图片显示的宽度
     * @param height : 指定图片显示的高度
     * @param asGif : 是否匹配Gif支持（占用内存较大，谨慎开启）
     * Glide可以支持自动计算图片大小，图片大小作为非必须参数开放
     * */
    public void loadImage(Object object, int loadImageId, final int errorImageId, int width, int height, boolean asGif){
        if (requestManager == null || object == null ){
            logger.w(" The Object RequestManager is Null !!");
            return;
        }

        DrawableTypeRequest typeRequest = null;
        if (object instanceof byte[]){
            byte[] bytes = (byte[]) object;
            typeRequest = requestManager.load(bytes);
        }else if (object instanceof File){
            File file = (File) object;
            typeRequest = requestManager.load(file);
        }else if (object instanceof Integer){
            int imageId = Integer.parseInt(String.valueOf(object));
            if (imageId > 0)
                typeRequest = requestManager.load(imageId);
            else {
                logger.w(" The image resources id is error !");
                return;
            }
        }else if (object instanceof String){
            String imageUrl = String.valueOf(object);
            typeRequest = requestManager.load(imageUrl);
        }else if (object instanceof Uri){
            Uri uri = (Uri) object;
            typeRequest = requestManager.load(uri);
        }

        if (typeRequest == null){
            logger.w(" The Image Load Object is null !!");
            return;
        }

        typeRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
        if (width > 0 && height > 0)
            typeRequest.override(width, height);
        if (errorImageId > 0)
            typeRequest.error(errorImageId);
        if (loadImageId > 0)
            typeRequest.placeholder(loadImageId);
        if (asGif)
            typeRequest.asGif();//匹配Gif
//        typeRequest.priority(Priority.HIGH)//加载优先级
//        typeRequest.centerCrop();
        typeRequest.dontAnimate();//不使用动画
        typeRequest.listener(new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                if (errorImageId > 0) {
                    NetImageView.this.setScaleType(scaleType);
                    NetImageView.this.setImageResource(errorImageId);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                NetImageView.this.setScaleType(scaleType);
                return false;
            }
        });
        typeRequest.into(this);
    }

    /**
     * 获取Bitmap
     * 支持类型：byte[], String，resourceId, File，Uri
     * */
    public static void loadBitmap(Context context, Object object, final OnLoadBitmapListener listener){
        RequestManager manager = Glide.with(context);
        DrawableTypeRequest typeRequest = null;
        if (object instanceof byte[]){
            byte[] bytes = (byte[]) object;
            typeRequest = manager.load(bytes);
        }else if (object instanceof File){
            File file = (File) object;
            typeRequest = manager.load(file);
        }else if (object instanceof Integer){
            int imageId = Integer.parseInt(String.valueOf(object));
            if (imageId > 0)
                typeRequest = manager.load(imageId);
            else {
                Log.w("LoadBitmap"," The image resources id is error !");
                return;
            }
        }else if (object instanceof String){
            String imageUrl = String.valueOf(object);
            typeRequest = manager.load(imageUrl);
        }else if (object instanceof Uri){
            Uri uri = (Uri) object;
            typeRequest = manager.load(uri);
        }

        if (typeRequest == null){
            Log.w("LoadBitmap", " The Image Load Object is null !!");
            return;
        }
        typeRequest.asBitmap();
        typeRequest.override(100,100);
        typeRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
        typeRequest.dontAnimate();//不使用动画
        typeRequest.priority(Priority.HIGH);
        typeRequest.into(new Target<Drawable>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                if (listener != null)
                    listener.loadFailed();
            }

            @Override
            public void onResourceReady(Drawable resource, GlideAnimation glideAnimation) {
                if (resource instanceof GifDrawable){
                    GifDrawable gifDrawable = (GifDrawable) resource;
                    if (listener != null)
                        listener.loadSuccess(gifDrawable.getFirstFrame());
                }else if (resource instanceof GlideDrawable) {
                    GlideDrawable glideDrawable = (GlideDrawable)resource;
                    GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable) glideDrawable.getCurrent();
                    if (listener != null)
                        listener.loadSuccess(bitmapDrawable.getBitmap());
                }else{
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                    if (listener != null)
                        listener.loadSuccess(bitmapDrawable.getBitmap());
                }
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {

            }

            @Override
            public void getSize(SizeReadyCallback cb) {

            }

            @Override
            public void setRequest(Request request) {

            }

            @Override
            public Request getRequest() {
                return null;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }
        });
    }

    public interface OnLoadBitmapListener{

        void loadSuccess(Bitmap bitmap);

        void loadFailed();
    }
}
