package com.hopeofseed.hopeofseed;

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
import com.hopeofseed.hopeofseed.Activitys.HomePageActivity;
import com.hopeofseed.hopeofseed.Activitys.RegisterAcitivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataTmp;
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
    Realm myRealm = Realm.getDefaultInstance();
    Handler mHandle = new Handler();
    private String mError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        btn_login = (TextView) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_register_company = (TextView) findViewById(R.id.btn_register_company);
        btn_register_company.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_login:
/*                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
                user_name = ((EditText) findViewById(R.id.et_username)).getText().toString();
                pass_word = ((EditText) findViewById(R.id.et_password)).getText().toString();
                if (isChecked(user_name, pass_word)) {
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
        opt_map.put("PassWord", password);
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

    }

    @Override
    public void onFail() {

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
                .equalTo("iscurrent", 1)//查询出name为name1的User对象
                .findFirst();//修改查询出的第一个对象的名字
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
        Log.e(TAG, "updateRealmData: " + Const.currentUser.toString());
        if (JPushInterface.isPushStopped(Application.getContext())) {
            JPushInterface.resumePush(Application.getContext());
            Application.initJpushLogin();
        }

        Intent intent = new Intent(LoginAcitivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
