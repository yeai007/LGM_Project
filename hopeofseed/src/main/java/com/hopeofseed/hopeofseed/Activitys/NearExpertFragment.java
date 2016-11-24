package com.hopeofseed.hopeofseed.Activitys;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.ExpertDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/9 19:39
 * 修改人：whisper
 * 修改时间：2016/10/9 19:39
 * 修改备注：
 */
public class NearExpertFragment extends Fragment implements NetCallBack {
    PullToRefreshListView lv_list;
    ExpertDataAdapter mExpertDataAdapter;
    ArrayList<ExpertData> arr_ExpertData = new ArrayList<>();
    android.os.Handler mHandler = new android.os.Handler();
    private String StrProvince, StrCity, StrZone, StrPolitic;
    private int PageNo = 0;
    ArrayList<ExpertData> arr_ExpertDataTmp = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_expert_near, null);
        initView(v);
        getData();
        return v;
    }

    private void initView(View v) {
        lv_list = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        mExpertDataAdapter = new ExpertDataAdapter(getActivity(), arr_ExpertData);
        lv_list.setAdapter(mExpertDataAdapter);
        lv_list.setMode(PullToRefreshBase.Mode.BOTH);
        lv_list.setOnItemClickListener(myListener);
    }

    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), ExpertActivity.class);
            intent.putExtra("ExpertId", String.valueOf(arr_ExpertData.get(i - 1).getExpertId()));
            startActivity(intent);
        }
    };

    private void getData() {
        Log.e(TAG, "getData: 获取品种数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("StrProvince", StrProvince);
        opt_map.put("StrCity", "StrCity");
        opt_map.put("StrZone", StrZone);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrPolitic", StrPolitic);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetNearByExpert.php", opt_map, ExpertDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        arr_ExpertDataTmp = ((ExpertDataTmp) rspBaseBean).getDetail();
        mHandler.post(runnableNotifyList);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable runnableNotifyList = new Runnable() {
        @Override
        public void run() {

            arr_ExpertData.clear();
            arr_ExpertData.addAll(arr_ExpertDataTmp);
            Log.e(TAG, "run: notity");
            mExpertDataAdapter.notifyDataSetChanged();
        }
    };
    public void setRefreshData(String... citySelected)
    {
        StrProvince = citySelected[0];
        StrCity = citySelected[1];
        StrZone = citySelected[2];
        getData();
    }
}
