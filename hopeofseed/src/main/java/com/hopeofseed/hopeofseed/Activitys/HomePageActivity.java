package com.hopeofseed.hopeofseed.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
 * 创建时间：2016/7/27 9:09
 * 修改人：whisper
 * 修改时间：2016/7/27 9:09
 * 修改备注：
 */
public class HomePageActivity extends FragmentActivity {
    private ViewPager vp_main;
    private MainViewPagerAdapter mainViewPagerAdapter;
    /**
     * 页面集合
     */
    List<Fragment> fragmentList;
    private RadioGroup rg_menu;
    MessageFragment mMessageFragment;
    NewsFragment mNewsFragment;
    UserInfoFragment mUserInfoFragment;
    DiscoverFragment mDiscoverFragment;
    public Button btn_topright, btn_topleft;
    int page = 0;
    TextView apptitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        fragmentList = new ArrayList<>();
        mMessageFragment = new MessageFragment();
        mNewsFragment = new NewsFragment();
        mUserInfoFragment = new UserInfoFragment();
        mDiscoverFragment = new DiscoverFragment();
        fragmentList.add(mNewsFragment);
        fragmentList.add(mMessageFragment);
        fragmentList.add(mDiscoverFragment);
        fragmentList.add(mUserInfoFragment);
        apptitle = (TextView) findViewById(R.id.apptitle);
        rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        vp_main.addOnPageChangeListener(onPageChangeListener);
        vp_main.setAdapter(mainViewPagerAdapter);
        vp_main.setOffscreenPageLimit(4);
        rg_menu.setOnCheckedChangeListener(mChangeRadio);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topleft = (Button) findViewById(R.id.btn_topleft);
        vp_main.setCurrentItem(page);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0://新鲜事
//                    apptitle.setText("新鲜事");
                    vp_main.setCurrentItem(0);
                   /* mNewsFragment.refreshData();*/
                    break;
                case 1://消息
//                    apptitle.setText("消息");
                    break;
                case 2://发现
//                    apptitle.setText("发现");
                    break;
                case 3://我的
//                    apptitle.setText("我的");
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
            if (checkedId == (findViewById(R.id.one)).getId()) {
                vp_main.setCurrentItem(0);
            } else if (checkedId == (findViewById(R.id.two)).getId()) {
//                apptitle.setText("消息");
                vp_main.setCurrentItem(1);
            } else if (checkedId == (findViewById(R.id.three)).getId()) {
//                apptitle.setText("发现");
                vp_main.setCurrentItem(2);
            } else if (checkedId == (findViewById(R.id.four)).getId()) {
//                apptitle.setText("我的");
                vp_main.setCurrentItem(3);
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mNewsFragment.refreshData();
        }
    }
}
