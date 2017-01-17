package com.hopeofseed.hopeofseed.Data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.hopeofseed.hopeofseed.JNXData.CurrentUser;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/2 8:31
 * 修改人：whisper
 * 修改时间：2016/8/2 8:31
 * 修改备注：
 */
public class Const {
    private static final String TAG = "Const";
    public static final String BASE_URL = "http://bxu2343580168.my3w.com/mobileinterface/";// 统一后台获取json数据接口前缀
    public static CurrentUser currentUser = new CurrentUser();
    public static final String IMG_URL = "http://bxu2343580168.my3w.com/mobileinterface/upload/";
    public static final String IMG_URL_FINAL = "http://bxu2343580168.my3w.com/mobileinterface/images/";
    public static final String JPUSH_PREFIX = "Jpush_";
    public static String UserLocation = "";
    public static double LocLat;
    public static String InputCathe;
    public static double LocLng;
    public static UserInfo mUserInfo;
    public static String Province = "";
    public static String City = "";
    public static String Zone = "";
    public static boolean isJpushLogin = false;
    public static boolean isVip=true;
    /**
     * 地址数据库版本号
     */
    public static int AreaConfig = 0;

    public static void SetShareData(Context context) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = context.getSharedPreferences("userotherinfo",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        if (TextUtils.isEmpty(Const.Province)) {
            editor.putString("Province", "山东");
        } else {
            editor.putString("Province", Const.Province);
        }
        if (TextUtils.isEmpty(Const.Province)) {
            editor.putString("City", Const.City);
        } else {
            editor.putString("City", "济南");
        }
        if (TextUtils.isEmpty(Const.Province)) {
            editor.putString("Zone", Const.Zone);
        } else {
            editor.putString("Zone", "历城区");
        }
        editor.putString("UserLocation", Const.UserLocation);
        editor.putInt("AreaConfig", Const.AreaConfig);
        editor.putString("LocLat",String.valueOf(Const.LocLat));
        editor.putString("LocLng",String.valueOf(Const.LocLng));
        editor.commit();
        Log.e(TAG, "SetShareData: SetShareData Success");
    }

    public static void GetShareData(Context context) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences("userotherinfo",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        Const.Province = sharedPreferences.getString("Province", "山东省");
        Const.City = sharedPreferences.getString("City", "济南市");
        Const.Zone = sharedPreferences.getString("Zone", "历城区");
        Const.UserLocation = sharedPreferences.getString("UserLocation", "济南市");
        Const.AreaConfig = sharedPreferences.getInt("AreaConfig", 0);
        Const.LocLat=Double.parseDouble(sharedPreferences.getString("LocLat", "36.710348"));
        Const.LocLng=Double.parseDouble(sharedPreferences.getString("LocLng", "117.086381"));
        Log.e(TAG, "GetShareData: " + Const.Province + Const.City + Const.Zone);
    }
}
