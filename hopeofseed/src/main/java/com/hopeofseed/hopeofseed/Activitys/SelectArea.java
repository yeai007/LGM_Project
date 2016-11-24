package com.hopeofseed.hopeofseed.Activitys;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hopeofseed.hopeofseed.Adapter.CityListAdapter;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;

import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CityData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CityDataTmp;

import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.SideBar;

import com.hopeofseed.hopeofseed.Services.LocationService;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/26 8:39
 * 修改人：whisper
 * 修改时间：2016/8/26 8:39
 * 修改备注：
 */
public class SelectArea extends AppCompatActivity implements View.OnClickListener, NetCallBack, BDLocationListener {
    private static final String TAG = "SelectArea";
    ArrayList<CityData> arrCityData = new ArrayList<>();
    ArrayList<CityData> arrCityDataTmp = new ArrayList<>();
    ListView lv_city;
    CityListAdapter mCityListAdapter;
    TextView tv_now_local;
    private LocationService locationService;
    SideBar sideBar;
    TextView dialog;
    Animation operatingAnim;
    ImageView img_loading;
    LinearInterpolator lin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectarea);
        // initLocation();

        initView();
        getCityData();

    }

    private void initView() {
        tv_now_local = (TextView) findViewById(R.id.tv_now_local);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        (findViewById(R.id.btn_reget_location)).setOnClickListener(this);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        img_loading = (ImageView) findViewById(R.id.img_loading);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mCityListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_city.setSelection(position);
                }

            }
        });

        lv_city = (ListView) findViewById(R.id.lv_city);
        mCityListAdapter = new CityListAdapter(getApplicationContext(), arrCityData);
        lv_city.setAdapter(mCityListAdapter);

        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Const.UserLocation = arrCityData.get(i).getCityName();
                tv_now_local.setText(arrCityData.get(i).getCityName());
            }
        });
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.img_tip);
        lin = new LinearInterpolator();
    }

    private void getCityData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCityData.php", opt_map, CityDataTmp.class, this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_reget_location:
                operatingAnim.setInterpolator(lin);
                img_loading.startAnimation(operatingAnim);
                initLocation();
                break;
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        arrCityDataTmp = ((CityDataTmp) rspBaseBean).getDetail();
        updateCityList();
    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateCityList() {
        Message msg = updateCityListHandle.obtainMessage();
        msg.arg1 = 1;
        msg.sendToTarget();
    }

    private Handler updateCityListHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:

                    break;
                case 1:
                    arrCityData.clear();
                    arrCityData.addAll(arrCityDataTmp);
                    Log.e("11111", "handleMessage: go" + arrCityData.size());
                    mCityListAdapter.notifyDataSetChanged();
                    break;
                case 2:

                    break;
            }
        }
    };

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
       // locationService.unregisterListener(this); //注销掉监听
      //  locationService.stop(); //停止定位服务
        super.onStop();
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
        tv_now_local.setText(bdLocation.getCity());
        img_loading.clearAnimation();
        locationService.stop();
    }
}



