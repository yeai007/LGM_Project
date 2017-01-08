package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.SelectDistributorAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AddRelationResult;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AddRelationResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/2 9:56
 * 修改人：whisper
 * 修改时间：2016/11/2 9:56
 * 修改备注：
 */
public class SettingDistributorActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingDistributorActiv";
    PullToRefreshListView lv_list;
    SelectDistributorAdapter mSelectDistributorAdapter;
    ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
    DistributorDataTmp mDistributorDataTmp = new DistributorDataTmp();
    Button btn_topright;
    String CommodityId;
    String DistributorIds;
    ArrayList<String> arrDistributorIds = new ArrayList<>();
    ArrayList<AddRelationResult> mAddRelationResult = new ArrayList<>();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                HashMap<Integer, Boolean> isSelected = mSelectDistributorAdapter.getIsSelected();
                Iterator iter = isSelected.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    Log.e(TAG, "onClick: " + arr_DistributorData.get(Integer.parseInt(String.valueOf(key))).getDistributorName() + String.valueOf(val));
                    if ((boolean) val) {
                        arrDistributorIds.add(arr_DistributorData.get(Integer.parseInt(String.valueOf(key))).getDistributorId());
                    }

                }

                Log.e(TAG, "onClick: " + arrDistributorIds.toString());
                AddRelation();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_distributor_activity);
        Intent intent = getIntent();
        CommodityId = intent.getStringExtra("CommodityId");
        initView();
        getData();
    }

    private void initView() {
        TextView Apptitle = (TextView) findViewById(R.id.apptitle);
        Apptitle.setText("经销商维护");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("确定");
        btn_topright.setOnClickListener(this);
        btn_topright.setVisibility(View.VISIBLE);
        lv_list = (PullToRefreshListView) findViewById(R.id.lv_list);
        mSelectDistributorAdapter = new SelectDistributorAdapter(SettingDistributorActivity.this, arr_DistributorData);
        lv_list.setAdapter(mSelectDistributorAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private void AddRelation() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("CommodityId", CommodityId);
        opt_map.put("arrDistributorIds", arrDistributorIds.toString().replace("[", "").replace("]", ""));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddRelationDistributor.php", opt_map, AddRelationResultTmp.class, netCallBack);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("CommodityId",CommodityId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorByAddRelation.php", opt_map, DistributorDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            if (rspBaseBean.RequestSign.equals("AddRelationDistributor")) {
                AddRelationResultTmp mAddRelationResultTmp = new AddRelationResultTmp();
                mAddRelationResultTmp = ObjectUtil.cast(rspBaseBean);
                mAddRelationResult = mAddRelationResultTmp.getDetail();
                updateResult();
            } else {
                mDistributorDataTmp = ObjectUtil.cast(rspBaseBean);
                updateView();
            }
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    private void updateResult() {
        Message msg = updateResultHandle.obtainMessage();
        msg.sendToTarget();
    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateResultHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int result = 0;
            for (int i = 0; i < mAddRelationResult.size(); i++) {
                if (mAddRelationResult.get(i).getResult().equals("已存在")) {
                    result = result + 1;
                } else if (Integer.parseInt(mAddRelationResult.get(i).getResult()) == 0) {
                } else {
                    result = result + 1;
                }
            }
            Toast.makeText(getApplicationContext(), "添加成功" + result, Toast.LENGTH_SHORT).show();
            finish();
        }
    };
    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: updateview");
            arr_DistributorData.clear();
            arr_DistributorData.addAll(mDistributorDataTmp.getDetail());
            Log.e(TAG, "handleMessage: updateview" + arr_DistributorData.size());
            mSelectDistributorAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(SettingDistributorActivity.this, DistributorActivity.class);
            intent.putExtra("ID", String.valueOf(arr_DistributorData.get(i - 1).getDistributorId()));
            startActivity(intent);
        }
    };
}
