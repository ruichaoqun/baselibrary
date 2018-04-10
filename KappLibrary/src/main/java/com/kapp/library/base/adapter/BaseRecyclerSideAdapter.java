package com.kapp.library.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.kapp.library.widget.sidebar.BaseSideInfo;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public abstract class BaseRecyclerSideAdapter<T extends RecyclerView.ViewHolder, S extends BaseSideInfo> extends BaseRecyclerAdapter<T, S>
        implements SectionIndexer {

    public BaseRecyclerSideAdapter(Context context, S[] arrays){
        super(context, arrays);
    }

    public BaseRecyclerSideAdapter(Context context, List<S> arrayList){
        super(context, arrayList);
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = getArrayList().get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return getArrayList().get(position).getSortLetters().charAt(0);
    }

    public void setCatalogView(TextView tvLetter, int position, BaseSideInfo info){
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText(info.getSortLetters());
        } else {
            tvLetter.setVisibility(View.GONE);
        }
    }
}
