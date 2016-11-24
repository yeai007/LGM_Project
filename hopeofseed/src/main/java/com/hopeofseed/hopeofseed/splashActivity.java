package com.hopeofseed.hopeofseed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.hopeofseed.hopeofseed.Activitys.HomePageActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.UserData;

import java.io.File;
import java.util.Locale;
import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @FileName:smamoo.mgkj.smamootwo
 * @Desc:the first activity
 * @Author:liguangming
 * @Date:2016/5/3
 * @Copyright:2014-2016 Moogeek
 */
public class splashActivity extends AppCompatActivity {
    private static final String TAG = "splashActivity";
    private static String KEY_APP_KEY = "JPUSH_APPKEY";
    private static final String SHAREDPREFERENCES_NAME = "first_pref";//登录次数标记，存储与系统中
    boolean isFirstIn = false;//第一次登录标记
    private static final int GO_HOME = 1000;//直接加载程序
    private static final int GO_GUIDE = 1001;//加载指南流程
    private static final long SPLASH_DELAY_MILLIS = 3000;//延时时间，用于延时页面显示
    Realm myRealm = Realm.getDefaultInstance();
    private static String APP_KEY;
    private File cache;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initFirst();

        //创建缓存目录，系统一运行就得创建缓存目录的，
        cache = new File(Environment.getExternalStorageDirectory(), "hopeofseed/images");

        if(!cache.exists()){
            cache.mkdirs();
        }
    }

    private void initFirst() {
        if (!JPushInterface.getConnectionState(getApplicationContext())) {
            Log.e(TAG, "initFirst: 长连接已经断开");
            JPushInterface.init(this);
        }
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        Log.e(TAG, "initFirst: " + isFirstIn);
        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (!isFirstIn) {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            firstLoginHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            firstLoginHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
    }

    /**
     * Handler:跳转到不同界面
     */
    private Handler firstLoginHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();// 跳转主页
                    break;
                case GO_GUIDE:
                    goGuide();// 跳转引导页
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //跳转到引导页
    private void goGuide() {
        initUserData();
        // finish();
    }

    //跳转到主页
    private void goHome() {
        initUserData();
        //  finish();
    }

    private void initUserData() {
        RealmResults<UserData> results1 =
                myRealm.where(UserData.class).equalTo("iscurrent", 1).findAll();
        Log.e(TAG, "initUserData: " + results1.size());
        if (results1.size() == 0) {
            toLogin();
        } else {
            Const.currentUser.user_id = results1.get(0).getUser_id();
            Const.currentUser.user_name = results1.get(0).getUser_name();
            Const.currentUser.password = results1.get(0).getPassword();
            Const.currentUser.nickname = results1.get(0).getNickname();
            Const.currentUser.user_mobile = results1.get(0).getUser_mobile();
            Const.currentUser.user_email = results1.get(0).getUser_email();
            Const.currentUser.createtime = results1.get(0).getCreatetime();
            Const.currentUser.user_permation = results1.get(0).getUser_permation();
            Const.currentUser.user_role = results1.get(0).getUser_role();
            Log.e(TAG, "initUserData: " + Const.currentUser.user_id + Const.currentUser.user_name);
            Intent intent = new Intent(splashActivity.this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    private void toLogin() {
        Intent intent = new Intent(splashActivity.this, LoginAcitivity.class);
        startActivity(intent);
    }

    public static String getAppKey(Context context) {
        Bundle metaData = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            } else {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }

        if (null != metaData) {
            APP_KEY = metaData.getString(KEY_APP_KEY);
            if (TextUtils.isEmpty(APP_KEY)) {
                return null;
            } else if (APP_KEY.length() != 24) {
                return null;
            }
            APP_KEY = APP_KEY.toLowerCase(Locale.getDefault());
        }
        return APP_KEY;
    }
}
