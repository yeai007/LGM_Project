package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.CustomPushListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CustomPushData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CustomPushDataTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/23 9:13
 * 修改人：whisper
 * 修改时间：2016/12/23 9:13
 * 修改备注：
 */
public class CustomPushActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    private static final String TAG = "CustomPushActivity";
    RecyclerView recycler_list;
    ArrayList<CustomPushData> arrCustomPushData = new ArrayList<>();
    ArrayList<CustomPushData> arrCustomPushDataTmp = new ArrayList<>();
    Handler mHandler = new Handler();
    CustomPushListAdapter mAdapter;
    Button btn_topright;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_push_activity);
        getData();
        initView();
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCustomPushList.php", opt_map, CustomPushDataTmp.class, this);
    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("推送设置");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(CustomPushActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new CustomPushListAdapter(CustomPushActivity.this, arrCustomPushData);
        recycler_list.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        arrCustomPushDataTmp = ((CustomPushDataTmp) rspBaseBean).getDetail();
        mHandler.post(updateList);
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
    }

    @Override
    public void onFail() {

    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            arrCustomPushData.clear();
            arrCustomPushData.addAll(arrCustomPushDataTmp);
            mAdapter.notifyDataSetChanged();
        }
    };

}
