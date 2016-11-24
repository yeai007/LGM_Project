package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hopeofseed.hopeofseed.R;


/**
 * 项目名称：liguangming
 * 类描述：用户信息维护
 * 创建人：whisper
 * 创建时间：2016/8/18 8:27
 * 修改人：whisper
 * 修改时间：2016/8/18 8:27
 * 修改备注：
 */
public class AccountManager extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountmanager);
        initView();
    }

    private void initView() {
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(listener);
        Button btn_topleft = (Button) findViewById(R.id.btn_topleft);
        btn_topleft.setOnClickListener(listener);
        RelativeLayout rel_accountsetting = (RelativeLayout) findViewById(R.id.rel_accountsetting);
        rel_accountsetting.setOnClickListener(listener);
        RelativeLayout rel_securitysetting = (RelativeLayout) findViewById(R.id.rel_securitysetting);
        rel_securitysetting.setOnClickListener(listener);
        RelativeLayout rel_seedvariety = (RelativeLayout) findViewById(R.id.rel_seedvariety);
        rel_seedvariety.setOnClickListener(listener);
        RelativeLayout rel_signout = (RelativeLayout) findViewById(R.id.rel_signout);
        rel_signout.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_topright:
                    break;
                case R.id.btn_topleft:
                    finish();
                    break;
                case R.id.rel_accountsetting:
                    //账号设置
                    intent = new Intent(getApplicationContext(), AccountSetting.class);
                    startActivity(intent);
                    break;
                case R.id.rel_securitysetting:
                    //帐号安全设置
                    intent = new Intent(getApplicationContext(), SecuritySetting.class);
                    startActivity(intent);
                    break;
                case R.id.rel_seedvariety:
                    //品种维护
                    intent = new Intent(getApplicationContext(), SeedVariety.class);
                    startActivity(intent);
                    break;
                case R.id.rel_signout:
                    //退出当前账号
                    break;
            }
        }
    };
}
