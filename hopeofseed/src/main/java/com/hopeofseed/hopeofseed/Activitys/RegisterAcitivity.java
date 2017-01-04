package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.DataForHttp.GetPhoneCode;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;
import com.lgm.utils.TimeCountUtil;

import java.util.HashMap;
import java.util.Random;

import io.realm.Realm;

import static com.hopeofseed.hopeofseed.R.id.et_phone;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/23 14:25
 * 修改人：whisper
 * 修改时间：2016/8/23 14:25
 * 修改备注：
 */
public class RegisterAcitivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    String TAG = "RegisterAcitivity";
    EditText et_username, et_password, et_phonecode;
    Button btn_register, get_code;
    String phone_code = "";
    Handler mHandler = new Handler();
    String RegisterError = "注册失败";
    Handler handler = new Handler();
    CommResultTmp commResultTmp = new CommResultTmp();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        Button btn_topleft = (Button) findViewById(R.id.btn_topleft);
        btn_topleft.setOnClickListener(this);
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("个人用户注册");
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("注册");
        btn_topright.setVisibility(View.VISIBLE);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_phonecode = (EditText) findViewById(R.id.et_phonecode);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        get_code = (Button) findViewById(R.id.get_code);
        get_code.setOnClickListener(this);
    }

    private String getRandomCode() {
        Random rad = new Random();
        String result = rad.nextInt(1000000) + "";
        if (result.length() != 4) {
            return getRandomCode();
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_register:
                UserRegister(et_username.getText().toString().trim(), et_password.getText().toString().trim(), et_phonecode.getText().toString().trim());
                break;
            case R.id.get_code:
                Toast.makeText(getApplicationContext(), "获取验证码", Toast.LENGTH_SHORT).show();
                if (et_username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入手机号",
                            Toast.LENGTH_SHORT).show();

                } else {
                    TimeCountUtil timeCountUtil = new TimeCountUtil(
                            RegisterAcitivity.this, 60000, 1000, get_code);
                    timeCountUtil.start();
                    getPhoneCode();
                }

                break;
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                UserRegister(et_username.getText().toString().trim(), et_password.getText().toString().trim(), et_phonecode.getText().toString().trim());
                break;

        }
    }

    private void getPhoneCode() {
        // TODO Auto-generated method stub
        Log.e(TAG, "getData: 获取验证码");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("mobile", et_username.getText().toString().trim());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getPhoneCode.php", opt_map, CommResultTmp.class, this);
    }



    protected void UserRegister(final String username, final String password, final String phonecode) {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserName", username);
        opt_map.put("PassWord", ObjectUtil.md5(password));
        opt_map.put("PhoneCode", phonecode);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "app_register.php", opt_map, UserDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("AppRegister")) {
            Log.e(TAG, "onRegister: 成功");
            updateRealmData(rspBaseBean);
        }else  if (rspBaseBean.RequestSign.equals("getPhoneCode")) {
            commResultTmp = ObjectUtil.cast(rspBaseBean);
            handler.post(updateGetCodeResult);
        }
    }

    @Override
    public void onError(final String error) {
        Log.e(TAG, "onError: " + error);
        RegisterError = error;
        mHandler.post(showError);

    }

    @Override
    public void onFail() {

    }

    Runnable showError = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), RegisterError, Toast.LENGTH_SHORT).show();
        }
    };

    private void updateRealmData(RspBaseBean rspBaseBean) {
        Realm updateRealm = Realm.getDefaultInstance();
        updateRealm.beginTransaction();//开启事务
        UserData updateUserData = updateRealm.where(UserData.class)
                .equalTo("iscurrent", 1)//查询出name为name1的User对象
                .findFirst();//修改查询出的第一个对象的名字
        if (updateUserData != null) {
            updateUserData.setIsCurrent(0);
        }
        updateRealm.commitTransaction();
        Log.e(TAG, "updateRealmData: 1");
        UserDataTmp mUserDataTmp = new UserDataTmp();
        mUserDataTmp = ObjectUtil.cast(rspBaseBean);
        mUserDataTmp.getDetail().setIsCurrent(1);
        UserData mUserDataTmp1 = new UserData();
        mUserDataTmp1 = mUserDataTmp.getDetail();
        Realm insertRealm = Realm.getDefaultInstance();
        insertRealm.beginTransaction();
        UserData insertUserData = insertRealm.copyToRealmOrUpdate(mUserDataTmp1);
        insertRealm.commitTransaction();
        Log.e(TAG, "updateRealmData: 2");
        Const.currentUser.user_id = insertUserData.getUser_id();
        Const.currentUser.user_name = insertUserData.getUser_name();
        Const.currentUser.password = insertUserData.getPassword();
        Const.currentUser.nickname = insertUserData.getNickname();
        Const.currentUser.user_mobile = insertUserData.getUser_mobile();
        Const.currentUser.user_email = insertUserData.getUser_email();
        Const.currentUser.createtime = insertUserData.getCreatetime();
        Const.currentUser.user_permation = insertUserData.getUser_permation();
        Const.currentUser.user_role = insertUserData.getUser_role();
        Const.currentUser.user_role_id = insertUserData.getUser_role_id();
        Const.currentUser.user_field = insertUserData.getUser_field();
        Const.currentUser.iscurrent = insertUserData.getIsCurrent();
        Log.e(TAG, "updateRealmData: " + Const.currentUser.toString());
        mHandler.post(updateResult);

    }

    Runnable updateResult = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(RegisterAcitivity.this, HomePageActivity.class);
            startActivity(intent);
        }
    };
    Runnable updateGetCodeResult = new Runnable() {
        @Override
        public void run() {

            Toast.makeText(getApplicationContext(), commResultTmp.getDetail(), Toast.LENGTH_SHORT).show();

        }
    };
}
