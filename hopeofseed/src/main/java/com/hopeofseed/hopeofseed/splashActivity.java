package com.hopeofseed.hopeofseed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.hopeofseed.hopeofseed.Activitys.HomePageActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.Services.LocationService;
import com.lgm.net.NetWorkUtils;
import com.lgm.update.OnUpdateListener;
import com.lgm.update.UpdateHelper;
import com.lgm.update.UpdateInfo;
import com.lgm.utils.AppUtil;

import java.io.File;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.hopeofseed.hopeofseed.Data.Const.City;
import static com.hopeofseed.hopeofseed.Data.Const.Province;

/**
 * @FileName:smamoo.mgkj.smamootwo
 * @Desc:the first activity
 * @Author:liguangming
 * @Date:2016/5/3
 * @Copyright:2014-2016 Moogeek
 */
public class splashActivity extends AppCompatActivity implements BDLocationListener, OnUpdateListener {
    private static final String TAG = "splashActivity";
    private static String KEY_APP_KEY = "JPUSH_APPKEY";
    private static final String SHAREDPREFERENCES_NAME = "first_pref";//登录次数标记，存储与系统中
    boolean isFirstIn = false;//第一次登录标记
    private static final int GO_HOME = 1000;//直接加载程序
    private static final int GO_GUIDE = 1001;//加载指南流程
    private static final long SPLASH_DELAY_MILLIS = 3000;//延时时间，用于延时页面显示
    Realm myRealm = Realm.getDefaultInstance();
    private static String APP_KEY;
    private LocationService locationService;
    private File cache;
    private TextView tv_log;
    private SharedPreferences preferences_update;
     UpdateHelper updateHelper = new UpdateHelper.Builder(this)
            .checkUrl(Const.BASE_URL + "GetLastVersion.php")
            .isAutoInstall(true) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
            .build();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        tv_log = (TextView) findViewById(R.id.tv_log);
        preferences_update = getSharedPreferences("Updater",Context.MODE_PRIVATE);
        checkUpdate();
      /*  initFirst();
        initLocation();*/
        //创建缓存目录，系统一运行就得创建缓存目录的，
        cache = new File(Environment.getExternalStorageDirectory(), "hopeofseed/images");

        if (!cache.exists()) {
            cache.mkdirs();
        }
    }

    private void checkUpdate() {

        updateHelper.check(new OnUpdateListener() {
            @Override
            public void onStartCheck() {
                tv_log.setText("检查版本更新中");
            }

            @Override
            public void onFinishCheck(UpdateInfo info) {
                tv_log.setText("");
                SharedPreferences.Editor editor = preferences_update.edit();
                if (info != null) {
                    if (Integer.parseInt(info.getVersionCode()) > AppUtil.getPackageInfo(getApplicationContext()).versionCode) {
                        editor.putBoolean("hasNewVersion", true);
                        editor.putString("lastestVersionCode",
                                info.getVersionCode());
                        editor.putString("lastestVersionName",
                                info.getVersionName());
                        showUpdateUI(info);
                    } else {
                        tv_log.setText("已是最新版本");
                        editor.putBoolean("hasNewVersion", false);
                        initFirst();
                        initLocation();
                    }
                } else {
                    tv_log.setText("已是最新版本");
                    initFirst();
                    initLocation();
                }
                editor.putString("currentVersionCode", AppUtil.getPackageInfo(getApplicationContext()).versionCode + "");
                editor.putString("currentVersionName", AppUtil.getPackageInfo(getApplicationContext()).versionName);
                editor.commit();

            }

            @Override
            public void onStartDownload() {

            }

            @Override
            public void onDownloading(int progress) {
               // tv_log.setText("下载进度："+progress+"%");
            }

            @Override
            public void onFinshDownload() {
                tv_log.setText("下载完成");
            }
        });
    }
    /**
     * 弹出提示更新窗口
     *
     * @param updateInfo
     */
    private void showUpdateUI(final UpdateInfo updateInfo) {
        AlertDialog.Builder upDialogBuilder = new AlertDialog.Builder(splashActivity.this);
        upDialogBuilder.setTitle(updateInfo.getUpdateTips());
        upDialogBuilder.setMessage(updateInfo.getChangeLog());
        upDialogBuilder.setNegativeButton("下次再说",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        initFirst();
                        initLocation();
                    }
                });
        upDialogBuilder.setPositiveButton("后台下载",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        NetWorkUtils netWorkUtils = new NetWorkUtils(splashActivity.this);
                        int type = netWorkUtils.getNetType();
                        if (type != 1) {
                            showNetDialog(updateInfo);
                        } else {
                            updateHelper.downLoadAPK(updateInfo);
                            initFirst();
                            initLocation();
                          /*  AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
                            asyncDownLoad.execute(updateInfo);*/
                        }
                    }
                });
        AlertDialog updateDialog = upDialogBuilder.create();
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
    }
    /**
     * 2014-10-27新增流量提示框，当网络为数据流量方式时，下载就会弹出此对话框提示
     *
     * @param updateInfo
     */
    private void showNetDialog(final UpdateInfo updateInfo) {
        AlertDialog.Builder netBuilder = new AlertDialog.Builder(splashActivity.this);
        netBuilder.setTitle("下载提示");
        netBuilder.setMessage("您在目前的网络环境下继续下载将可能会消耗手机流量，请确认是否继续下载？");
        netBuilder.setNegativeButton("取消下载",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        initFirst();
                        initLocation();
                    }
                });
        netBuilder.setPositiveButton("继续下载",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateHelper.downLoadAPK(updateInfo);
                      /*  AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
                        asyncDownLoad.execute(updateInfo);*/
                        initFirst();
                        initLocation();
                    }
                });
        AlertDialog netDialog = netBuilder.create();
        netDialog.setCanceledOnTouchOutside(false);
        netDialog.show();
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
        if (results1.size() == 0 || results1.get(0).getUser_id() == 0) {
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
            finish();
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

    /**
     * 获取位置信息
     */
    private void initLocation() {
        Const.GetShareData(getApplicationContext());


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
        //打印出当前位置
        Log.i("TAG", "location.getAddrStr()=" + bdLocation.getAddrStr());
        //打印出当前城市
        Log.i("TAG", "location.getCity()=" + bdLocation.getCity());
        //返回码
        int i = bdLocation.getLocType();
        //LatLng llA = new LatLng(36.710353, 117.086401);
        LatLng llA = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        Const.LocLat = bdLocation.getLatitude();
        Const.LocLng = bdLocation.getLongitude();
        if (bdLocation.getCity() != null) {
            Const.Province = bdLocation.getProvince();
            Const.City = bdLocation.getCity();
            Const.Zone = bdLocation.getDistrict();
            Const.UserLocation = bdLocation.getCity();
        }
        Log.e(TAG, "onReceiveLocation: " + Province + City + Const.Zone);
        if (!Province.equals("")) {
            Const.SetShareData(getApplicationContext());
        }

        locationService.stop();
    }

    @Override
    public void onStartCheck() {
        Log.e(TAG, "onStartCheck: 检查最新版本");
    }

    @Override
    public void onFinishCheck(UpdateInfo info) {

    }

    @Override
    public void onStartDownload() {

    }

    @Override
    public void onDownloading(int progress) {

    }

    @Override
    public void onFinshDownload() {

    }
}
