package com.kapp.library.base.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kapp.library.base.activity.BaseSwipeActivity;
import com.kapp.library.base.adapter.BaseRecyclerAdapter;
import com.kapp.library.base.adapter.BaseRecyclerSideAdapter;
import com.kapp.library.widget.sidebar.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19 0019.
 *
 * 右边A~Z，中文排序效果
 */

public abstract class BaseSwipeTableSideFragment<T> extends BaseSwipeFragment implements  BaseRecyclerAdapter.OnItemClickListener, BaseRecyclerAdapter.OnItemLongClickListener{

    private SideBar sideBar;protected RecyclerView recyclerView;
    protected List<T> arrayList = new ArrayList<>();
    protected BaseRecyclerSideAdapter adapter;

    public RecyclerView bindSwipeRecycler(int recyclerId, BaseRecyclerSideAdapter adapter){
        return bindSwipeRecycler(recyclerId, new LinearLayoutManager(getContext()), adapter);
    }

    public RecyclerView bindSwipeRecycler(int recyclerId, RecyclerView.LayoutManager lm, BaseRecyclerSideAdapter adapter){
        recyclerView = (RecyclerView) getView().findViewById(recyclerId);

        this.adapter = adapter;
        if (adapter instanceof BaseRecyclerAdapter) {
            ((BaseRecyclerAdapter) adapter).setOnItemClickListener(this);
            ((BaseRecyclerAdapter) adapter).setOnItemLongClickListener(this);
        }
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    /** 绑定SideBar （显示大字母View）*/
    public void bindSideView(int resourcesId, int showDialogId){
        bindSideView((SideBar) getView().findViewById(resourcesId), (TextView) getView().findViewById(showDialogId));
    }

    /** 绑定SideBar （显示大字母View）*/
    public void bindSideView(SideBar sideBar, TextView showDialogView){
        this.sideBar = sideBar;
        this.sideBar.setTextView(showDialogView);
        // 设置右侧[A-Z]快速导航栏触摸监听
        this.sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recyclerView.scrollToPosition(position);
                }
            }
        });
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
