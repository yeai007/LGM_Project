package com.hopeofseed.hopeofseed.util;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Activitys.HomePageActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.LoginAcitivity;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static com.hopeofseed.hopeofseed.Activitys.MessageFragment.MESSAGE_UPDATE_LIST;
import static com.hopeofseed.hopeofseed.Activitys.NewsFragment.NEWS_UPDATE_LIST;
import static com.hopeofseed.hopeofseed.Activitys.UserInfoFragment.UPDATE_USER_INFO;
import static com.hopeofseed.hopeofseed.Data.Const.isJpushLogin;

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
    public static String MESSAGE_RECEIVE = "MESSAGE_RECEIVE";
    Handler mHandler;
    private int user_id = 0;
    private boolean isRunning = false;
    String userName;
     String password = "jpush_123456";
    public JpushUtil(Context context) {
        super();
        this.mContext = context;
        this.mHandler = new Handler(mContext.getMainLooper());
    }

    public void initJpushUser() {
        isRunning = true;
        initCount = initCount + 1;
        userName = Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim();

        if (userName.equals(Const.JPUSH_PREFIX + "0")) {
            // Log.e(TAG, "initJpushUser: refresh initJpushUser" + Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim());
            mHandler.postDelayed(init, 500);

        } else {
            //  Log.e(TAG, "initJpushUser: to initJpushUser" + Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim());
            /**=================     调用SDk登陆接口    =================*/
            JMessageClient.login(userName, password, new BasicCallback() {
                @Override
                public void gotResult(int responseCode, String LoginDesc) {
                    Log.e(TAG, "gotResult: " + responseCode);
                    if (responseCode == 0) {
                        isRunning = false;
                        mHandler.removeCallbacks(init);
                        isJpushLogin = true;

                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, HomePageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        JPushInterface.setAlias(mContext, Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim(), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                if (i == 0) {
                                    Log.e(TAG, "gotResult: jpush别名设置成功");
                                } else {
                                    Log.e(TAG, "gotResult: 别名设置失败");
                                }
                            }
                        });
                        Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                        Intent intent_update = new Intent();  //Itent就是我们要发送的内容
                        intent_update.setAction(MESSAGE_RECEIVE);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                        mContext.sendBroadcast(intent_update);   //发送广播
                        Intent intent_update_userinfo = new Intent();  //Itent就是我们要发送的内容
                        intent_update_userinfo.setAction(UPDATE_USER_INFO);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                        mContext.sendBroadcast(intent_update_userinfo);   //发送广播
                        Intent intent_update_new_list = new Intent();  //Itent就是我们要发送的内容
                        intent_update_new_list.setAction(NEWS_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                        mContext.sendBroadcast(intent_update_new_list);   //发送广播
                        updateJpushNickName();
                    } else if (responseCode == 871308) {
                        Log.e(TAG, "gotResult: 尚未初始化/初始化失败->重新初始化");
                        JMessageClient.init(mContext);
                        mHandler.postDelayed(init, 500);
                    } else if (responseCode == 871201) {
                        Log.e(TAG, "gotResult: 登录超时");
                        JMessageClient.init(mContext);
                        mHandler.postDelayed(init, 500);
                        if (initCount > 10) {
                            Toast.makeText(mContext, "登录超时,请稍后再试！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, LoginAcitivity.class);
                            mContext.startActivity(intent);
                        }
                    } else if (responseCode == 801003) {
                        addJpushUserData();
                    } else if (responseCode == 801004) {
                        JMessageClient.updateUserPassword(userName, password, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if(i==0)
                                {
                                    mHandler.postDelayed(init, 500);
                                }
                                else
                                {
                                    Toast.makeText(mContext, "登录失败，请联系管理员！", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "gotResult: "+s+i);
                                }
                            }
                        });
                    } else {
                        Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                    }


                }
            });
        }
    }

    Runnable init = new Runnable() {
        @Override
        public void run() {
            initJpushUser();
        }
    };

    private void addJpushUserData() {
        final String userName = Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id).trim();
        final String password ="jpush_123456";
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
                    Intent intent = new Intent();  //Itent就是我们要发送的内容
                    intent.setAction(MESSAGE_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                    mContext.sendBroadcast(intent);   //发送广播
                    Log.i(TAG, "昵称修改成功 " + ", responseCode = " + responseCode + " ; registerDesc = " + s);
                } else {
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + s);
                }
            }
        });
        JMessageClient.getMyInfo().setSignature(Const.currentUser.user_role);
        JMessageClient.updateMyInfo(UserInfo.Field.signature, JMessageClient.getMyInfo(), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String s) {
                if (responseCode == 0) {
                    Log.i(TAG, "用户级别修改成功 " + ", responseCode = " + responseCode + " ; registerDesc = " + s);
                } else {
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + s);
                }
            }
        });
    }

    private void getUserJpushInfo(String user_name, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                Const.mUserInfo = userInfo;
            }
        });
    }
}
