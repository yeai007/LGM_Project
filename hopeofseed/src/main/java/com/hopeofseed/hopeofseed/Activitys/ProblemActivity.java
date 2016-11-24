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
import com.hopeofseed.hopeofseed.JNXData.ProblemData;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.ProblemDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.tv_fans_sum;
import static com.hopeofseed.hopeofseed.R.id.tv_follow_sum;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/25 11:40
 * 修改人：whisper
 * 修改时间：2016/10/25 11:40
 * 修改备注：
 */
public class ProblemActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    TextView tv_title, tv_content;
    String ProblemId;
    ProblemData mProblemData = new ProblemData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem_activity);
        Intent intent = getIntent();
        ProblemId = intent.getStringExtra("ProblemId");
        initView();
        getData();
    }

    private void initView() {
        TextView Apptitle = (TextView) findViewById(R.id.apptitle);
        Apptitle.setText("问题");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("ProblemId", ProblemId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetProblemById.php", opt_map, ProblemDataTmp.class, this);
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
        mProblemData = ((ProblemDataTmp) ObjectUtil.cast(rspBaseBean)).getDetail().get(0);
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
            tv_title.setText(mProblemData.getProblemTitle());
            tv_content.setText(mProblemData.getProblemContent());
        }
    };
}
