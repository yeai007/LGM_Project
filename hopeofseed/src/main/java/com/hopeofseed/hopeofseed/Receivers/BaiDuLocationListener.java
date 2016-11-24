package com.hopeofseed.hopeofseed.Receivers;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/5 10:22
 * 修改人：whisper
 * 修改时间：2016/11/5 10:22
 * 修改备注：
 */
public class BaiDuLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //Receive Location
        //经纬度
        double lati = location.getLatitude();
        double longa = location.getLongitude();
        //打印出当前位置
        Log.i("TAG", "location.getAddrStr()=" + location.getAddrStr());
        //打印出当前城市
        Log.i("TAG", "location.getCity()=" + location.getCity());
        //返回码
        int i = location.getLocType();
    }
}
