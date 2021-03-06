package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommHttpResult;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommHttpResultTmp;
import com.hopeofseed.hopeofseed.LoginAcitivity;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.realm.Realm;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/26 15:52
 * 修改人：whisper
 * 修改时间：2016/10/26 15:52
 * 修改备注：
 */
public class AlertPassword extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    EditText et_oldpass, et_newpass, et_newpass_comfirm;
    String oldpass;
    String newpass;
    String newpass_comfirm;
    CommHttpResult commHttpResult = new CommHttpResult();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_password_activity);
        initView();
    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("修改密码");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("修改");
        btn_topright.setOnClickListener(this);
        btn_topright.setVisibility(View.VISIBLE);
        et_oldpass = (EditText) findViewById(R.id.et_oldpass);
        et_newpass = (EditText) findViewById(R.id.et_newpass);
        et_newpass_comfirm = (EditText) findViewById(R.id.et_newpass_comfirm);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_topright:
                if (checkInput()) {
                    updatePassWordHttp();
                }
                break;
            case R.id.btn_topleft:
                finish();
                break;
        }

    }

/*    private void updatePassWord() {
        JMessageClient.updateUserPassword(Const.JPUSH_PREFIX + String.valueOf(Const.currentUser.user_id), ObjectUtil.md5(newpass), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {*/
                 /*   updatePassWordHttp();*/
/*                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });*/

/*    }*/

    private void updatePassWordHttp() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("OldPass", ObjectUtil.md5(oldpass));
        opt_map.put("NewPass", ObjectUtil.md5(newpass));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "UpdatePassWord.php", opt_map, CommHttpResultTmp.class, this);
    }

    private Boolean checkInput() {
        Boolean ischecked = true;
        oldpass = et_oldpass.getText().toString().trim();
        newpass = et_newpass.getText().toString().trim();
        newpass_comfirm = et_newpass_comfirm.getText().toString().trim();
        if (oldpass.equals("")) {
            Toast.makeText(getApplicationContext(), "请输入旧密码", Toast.LENGTH_SHORT).show();
            ischecked = false;
        }
        if (newpass.equals("")) {
            Toast.makeText(getApplicationContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
            ischecked = false;
        }
        if (!newpass.equals(newpass_comfirm)) {
            Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            ischecked = false;
        }
        return ischecked;
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        CommHttpResultTmp mCommHttpResultTmp = ObjectUtil.cast(rspBaseBean);
        commHttpResult = mCommHttpResultTmp.getDetail().get(0);
        updateView();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (commHttpResult.getResult().equals("旧密码错误")) {
                Toast.makeText(getApplicationContext(), "旧密码错误", Toast.LENGTH_SHORT).show();
            } else if (commHttpResult.getResult().equals("1")) {
                Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                //退出当前账号
                Realm updateRealm = Realm.getDefaultInstance();
                updateRealm.beginTransaction();//开启事务
                UserData updateUserData = updateRealm.where(UserData.class)
                        .equalTo("iscurrent", 1)//查询出name为name1的User对象
                        .findFirst();//修改查询出的第一个对象的名字
                if (updateUserData != null) {
                    updateUserData.setIsCurrent(0);
                }
                updateRealm.commitTransaction();
                Intent intent = new Intent(AlertPassword.this, LoginAcitivity.class);
                startActivity(intent);
                JMessageClient.logout();
                JPushInterface.stopPush(Application.getContext());
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "密码修改失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
}
