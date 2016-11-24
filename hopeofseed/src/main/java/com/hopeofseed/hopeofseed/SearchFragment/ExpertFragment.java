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
import com.hopeofseed.hopeofseed.Activitys.CropActivity;
import com.hopeofseed.hopeofseed.Activitys.ExpertActivity;
import com.hopeofseed.hopeofseed.Adapter.ExpertDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.ProblemDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;
import com.hopeofseed.hopeofseed.JNXData.ProblemData;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.ProblemDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 专家
 */
public class ExpertFragment extends Fragment {
    private static final String TAG = "ExpertFragment";
    private static final String ARG_POSITION = "position";

    private int position;
    PullToRefreshListView lv_list;
     ExpertDataAdapter mExpertDataAdapter;
     ArrayList<ExpertData> arr_ExpertData = new ArrayList<>();
     ArrayList<ExpertData> arr_ExpertDataTmp = new ArrayList<>();
    static  String Str_search="";
    public static  ExpertFragment newInstance(int position, String search) {
        Str_search = search;
        ExpertFragment f = new ExpertFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

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
        mExpertDataAdapter = new ExpertDataAdapter(getActivity(), arr_ExpertData);
        lv_list.setAdapter(mExpertDataAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private  void getData(String Str_search) {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchExpertData.php", opt_map, ExpertDataTmp.class, netCallBack);
    }

     NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            ExpertDataTmp mExpertDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_ExpertDataTmp = mExpertDataTmp.getDetail();
            updateView();
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    private  void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private  Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: updateview");
            arr_ExpertData.clear();
            arr_ExpertData.addAll(arr_ExpertDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_ExpertData.size());
            mExpertDataAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), ExpertActivity.class);
            intent.putExtra("ExpertId", String.valueOf(arr_ExpertData.get(i - 1).getExpertId()));
            startActivity(intent);
        }
    };
    public  void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}