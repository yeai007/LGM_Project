package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyInfo;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.radar.RadarUploadInfoCallback;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.Services.LocationService;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;


/**
 * Created by whisper on 2016/10/7.
 */
public class SelectSeedFriend extends AppCompatActivity implements View.OnClickListener, RadarSearchListener, BDLocationListener, RadarUploadInfoCallback {
    private LocationService locationService;
    private static final String TAG = "SelectSeedFriend";
    RadarSearchManager mManager;
    RadarUploadInfo infoAuto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_seed_friend);
        initView();
        initLocation();

    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("找种友");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    @Override
    public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError radarSearchError) {
        if (!(radarSearchError == RadarSearchError.RADAR_NO_RESULT)) {
            Log.i(TAG, "totalNum:" + radarNearbyResult.totalNum);  // 总结果个数
            Log.i(TAG, "pageIndex:" + radarNearbyResult.pageIndex);    // 页码
            Log.i(TAG, "pageNum:" + radarNearbyResult.pageNum);    // 总页数
            Log.i(TAG, "infoList.size:" + radarNearbyResult.infoList.size());
            Log.e(TAG, "onGetNearbyInfoList: " + radarSearchError.toString());
            ArrayList<RadarNearbyInfo> arrRadarNearbyInfo = new ArrayList<>();
            arrRadarNearbyInfo = ObjectUtil.cast(radarNearbyResult.infoList);
            Log.e(TAG, "onGetNearbyInfoList: " + arrRadarNearbyInfo);
        } else {
            Log.e(TAG, "onGetNearbyInfoList: 获取数据失败" + radarSearchError);
        }
    }

    @Override
    public void onGetUploadState(RadarSearchError radarSearchError) {
        // TODO Auto-generated method stub
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            //上传成功
            Log.e(TAG, "onGetUploadState: 单次上传位置成功");
            Toast.makeText(getApplicationContext(), "单次上传位置成功", Toast.LENGTH_LONG)
                    .show();
            LatLng llA = new LatLng(Const.LocLat, Const.LocLng);
            RadarNearbySearchOption option = new RadarNearbySearchOption().centerPt(llA).pageNum(0).radius(90000);
            //发起查询请求
            mManager.nearbyInfoRequest(option);

        } else {
            //上传失败
            Log.e(TAG, "onGetUploadState: 单次上传位置失败");
            Toast.makeText(getApplicationContext(), radarSearchError.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetClearInfoState(RadarSearchError radarSearchError) {

    }

    private void initLocation() {
        // -----------location config ------------
        locationService = ((Application) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(this);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(this); //注销掉监听
        //  locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除监听
        mManager.removeNearbyInfoListener(this);
//清除用户信息
        mManager.clearUserInfo();
//释放资源
        mManager.destroy();
        mManager = null;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null)
            return;
        //Receive Location
        //经纬度
        double lati = bdLocation.getLatitude();
        double longa = bdLocation.getLongitude();
        //打印出当前位置
        Log.i("TAG", "location.getAddrStr()=" + bdLocation.getAddrStr());
        //打印出当前城市
        Log.i("TAG", "location.getCity()=" + bdLocation.getCity());
        //返回码
        int i = bdLocation.getLocType();
        //LatLng llA = new LatLng(36.710353, 117.086401);
        LatLng llA = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        Const.LocLat = bdLocation.getLatitude();
        Const.LocLng = bdLocation.getLongitude();

        getNearByData(llA);
        locationService.stop();
    }

    private void getNearByData(LatLng llA) {
        Log.e(TAG, "getNearByData: " + llA.toString());
        mManager = RadarSearchManager.getInstance();
        //周边雷达设置监听
        mManager.addNearbyInfoListener(this);
        //周边雷达设置用户身份标识，id为空默认是设备标识
        mManager.setUserID(String.valueOf(Const.currentUser.user_id));
        //上传位置
        RadarUploadInfo info = new RadarUploadInfo();
        infoAuto = new RadarUploadInfo();
        info.comments = Const.currentUser.user_name;
        info.pt = llA;

        infoAuto.comments = "1";
        infoAuto.pt = llA;
        // mManager.uploadInfoRequest(info);
        mManager.startUploadAuto(SelectSeedFriend.this, 5000);
    }


    @Override
    public RadarUploadInfo onUploadInfoCallback() {


        return infoAuto;
    }
}