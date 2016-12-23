package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.MapDataInfoPopupWindow;
import com.hopeofseed.hopeofseed.Services.LocationService;
import com.hopeofseed.hopeofseed.util.OverlayManager;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/7 20:30
 * 修改人：whisper
 * 修改时间：2016/10/7 20:30
 * 修改备注：
 */
public class DistributorMapFragment extends Fragment implements BDLocationListener, NetCallBack {
    MapView mMapView;
    private LocationService locationService;
    BaiduMap baiduMap;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
    boolean ifFrist = true;
    ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
    HashMap<Integer, DistributorData> map_DistributorData = new HashMap<>();
    ArrayList<DistributorData> arr_DistributorDataTmp = new ArrayList<>();
    //自定义的弹出框类
    MapDataInfoPopupWindow menuWindow;

    List<OverlayOptions> overlayOptionses = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.pager_map_distributor, null);
        initView(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((Application) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(this);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    private void initView(final View v) {
        //获取地图控件引用
        mMapView = (MapView) v.findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        // 设置可改变地图位置
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e(TAG, "onMarkerClick: " + marker.getTitle());
                DistributorData itemDis = new DistributorData();
                itemDis = (map_DistributorData.get(Integer.parseInt(marker.getTitle())));
                //实例化SelectPicPopupWindow
                final DistributorData finalItemDis = itemDis;
                menuWindow = new MapDataInfoPopupWindow(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.btn_show_info) {
                            Intent intent = new Intent(getActivity(), UserActivity.class);
                            intent.putExtra("userid", finalItemDis.getUser_id());
                            intent.putExtra("UserRole", finalItemDis.getUser_role());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                menuWindow.SetTitle(itemDis.getDistributorName());
                menuWindow.SetAddress(itemDis.getDistributorProvince() + " " + itemDis.getDistributorCity() + " " + itemDis.getDistributorZone() + "\n" + itemDis.getDistributorAddressDetail());
                //显示窗口
                menuWindow.showAtLocation(v.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                return false;
            }
        });

        getData();
    }

    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(this); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // map view 销毁后不在处理新接收的位置
        if (bdLocation == null || mMapView == null)
            return;
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        Log.e(TAG, "onReceiveLocation: " + bdLocation.getLatitude() + "," + bdLocation.getLongitude());
        baiduMap.setMyLocationData(locData);    //设置定位数据
        if (ifFrist) {
            ifFrist = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(),
                    bdLocation.getLongitude());
          /*  MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 36);   //设置地图中心点以及缩放级别
//              MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(u);*/
        }
        locationService.stop();
    }

    /**
     * 添加marker
     */
    public void showPoint(HashMap<Integer, DistributorData> dbd) {
        Iterator iter = dbd.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            DistributorData itemdata = new DistributorData();
            int itemId = (int) key;
            itemdata = ObjectUtil.cast(entry.getValue());
            // 定义marker坐标点
            LatLng point = new LatLng(Double.parseDouble(itemdata.getDistributorLat()), Double.parseDouble(itemdata.getDistributorLon()));
            // 构建markerOption，用于在地图上添加marker
            OverlayOptions option = new MarkerOptions()//
                    .position(point)// 设置marker的位置
                    .icon(bdA).title(String.valueOf(itemId));// 设置marker的图标
            overlayOptionses.add(option);
            // 在地图上添加marker，并显示


        }
        OverlayManager overlayManager = new OverlayManager(baiduMap) {
            @Override
            public List<OverlayOptions> getOverlayOptions() {
                return overlayOptionses;
            }

            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }

            @Override
            public boolean onPolylineClick(Polyline polyline) {
                return false;
            }
        };
        overlayManager.addToMap();
        overlayManager.zoomToSpan();
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("LocLat", String.valueOf(Const.LocLat));
        opt_map.put("LocLng", String.valueOf(Const.LocLng));
        opt_map.put("Range", "50000");
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributor.php", opt_map, DistributorDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        DistributorDataTmp distibutorDataTmp = ObjectUtil.cast(rspBaseBean);
        Log.e(TAG, "onSuccess: " + distibutorDataTmp.toString());
        arr_DistributorDataTmp = distibutorDataTmp.getDetail();
        updateView();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            arr_DistributorData.clear();
            arr_DistributorData.addAll(arr_DistributorDataTmp);
            for (int i = 0; i < arr_DistributorData.size(); i++) {
                DistributorData dddd = new DistributorData();
                dddd = arr_DistributorData.get(i);
                map_DistributorData.put(Integer.parseInt(dddd.getDistributorId()), dddd);
            }
            Log.e(TAG, "addMapPoint: " + map_DistributorData.size());
            showPoint(map_DistributorData);
        }
    };

    public void setUserLoc() {
        getData();
    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
    }
}
