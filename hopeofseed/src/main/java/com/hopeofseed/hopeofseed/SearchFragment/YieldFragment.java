package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.ExpertActivity;
import com.hopeofseed.hopeofseed.Activitys.YieldActivity;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.YieldDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.YieldData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.YieldDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 产量
 */
public class YieldFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private int position;
    PullToRefreshListView lv_list;
    YieldDataAdapter mYieldDataAdapter;
    ArrayList<YieldData> arr_YieldData = new ArrayList<>();
    ArrayList<YieldData> arr_YieldDataTmp = new ArrayList<>();
    static  String Str_search="";
/*
    public static YieldFragment newInstance(int position, String search) {
        Str_search = search;
        YieldFragment f = new YieldFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.search_list_fragment, null);
        initView(v);
        getData(Str_search);
        return v;
    }

    private void initView(View v) {
        lv_list = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        mYieldDataAdapter = new YieldDataAdapter(getActivity(), arr_YieldData);
        lv_list.setAdapter(mYieldDataAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private void getData(String Str_search) {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchYieldResult.php", opt_map, YieldDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            YieldDataTmp mYieldDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_YieldDataTmp = mYieldDataTmp.getDetail();
            updateView();
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: updateview");
            arr_YieldData.clear();
            arr_YieldData.addAll(arr_YieldDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_YieldData.size());
            mYieldDataAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), YieldActivity.class);
            intent.putExtra("YieldId", String.valueOf(arr_YieldData.get(i - 1).getYieldId()));
            startActivity(intent);
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}