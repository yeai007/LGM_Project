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
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AuthorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AuthorDataTmp;
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
public class AuthorMapFragment extends Fragment implements BDLocationListener, NetCallBack {
    MapView mMapView;
    private LocationService locationService;
    BaiduMap baiduMap;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
    boolean ifFrist = true;
    ArrayList<AuthorData> arrAuthorData = new ArrayList<>();
    HashMap<Integer, AuthorData> mapAuthorData = new HashMap<>();
    ArrayList<AuthorData> arrAuthorDataTmp = new ArrayList<>();
    //自定义的弹出框类
    MapDataInfoPopupWindow menuWindow;

    List<OverlayOptions> overlayOptionses = new ArrayList<>();
    int ClassId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.pager_map_author, null);
        initView(v);
        return v;
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
                AuthorData itemDis = new AuthorData();
                itemDis = (mapAuthorData.get(Integer.parseInt(marker.getTitle())));
                //实例化SelectPicPopupWindow
                final AuthorData finalItemDis = itemDis;
                menuWindow = new MapDataInfoPopupWindow(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), UserActivity.class);
                        intent.putExtra("userid", finalItemDis.getUser_id());
                        startActivity(intent);
                    }
                });
                menuWindow.SetTitle(itemDis.getAuthorName());
                menuWindow.SetAddress(itemDis.getAuthorProvince() + " " + itemDis.getAuthorCity() + " " + itemDis.getAuthorZone() + "\n" + itemDis.getAuthorAddressDetail());
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
        //   locationService.unregisterListener(this); //注销掉监听
        //   locationService.stop(); //停止定位服务
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
    public void showPoint(HashMap<Integer, AuthorData> dbd) {
        Iterator iter = dbd.entrySet().iterator();
        overlayOptionses.clear();
        baiduMap.clear();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            AuthorData itemdata = new AuthorData();
            int itemId = (int) key;
            itemdata = ObjectUtil.cast(entry.getValue());
            // 定义marker坐标点
            LatLng point = new LatLng(Double.parseDouble(itemdata.getAuthorLat()), Double.parseDouble(itemdata.getAuthorLon()));
            // 构建markerOption，用于在地图上添加marker
            OverlayOptions option = new MarkerOptions()//
                    .position(point)// 设置marker的位置
                    .icon(bdA).title(String.valueOf(itemId));// 设置marker的图标
            overlayOptionses.add(option);
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
        overlayManager.removeFromMap();
        overlayManager.addToMap();
        overlayManager.zoomToSpan();
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("LocLat", String.valueOf(Const.LocLat));
        opt_map.put("LocLng", String.valueOf(Const.LocLng));
        opt_map.put("Range", "50000");
        opt_map.put("ClassId", String.valueOf(ClassId));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetAuthorDataByClass.php", opt_map, AuthorDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        AuthorDataTmp authorDataTmp = ObjectUtil.cast(rspBaseBean);
        Log.e(TAG, "onSuccess: " + authorDataTmp.toString());
        arrAuthorDataTmp = authorDataTmp.getDetail();
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
            arrAuthorData.clear();
            mapAuthorData.clear();
            arrAuthorData.addAll(arrAuthorDataTmp);
            for (int i = 0; i < arrAuthorData.size(); i++) {
                AuthorData dddd = new AuthorData();
                dddd = arrAuthorData.get(i);
                mapAuthorData.put(Integer.parseInt(dddd.getAuthorId()), dddd);
            }
            Log.e(TAG, "addMapPoint: " + mapAuthorData.size());
            showPoint(mapAuthorData);
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

    public void setClass(int classId) {
        ClassId = classId;
    }

    public void refreshData() {
        getData();
    }
}
