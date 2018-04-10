package com.kapp.library.widget.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kapp.library.R;
import com.kapp.library.popup.BasePopup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class PopupShareView extends BasePopup {

    public static final int SHARE_WX = 1;
    public static final int SHARE_WX_CIRCLE = 2;
    public static final int SHARE_QQ = 3;
    public static final int SHARE_QQ_ZONE = 4;
    public static final int SHARE_SINA = 5;

    private TextView ivOne, ivTwo, ivThree, ivFour;
    private List<Integer> shareList;
    private OnShareItemClickListener listener;

    public PopupShareView(Activity context) {
        super(context);

        ivOne = (TextView) getView().findViewById(R.id.share_one_iv);
        ivTwo = (TextView) getView().findViewById(R.id.share_two_iv);
        ivThree = (TextView) getView().findViewById(R.id.share_three_iv);
        ivFour = (TextView) getView().findViewById(R.id.share_four_iv);
        setClosePopupListener(getView().findViewById(R.id.cancel_btn));
        setClosePopupListener(getView().findViewById(R.id.cancel_tv));

        ivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.shareItemClick(shareList.get(0));
                dismissPopup();
            }
        });

        ivTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.shareItemClick(shareList.get(1));
                dismissPopup();
            }
        });

        ivThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.shareItemClick(shareList.get(2));
                dismissPopup();
            }
        });

        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.shareItemClick(shareList.get(3));
                dismissPopup();
            }
        });
    }

    /**
     * 初始化数据
     */
    public void setShareContent(Integer[] shares) {
        setShareContent(Arrays.asList(shares));
    }

    /**
     * 初始化数据
     */
    public void setShareContent(List<Integer> shareList) {
        if (shareList == null || shareList.size() == 0) {
            dismissPopup();
            return;
        }
        this.shareList = shareList;
        ivOne.setVisibility(View.GONE);
        ivTwo.setVisibility(View.GONE);
        ivThree.setVisibility(View.GONE);
        ivFour.setVisibility(View.GONE);

        int count = shareList.size();
        for (int i = 0; i < count; i++) {
            int value = shareList.get(i);
            switch (i) {
                case 0:
                    ivOne.setVisibility(View.VISIBLE);
                    ivOne.setText(getShareTextRes(value));
                    ivOne.setCompoundDrawablesWithIntrinsicBounds(0, getShareImageRes(value), 0, 0);
                    break;
                case 1:
                    ivTwo.setVisibility(View.VISIBLE);
                    ivTwo.setText(getShareTextRes(value));
                    ivTwo.setCompoundDrawablesWithIntrinsicBounds(0, getShareImageRes(value), 0, 0);
                    break;
                case 2:
                    ivThree.setVisibility(View.VISIBLE);
                    ivThree.setText(getShareTextRes(value));
                    ivThree.setCompoundDrawablesWithIntrinsicBounds(0, getShareImageRes(value), 0, 0);
                    break;
                case 3:
                    ivFour.setVisibility(View.VISIBLE);
                    ivFour.setText(getShareTextRes(value));
                    ivFour.setCompoundDrawablesWithIntrinsicBounds(0, getShareImageRes(value), 0, 0);
                    break;
            }
        }

    }

    @Override
    public View setContextView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.popup_share_view, null);
    }

    @Override
    public void setPopupAttrs(PopupWindow popupWindow) {

    }

    //标题
    private String getShareTextRes(int value) {
        switch (value) {
            case SHARE_WX:
                return "微信好友";
            case SHARE_WX_CIRCLE:
                return "朋友圈";
            case SHARE_QQ:
                return "QQ";
            case SHARE_QQ_ZONE:
                return "QQ空间";
            case SHARE_SINA:
                return "微博";
            default:
                return "";
        }
    }

    //获取资源图片
    private int getShareImageRes(int value) {
        switch (value) {
            case SHARE_WX:
                return R.mipmap.icon_share_wx;
            case SHARE_WX_CIRCLE:
                return R.mipmap.icon_share_wx_circle;
            case SHARE_QQ:
                return R.mipmap.icon_share_qq;
            case SHARE_QQ_ZONE:
                return R.mipmap.icon_share_zone;
            case SHARE_SINA:
                return R.mipmap.icon_share_sina;
            default:
                return 0;
        }
    }

    public void setOnShareItemClickListener(OnShareItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnShareItemClickListener {

        void shareItemClick(int type);
    }
}
