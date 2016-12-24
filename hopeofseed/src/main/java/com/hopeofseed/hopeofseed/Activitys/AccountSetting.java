package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXData.UserMoreData;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserMoreDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/19 17:02
 * 修改人：whisper
 * 修改时间：2016/8/19 17:02
 * 修改备注：
 */
public class AccountSetting extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "AccountSetting";
    EditText et_nickname;
    Button btn_field, btn_topright;
    private static int SELECTFIELD = 143;
    UserDataNoRealm mUserDataNoRealm = new UserDataNoRealm();
    RelativeLayout rel_introduction;
    TextView tv_introduction;
    UserMoreData mUserMoreData = new UserMoreData();
    Handler mHandler = new Handler();
    private static int MODIFY_INTRODUCE = 145;

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_field:
                intent = new Intent(AccountSetting.this, ModifyFieldActivity.class);
                intent.putExtra("UserId", mUserMoreData.getUserId());
                startActivityForResult(intent, SELECTFIELD);
                break;
            case R.id.rel_introduction:
                intent = new Intent(AccountSetting.this, ModifyIntroduceActivity.class);
                intent.putExtra("UserId", mUserMoreData.getUserId());
                intent.putExtra("UserRoleId", mUserMoreData.getUserRoleId());
                intent.putExtra("Introduce", mUserMoreData.getIntroduce());
                startActivityForResult(intent, MODIFY_INTRODUCE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == SELECTFIELD && resultCode == RESULT_OK) {
            Log.e(TAG, "onActivityResult: " + data.getStringExtra(FieldActivity.SELECT_FIEDL));
            btn_field.setText(Const.currentUser.user_field);
        } else if (requestCode == MODIFY_INTRODUCE && resultCode == RESULT_OK) {
            getUserMoreData();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
        Intent intent = getIntent();
        mUserDataNoRealm = intent.getParcelableExtra("User");
        getUserMoreData();
        initView();
    }

    private void getUserMoreData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", mUserDataNoRealm.getUser_id());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUserMoreData.php", opt_map, UserMoreDataTmp.class, this);
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
        btn_field.setText(Const.currentUser.user_field);
        rel_introduction = (RelativeLayout) findViewById(R.id.rel_introduction);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        rel_introduction.setOnClickListener(this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        UserMoreDataTmp mUserMoreDataTmp = ObjectUtil.cast(rspBaseBean);
        mUserMoreData = mUserMoreDataTmp.getDetail();
        mHandler.post(updateIntroduce);
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
    }

    @Override
    public void onFail() {

    }

    Runnable updateIntroduce = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(mUserMoreData.getIntroduce())) {
                tv_introduction.setText("简介：\n" + mUserMoreData.getIntroduce());
            }
        }
    };
}
