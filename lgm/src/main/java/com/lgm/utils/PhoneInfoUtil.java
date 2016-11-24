package com.lgm.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @FileName:utils
 * @Desc:获取设备信息
 * @Author:liguangming
 * @Date:2016/3/15
 * @Copyright:Moogeek
 */
public class PhoneInfoUtil {
    /**
     * 获取设备品牌及型号
     *
     * @param context
     * @return 运行当前应用的设备品牌及型号
     */
    public static String getPhoneModel(Context context) {
        return Build.MODEL;
    }

    /**
     * [获取设备android版本号]
     *
     * @param context
     * @return 运行当前应用的设备品牌及型号
     */
    public static String getPhoneRelease(Context context) {
        return Build.VERSION.RELEASE;
    }

    /**
     * [获取设备品牌]
     *
     * @param context
     * @return string 设备品牌
     */
    public static String getPhoneBrand(Context context) {
        return Build.BRAND;
    }

    /**
     * [获取设备屏幕宽度]
     *
     * @param context
     * @return int 屏幕宽度
     */
    public static int getPhoneScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * [获取设备屏幕高度]
     *
     * @param context
     * @return int 屏幕高度
     */
    public static int getPhoneScreenHidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;

    }
}
