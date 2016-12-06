package com.hopeofseed.hopeofseed.Receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.lgm.utils.ObjectUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.helpers.IMReceiver;

import static android.R.attr.keySet;
import static android.R.attr.lockTaskMode;


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

    @Override
    public void onReceive(Context context, Intent intent) {
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


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "用户点击打开了通知");
     /*       Bundle bundle = intent.getExtras();
            openNotification(context, bundle);*/

        }
        else if(intent.getAction().equals("cn.jpush.im.android.action.IM_RESPONSE"))
        {
        }
        else if(intent.getAction().equals("cn.jpush.im.android.action.NOTIFICATION_CLICK_PROXY"))
        {
            Log.e(TAG, "onReceive:用户打开了通知" );

        }
        else {
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
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
      /*  if (TYPE_THIS.equals(myValue)) {
            Intent mIntent = new Intent(context, ThisActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        } else if (TYPE_ANOTHER.equals(myValue)){
            Intent mIntent = new Intent(context, AnotherActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }*/
    }
}
