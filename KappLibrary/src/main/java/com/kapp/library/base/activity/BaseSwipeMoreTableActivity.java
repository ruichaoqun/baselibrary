package com.kapp.library.base.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.kapp.library.refresh.api.RefreshLayout;
import com.kapp.library.refresh.footer.ClassicsFooter;
import com.kapp.library.refresh.listener.OnRefreshLoadmoreListener;

/**
 * Created by Administrator on 2016/12/10 0010.
 * 服务于下拉刷新及上拉刷新需求
 *
 * https://github.com/Chanven/CommonPullToRefresh
 */
public abstract class BaseSwipeMoreTableActivity<T extends Object> extends BaseSwipeTableActivity<T> {

    public void bindSwipeMoreRecycler(int recyclerId, RecyclerView.Adapter adapter){
        bindSwipeMoreRecycler(recyclerId, new LinearLayoutManager(this), adapter);
    }

    public void bindSwipeMoreRecycler(int recyclerId, RecyclerView.LayoutManager lm, RecyclerView.Adapter adapter){
        super.bindSwipeRecycler(recyclerId, lm, adapter);

        if (this.refreshLayout !=null) {
            this.refreshLayout.setEnableLoadmore(true);
            this.refreshLayout.setRefreshFooter(new ClassicsFooter(this));
            this.refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    BaseSwipeMoreTableActivity.this.loadMore();
                }

                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    BaseSwipeMoreTableActivity.this.onRefresh();
                }
            });
        }else{
            Log.w("BaseSwipeMoreTable", "You must implement it first 'bindRefreshLayout()' mothed !");
        }
    }

    public abstract void loadMore();

    @Override
    public void startRefresh() {
        super.startRefresh();
    }

    @Override
    public void stopRefresh() {
       super.stopRefresh();
        if (this.refreshLayout != null){
            this.refreshLayout.finishLoadmore();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        setLoadMoreState(arrayList.size() > 0);
    }

    public void setLoadMoreState(boolean isLoadMore){
        if (this.refreshLayout != null) {
            this.refreshLayout.setEnableLoadmore(isLoadMore);
        }
    }
}
