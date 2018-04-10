package com.kapp.library.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2016/10/21 0021.
 * RecyclerView.Adapter封装基类
 */
public abstract class BaseRecyclerAdapter<T extends RecyclerView.ViewHolder, S extends Object> extends RecyclerView.Adapter<T> {

    private Context context;
    private List<S> arrayList;

    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerAdapter(Context context, S[] arrays){
        this.context = context;
        arrayList = Arrays.asList(arrays);
    }

    public BaseRecyclerAdapter(Context context, List<S> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(BaseRecyclerAdapter.this.getLayoutId(), parent, false);
        return BaseRecyclerAdapter.this.createNewHolder(itemView);
    }

    public abstract int getLayoutId();
    public abstract T createNewHolder(View itemView);

    @Override
    public void onBindViewHolder(final T holder, final int position) {
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder, v, position);
                }
            });
        }

        if (onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(holder, v, position);
                    return false;
                }
            });
        }
    }

    public List<S> getArrayList() {
        return arrayList;
    }

    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        return arrayList.size();
    }

    public S getItem(int position){
        if (arrayList != null && arrayList.size() > position)
            return arrayList.get(position);
        return null;
    }

    public Context getContext(){
        return context;
    }

    public Activity getActivity(){
        if (context instanceof Activity)
            return (Activity)context;
        return null;
    }

    public void setItem(S info, int position){
        arrayList.set(position, info);
        notifyDataSetChanged();
    }

    public int getColor(int colorId){
        return ContextCompat.getColor(context, colorId);
    }

    public String getString(int stringId){
        return context.getResources().getString(stringId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemClicklongListener) {
        this.onItemLongClickListener = onItemClicklongListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }
}
