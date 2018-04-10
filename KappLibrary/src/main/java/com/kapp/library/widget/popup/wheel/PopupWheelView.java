package com.kapp.library.widget.popup.wheel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.popup.BasePopup;
import com.kapp.library.utils.ToastUtils;
import com.kapp.library.widget.wheel.easy.BaseEasyPickerInfo;
import com.kapp.library.widget.wheel.easy.EasyPickerView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class PopupWheelView<T extends BaseEasyPickerInfo> extends BasePopup {

    private TextView tvTitle;
    private EasyPickerView<T> pickerView;
    private List<T> arrayList;
    private OnWheelItemChangeListener<T> listener;
    private int currentItem = 0;//选中位置

    public PopupWheelView(Activity context, final List<T> arrayList) {
        super(context);

        this.arrayList = arrayList;

        tvTitle = (TextView) getView().findViewById(R.id.tvTitle);
        pickerView = (EasyPickerView<T>) getView().findViewById(R.id.picker_view);

        pickerView.setDataList(this.arrayList);

        pickerView.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {

            }
        });

        setClosePopupListener(getView().findViewById(R.id.btnCancel));

        getView().findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem = pickerView.getCurIndex();
                if (listener != null){
                    listener.onWheelItemChange(currentItem, arrayList.get(currentItem));
                }
                dismissPopup();
            }
        });
    }

    @Override
    public View setContextView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.popup_wheel_view, null);
    }

    @Override
    public void setPopupAttrs(PopupWindow popupWindow) {

    }

    public void setOnWheelItemChangeListener(OnWheelItemChangeListener<T> listener){
        this.listener = listener;
    }

    public interface OnWheelItemChangeListener<T>{

        void onWheelItemChange(int position, T info);
    }

    @Override
    public void showPopup(int viewId) {
        super.showPopup(viewId);
        if (pickerView != null && currentItem > 0)
            pickerView.moveTo(currentItem);
    }
}
