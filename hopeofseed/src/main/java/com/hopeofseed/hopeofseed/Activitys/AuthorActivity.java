package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AuthorData;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.AuthorDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

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
    ImageView img_user_avatar;
    TextView tv_username, tv_follow_sum, tv_fans_sum, AppTitle;
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
        img_user_avatar = (ImageView) findViewById(R.id.img_user_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_follow_sum = (TextView) findViewById(R.id.tv_follow_sum);
        tv_fans_sum = (TextView) findViewById(R.id.tv_fans_sum);
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

    private void getUserJpushInfo(String user_name) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Glide.with(getApplicationContext())
                        .load(userInfo.getAvatarFile())
                        .centerCrop()
                        .into(img_user_avatar);
            }
        });
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
            tv_username.setText(mAuthorData.getAuthorName());
            tv_follow_sum.setText(mAuthorData.getFllowed_count());
            tv_fans_sum.setText(mAuthorData.getBeen_fllowed_count());
            getUserJpushInfo(Const.JPUSH_PREFIX+mAuthorData.getUser_id());

        }
    };
}
