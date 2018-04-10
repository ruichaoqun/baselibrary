package com.kapp.library.base.adapter.pager;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.kapp.library.R;
import com.kapp.library.utils.Logger;
import com.kapp.library.widget.pictureimage.PictureImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ViewPagerAsyncAdapter<T extends View, S extends Object> extends PagerAdapter {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    private List<T> arrayList;
    private List<S> dataList;

    public ViewPagerAsyncAdapter(List<T> arrayList, List<S> dataList) {
        super();
        this.arrayList = arrayList;
        this.dataList = dataList;
    }

    // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T itemView = arrayList.get(position);
        if (itemView instanceof PictureImageView && dataList != null && dataList.size() > position) {
            PictureImageView piv = (PictureImageView) itemView;
            S itemData = dataList.get(position);
            if (itemData instanceof Integer) {
                piv.setImageResource(Integer.parseInt(String.valueOf(itemData)));
                container.addView(piv);
                return piv;
            } else if (itemData instanceof String) {
                String url = String.valueOf(itemData);
                logger.i(url);
                piv.loadImage(String.valueOf(itemData), R.mipmap.img_default_square);
                container.addView(piv);
                return piv;
            }
        }
        container.addView(itemView);
        return itemView;
    }

    // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(arrayList.get(position));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
