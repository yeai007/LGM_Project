package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.ExperienceData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExperienceDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.AutoSplitTextView;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/24 19:58
 * 修改人：whisper
 * 修改时间：2016/10/24 19:58
 * 修改备注：
 */
public class ExperienceActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    TextView tv_title;
    AutoSplitTextView tv_content;
    String ExperienceId;
    ExperienceData mExperienceData = new ExperienceData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_activity);
        Intent intent = getIntent();
        ExperienceId = intent.getStringExtra("ExperienceId");
        initView();
        getData();
    }

    private void initView() {
        TextView Apptitle = (TextView) findViewById(R.id.apptitle);
        Apptitle.setText("农技经验");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (AutoSplitTextView) findViewById(R.id.tv_content);
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("ExperienceId", ExperienceId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetExperienceById.php", opt_map, ExperienceDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        ExperienceDataTmp mExperienceDataTmp = ObjectUtil.cast(rspBaseBean);
        mExperienceData = mExperienceDataTmp.getDetail().get(0);
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

            tv_title.setText(mExperienceData.getExperienceTitle());
            tv_content.setText(mExperienceData.getExperienceContent());


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
