package com.kapp.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.kapp.library.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13 0013.
 * MixedTextDrawView管理类，类似于RadioGroup功能
 */
public class MixedGroup extends LinearLayout {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    private List<MixedTextDrawView> arrayList = new ArrayList<>();
    private OnMixedGroupItemClickListener listener;

    public MixedGroup(Context context) {
        super(context);
    }

    public MixedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MixedGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        logger.test_i("onAttachedToWindow Child Num : ", String.valueOf(getChildCount()));
        for (int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            if (view != null && view instanceof MixedTextDrawView){
                arrayList.add((MixedTextDrawView)view);
            }
        }
        init();
    }

    private void init(){
        if (arrayList.size() == 0)
            return;

        for (MixedTextDrawView view : arrayList){
            view.setOnClickListener(new MixedGroupListener());
        }
    }

    private class MixedGroupListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            for (MixedTextDrawView view : arrayList) {
                boolean flag = v.getId() == view.getId();
                if (flag != view.isChecked())
                    view.notifyMixedTextDraw(flag);
                if (flag && listener != null)
                    listener.mixedItemClick(view, view.getId());
            }
        }
    }

    public void setOnMixedGroupItemClickListener(OnMixedGroupItemClickListener listener){
        this.listener = listener;
    }

    public interface OnMixedGroupItemClickListener{

        void mixedItemClick(MixedTextDrawView view, int layoutId);
    }

    public MixedTextDrawView getMixedView(int position){
        if (position >= 0 && position < arrayList.size())
            return arrayList.get(position);
        return null;
    }

}
