package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.EnterpriseDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.hopeofseed.hopeofseed.R.id.img_user_avatar;
import static com.hopeofseed.hopeofseed.R.id.tv_fans_sum;
import static com.hopeofseed.hopeofseed.R.id.tv_follow_sum;
import static com.hopeofseed.hopeofseed.R.id.tv_username;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/25 16:33
 * 修改人：whisper
 * 修改时间：2016/10/25 16:33
 * 修改备注：
 */
public class EnterpriseActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    TextView AppTitle;
    String EnterpriseId;
    EnterpriseData mEnterpriseData = new EnterpriseData();
    EditText et_nickname, et_address, et_address_detail, et_tel, et_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterprise_activity);
        Intent intent = getIntent();
        EnterpriseId = intent.getStringExtra("EnterpriseId");
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
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_tel.setFocusable(false);
        et_info = (EditText) findViewById(R.id.et_info);
        et_info.setFocusable(false);
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("EnterpriseId", EnterpriseId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetEnterpriseById.php", opt_map, EnterpriseDataTmp.class, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        mEnterpriseData = ((EnterpriseDataTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);
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
            AppTitle.setText(mEnterpriseData.getEnterpriseName());
            et_nickname.setText(mEnterpriseData.getEnterpriseName());
            et_address.setText(mEnterpriseData.getEnterpriseProvince() + " " + mEnterpriseData.getEnterpriseCity() + " " + mEnterpriseData.getEnterpriseZone());
            et_address_detail.setText(mEnterpriseData.getEnterpriseAddressDetail());
            et_tel.setText(mEnterpriseData.getEnterpriseTelephone());
            et_info.setText(mEnterpriseData.getEnterpriseIntroduce());
        }
    };


}
