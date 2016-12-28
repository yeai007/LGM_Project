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
import com.hopeofseed.hopeofseed.Adapter.SelectCommodityAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AddRelationResult;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AddRelationResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
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
public class SettingCommodityActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingDistributorActiv";
    PullToRefreshListView lv_list;
    SelectCommodityAdapter mSelectCommodityAdapter;
    ArrayList<CommodityData> arrCommodityData = new ArrayList<>();
    CommodityDataTmp mCommodityDataTmp = new CommodityDataTmp();
    Button btn_topright;
    String CommodityIds;
    String DistributorId;
    ArrayList<String> arrCommodityIds = new ArrayList<>();
    ArrayList<AddRelationResult> mAddRelationResult = new ArrayList<>();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                HashMap<Integer, Boolean> isSelected = mSelectCommodityAdapter.getIsSelected();
                Iterator iter = isSelected.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    Log.e(TAG, "onClick: " + arrCommodityData.get(Integer.parseInt(String.valueOf(key))).getCommodityName() + String.valueOf(val));
                    if ((boolean) val) {
                        arrCommodityIds.add(arrCommodityData.get(Integer.parseInt(String.valueOf(key))).getCommodityId());
                    }

                }

                Log.e(TAG, "onClick: " + arrCommodityIds.toString());
                AddRelation();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_distributor_activity);
        Intent intent = getIntent();
        DistributorId = intent.getStringExtra("DistributorId");
        initView();
        getData();
    }

    private void initView() {
        TextView Apptitle = (TextView) findViewById(R.id.apptitle);
        Apptitle.setText("商品维护");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("确定");
        btn_topright.setOnClickListener(this);
        btn_topright.setVisibility(View.VISIBLE);
        lv_list = (PullToRefreshListView) findViewById(R.id.lv_list);
        mSelectCommodityAdapter = new SelectCommodityAdapter(SettingCommodityActivity.this, arrCommodityData);
        lv_list.setAdapter(mSelectCommodityAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private void AddRelation() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("DistributorId", DistributorId);
        opt_map.put("arrCommodityIds", arrCommodityIds.toString().replace("[", "").replace("]", ""));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddRelationCommodity.php", opt_map, AddRelationResultTmp.class, netCallBack);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityByEnterprise.php", opt_map, CommodityDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            if (rspBaseBean.RequestSign.equals("AddRelationCommodity")) {
                AddRelationResultTmp mAddRelationResultTmp = new AddRelationResultTmp();
                mAddRelationResultTmp = ObjectUtil.cast(rspBaseBean);
                mAddRelationResult = mAddRelationResultTmp.getDetail();
                updateResult();
            } else {
                mCommodityDataTmp = ObjectUtil.cast(rspBaseBean);
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
            arrCommodityData.clear();
            arrCommodityData.addAll(mCommodityDataTmp.getDetail());
            Log.e(TAG, "handleMessage: updateview" + arrCommodityData.size());
            mSelectCommodityAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(SettingCommodityActivity.this, DistributorActivity.class);
            intent.putExtra("ID", String.valueOf(arrCommodityData.get(i - 1).getCommodityId()));
            startActivity(intent);
        }
    };
}
