package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;

import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;



/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/25 16:02
 * 修改人：whisper
 * 修改时间：2016/11/25 16:02
 * 修改备注：
 */
public class UserBliedActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "UserBliedActivity";
    EditText et_nickname, et_address, et_address_detail;
    TextView AppTitle;
    String UserId;
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_blied_activity);
        Intent intent = getIntent();
        UserId = intent.getStringExtra("ID");
        initView();
        getData();
    }

    private void initView() {
        AppTitle = (TextView) findViewById(R.id.apptitle);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_nickname.setFocusable(false);
        et_address = (EditText) findViewById(R.id.et_address);
        et_address.setFocusable(false);
        et_address_detail = (EditText) findViewById(R.id.et_address_detail);
        et_address_detail.setFocusable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUserInfoById.php", opt_map, UserDataNoRealmTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        mUserDataNoRealm = ((UserDataNoRealmTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);

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
            Log.e(TAG, "handleMessage: updateview");
            AppTitle.setText(mUserDataNoRealm.getNickname());
            et_nickname.setText(mUserDataNoRealm.getNickname());
            et_address.setText(mUserDataNoRealm.getUserProvince()+" "+mUserDataNoRealm.getUserCity()+"  "+mUserDataNoRealm.getUserZone());
            et_address_detail.setText(mUserDataNoRealm.getUserAddressDetail());
        }
    };


}
