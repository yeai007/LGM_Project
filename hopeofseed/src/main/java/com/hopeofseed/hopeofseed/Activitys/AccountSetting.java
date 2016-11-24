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

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.UpdateUserInfo;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.R;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/19 17:02
 * 修改人：whisper
 * 修改时间：2016/8/19 17:02
 * 修改备注：
 */
public class AccountSetting extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AccountSetting";
    EditText et_nickname;
    Button btn_field, btn_topright;
    private static int SELECTFIELD = 143;
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                updateUserInfo();
                break;
            case R.id.btn_field:
                getField();
                break;
        }
    }

    private void updateUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateUserInfo updateUserInfo = new UpdateUserInfo();
                updateUserInfo.UserId = String.valueOf(Const.currentUser.user_id);
                updateUserInfo.NickName = et_nickname.getText().toString().trim();
                updateUserInfo.Field = btn_field.getText().toString().trim();
                Boolean bRet = updateUserInfo.RunData();
                Message msg = updateUserInfoHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler updateUserInfoHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:

                    break;
                case 2:

                    break;
            }
        }
    };

    private void getField() {
        //加载关注领域
        Intent intent = new Intent(AccountSetting.this, FieldActivity.class);
        startActivityForResult(intent, SELECTFIELD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == SELECTFIELD && resultCode == RESULT_OK) {
            Log.e(TAG, "onActivityResult: " + data.getStringExtra(FieldActivity.SELECT_FIEDL));
            btn_field.setText(data.getStringExtra(FieldActivity.SELECT_FIEDL));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
        Intent intent = getIntent();
        mUserDataNoRealm = intent.getParcelableExtra("User");
        initView();
    }


    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("帐号设置");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("更新");
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        btn_field = (Button) findViewById(R.id.btn_field);
        btn_field.setOnClickListener(this);
        et_nickname.setText(mUserDataNoRealm.getNickname());
        btn_field.setText(mUserDataNoRealm.getUser_field());
    }
}
