package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.Services.LocationService;

import java.util.ArrayList;
import java.util.List;

public class SelectDistributor extends AppCompatActivity implements View.OnClickListener, BDLocationListener {
    private static final String TAG = "SelectDistributor";
    int page = 0;
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    DistributorListFragment mDistributorListFragment;
    DistributorMapFragment mDistributorMapFragment;
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private LocationService locationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_distributor);
        initView();
        initViewPager();
        initUserLocation();
    }

    private void initViewPager() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mDistributorListFragment = new DistributorListFragment();
        mDistributorMapFragment = new DistributorMapFragment();
        fragmentList.add(mDistributorListFragment);
        fragmentList.add(mDistributorMapFragment);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        vp_main.addOnPageChangeListener(onPageChangeListener);
        vp_main.setAdapter(mainViewPagerAdapter);
        vp_main.setOffscreenPageLimit(2);
        vp_main.setCurrentItem(page);
        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
    }

    //底部菜单方法
    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == (findViewById(R.id.one)).getId()) {
                vp_main.setCurrentItem(0);
            } else if (checkedId == (findViewById(R.id.two)).getId()) {
                vp_main.setCurrentItem(1);
            }
        }
    };
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0://列表显示
                    vp_main.setCurrentItem(0);
                    break;
                case 1://地图显示

                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initView() {
        TextView appTtitle = (TextView) findViewById(R.id.apptitle);
        appTtitle.setText("附近经销商");
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("重新定位");
        btn_topright.setOnClickListener(this);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                initUserLocation();
                break;
        }
    }


    private void initUserLocation() {
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

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null)
            return;
        //Receive Location
        //经纬度
        double lati = bdLocation.getLatitude();
        double longa = bdLocation.getLongitude();
        Const.LocLat = lati;
        Const.LocLng = longa;
        if (TextUtils.isEmpty(String.valueOf(Const.LocLat))) {
            Const.LocLat = 36.710348;
        }
        if (TextUtils.isEmpty(String.valueOf(Const.LocLng))) {
            Const.LocLng = 117.086381;
        }
        mDistributorListFragment.setUserLoc();
        mDistributorMapFragment.setUserLoc();
        locationService.stop();
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

}
