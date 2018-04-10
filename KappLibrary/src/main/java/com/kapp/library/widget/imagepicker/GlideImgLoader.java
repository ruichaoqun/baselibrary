package com.kapp.library.widget.imagepicker;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kapp.library.R;

import java.io.File;

/**
 * <b>desc your class</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class GlideImgLoader implements ImgLoader {

    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        Log.i("GlideImageLoader", " size : "+size);
//        if (size <= 0)
//            size = 200;
        Glide.with(imageView.getContext())
                .load(new File(imageUri))
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .override(size/4*3, size/4*3)
                .placeholder(R.mipmap.img_default_square)
                .error(R.mipmap.img_default_square)
                .into(imageView);
    }

}
