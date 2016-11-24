package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.EnterpriseAdapter;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.EnterpriseDataTmp;
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
public class EnterpriseListFragment extends Fragment implements NetCallBack {
    private static final String TAG = "EnterpriseListFragment";
    PullToRefreshListView lv_distributor;
    EnterpriseAdapter mEnterpriseAdapter;
    ArrayList<EnterpriseData> arr_EnterpriseData = new ArrayList<>();
    ArrayList<EnterpriseData> arr_EnterpriseDataTmp = new ArrayList<>();

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
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetEnterprise.php", opt_map, EnterpriseDataTmp.class, this);
    }

    private void initView(View v) {
        lv_distributor = (PullToRefreshListView) v.findViewById(R.id.lv_distributor);
        mEnterpriseAdapter = new EnterpriseAdapter(getActivity(), arr_EnterpriseData);
        lv_distributor.setAdapter(mEnterpriseAdapter);

    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        EnterpriseDataTmp enterpriseDataTmp = ObjectUtil.cast(rspBaseBean);
        Log.e(TAG, "onSuccess: " + enterpriseDataTmp.toString());
        arr_EnterpriseDataTmp = enterpriseDataTmp.getDetail();
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
            arr_EnterpriseData.clear();
            arr_EnterpriseData.addAll(arr_EnterpriseDataTmp);
            mEnterpriseAdapter.notifyDataSetChanged();
        }
    };
}
