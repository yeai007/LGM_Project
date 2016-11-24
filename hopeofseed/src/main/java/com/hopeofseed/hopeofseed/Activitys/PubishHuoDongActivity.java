package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
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
import com.hopeofseed.hopeofseed.JNXDataTmp.CommHttpResultTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/17 10:20
 * 修改人：whisper
 * 修改时间：2016/11/17 10:20
 * 修改备注：
 */
public class PubishHuoDongActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "PubishHuoDongActivity";
    EditText et_title, et_content;
    Button btn_topright;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_huodong_activity);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("发起活动");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("发起");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                SubmitHuoDong();
                break;
        }
    }

    private void SubmitHuoDong() {


        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("HuoDongTitle", et_title.getText().toString());
        opt_map.put("HuoDongContent", et_content.getText().toString());
        opt_map.put("CreateUser", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddNewProblemData.php", opt_map, CommHttpResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }
}
