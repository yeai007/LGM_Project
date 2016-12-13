package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NotifyData;
import com.hopeofseed.hopeofseed.JNXDataTmp.GroupInfoNoJpushTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import io.realm.Realm;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/10 9:09
 * 修改人：whisper
 * 修改时间：2016/12/10 9:09
 * 修改备注：
 */
public class JoinTheGroup extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "JoinTheGroup";
    EditText et_txt;
    String GroupId;
    Handler mHandler = new Handler();
    Realm myRealm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_the_group_activity);
        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GroupId");
        initView();
    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("申请加群");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("申请加入");
        btn_topright.setOnClickListener(this);
        btn_topright.setVisibility(View.VISIBLE);
        et_txt = (EditText) findViewById(R.id.et_txt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                joinTheGroup();
                break;
        }
    }

    private void joinTheGroup() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("GroupId", GroupId);
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "JoinTheGroup.php", opt_map, pushFileResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        pushFileResultTmp mpushFileResultTmp = ObjectUtil.cast(rspBaseBean);
        Log.e(TAG, "onSuccess: " + mpushFileResultTmp.getDetail());
        mHandler.post(updateResult);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateResult = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
}
