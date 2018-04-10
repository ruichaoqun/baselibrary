package com.kapp.library.widget.imagepicker.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.widget.imagepicker.AndroidImagePicker;
import com.kapp.library.widget.imagepicker.bean.ImageItem;
import com.kapp.library.widget.imagepicker.ui.ImagesGridFragment;

public class ImagesGridActivity extends FragmentActivity implements View.OnClickListener,AndroidImagePicker.OnImageSelectedChangeListener {
    private static final String TAG = ImagesGridActivity.class.getSimpleName();

    private TextView mBtnOk;

    ImagesGridFragment mFragment;
    AndroidImagePicker androidImagePicker;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_grid);

        androidImagePicker = AndroidImagePicker.getInstance();
        androidImagePicker.clearSelectedImages();//most of the time you need to clear the last selected images or you can comment out this line

        mBtnOk = (TextView) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE){
            mBtnOk.setVisibility(View.GONE);
        }else{
            mBtnOk.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.btn_backpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //final boolean isCrop = getIntent().getBooleanExtra("isCrop",false);
        final boolean isCrop = androidImagePicker.cropMode;
        imagePath = getIntent().getStringExtra(AndroidImagePicker.KEY_PIC_PATH);
        mFragment = new ImagesGridFragment();
        /*Bundle data = new Bundle();
        data.putString(AndroidImagePicker.KEY_PIC_PATH,imagePath);
        mFragment.setArguments(data);*/

        mFragment.setOnImageItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position = androidImagePicker.isShouldShowCamera() ? position-1 : position;

                if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_MULTI){
                    go2Preview(position);
                }else if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE){
                    if(isCrop){
                        Intent intent = new Intent();
                        intent.setClass(ImagesGridActivity.this,ImageCropActivity.class);
                        intent.putExtra(AndroidImagePicker.KEY_PIC_PATH,androidImagePicker.getImageItemsOfCurrentImageSet().get(position).path);
                        startActivity(intent);
                    }else{
                        androidImagePicker.clearSelectedImages();
                        androidImagePicker.addSelectedImageItem(position, androidImagePicker.getImageItemsOfCurrentImageSet().get(position));
                        setResult(RESULT_OK);

                        finish();
                        androidImagePicker.notifyOnImagePickComplete();
                    }

                }

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();

        androidImagePicker.addOnImageSelectedChangeListener(this);

        int selectedCount = androidImagePicker.getSelectImageCount();
        onImageSelectChange(0, null, selectedCount, androidImagePicker.getSelectLimit());

    }

    /**
     * 预览页面
     * @param position
     */
    private void go2Preview(int position) {
        Intent intent = new Intent();
        intent.putExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, position);
        intent.setClass(ImagesGridActivity.this, ImagePreviewActivity.class);
        startActivityForResult(intent, AndroidImagePicker.REQ_PREVIEW);
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_ok){
            finish();
            androidImagePicker.notifyOnImagePickComplete();
        }else if(v.getId() == R.id.btn_pic_rechoose){
            finish();
        }

    }


    @Override
    public void onImageSelectChange(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit) {
        if(selectedItemsCount > 0){
            mBtnOk.setEnabled(true);
            //mBtnOk.setText("完成("+selectedItemsCount+"/"+maxSelectLimit+")");
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
        androidImagePicker.clearImageSets();
        Log.i(TAG, "=====removeOnImageItemSelectedChangeListener");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == Activity.RESULT_OK){

           if(requestCode == AndroidImagePicker.REQ_PREVIEW){
                setResult(RESULT_OK);
                finish();
                androidImagePicker.notifyOnImagePickComplete();
            }

        }

    }


}
