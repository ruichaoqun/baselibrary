package com.kapp.library.widget.imagepicker.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kapp.library.R;
import com.kapp.library.widget.imagepicker.AndroidImagePicker;
import com.kapp.library.widget.imagepicker.bean.ImageItem;
import com.kapp.library.widget.imagepicker.ui.ImagePreviewFragment;

import java.io.Serializable;
import java.util.List;

public class ImagePreviewActivity extends FragmentActivity implements View.OnClickListener,ImagePreviewFragment.OnImageSingleTapClickListener,ImagePreviewFragment.OnImagePageSelectedListener,AndroidImagePicker.OnImageSelectedChangeListener {
    private static final String TAG = ImagePreviewActivity.class.getSimpleName();

    ImagePreviewFragment mFragment;
    TextView mTitleCount;
    CheckBox mCbSelected;
    TextView mBtnOk;

    List<ImageItem> mImageList;
    int mShowItemPosition = 0;
    AndroidImagePicker androidImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pre);

        androidImagePicker = AndroidImagePicker.getInstance();
        androidImagePicker.addOnImageSelectedChangeListener(this);

        mImageList = AndroidImagePicker.getInstance().getImageItemsOfCurrentImageSet();
        mShowItemPosition = getIntent().getIntExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION,0);

        mBtnOk = (TextView) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        mCbSelected = (CheckBox) findViewById(R.id.btn_check);
        mTitleCount = (TextView) findViewById(R.id.tv_title_count);
        mTitleCount.setText("1/" + mImageList.size());

        int selectedCount = AndroidImagePicker.getInstance().getSelectImageCount();

        onImageSelectChange(0, null, selectedCount, androidImagePicker.getSelectLimit());

        //back press
        findViewById(R.id.btn_backpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCbSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (androidImagePicker.getSelectImageCount() > androidImagePicker.getSelectLimit()) {
                    if (mCbSelected.isChecked()) {
                        //holder.cbSelected.setCanChecked(false);
                        mCbSelected.toggle();
                        String toast = getResources().getString(R.string.you_have_a_select_limit,
                                String.valueOf(androidImagePicker.getSelectLimit()));
                        Toast.makeText(ImagePreviewActivity.this, toast, Toast.LENGTH_SHORT).show();
                    } else {
                        //
                    }
                } else {
                    //
                }
            }

        });

        mCbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFragment.selectCurrent(isChecked);
            }
        });


        mFragment = new ImagePreviewFragment();
        Bundle data = new Bundle();
        data.putSerializable(AndroidImagePicker.KEY_PIC_PATH, (Serializable) mImageList);
        data.putInt(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, mShowItemPosition);
        mFragment.setArguments(data);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ok){
            setResult(RESULT_OK);// select complete
            finish();
        }else if(v.getId() == R.id.btn_pic_rechoose){
            finish();
        }


    }


    @Override
    public void onImageSingleTap(MotionEvent e) {
        View topBar = findViewById(R.id.top_bar);
        View bottomBar = findViewById(R.id.bottom_bar);
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(ImagePreviewActivity.this, R.anim.top_out));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(ImagePreviewActivity.this, R.anim.fade_out));
            topBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(ImagePreviewActivity.this, R.anim.top_in));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(ImagePreviewActivity.this, R.anim.fade_in));
            topBar.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onImagePageSelected(int position, ImageItem item, boolean isSelected) {
        mTitleCount.setText(position + 1 + "/" + mImageList.size());
        mCbSelected.setChecked(isSelected);
    }

    @Override
    public void onImageSelectChange(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit) {
        if(selectedItemsCount > 0){
            mBtnOk.setEnabled(true);
            mBtnOk.setText(getResources().getString(R.string.select_complete,
                    String.valueOf(selectedItemsCount), String.valueOf(maxSelectLimit)));
        }else{
            mBtnOk.setText(getResources().getString(R.string.complete));
            mBtnOk.setEnabled(false);
        }
        Log.i(TAG, "=====EVENT:onImageSelectChange");
    }

    @Override
    protected void onDestroy() {
        androidImagePicker.removeOnImageItemSelectedChangeListener(this);
        Log.i(TAG, "=====removeOnImageItemSelectedChangeListener");
        super.onDestroy();
    }

}
