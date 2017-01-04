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

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommHttpResult;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommHttpResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.LoginAcitivity;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;
import com.lgm.utils.TimeCountUtil;

import java.util.HashMap;
import java.util.Random;

import cn.jpush.im.android.api.JMessageClient;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/4 16:29
 * 修改人：whisper
 * 修改时间：2017/1/4 16:29
 * 修改备注：
 */
public class ForgetPassWordActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "ForgetPassWordActivity";
    Button get_code;
    EditText et_phonecode, et_phone;
    Button btn_topright;
    String StrPhoneCode;
    CommHttpResult commHttpResult = new CommHttpResult();
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();
    Handler handler = new Handler();
    CommResultTmp commResultTmp = new CommResultTmp();
    CommResultTmp updateResultTmp = new CommResultTmp();
    boolean isCanAlert = false;
    EditText et_new_password, et_format;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);
        initView();
    }

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("修改手机");
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("修改");

        btn_topright.setOnClickListener(this);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        get_code = (Button) findViewById(R.id.get_code);
        get_code.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phonecode = (EditText) findViewById(R.id.et_phonecode);
        StrPhoneCode = getRandomCode();
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_format = (EditText) findViewById(R.id.et_format);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.get_code:
                getNewPhoneCode();
                break;
            case R.id.btn_topright:
                updatePhone();
                break;
        }
    }

    private void updatePhone() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("phone", et_phone.getText().toString().trim());
        opt_map.put("PhoneCode", et_phonecode.getText().toString().trim());
        opt_map.put("PassWord", ObjectUtil.md5(et_new_password.getText().toString().trim()));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "ForgetPasswordToUpdate.php", opt_map, CommResultTmp.class, this);
    }

    private void getPhoneCode() {
        Log.e(TAG, "getData: 获取验证码");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("mobile", et_phone.getText().toString().trim());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getForgetPhoneCode.php", opt_map, CommResultTmp.class, this);
    }


    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("getForgetPhoneCode")) {
            commResultTmp = ObjectUtil.cast(rspBaseBean);
            handler.post(updateResult);
        } else if (rspBaseBean.RequestSign.equals("ForgetPasswordToUpdate")) {
            updateResultTmp = ObjectUtil.cast(rspBaseBean);
            handler.post(updatePasswordResult);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateResult = new Runnable() {
        @Override
        public void run() {
            if (commResultTmp.getDetail().equals("提交成功") || commResultTmp.getDetail().equals("同一手机号验证码短信发送超出5条")) {
                isCanAlert = true;
                btn_topright.setVisibility(View.VISIBLE);
            } else {
                isCanAlert = false;
                Toast.makeText(getApplicationContext(), commResultTmp.getDetail(), Toast.LENGTH_SHORT).show();
            }
        }
    };
    Runnable updatePasswordResult = new Runnable() {
        @Override
        public void run() {
            if (updateResultTmp.getDetail().equals("验证码错误")) {
                Toast.makeText(getApplicationContext(), updateResultTmp.getDetail(), Toast.LENGTH_SHORT).show();
            } else if (updateResultTmp.getDetail().equals("1")) {
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginAcitivity.class);
                startActivity(intent);
            } else if (updateResultTmp.getDetail().equals("2")) {
                Toast.makeText(getApplicationContext(), "修改失败，请重新尝试修改", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 获取验证码逻辑
     */
    private void getNewPhoneCode() {
        if (et_phone.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请输入手机号",
                    Toast.LENGTH_SHORT).show();

        } else {
            TimeCountUtil timeCountUtil = new TimeCountUtil(
                    ForgetPassWordActivity.this, 60000, 1000, get_code);
            timeCountUtil.start();
            getPhoneCode();
        }
    }

    private String getRandomCode() {
        Random rad = new Random();
        String result = rad.nextInt(10000) + "";
        if (result.length() != 4) {
            return getRandomCode();
        }
        return result;
    }
}
