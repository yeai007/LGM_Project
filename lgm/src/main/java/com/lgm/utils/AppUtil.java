package com.lgm.utils;
import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import java.util.List;

/**
 * @FileName:utils
 * @Desc:获取应用APP相关通用信息
 * @Author:liguangming
 * @Date:2016/3/15
 * @Copyright:Moogeek
 */
public class AppUtil {
    static final String TAG = "AppInfo";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    private AppUtil() throws UnsupportedOperationException {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getAppName(Context context) {
        String appName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName = context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getAppName: " + appName);
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            versionName = packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getVersionName: " + versionName);
        return versionName;
    }
    /**
     * 获取当前app版本
     *
     * @return
     * @throws android.content.pm.PackageManager.NameNotFoundException
     */
    public static PackageInfo getPackageInfo(Context mContext) {
        PackageInfo pinfo = null;
        if (mContext != null) {
            try {
                pinfo = mContext.getPackageManager().getPackageInfo(
                        mContext.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pinfo;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @return boolean
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        if (context == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个服务是否正在运行
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        Log.d(TAG, "isServiceWork: " + isWork);
        return isWork;
    }

    /**
     * 设置为4.4的沉浸式状态栏
     *
     * @param activity 当前Activity对象
     * @param root     当前布局文件中的根view，此view背景色应该与状态栏背景色相同
     */
    public static void setchenjin(Activity activity, View root) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (root != null) {
                Log.e("状态栏高度：", String.valueOf(getStatusHeight(activity)));
                root.setPadding(0, getStatusHeight(activity), 0, 0);
            }

        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return int
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getStatusHeight: " + String.valueOf(statusHeight));
        return statusHeight;
    }
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}
