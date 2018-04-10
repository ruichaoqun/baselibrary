package com.kapp.library.widget.imagepicker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kapp.library.R;
import com.kapp.library.widget.imagepicker.AndroidImagePicker;
import com.kapp.library.widget.imagepicker.GlideImgLoader;
import com.kapp.library.widget.imagepicker.ImgLoader;
import com.kapp.library.widget.imagepicker.bean.ImageItem;
import com.kapp.library.widget.imagepicker.widget.TouchImageView;
import com.kapp.library.widget.pictureimage.PictureImageView;

import java.util.List;

public class ImagePreviewFragment extends Fragment {
    private static final String TAG = ImagePreviewFragment.class.getSimpleName();

    Activity mContext;

    ViewPager mViewPager;
    TouchImageAdapter mAdapter ;

    List<ImageItem> mImageList;
    private int mCurrentItemPosition = 0;

    private boolean enableSingleTap = true;//singleTap to do something

    ImgLoader mImagePresenter;//interface to load image,you can implements it with your own code
    AndroidImagePicker androidImagePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        //mSelectedImages = new SparseArray<>();
        androidImagePicker = AndroidImagePicker.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_preview,null);
        mImageList = androidImagePicker.getImageItemsOfCurrentImageSet();
        mCurrentItemPosition = getArguments().getInt(AndroidImagePicker.KEY_PIC_SELECTED_POSITION,0);
        mImagePresenter = new GlideImgLoader();
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mViewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        mAdapter = new TouchImageAdapter(((FragmentActivity)mContext).getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentItemPosition, false);
        ImageItem item = mImageList.get(mCurrentItemPosition);
        if(mContext instanceof OnImagePageSelectedListener){
            boolean isSelected = false;
            if(androidImagePicker.isSelect(mCurrentItemPosition,item) ){
                isSelected = true;
            }
            ((OnImagePageSelectedListener)mContext).onImagePageSelected(mCurrentItemPosition, mImageList.get(mCurrentItemPosition), isSelected);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
                if(mContext instanceof OnImagePageSelectedListener){
                    boolean isSelected = false;
                    ImageItem item = mImageList.get(mCurrentItemPosition);
                    if(androidImagePicker.isSelect(position,item)  ){
                        isSelected = true;
                    }
                    ((OnImagePageSelectedListener)mContext).onImagePageSelected(mCurrentItemPosition,item,isSelected);
                }
            }
            @Override public void onPageScrollStateChanged(int state) { }

        });

    }

    /**
     * public method:select the current show image
     */
    public void selectCurrent(boolean isCheck){
        ImageItem item = mImageList.get(mCurrentItemPosition);
        boolean isSelect = androidImagePicker.isSelect(mCurrentItemPosition,item);
        if(isCheck){
            if(!isSelect){
                AndroidImagePicker.getInstance().addSelectedImageItem(mCurrentItemPosition,item);
            }
        }else{
            if(isSelect){
                AndroidImagePicker.getInstance().deleteSelectedImageItem(mCurrentItemPosition, item);
            }
        }

    }

    class TouchImageAdapter extends FragmentStatePagerAdapter {
        public TouchImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Fragment getItem(int position) {
            SinglePreviewFragment fragment = new SinglePreviewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SinglePreviewFragment.KEY_URL, mImageList.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }

    }

    @SuppressLint("ValidFragment")
    private class SinglePreviewFragment extends Fragment {
        public static final String KEY_URL = "key_url";
        private TouchImageView imageView;
//        private PictureImageView imageView;
        private String url;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();

            ImageItem imageItem = (ImageItem) bundle.getSerializable(KEY_URL);

            url = imageItem.path;

            Log.i(TAG, "=====current show image path:" + url);

            imageView = new TouchImageView(mContext);
//            imageView = new PictureImageView(mContext);
            imageView.setBackgroundColor(0xff000000);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);

            imageView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if (enableSingleTap) {
                        if(mContext instanceof OnImageSingleTapClickListener){
                            ((OnImageSingleTapClickListener)mContext).onImageSingleTap(e);
                        }
                    }
                    return false;
                }
                @Override public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
                @Override public boolean onDoubleTap(MotionEvent e) {
                    return false;
                }

            });

            imageView.loadImage(url, R.mipmap.img_default_square);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            return imageView;
        }

    }


    /**
     * Interface for SingleTap Watching
     */
    public interface OnImageSingleTapClickListener{
        void onImageSingleTap(MotionEvent e);
    }

    /**
     * Interface for swipe page watching,you can get the current item,item position and whether the item is selected
     */
    public interface OnImagePageSelectedListener {
        void onImagePageSelected(int position, ImageItem item, boolean isSelected);
    }

}
