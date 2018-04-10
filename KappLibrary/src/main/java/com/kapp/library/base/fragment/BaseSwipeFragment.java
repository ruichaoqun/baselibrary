package com.kapp.library.base.fragment;

import com.kapp.library.R;
import com.kapp.library.refresh.api.RefreshLayout;
import com.kapp.library.refresh.header.EmeraldHeader;
import com.kapp.library.refresh.header.MaterialHeader;
import com.kapp.library.refresh.listener.OnRefreshListener;

/**
 * Created by Administrator on 2016/10/11 0011.
 *
 */
public abstract class BaseSwipeFragment extends BaseFragment {

    protected RefreshLayout refreshLayout;

    public RefreshLayout bindRefreshLayout(int refreshLayoutId){
        if (getView() != null)
           return bindRefreshLayout((RefreshLayout) getView().findViewById(refreshLayoutId));
        return null;
    }

    public RefreshLayout bindRefreshLayout(RefreshLayout refreshLayout){
        this.refreshLayout = refreshLayout;
        this.refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        this.refreshLayout.setEnableHeaderTranslationContent(true);
        this.refreshLayout.setEnableLoadmore(false);
        this.refreshLayout.setPrimaryColorsId(R.color.color_theme, android.R.color.white);

        this.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                BaseSwipeFragment.this.onRefresh();
            }
        });
        return this.refreshLayout;
    }

    public abstract void onRefresh();

    /** 开启刷新 */
    public void startRefresh(){
        if (refreshLayout != null){
            refreshLayout.autoRefresh();
        }
    }

    /** 关闭刷新 */
    public void stopRefresh(){
        if (this.refreshLayout != null)
            this.refreshLayout.finishRefresh();
    }
}
