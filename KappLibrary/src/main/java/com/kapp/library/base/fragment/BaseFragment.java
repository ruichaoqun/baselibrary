package com.kapp.library.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kapp.library.utils.Logger;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public abstract class BaseFragment extends Fragment {

    protected Logger logger = new Logger(this.getClass().getSimpleName());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public abstract int getLayoutId();

    public String getViewStr(TextView textView){
        return textView.getText().toString().trim();
    }

    public int getResColor(int colorId){
        return ContextCompat.getColor(getContext(), colorId);
    }
}
