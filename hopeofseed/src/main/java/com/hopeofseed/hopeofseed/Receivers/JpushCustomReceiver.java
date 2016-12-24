package com.hopeofseed.hopeofseed.Receivers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.NotifyData;
import com.hopeofseed.hopeofseed.util.NullStringToEmptyAdapterFactory;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.helpers.IMReceiver;
import io.realm.Realm;

import static com.hopeofseed.hopeofseed.Activitys.MessageFragment.MESSAGE_UPDATE_LIST;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/15 10:43
 * 修改人：whisper
 * 修改时间：2016/11/15 10:43
 * 修改备注：
 */
public class JpushCustomReceiver extends IMReceiver {
    private static final String TAG = "JpushCustomReceiver";

    private NotificationManager nm;
    Context mContext;
    Handler mHandler = new Handler();

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        // Log.e(TAG, "onReceive - " + intent.getAction() + ", extras: " + bundle);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.e(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的通知");
           /* Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);*/
            receivingNotification(context, bundle);
            mHandler.postDelayed(rSendUpdateMessage, 1000);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "用户点击打开了通知");
            openNotification(context, bundle);
        } else if (intent.getAction().equals("cn.jpush.im.android.action.IM_RESPONSE")) {
            Log.e(TAG, "cn.jpush.im.android.action.IM_RESPONSE");
            mHandler.postDelayed(rSendUpdateMessage, 1000);
        } else if (intent.getAction().equals("cn.jpush.im.android.action.NOTIFICATION_CLICK_PROXY")) {
            Log.e(TAG, "onReceive:用户打开了通知");
        } else {
            Log.e(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.e(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.e(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG, "extras : " + extras);
        try {
            JSONObject extrasJson = new JSONObject(extras);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            NotifyData insertNotifyData = new NotifyData();
            insertNotifyData = gson.fromJson(extras, NotifyData.class);
            insertNotifyData.setNotifyToUser(String.valueOf(Const.currentUser.user_id));
            insertRealm(insertNotifyData);
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
    }


    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            NotifyData insertNotifyData = new NotifyData();
            insertNotifyData = gson.fromJson(extras, NotifyData.class);
            insertNotifyData.setNotifyToUser(String.valueOf(Const.currentUser.user_id));
            Log.e(TAG, "openNotification: " + Const.currentUser.user_id);
         //   insertRealm(insertNotifyData);

        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
    }

    Runnable rSendUpdateMessage = new Runnable() {
        @Override
        public void run() {
            Intent intent_update = new Intent();  //Itent就是我们要发送的内容
            intent_update.setAction(MESSAGE_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
            mContext.sendBroadcast(intent_update);   //发送广播
        }
    };

    private void insertRealm(NotifyData insertNotifyData) {
        Realm insertRealm = Realm.getDefaultInstance();
        insertRealm.beginTransaction();
        NotifyData inNotifyData = insertRealm.copyToRealmOrUpdate(insertNotifyData);
        insertRealm.commitTransaction();
       // Log.e(TAG, "openNotification: " + inNotifyData.getNotifyIsRead() + inNotifyData.getNotifyToUser());
    }

}
