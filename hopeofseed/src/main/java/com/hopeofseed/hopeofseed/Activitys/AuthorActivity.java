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
import com.hopeofseed.hopeofseed.JNXData.AuthorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AuthorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/26 16:30
 * 修改人：whisper
 * 修改时间：2016/9/26 16:30
 * 修改备注：
 */
public class AuthorActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    TextView AppTitle;
    EditText et_nickname, et_address, et_address_detail, et_info;
    AuthorData mAuthorData = new AuthorData();
    String AuthorId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_activity);
        Intent intent = getIntent();
        AuthorId = intent.getStringExtra("AuthorId");
        initView();
        // getUserJpushInfo(UserName);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                //   Toast.makeText(getApplicationContext(), "OnClick", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_topright:
                break;
        }
    }


    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("AuthorId", AuthorId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetAuthorById.php", opt_map, AuthorDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        mAuthorData = ((AuthorDataTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);
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
            AppTitle.setText(mAuthorData.getAuthorName());
            et_nickname.setText(mAuthorData.getAuthorName());
            et_address.setText(mAuthorData.getAuthorProvince() + " " + mAuthorData.getAuthorCity() + " " + mAuthorData.getAuthorZone());
            et_address_detail.setText(mAuthorData.getAuthorAddressDetail());
            et_info.setText(mAuthorData.getAuthorIntroduce());

        }
    };
}
