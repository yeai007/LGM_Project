package com.hopeofseed.hopeofseed.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.MainViewPagerAdapter;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;

import static cn.jpush.im.android.api.model.Conversation.createGroupConversation;


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
    private static final String TAG = "HomePageActivity";
    public static String MESSAGE_RECEIVE = "MESSAGE_RECEIVE";
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
    int page = 2;
    TextView apptitle;
    RadioButton two;

    class UpdateBroadcastReceiver extends BroadcastReceiver {
        /* 覆写该方法，对广播事件执行响应的动作  */
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: 收到启动消息广播");
            registerJpush();
        }
    }

    private UpdateBroadcastReceiver updateBroadcastReceiver;  //刷新列表广播

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initView();
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        unregisterReceiver(updateBroadcastReceiver);
        super.onDestroy();
    }

    private void initReceiver() {

        // 注册广播接收
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_RECEIVE);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(updateBroadcastReceiver, filter);
    }

    public void registerJpush() {
        JMessageClient.registerEventReceiver(this);
    }

    private void initView() {

        Intent intent = getIntent();
        page = intent.getIntExtra("page", 2);
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
        two = (RadioButton) findViewById(R.id.two);
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

    public void isHavaMessage(boolean ishava) {
        if (ishava) {
            two.setBackgroundResource(R.drawable.btn_main_radio_xiaoxi_message);
        } else {
            two.setBackgroundResource(R.drawable.btn_main_radio_xiaoxi);
        }
    }
    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent i = new Intent(Intent.ACTION_MAIN);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addCategory(Intent.CATEGORY_HOME);

            startActivity(i);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
