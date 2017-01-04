package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.LoginAcitivity;
import com.hopeofseed.hopeofseed.R;

import static com.hopeofseed.hopeofseed.R.id.et_phone;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/19 17:02
 * 修改人：whisper
 * 修改时间：2016/8/19 17:02
 * 修改备注：
 */
public class SecuritySetting extends AppCompatActivity implements View.OnClickListener {
    Button alert_phone;
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();
    Button alert_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_setting);
        Intent intent = getIntent();
        mUserDataNoRealm = intent.getParcelableExtra("User");
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("安全设置");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        alert_phone = (Button) findViewById(R.id.alert_phone);
        alert_phone.setOnClickListener(this);
        alert_password = (Button) findViewById(R.id.alert_password);
        alert_password.setOnClickListener(this);
        RelativeLayout rel_phone = (RelativeLayout) findViewById(R.id.rel_phone);
        if (Integer.parseInt(mUserDataNoRealm.getUser_role()) == 2) {
            rel_phone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.alert_phone:
                goAlertPhone();
                break;
            case R.id.alert_password:
                Intent intent = new Intent(SecuritySetting.this, AlertPassword.class);
                intent.putExtra("User", mUserDataNoRealm);
                startActivity(intent);
                break;
        }
    }

    private void goAlertPhone() {
        Intent intent = new Intent(SecuritySetting.this, AlertPhoneActivity.class);
        intent.putExtra("User", mUserDataNoRealm);
        startActivityForResult(intent, 148);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == 148 && resultCode == RESULT_OK) {
            Intent intent = new Intent(SecuritySetting.this, LoginAcitivity.class);
            startActivity(intent);
            finish();
        }

    }
}
