package com.hopeofseed.hopeofseed.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Data.Const;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/17 17:07
 * 修改人：whisper
 * 修改时间：2016/11/17 17:07
 * 修改备注：
 */
public class JpushUtil {
    private static final String TAG = "JpushUtil";
    Context mContext;
    private int initCount = 0;

    public JpushUtil(Context context) {
        super();
        this.mContext = context;
    }

    public void initJpushUser() {
        initCount = initCount + 1;
        Log.e(TAG, "initJpushUser: " + initCount + Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim());
        final String userName = Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim();
        final String password = Const.currentUser.password;
        if (userName.equals(Const.JPUSH_PREFIX + "0")) {
            Log.e(TAG, "initJpushUser: refresh initJpushUser" + Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim());
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initJpushUser();
                }
            }, 1000);

        } else {
            Log.e(TAG, "initJpushUser: to initJpushUser" + Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim());
            /**=================     调用SDk登陆接口    =================*/
            JMessageClient.login(userName, password, new BasicCallback() {
                @Override
                public void gotResult(int responseCode, String LoginDesc) {
                    if (responseCode == 0) {
                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                        updateJpushNickName();
                    } else if (responseCode == 871308) {
                        Log.e(TAG, "gotResult: 尚未初始化/初始化失败->重新初始化");
                        JMessageClient.init(mContext);
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initJpushUser();
                            }
                        }, 1000);
                    } else {
                        if (responseCode == 801003) {
                            addJpushUserData();
                        } else {
                            Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                        }

                    }
                }
            });
        }
    }

    private void addJpushUserData() {
        final String userName = Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim();
        final String password = Const.currentUser.password;
/**=================     调用SDK注册接口    =================*/
        JMessageClient.register(userName, password, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String registerDesc) {
                if (responseCode == 0) {
                    Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                    Log.i("RegisterActivity", "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                    initJpushUser();
                } else {

                    Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                    Log.i("RegisterActivity", "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                }
            }
        });
    }

    private void updateJpushNickName() {
        JMessageClient.getMyInfo().setNickname(Const.currentUser.nickname);
        JMessageClient.updateMyInfo(UserInfo.Field.nickname, JMessageClient.getMyInfo(), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String s) {
                if (responseCode == 0) {
                    Log.i(TAG, "昵称修改成功 " + ", responseCode = " + responseCode + " ; registerDesc = " + s);
                } else {
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + s);
                }
            }
        });
    }
}
