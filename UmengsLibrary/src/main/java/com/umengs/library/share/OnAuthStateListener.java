package com.umengs.library.share;

/**
 * Created by Administrator on 2016/10/10 0010.
 * 授权回调接口
 */
public interface OnAuthStateListener {

//    public void authState(boolean isAuth, String userId);
    public void authState(boolean isAuth, ThirdUserInfo info);
}
