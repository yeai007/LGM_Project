package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/27 8:24
 * 修改人：whisper
 * 修改时间：2016/12/27 8:24
 * 修改备注：
 */
public class MyFriendSetting extends AppCompatActivity implements View.OnClickListener {
    ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    /**
     * 页面集合
     */
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    FollowAndFanFragment mFollowAndFanFragment;
    MyFollowFragment myFollowFragment;
    MyFanFragment myFanFragment;
    RadioButton radio_group;
    RadioButton radio_followandfan, radio_myfollow, radio_fan;
    int page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friend_setting);
        initView();
        initViewPager();
    }

    private void initViewPager() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mFollowAndFanFragment = new FollowAndFanFragment();
        myFollowFragment = new MyFollowFragment();
        myFanFragment = new MyFanFragment();
        fragmentList.add(mFollowAndFanFragment);
        fragmentList.add(myFollowFragment);
        fragmentList.add(myFanFragment);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        vp_main.addOnPageChangeListener(onPageChangeListener);
        vp_main.setAdapter(mainViewPagerAdapter);
        vp_main.setOffscreenPageLimit(4);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
        vp_main.setCurrentItem(page);
    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("好友管理");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
        radio_followandfan = (RadioButton) findViewById(R.id.radio_followandfan);
        radio_myfollow = (RadioButton) findViewById(R.id.radio_myfollow);
        radio_fan = (RadioButton) findViewById(R.id.radio_fan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0://新鲜事
                    vp_main.setCurrentItem(0);
                    radio_followandfan.setChecked(true);
                    break;
                case 1://消息
                    vp_main.setCurrentItem(1);
                    radio_myfollow.setChecked(true);
                    break;
                case 2://消息
                    vp_main.setCurrentItem(2);
                    radio_fan.setChecked(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    //底部菜单方法
    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == R.id.radio_followandfan) {
                vp_main.setCurrentItem(0);
            } else if (checkedId == R.id.radio_myfollow) {
                vp_main.setCurrentItem(1);
            } else if (checkedId == R.id.radio_fan) {
                vp_main.setCurrentItem(2);
            }
        }
    };

}
