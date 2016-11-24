package com.hopeofseed.hopeofseed.Data;

import com.baidu.mapapi.model.LatLng;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.JNXData.CurrentUser;
import com.lgm.utils.AppUtil;

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
    public static final String BASE_URL = "http://zyg168.com/mobileinterface/";// 统一后台获取json数据接口前缀
    public static CurrentUser currentUser = new CurrentUser();
    public static final String IMG_URL = "http://zyg168.com/mobileinterface/upload/";
    public static final String IMG_URL_FINAL = "http://zyg168.com/mobileinterface/images/";
    public static final String JPUSH_PREFIX = "Jpush_";
    public static String UserLocation = "";
    public static double  LocLat;
    public  static double LocLng;
    public static  String InputCathe;
}
