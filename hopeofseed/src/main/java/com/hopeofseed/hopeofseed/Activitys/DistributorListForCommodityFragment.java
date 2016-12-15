package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.AutoTextDistributoAdapter;
import com.hopeofseed.hopeofseed.Adapter.DistributorAdapter;
import com.hopeofseed.hopeofseed.Adapter.DistributorForCommodityAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
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
public class DistributorListForCommodityFragment extends Fragment implements NetCallBack {
    private static final String TAG = "DistributorListFragment";
    RecyclerView lv_distributor;
    DistributorForCommodityAdapter mDistributorForCommodityAdapter;
    ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
    ArrayList<DistributorData> arr_DistributorDataTmp = new ArrayList<>();
    AutoCompleteTextView tv_search;
    ArrayList<DistributorData> arrAutoDistributorDataTmp = new ArrayList<>();
    ArrayList<DistributorData> arrAutoDistributorData = new ArrayList<>();
    AutoTextDistributoAdapter mAutoTextDistributoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.distributor_commodity_fragment, null);
        initView(v);
        initData();
        getData();

        return v;
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributor.php", opt_map, DistributorDataTmp.class, this);
    }

    private void initView(View v) {
        lv_distributor = (RecyclerView) v.findViewById(R.id.lv_distributor);
        mDistributorForCommodityAdapter = new DistributorForCommodityAdapter(getActivity(), arr_DistributorData);
        lv_distributor.setAdapter(mDistributorForCommodityAdapter);
        tv_search = (AutoCompleteTextView) v.findViewById(R.id.tv_search);
        mAutoTextDistributoAdapter = new AutoTextDistributoAdapter(getActivity(), arr_DistributorData);
        tv_search.setAdapter(mAutoTextDistributoAdapter);
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorAutoData.php", opt_map, DistributorDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetAutoDistributor")) {
            DistributorDataTmp mDistributorDataTmp = ObjectUtil.cast(rspBaseBean);
            arrAutoDistributorDataTmp = mDistributorDataTmp.getDetail();
            updateAutoDistributor();

        } else {
            DistributorDataTmp distibutorDataTmp = ObjectUtil.cast(rspBaseBean);
            Log.e(TAG, "onSuccess: " + distibutorDataTmp.toString());
            arr_DistributorDataTmp = distibutorDataTmp.getDetail();
            updateView();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateAutoDistributor() {
        Message msg = updateAutoDistributorHandle.obtainMessage();
        msg.sendToTarget();
    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateAutoDistributorHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: updateview");
            arrAutoDistributorData.clear();
            arrAutoDistributorData.addAll(arrAutoDistributorDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arrAutoDistributorData.size());
            mAutoTextDistributoAdapter.notifyDataSetChanged();

        }
    };
    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            arr_DistributorData.clear();
            arr_DistributorData.addAll(arr_DistributorDataTmp);
            mDistributorForCommodityAdapter.notifyDataSetChanged();
        }
    };
}
