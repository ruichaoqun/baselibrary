package com.kapp.library.base.adapter;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kapp.library.KAPPApplication;
import com.kapp.library.utils.UIUtils;

/**
 * Created by Administrator on 2016/11/18 0018.
 * 用户ReceyclerView适配器Item之间的间距设置
 */
public class BaseSpaceItemDecoration extends RecyclerView.ItemDecoration {

//    private Logger logger = new Logger(this.getClass().getSimpleName());
    private int spanCount;//列数
    private int space;//行距
    private int cutSpace;//半个单位行距
    public static final int ALL_SPACE_SET = -1;//全域设置边距
    private boolean lockLength;//锁定宽高（只对大于2的多列数据支持）
    private boolean needTop;//需要显示首行顶部间距

    public BaseSpaceItemDecoration(int spanCount,int space) {
        this(spanCount, space, false);
    }

    public BaseSpaceItemDecoration(int spanCount,int space, boolean lockLength){
        this.lockLength = lockLength;
        this.spanCount = spanCount;
        this.space = UIUtils.dip2px(KAPPApplication.getContext(), space);
        this.cutSpace = this.space / 3;
    }

    public BaseSpaceItemDecoration(int spanCount,int space , boolean lockLength,boolean needTop) {
        this.lockLength = lockLength;
        this.needTop = needTop;
        this.spanCount = spanCount;
        this.space = UIUtils.dip2px(KAPPApplication.getContext(), space);
        this.cutSpace = this.space / 3;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = 0;
        outRect.top = 0;
        outRect.right = 0;
        outRect.bottom = 0;

        int num = parent.getChildAdapterPosition(view);

        if (num ==0&&needTop){
            outRect.top = space;
        }

        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if(manager instanceof LinearLayoutManager){//单列模式
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            if(linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL)
                outRect.right = space;
            else
                outRect.bottom = space;
        }

        if(manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            if (num >= 0) {
                if (lookup.getSpanSize(num) == spanCount) {//完全合并列
                    outRect.bottom = space;
                } else {//非合并列
                    //lookup.getSpanIndex(num,2) 直接判断该item在列的第几列
                    //lookup.getSpanSize(num) 判断该item是否是合并列，大于1就是合并列
                    if(lookup.getSpanIndex(num,spanCount) == 0){//左侧
                        outRect.left = 0;
                        outRect.right = cutSpace;
                        outRect.bottom = space;
                    }else if(lookup.getSpanIndex(num,spanCount) == spanCount - 1){//右侧
                        outRect.left = cutSpace;
                        outRect.right = 0;
                        outRect.bottom = space;
                    }else{//中间
                        outRect.left = cutSpace;
                        outRect.right = cutSpace;
                        outRect.bottom = space;
                    }
                }
            }
        }


//
//            int cutNum = num % spanCount;
//            if (cutNum == 0) {//最左边的列
//                outRect.left = 0;
//                outRect.right = cutSpace;
//                outRect.bottom = space;
////                logger.i(" Item Left Line ! "+outRect.toString());
//            } else if (cutNum == (spanCount - 1)) {//最右边的列
//                outRect.left = cutSpace;
//                outRect.right = 0;
//                outRect.bottom = space;
////                logger.i(" Item Right Line ! "+outRect.toString());
//            }else{//中间列
//                if (lockLength){
//                    int lockSpaceNum = cutSpace + cutSpace/2;
//                    outRect.left = lockSpaceNum;
//                    outRect.right = lockSpaceNum;
//                    outRect.bottom = space;
//                }else {
//                    outRect.left = cutSpace;
//                    outRect.right = cutSpace;
//                    outRect.bottom = space;
//                }
////                logger.i(" Item Center Line ! "+outRect.toString());
//            }
//
//        }
//        if (spanCount > 1) {//多列模式
//            if (cutNum == 0) {//最左边的列
//                outRect.left = 0;
//                outRect.right = cutSpace;
//                outRect.bottom = space;
////                logger.i(" Item Left Line ! "+outRect.toString());
//            } else if (cutNum == (spanCount - 1)) {//最右边的列
//                outRect.left = cutSpace;
//                outRect.right = space;
//                outRect.bottom = space;
////                logger.i(" Item Right Line ! "+outRect.toString());
//            }else{//中间列
//                if (lockLength){
//                    int lockSpaceNum = cutSpace + cutSpace/2;
//                    outRect.left = lockSpaceNum;
//                    outRect.right = lockSpaceNum;
//                    outRect.bottom = space;
//                }else {
//                    outRect.left = cutSpace;
//                    outRect.right = cutSpace;
//                    outRect.bottom = space;
//                }
////                logger.i(" Item Center Line ! "+outRect.toString());
//            }
//        }else{//单列只间隔底部
//            switch (spanCount){
//                case ALL_SPACE_SET:
//                    outRect.left = space;
//                    outRect.right = space;
//                    outRect.top = cutSpace;
//                    outRect.bottom = cutSpace;
//                    break;
//                default:
//                    outRect.bottom = space;
//                    break;
//            }
//        }
    }
}
