package com.kapp.library.base.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kapp.library.base.adapter.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13 0013.
 * SwipeTable基类去除上拉加载功能，作为普通的列表展示框体使用
 */
public abstract class BaseSwipeTableActivity<T extends Object> extends BaseSwipeActivity implements BaseRecyclerAdapter.OnItemClickListener, BaseRecyclerAdapter.OnItemLongClickListener {

    protected RecyclerView recyclerView;
    protected List<T> arrayList = new ArrayList<>();
    protected RecyclerView.Adapter adapter;

    public RecyclerView bindSwipeRecycler(int recyclerId, RecyclerView.Adapter adapter){
        return bindSwipeRecycler(recyclerId, new LinearLayoutManager(this), adapter);
    }

    public RecyclerView bindSwipeRecycler(int recyclerId, RecyclerView.LayoutManager lm, RecyclerView.Adapter adapter){
        recyclerView = (RecyclerView) findViewById(recyclerId);

        this.adapter = adapter;
        if (adapter instanceof BaseRecyclerAdapter) {
            ((BaseRecyclerAdapter) adapter).setOnItemClickListener(this);
            ((BaseRecyclerAdapter) adapter).setOnItemLongClickListener(this);
        }
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {

    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position) {

    }

    /** 刷新适配器 */
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

}
