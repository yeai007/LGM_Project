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
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/22 10:23
 * 修改人：whisper
 * 修改时间：2016/10/22 10:23
 * 修改备注：
 */
public class DistributorActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "DistributorActivity";
    String DistributorId;
    EditText et_nickname, et_address, et_address_detail, et_tel, et_info;
    TextView AppTitle;
    DistributorData mDistributorData = new DistributorData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributor_activity);
        Intent intent = getIntent();
        DistributorId = intent.getStringExtra("ID");
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
        opt_map.put("DistributorId", DistributorId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorById.php", opt_map, DistributorDataTmp.class, this);
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
        DistributorDataTmp distibutorDataTmp = ObjectUtil.cast(rspBaseBean);
        Log.e(TAG, "onSuccess: " + distibutorDataTmp.toString());
        mDistributorData = distibutorDataTmp.getDetail().get(0);
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
            AppTitle.setText(mDistributorData.getDistributorName());
            et_nickname.setText(mDistributorData.getDistributorName());
            et_address.setText(mDistributorData.getDistributorProvince() + " " + mDistributorData.getDistributorCity() + " " + mDistributorData.getDistributorZone());
            et_address_detail.setText(mDistributorData.getDistributorAddressDetail());
            et_tel.setText(mDistributorData.getDistributorTelephone());
            et_info.setText(mDistributorData.getDistributorIntroduce());
        }
    };


}
