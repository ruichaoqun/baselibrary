package com.umengs.library.share;

/**
 * Created by Administrator on 2016/10/10 0010.
 */

public class CheckStateInfo {

    public boolean isState = false;
    public String error;

    public static final String ERROR_NO_INSTALL = "未安装客户端，无法继续分享！！";
    public static final String ERROR_NO_SUPPERT = "不支持当前分享接口，请更新第三方客户端再分享！！";
    public static final String ERROR_NORMAL = "未知第三方平台，无法分享！！";
    public static final String ERROR_INIT_FAIL = "分享功能初始化失败！！";
}
