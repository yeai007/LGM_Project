package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.DistributorAdapter;
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

import static com.hopeofseed.hopeofseed.R.id.lv_list;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/7 20:30
 * 修改人：whisper
 * 修改时间：2016/10/7 20:30
 * 修改备注：
 */
public class DistributorListFragment extends Fragment implements NetCallBack {
    private static final String TAG = "DistributorListFragment";
    PullToRefreshListView lv_distributor;
    DistributorAdapter mDistributorAdapter;
    ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
    ArrayList<DistributorData> arr_DistributorDataTmp = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.pager_list_distributor, null);
        initView(v);
        getData();
        return v;
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("LocLat", String.valueOf(Const.LocLat));
        opt_map.put("LocLng", String.valueOf(Const.LocLng));
        opt_map.put("Range", "2000");
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributor.php", opt_map, DistributorDataTmp.class, this);
    }

    private void initView(View v) {
        lv_distributor = (PullToRefreshListView) v.findViewById(R.id.lv_distributor);
        mDistributorAdapter = new DistributorAdapter(getActivity(), arr_DistributorData);
        lv_distributor.setAdapter(mDistributorAdapter);
        lv_distributor.setOnItemClickListener(myListener);
    }

    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), DistributorActivity.class);
            intent.putExtra("ID", String.valueOf(arr_DistributorData.get(i - 1).getDistributorId()));
            startActivity(intent);
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        DistributorDataTmp distibutorDataTmp = ObjectUtil.cast(rspBaseBean);
        Log.e(TAG, "onSuccess: " + distibutorDataTmp.toString());
        arr_DistributorDataTmp = distibutorDataTmp.getDetail();
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
            mDistributorAdapter.notifyDataSetChanged();
        }
    };

    public void setUserLoc() {
        getData();
    }
}
