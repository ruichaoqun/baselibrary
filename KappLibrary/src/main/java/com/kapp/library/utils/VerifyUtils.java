package com.kapp.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/17 0017.
 * 验证帮助类
 */
public class VerifyUtils {

    /** 验证手机号码 */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^1[0-9]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        if (m.matches())
            return m.matches();

//        if (mobiles.length() == 8)//香港号段
//            return true;

        return false;
    }

}
