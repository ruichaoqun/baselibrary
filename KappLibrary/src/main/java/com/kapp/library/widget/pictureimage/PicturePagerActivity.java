package com.kapp.library.widget.pictureimage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.base.activity.BaseActivity;
import com.kapp.library.base.adapter.pager.ViewPagerAsyncAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 * 一组图片查看
 */
public class PicturePagerActivity extends BaseActivity {

    private TextView tvTitle;
    private ViewPager vp;
    private PicturePagerInfo info;
    private List<PictureImageView> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_pager_view);

        info = getIntent().getParcelableExtra("info");
        if (info == null || (info.getImageResList() == null && info.getImagesList() == null)) {
            finish();
            return;
        }

        tvTitle = (TextView) findViewById(R.id.title_tv);
        vp = (ViewPager) findViewById(R.id.view_pager);

        int count = 0;
        if (info.getImagesList() != null) {
            count = info.getImagesList().size();
            if (!TextUtils.isEmpty(info.getHostUrl())) {
                for (int i = 0; i < count; i++) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(info.getHostUrl());
                    sb.append(info.getImagesList().get(i));
                    info.getImagesList().set(i, sb.toString());
                }
            }
            formatPictureList(count);
            ViewPagerAsyncAdapter<PictureImageView, String> adapter = new ViewPagerAsyncAdapter<>(arrayList, info.getImagesList());
            vp.setAdapter(adapter);
        } else if (info.getImageResList() != null) {
            count = info.getImageResList().size();
            formatPictureList(count);
            ViewPagerAsyncAdapter<PictureImageView, Integer> adapter = new ViewPagerAsyncAdapter<>(arrayList, info.getImageResList());
            vp.setAdapter(adapter);
        }
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (info.getTitleList() != null && info.getTitleList().size() > position) {
                    tvTitle.setText(info.getTitleList().get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp.setCurrentItem(info.getCurrentItem());
        if (info.getTitleList() != null && info.getTitleList().size() > info.getCurrentItem()) {
            tvTitle.setText(info.getTitleList().get(info.getCurrentItem()));
        }
    }

    private void formatPictureList(int count) {
        arrayList.clear();
        for (int i = 0; i < count; i++) {
            PictureImageView piv = new PictureImageView(this);
            piv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            piv.setScaleType(ImageView.ScaleType.MATRIX);
            arrayList.add(piv);
        }
    }

    /**
     * Xml Click
     */
    public void onClick(View view) {
        finish();
    }
}
