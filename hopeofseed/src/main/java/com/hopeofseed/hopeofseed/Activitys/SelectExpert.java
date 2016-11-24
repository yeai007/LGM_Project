package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.Adapter.Sp_PoliticsAdapter;
import com.hopeofseed.hopeofseed.DataForHttp.GetVarietyData;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.PoliticData;
import com.hopeofseed.hopeofseed.JNXData.SpinnerAreaData;
import com.hopeofseed.hopeofseed.JNXData.VarietyData;
import com.hopeofseed.hopeofseed.Adapter.VarietyClassAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXDataTmp.CompanyDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.PoliticDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.SpinnerAreaDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.SideBar;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citypickerview.widget.CityPicker;

import static com.baidu.location.h.j.S;

/**
 * Created by whisper on 2016/10/7.
 */
public class SelectExpert extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "SelectExpert";
    /**
     * 页面集合
     */
    int page = 0;
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    NearExpertFragment mNearExpertFragment;
    ExpertShareFragment mExpertShareFragment;
    Spinner sp_politic;
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    Sp_PoliticsAdapter sp_politicsAdapter;
    ArrayList<PoliticData> arrPolitics = new ArrayList<>();
    ArrayList<PoliticData> arrPoliticsTmp = new ArrayList<>();
    Handler updateSpinnerHandle = new Handler();
    Handler updateAreaHandle = new Handler();
    ArrayList<SpinnerAreaData> arrSpinnerAreaData = new ArrayList<>();
    private String StrProvince, StrCity, StrZone, StrPolitic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_expert);
        getPoliticsData();
        initView();
        initViewPager();
    }


    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("找专家");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        sp_politic = (Spinner) findViewById(R.id.sp_politic);
        sp_politicsAdapter = new Sp_PoliticsAdapter(getApplicationContext(), arrPolitics);
        sp_politic.setAdapter(sp_politicsAdapter);
        sp_politic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrPolitic = arrPolitics.get(position).getPoliticName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CityPicker cityPicker = new CityPicker.Builder(SelectExpert.this).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province("山东省")
                        .city("济南市")
                        .district("历城区")
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(true)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .build();

                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        go.setText("" + citySelected[0] + "  " + citySelected[1] + "  "
                                + citySelected[2]);
                        StrProvince = citySelected[0];
                        StrCity = citySelected[1];
                        StrZone = citySelected[2];
                        mExpertShareFragment.setRefreshData(citySelected);
                        mNearExpertFragment.setRefreshData(citySelected);
                    }
                });
            }
        });
    }

    private void initViewPager() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mNearExpertFragment = new NearExpertFragment();
        mExpertShareFragment = new ExpertShareFragment();
        fragmentList.add(mNearExpertFragment);
        fragmentList.add(mExpertShareFragment);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    private void getPoliticsData() {
        Log.e(TAG, "getPoliticsData: 获取列表");
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetPoliticData.php", opt_map, PoliticDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetPoliticData")) {
            arrPoliticsTmp = ((PoliticDataTmp) rspBaseBean).getDetail();
            updateSpinnerHandle.post(runnable);
        } else if (rspBaseBean.RequestSign.equals("GetSpinnerAreaData")) {
            arrSpinnerAreaData = ((SpinnerAreaDataTmp) rspBaseBean).getDetail();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            arrPolitics.clear();
            arrPolitics.addAll(arrPoliticsTmp);
            Log.e(TAG, "run: notifyDataSetChanged");
            sp_politicsAdapter.notifyDataSetChanged();
            Log.e(TAG, "run: notifyDataSetChanged Success");
        }
    };

}
