package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.Adapter.SpStringAdapter;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.Services.LocationService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by whisper on 2016/10/7.
 */
public class SelectAuthor extends AppCompatActivity implements View.OnClickListener, BDLocationListener {
    /**
     * 页面集合
     */
    int page = 0;
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    AuthorListFragment mAuthorListFragment;
    AuthorMapFragment mAuthorMapFragment;
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private LocationService locationService;
    Spinner sp_class;
    ArrayList<String> spData = new ArrayList<>();
    int ClassId = 0;
    SpStringAdapter spStringAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_author);
        initdata();
        initView();
        initViewPager();
        initUserLocation();
    }

    private void initdata() {
        spData.add("全部");
        spData.add("政务咨询");
        spData.add("业务审批");
        spData.add("维权投诉");
        spData.add("农技推广");
    }

    private void initView() {
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("重新定位");
        btn_topright.setOnClickListener(this);
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        appTitle.setText("找机构");
        sp_class = (Spinner) findViewById(R.id.sp_class);
        spStringAdapter = new SpStringAdapter(getApplicationContext(), spData);
        sp_class.setAdapter(spStringAdapter);
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAuthorListFragment.setClass(position);
                mAuthorMapFragment.setClass(position);
                mAuthorListFragment.refreshData();
                mAuthorMapFragment.refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                initUserLocation();
                break;
        }
    }

    private void initViewPager() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mAuthorListFragment = new AuthorListFragment();
        mAuthorListFragment.setClass(ClassId);
        mAuthorMapFragment = new AuthorMapFragment();
        mAuthorMapFragment.setClass(ClassId);
        fragmentList.add(mAuthorListFragment);
        fragmentList.add(mAuthorMapFragment);
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
        //打印出当前位置
        Log.i("TAG", "location.getAddrStr()=" + bdLocation.getAddrStr());
        //打印出当前城市
        Log.i("TAG", "location.getCity()=" + bdLocation.getCity());

        mAuthorListFragment.setUserLoc();
        mAuthorMapFragment.setUserLoc();
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