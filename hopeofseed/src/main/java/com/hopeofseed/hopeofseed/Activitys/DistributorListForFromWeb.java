package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.DistributorForCommodityAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/7 20:30
 * 修改人：whisper
 * 修改时间：2016/10/7 20:30
 * 修改备注：
 */
public class DistributorListForFromWeb extends AppCompatActivity implements NetCallBack {
    private static final String TAG = "DistributorListFragment";
    RecyclerView lv_distributor;
    DistributorForCommodityAdapter mDistributorForCommodityAdapter;
    ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
    ArrayList<DistributorData> arr_DistributorDataTmp = new ArrayList<>();
    String StrSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_list_distributor_fromweb);
        Intent intent = getIntent();
        StrSearch = intent.getStringExtra("StrSearch");
        initView();
        initData();
    }


    private void initView() {
        TextView AppTitle = (TextView) findViewById(R.id.apptitle);
        AppTitle.setText("搜索结果");
        lv_distributor = (RecyclerView) findViewById(R.id.lv_distributor);
        mDistributorForCommodityAdapter = new DistributorForCommodityAdapter(getApplicationContext(), arr_DistributorData);
        lv_distributor.setAdapter(mDistributorForCommodityAdapter);
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("StrSearch", StrSearch);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchDistributor.php", opt_map, DistributorDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        DistributorDataTmp mDistributorDataTmp = ObjectUtil.cast(rspBaseBean);
        arr_DistributorDataTmp = mDistributorDataTmp.getDetail();
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
            arr_DistributorData.clear();
            arr_DistributorData.addAll(arr_DistributorDataTmp);
            mDistributorForCommodityAdapter.notifyDataSetChanged();
        }
    };


}
