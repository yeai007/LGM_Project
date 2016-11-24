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
import com.hopeofseed.hopeofseed.DataForHttp.UserRegister;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.TimeCountUtil;

import java.util.Random;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/23 14:25
 * 修改人：whisper
 * 修改时间：2016/8/23 14:25
 * 修改备注：
 */
public class RegisterAcitivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "RegisterAcitivity";
    EditText et_username, et_password, et_phonecode;
    Button btn_register, get_code;
    String phone_code = "";

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetPhoneCode getPhoneCode = new GetPhoneCode();
                getPhoneCode.mobile = et_username.getText().toString().trim();
                phone_code = getRandomCode();
                getPhoneCode.content = "您的验证码是：" + phone_code + "。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
                Boolean bRet = getPhoneCode.RunData();
                Message msg = getPhoneCodeUserHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.obj = getPhoneCode.dataMessage.obj;
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler getPhoneCodeUserHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    protected void UserRegister(final String username, final String password, final String phonecode) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserRegister userRegister = new UserRegister();
                userRegister.UserName = username;
                userRegister.PassWord = password;
                userRegister.PhoneCode = phonecode;
                Boolean bRet = userRegister.RunData();
                Message msg = loginUserHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = userRegister.dataMessage.arg1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler loginUserHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    Intent intent = new Intent(RegisterAcitivity.this, HomePageActivity.class);
                    startActivity(intent);
                    Log.e(TAG, "handleMessage: " + Const.currentUser.user_name + Const.currentUser.password);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "handleMessage: " + Const.currentUser.user_name + Const.currentUser.password);
                    break;
            }
        }
    };
}
