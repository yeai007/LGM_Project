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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.hopeofseed.hopeofseed.R.id.et_address;
import static com.hopeofseed.hopeofseed.R.id.et_address_detail;
import static com.hopeofseed.hopeofseed.R.id.et_info;
import static com.hopeofseed.hopeofseed.R.id.et_nickname;
import static com.hopeofseed.hopeofseed.R.id.et_tel;
import static com.hopeofseed.hopeofseed.R.id.tv_fans_sum;
import static com.hopeofseed.hopeofseed.R.id.tv_follow_sum;
import static com.hopeofseed.hopeofseed.R.id.tv_username;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/25 10:05
 * 修改人：whisper
 * 修改时间：2016/10/25 10:05
 * 修改备注：
 */
public class ExpertActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    String ExpertId;
    ExpertData mExpertData = new ExpertData();

    TextView Apptitle;
    EditText et_nickname, et_address, et_address_detail, et_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_activity);
        Intent intent = getIntent();
        ExpertId = intent.getStringExtra("ExpertId");
        initView();
        getData();
    }

    private void initView() {
        Apptitle = (TextView) findViewById(R.id.apptitle);
        Apptitle.setText("专家");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_nickname.setFocusable(false);
        et_address = (EditText) findViewById(R.id.et_address);
        et_address.setFocusable(false);
        et_address_detail = (EditText) findViewById(R.id.et_address_detail);
        et_address_detail.setFocusable(false);
        et_info = (EditText) findViewById(R.id.et_info);
        et_info.setFocusable(false);
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("ExpertId", ExpertId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetExpertDataById.php", opt_map, ExpertDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        ExpertDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
        mExpertData = mCropDataTmp.getDetail().get(0);
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
            Apptitle.setText(mExpertData.getNickname());
            et_nickname.setText(mExpertData.getExpertName());
            et_address.setText(mExpertData.getExpertProvince() + " " + mExpertData.getExpertCity() + " " + mExpertData.getExpertZone());
            et_address_detail.setText(mExpertData.getExpertAddressDetail());
            et_info.setText(mExpertData.getExpertIntroduce());

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_topleft:
                finish();
                break;
        }
    }
}
