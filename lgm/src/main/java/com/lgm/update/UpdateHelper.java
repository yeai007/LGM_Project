package com.lgm.update;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.lgm.net.HttpRequest;
import com.lgm.net.URLUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import java.io.File;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/5 9:04
 * 修改人：whisper
 * 修改时间：2016/12/5 9:04
 * 修改备注：
 */
public class UpdateHelper {
    private static final String TAG = "UpdateHelper";
    private Context mContext;
    private String checkUrl;
    private boolean isAutoInstall;
    private boolean isHintVersion;
    private OnUpdateListener updateListener;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder ntfBuilder;

    private static final int UPDATE_NOTIFICATION_PROGRESS = 0x1;
    private static final int COMPLETE_DOWNLOAD_APK = 0x2;
    private static final int DOWNLOAD_NOTIFICATION_ID = 0x3;
    private static final String PATH = Environment
            .getExternalStorageDirectory().getPath();
    private static final String SUFFIX = ".apk";
    private static final String APK_PATH = "APK_PATH";
    private static final String APP_NAME = "APP_NAME";
  //  private SharedPreferences preferences_update;

    private HashMap<String, String> cache = new HashMap<String, String>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_NOTIFICATION_PROGRESS:
                    showDownloadNotificationUI((UpdateInfo) msg.obj, msg.arg1);
                    break;
                case COMPLETE_DOWNLOAD_APK:
                    if (UpdateHelper.this.isAutoInstall) {
                        installApk(Uri.parse("file://" + cache.get(APK_PATH)));
                    } else {
                        if (ntfBuilder == null) {
                            ntfBuilder = new NotificationCompat.Builder(mContext);
                        }
                        ntfBuilder.setSmallIcon(mContext.getApplicationInfo().icon)
                                .setContentTitle(cache.get(APP_NAME))
                                .setContentText("下载完成，点击安装").setTicker("任务下载完成");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(
                                Uri.parse("file://" + cache.get(APK_PATH)),
                                "application/vnd.android.package-archive");
                        PendingIntent pendingIntent = PendingIntent.getActivity(
                                mContext, 0, intent, 0);
                        ntfBuilder.setContentIntent(pendingIntent);
                        if (notificationManager == null) {
                            notificationManager = (NotificationManager) mContext
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                        }
                        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,
                                ntfBuilder.build());
                    }
                    break;
            }
        }

    };

    private UpdateHelper(Builder builder) {
        this.mContext = builder.context;
        this.checkUrl = builder.checkUrl;
        this.isAutoInstall = builder.isAutoInstall;
        this.isHintVersion = builder.isHintNewVersion;
        /*preferences_update = mContext.getSharedPreferences("Updater",
                Context.MODE_PRIVATE);*/
    }

    /**
     * 检查app是否有新版本，check之前先Builer所需参数
     */
    public void check() {
        check(null);
    }

    public void check(OnUpdateListener listener) {
        if (listener != null) {
            this.updateListener = listener;
        }
        if (mContext == null) {
            Log.e("NullPointerException", "The context must not be null.");
            return;
        }
        AsyncCheck asyncCheck = new AsyncCheck();
        asyncCheck.execute(checkUrl);
    }





    /**
     * 通知栏弹出下载提示进度
     *
     * @param updateInfo
     * @param progress
     */
    private void showDownloadNotificationUI(UpdateInfo updateInfo,
                                            final int progress) {
        if (mContext != null) {
            String contentText = new StringBuffer().append(progress)
                    .append("%").toString();
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                    0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            if (notificationManager == null) {
                notificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (ntfBuilder == null) {
                ntfBuilder = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(mContext.getApplicationInfo().icon)
                        .setTicker("开始下载...")
                        .setContentTitle(updateInfo.getAppName())
                        .setContentIntent(contentIntent);
            }
            ntfBuilder.setContentText(contentText);
            ntfBuilder.setProgress(100, progress, false);
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,
                    ntfBuilder.build());
        }
    }


    /**
     * 检查更新任务
     */
    private class AsyncCheck extends AsyncTask<String, Integer, UpdateInfo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (UpdateHelper.this.updateListener != null) {
                UpdateHelper.this.updateListener.onStartCheck();
            }
        }

        @Override
        protected UpdateInfo doInBackground(String... params) {
            UpdateInfo updateInfo = null;
            if (params.length == 0) {
                Log.e("NullPointerException",
                        " Url parameter must not be null.");
                return null;
            }
            String url = params[0];
            if (!URLUtils.isNetworkUrl(url)) {
                Log.e("IllegalArgumentException", "The URL is invalid.");
                return null;
            }
            try {
                updateInfo = JSONHandler.toUpdateInfo(HttpRequest.get(url));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return updateInfo;
        }

        @Override
        protected void onPostExecute(UpdateInfo updateInfo) {
            super.onPostExecute(updateInfo);
            if (UpdateHelper.this.updateListener != null) {
                UpdateHelper.this.updateListener.onFinishCheck(updateInfo);
            }
        }
    }

    /**
     * 文件下载
     */
    public void downLoadAPK(final UpdateInfo updateInfo) {

        OkGo.get(updateInfo.getApkUrl())//
                .tag(this)//
                .execute(new FileCallback(PATH + File.separator + updateInfo.getAppName(),
                        updateInfo.getAppName() + updateInfo.getVersionName() + SUFFIX) {  //文件下载时，需要指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        // file 即为文件数据，文件保存在指定目录
                        String apkName = updateInfo.getAppName()
                                + updateInfo.getVersionName() + SUFFIX;
                        cache.put(APP_NAME, updateInfo.getAppName() + updateInfo.getVersionName() + SUFFIX);
                        cache.put(APK_PATH,
                                PATH + File.separator + updateInfo.getAppName()
                                        + File.separator + apkName);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //Log.e(TAG, "downloadProgress: currentSize"+String.valueOf(currentSize)+"totalSize:"+String.valueOf(totalSize)+"progress"+String.valueOf(progress)+"networkSpeed"+String.valueOf(networkSpeed) );
                        //  Log.e(TAG, "downloadProgress: "+String.valueOf((int)(progress*100f)));
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                        int int_progress = (int) (progress * 100f);
                        if (int_progress % 5 == 0) {
                            Log.e(TAG, "downloadProgress:NOTIFICATION " + int_progress);
                            handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS,
                                    int_progress, -1, updateInfo).sendToTarget();
                        }
                        if (UpdateHelper.this.updateListener != null) {
                            Log.e(TAG, "downloadProgress: onDownloading" + int_progress);
                            UpdateHelper.this.updateListener
                                    .onDownloading(int_progress);
                        }
                        if (int_progress == 100) {

                            handler.obtainMessage(COMPLETE_DOWNLOAD_APK,
                                    int_progress, -1, updateInfo).sendToTarget();
                        }
                    }
                });
    }

    public static class Builder {
        private Context context;
        private String checkUrl;
        private boolean isAutoInstall = true;
        private boolean isHintNewVersion = true;

        public Builder(Context ctx) {
            this.context = ctx;
        }

        /**
         * 检查是否有新版本App的URL接口路径
         *
         * @param checkUrl
         * @return
         */
        public Builder checkUrl(String checkUrl) {
            this.checkUrl = checkUrl;
            return this;
        }

        /**
         * 是否需要自动安装, 不设置默认自动安装
         *
         * @param isAuto true下载完成后自动安装，false下载完成后需在通知栏手动点击安装
         * @return
         */
        public Builder isAutoInstall(boolean isAuto) {
            this.isAutoInstall = isAuto;
            return this;
        }

        /**
         * 当没有新版本时，是否Toast提示
         *
         * @param isHint
         * @return true提示，false不提示
         */
        public Builder isHintNewVersion(boolean isHint) {
            this.isHintNewVersion = isHint;
            return this;
        }

        /**
         * 构造UpdateManager对象
         *
         * @return
         */
        public UpdateHelper build() {
            return new UpdateHelper(this);
        }
    }

    private void installApk(Uri data) {
        if (mContext != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(data, "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
            if (notificationManager != null) {
                notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
            }
        } else {
            Log.e("NullPointerException", "The context must not be null.");
        }

    }

}
