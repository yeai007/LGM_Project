package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.DistributorListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorCountByClassTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/4 8:42
 * 修改人：whisper
 * 修改时间：2017/1/4 8:42
 * 修改备注：
 */
public class DistributorListForReport extends AppCompatActivity {
    private static final String TAG = "DistributorListForRepor";
    RecyclerView recycler_list;
    int AreaId = 0;
    String[] data;
    ArrayList<DistributorData> arrDistributor = new ArrayList<>();
    ArrayList<DistributorData> arrDistributorTmp = new ArrayList<>();
    Handler handler = new Handler();
    DistributorListAdapter mDistributorListAdapter;
    int Class = 0;
    int ClassId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributor_list_for_report);
        Intent intent = getIntent();
        Class = intent.getIntExtra("Class", 0);
        initView();
        if (Class == 0) {
            ClassId = intent.getIntExtra("ClassId", 0);
        } else {
            AreaId = intent.getIntExtra("AreaId", 0);
            data = intent.getStringArrayExtra("Condition");

        }
        getDistributors();
    }

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("经销商列表");
        (findViewById(R.id.btn_topleft)).setOnClickListener(listener);
        LinearLayoutManager manager = new LinearLayoutManager(DistributorListForReport.this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(manager);
        mDistributorListAdapter = new DistributorListAdapter(DistributorListForReport.this, arrDistributor);
        recycler_list.setAdapter(mDistributorListAdapter);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_topleft:
                    finish();
                    break;
            }
        }
    };


    private void getDistributors() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("Class", String.valueOf(Class));
        if (Class == 0) {
            opt_map.put("ClassId", String.valueOf(ClassId));
        } else {
            opt_map.put("StrSp1", data[1]);
            opt_map.put("StrSp2", data[2]);
            opt_map.put("StrSp3", data[3]);
            opt_map.put("AreaId",String.valueOf(AreaId));
        }
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorListForReport.php", opt_map, DistributorDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            arrDistributorTmp = ((DistributorDataTmp) rspBaseBean).getDetail();
            handler.post(updateList);
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };
    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            arrDistributor.clear();
            arrDistributor.addAll(arrDistributorTmp);
            mDistributorListAdapter.notifyDataSetChanged();
        }
    };
}
