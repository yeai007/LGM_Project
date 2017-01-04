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
import com.hopeofseed.hopeofseed.DataForHttp.GetPhoneCode;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommHttpResult;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommHttpResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;
import com.lgm.utils.TimeCountUtil;

import java.util.HashMap;
import java.util.Random;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/26 10:35
 * 修改人：whisper
 * 修改时间：2016/10/26 10:35
 * 修改备注：
 */
public class AlertPhoneActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "AlertPhoneActivity";
    Button get_code;
    EditText et_phonecode, et_phone;
    Button btn_topright;
    String StrPhoneCode;
    CommHttpResult commHttpResult = new CommHttpResult();
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();
    Handler handler = new Handler();
    CommResultTmp commResultTmp = new CommResultTmp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_phone_activity);
        Intent intent = getIntent();
        mUserDataNoRealm = intent.getParcelableExtra("User");
        initView();
    }

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("修改手机");
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("更新");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        get_code = (Button) findViewById(R.id.get_code);
        get_code.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phonecode = (EditText) findViewById(R.id.et_phonecode);
        StrPhoneCode = getRandomCode();
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
        Log.e(TAG, "getData: 更新手机号");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("UserName", String.valueOf(Const.currentUser.user_name));
        opt_map.put("NewPhone", et_phone.getText().toString().trim());
        opt_map.put("PhoneCode", et_phonecode.getText().toString().trim());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "UpdatePhone.php", opt_map, CommHttpResultTmp.class, this);
    }

    private void getPhoneCode() {
        Log.e(TAG, "getData: 获取验证码");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("mobile", et_phone.getText().toString().trim());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getPhoneCode.php", opt_map, CommResultTmp.class, this);
    }


    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("getPhoneCode")) {
            commResultTmp = ObjectUtil.cast(rspBaseBean);
            handler.post(updateResult);
        } else {
            CommHttpResultTmp mCommHttpResultTmp = ObjectUtil.cast(rspBaseBean);
            commHttpResult = mCommHttpResultTmp.getDetail().get(0);
            updateView();
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

            Toast.makeText(getApplicationContext(), commResultTmp.getDetail(), Toast.LENGTH_SHORT).show();

        }
    };

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (commHttpResult.equals("1")) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), commHttpResult.getResult(), Toast.LENGTH_SHORT).show();
                //finish();
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
                    AlertPhoneActivity.this, 60000, 1000, get_code);
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
}
