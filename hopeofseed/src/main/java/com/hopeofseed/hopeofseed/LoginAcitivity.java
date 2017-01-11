package com.hopeofseed.hopeofseed;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Activitys.CompanyRegActivity;
import com.hopeofseed.hopeofseed.Activitys.ForgetPassWordActivity;
import com.hopeofseed.hopeofseed.Activitys.PubishMainActivity;
import com.hopeofseed.hopeofseed.Activitys.RegisterAcitivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataTmp;
import com.hopeofseed.hopeofseed.curView.WeiboDialogUtils;
import com.hopeofseed.hopeofseed.util.JpushUtil;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/25 10:01
 * 修改人：whisper
 * 修改时间：2016/7/25 10:01
 * 修改备注：
 */
public class LoginAcitivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    String TAG = "LoginActicity";
    TextView btn_login, btn_register, btn_register_company;
    String user_name, pass_word;
    Handler mHandle = new Handler();
    private String mError;
    WeiboDialogUtils dialogUtils;
    TextView btn_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        dialogUtils = new WeiboDialogUtils(LoginAcitivity.this);
    }

    private void initView() {
        btn_login = (TextView) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_register_company = (TextView) findViewById(R.id.btn_register_company);
        btn_register_company.setOnClickListener(this);
        btn_forget = (TextView) findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_login:

                user_name = ((EditText) findViewById(R.id.et_username)).getText().toString();
                pass_word = ((EditText) findViewById(R.id.et_password)).getText().toString();
                if (isChecked(user_name, pass_word)) {
                    dialogUtils.showDialog("正在登录\n请稍候...");
                    UserLogin(user_name, pass_word, "0");
                }

                break;
            case R.id.btn_register:
                intent = new Intent(getApplicationContext(), RegisterAcitivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register_company:
                intent = new Intent(getApplicationContext(), CompanyRegActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forget:
                intent = new Intent(getApplicationContext(), ForgetPassWordActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean isChecked(String username, String password) {
        boolean ischeck = true;
        if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写手机号/用户名", Toast.LENGTH_SHORT).show();
            ischeck = false;
        }
        if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写密码", Toast.LENGTH_SHORT).show();
            ischeck = false;
        }
        return ischeck;
    }

    protected void UserLogin(final String username, final String password, final String login_register_type) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserName", username);
        opt_map.put("PassWord", ObjectUtil.md5(password));
        opt_map.put("Type", login_register_type);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "app_login.php", opt_map, UserDataTmp.class, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.resultNote.equals("success")) {
            updateRealmData(rspBaseBean);
        } else {
            updateView(rspBaseBean);
        }

    }

    @Override
    public void onError(String error) {
        mError = error;
        mHandle.post(rspResult);
        dialogUtils.closeDialog();
    }

    @Override
    public void onFail() {
        dialogUtils.closeDialog();
    }


    Runnable rspResult = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), mError, Toast.LENGTH_SHORT).show();
        }
    };

    private void updateView(RspBaseBean rspBaseBean) {
        Message msg = showMsgHandle.obtainMessage();
        msg.arg1 = 1;
        msg.obj = rspBaseBean.resultNote;
        msg.sendToTarget();
    }

    private Handler showMsgHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    private void updateRealmData(RspBaseBean rspBaseBean) {
        UserDataTmp mUserDataTmp = new UserDataTmp();
        mUserDataTmp = ObjectUtil.cast(rspBaseBean);
        Realm updateRealm = Realm.getDefaultInstance();
        updateRealm.beginTransaction();//开启事务
        UserData updateUserData = updateRealm.where(UserData.class)
                .equalTo("iscurrent", 1)
                .findFirst();
        if (updateUserData != null) {
            updateUserData.setIsCurrent(0);
        }
        updateRealm.commitTransaction();
        /*******************************
         * */
        UserData o = mUserDataTmp.getDetail();
        updateRealm.beginTransaction();
        o.setIsCurrent(1);
        UserData newdata = updateRealm.copyToRealmOrUpdate(o);
        updateRealm.commitTransaction();
        Const.currentUser.user_id = newdata.getUser_id();
        Const.currentUser.user_name = newdata.getUser_name();
        Const.currentUser.password = newdata.getPassword();
        Const.currentUser.nickname = newdata.getNickname();
        Const.currentUser.user_mobile = newdata.getUser_mobile();
        Const.currentUser.user_email = newdata.getUser_email();
        Const.currentUser.createtime = newdata.getCreatetime();
        Const.currentUser.user_permation = newdata.getUser_permation();
        Const.currentUser.user_role = newdata.getUser_role();
        Const.currentUser.user_role_id = newdata.getUser_role_id();
        Const.currentUser.user_field = newdata.getUser_field();
        Const.currentUser.iscurrent = newdata.getIsCurrent();
        Const.currentUser.UserAvatar = newdata.getUserAvatar();
        Log.e(TAG, "updateRealmData: " + Const.currentUser.toString());
        if (JPushInterface.isPushStopped(Application.getContext())) {
            JPushInterface.resumePush(Application.getContext());
            JpushUtil jpushUtil = new JpushUtil(LoginAcitivity.this);
            jpushUtil.initJpushUser();
        } else {
            JpushUtil jpushUtil = new JpushUtil(LoginAcitivity.this);
            jpushUtil.initJpushUser();
        }
    }
}
