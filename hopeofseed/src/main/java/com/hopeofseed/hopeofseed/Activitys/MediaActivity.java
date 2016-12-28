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
import com.hopeofseed.hopeofseed.JNXData.MediaUserData;
import com.hopeofseed.hopeofseed.JNXDataTmp.MediaUserDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/26 14:07
 * 修改人：whisper
 * 修改时间：2016/12/26 14:07
 * 修改备注：
 */
public class MediaActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    TextView AppTitle;
    String MediaId;
    EditText et_nickname, et_address, et_address_detail, et_info;
    MediaUserData mMediaUserData= new MediaUserData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_user_acitivity);
        Intent intent = getIntent();
        MediaId = intent.getStringExtra("MediaId");
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
        et_info = (EditText) findViewById(R.id.et_info);
        et_info.setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("MediaId", MediaId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetMediaById.php", opt_map, MediaUserDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        mMediaUserData = ((MediaUserDataTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);
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
            AppTitle.setText(mMediaUserData.getAppMediaName());
            et_nickname.setText(mMediaUserData.getAppMediaName());
            et_address.setText(mMediaUserData.getAppMediaProvince() + " " + mMediaUserData.getAppMediaCity() + " " + mMediaUserData.getAppMediaZone());
            et_address_detail.setText(mMediaUserData.getAppMediaAddressDetail());
            et_info.setText(mMediaUserData.getAppMediaIntroduce());

        }
    };
}
