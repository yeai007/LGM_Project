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
import com.hopeofseed.hopeofseed.JNXData.YieldData;
import com.hopeofseed.hopeofseed.JNXDataTmp.YieldDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/25 15:11
 * 修改人：whisper
 * 修改时间：2016/10/25 15:11
 * 修改备注：
 */
public class YieldActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    String YieldId;
    YieldData mYieldData = new YieldData();
    TextView tv_varitvy, tv_yield_sum, tv_planting_area, tv_yield, tv_essay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yield_activity);
        Intent intent = getIntent();
        YieldId = intent.getStringExtra("YieldId");
        initView();
        getData();
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("YieldId", YieldId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetYieldById.php", opt_map, YieldDataTmp.class, this);
    }

    private void initView() {
        tv_varitvy = (TextView) findViewById(R.id.tv_varitvy);
        tv_yield_sum = (TextView) findViewById(R.id.tv_yield_sum);
        tv_planting_area = (TextView) findViewById(R.id.tv_planting_area);
        tv_yield = (TextView) findViewById(R.id.tv_yield);
        tv_essay = (TextView) findViewById(R.id.tv_essay);
        TextView AppTitle = (TextView) findViewById(R.id.apptitle);
        AppTitle.setText("产量表现");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
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
        YieldDataTmp mYieldDataTmp = ObjectUtil.cast(rspBaseBean);
        mYieldData = mYieldDataTmp.getDetail().get(0);
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
            tv_varitvy.setText(mYieldData.getYieldVariety());
            tv_yield_sum.setText(mYieldData.getYieldSum());
            tv_planting_area.setText(mYieldData.getYieldArea());
            tv_yield.setText(mYieldData.getYieldYield());
            tv_essay.setText(mYieldData.getYieldEssay());
        }
    };

}
