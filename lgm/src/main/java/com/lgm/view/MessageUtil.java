package com.lgm.view;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/24 8:32
 * 修改人：whisper
 * 修改时间：2016/12/24 8:32
 * 修改备注：
 */
public class MessageUtil {

    static Context mContext;
    static String StrMessage = "";

    public static void AltertMessage(Context context, String message) {
        mContext = context;
        StrMessage = message;
        Handler mHandler = new Handler(mContext.getMainLooper());
        mHandler.post(AlertThisMessage);
    }

    static Runnable AlertThisMessage = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(mContext.getApplicationContext(), StrMessage, Toast.LENGTH_SHORT).show();
        }
    };
}
