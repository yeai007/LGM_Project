package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/27 11:52
 * 修改人：whisper
 * 修改时间：2016/9/27 11:52
 * 修改备注：
 */
public class EnterpriseCommodityActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyCommodity";
    Button btn_topright;
    int page = 0;
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    CommodityListFragment mCommodityListFragment;
    DistributorListForCommodityFragment mDistributorListForCommodityFragment;
    RadioButton one,two;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterprise_commodity);
        initView();
        initViewPager();

    }


    private void initViewPager() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mCommodityListFragment = new CommodityListFragment();
        mDistributorListForCommodityFragment = new DistributorListForCommodityFragment();
        fragmentList.add(mCommodityListFragment);
        fragmentList.add(mDistributorListForCommodityFragment);
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
                case 0://商品
                    vp_main.setCurrentItem(0);
                    one.setChecked(true);
                    break;
                case 1://经销商
                    vp_main.setCurrentItem(1);
                    two.setChecked(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {

            }
        }
    };


    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("我的商品");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("添加商品");
        one = (RadioButton) findViewById(R.id.one);
        two = (RadioButton) findViewById(R.id.two);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                //添加商品
                intent = new Intent(EnterpriseCommodityActivity.this, AddCommodity.class);
                intent.putExtra("commodityId", "0");
                startActivity(intent);
                break;
        }
    }

}
